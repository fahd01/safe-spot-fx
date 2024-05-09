package com.safespot.fx.integrations;

import com.safespot.fx.services.LoanService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.builder.sql.LanguageModelSqlFilterBuilder;
import dev.langchain4j.store.embedding.filter.builder.sql.TableDefinition;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.sql.SQLException;

import static dev.langchain4j.data.document.Metadata.metadata;

public class InMemoryEmbeddingStoreManager {

    public static ContentRetriever initEmbeddingStore(ChatLanguageModel chatLanguageModel) {
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();

        // describe metadata keys as if they were columns in the SQL table
        TableDefinition tableDefinition = TableDefinition.builder()
                .name("loan")
                .addColumn("id", "INT", "Identifier of the loan")
                .addColumn("amount", "DECIMAL", "Amount of money required for this loan in TND")
                .addColumn("interest", "DECIMAL", "The interest rate of the loan in %")
                .addColumn("term", "smallint", "Number of months required to pay the loan")
                .addColumn("purpose", "varchar", "What will this loan be used for")
                .addColumn("status", "varchar", "The status of loan one of: [IN_BIDDING, ACTIVE, PAID], " +
                        "IN_BIDDING means people are still placing bids for the P2P loan, ACTIVE means the borrower collected " +
                        "enough bids to cover the loan amount and the loan term started")
                .description("This table contain loans that investors can participate in providing the required loan amount in a P2P lending style")
                .build();

        LanguageModelSqlFilterBuilder sqlFilterBuilder = new LanguageModelSqlFilterBuilder(chatLanguageModel, tableDefinition);

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        try {
            new LoanService().findAll().forEach(
                    loan -> {
                        TextSegment textSegment = TextSegment.from(
                                loan.toString(),
                                metadata("id", loan.getId())
                                        .put("amount", loan.getAmount().doubleValue())
                                        .put("interest", loan.getInterest().doubleValue())
                                        .put("term", loan.getTerm())
                                        .put("purpose", loan.getPurpose())
                                        .put("status", loan.getStatus().toString())
                        );
                        embeddingStore.add(embeddingModel.embed(textSegment).content(), textSegment);
                    });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .dynamicFilter(query -> sqlFilterBuilder.build(query)) // LLM will generate the filter dynamically
                .build();
    }
}