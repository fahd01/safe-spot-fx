package com.safespot.fx.integrations;
import com.safespot.fx.services.LoanService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.builder.sql.LanguageModelSqlFilterBuilder;
import dev.langchain4j.store.embedding.filter.builder.sql.TableDefinition;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static dev.langchain4j.data.document.Metadata.metadata;
import static io.grpc.netty.shaded.io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess.UNSAFE;

public class LargeLanguageModelAssistantManager {
    private static final String GOOGLE_PROJECT_ID = "safe-spot-422812";
    // `chat-bison` means PaLM2 general purpose chat model
    private static final String GOOGLE_MODEL_NAME = "chat-bison";

    private Assistant assistant;

    private static LargeLanguageModelAssistantManager instance;

    public static LargeLanguageModelAssistantManager getInstance() {
        if (Objects.isNull(instance))
            instance = new LargeLanguageModelAssistantManager();
        return instance;
    }

    private LargeLanguageModelAssistantManager(){
        initialize();
    }

    public void initialize (){
        Path gcpCreds;
        try {
            gcpCreds = Paths.get(LargeLanguageModelAssistantManager.class.getResource("gcloud/application_default_credentials.json").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        setEnvironmentalVariable("GOOGLE_APPLICATION_CREDENTIALS", gcpCreds.toAbsolutePath().toString());

        ChatLanguageModel chatLanguageModel = VertexAiChatModel.builder()
                .endpoint("us-central1-aiplatform.googleapis.com:443")
                .location("us-central1")
                .publisher("google")
                .project(GOOGLE_PROJECT_ID)
                .modelName(GOOGLE_MODEL_NAME)
                .temperature(0.0)
                .build();

        ContentRetriever contentRetriever = InMemoryEmbeddingStoreManager.initEmbeddingStore(chatLanguageModel);

        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .build();

    }

    // This is a peer to peer lending platform, as an investor recommend me a profitable loan to bid for (bid means lending the loan owner the needed amount or part of it
    public String ask(String question) {
        return assistant.answer(question);
    }

    private static void setEnvironmentalVariable(final String key, final String value) {
        try {
            final Map<String, String> unwritable = System.getenv();
            final Map<String, String> writable =
                    (Map<String, String>) getField(unwritable, "m");
            writable.put(key, value);
        } catch (final NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    private static Object getField( final Object object, final String name)
            throws NoSuchFieldException {
        return getField(object.getClass(), object, name);
    }

    private static Object getField(
            final Class<?> clazz, final Object object, final String name)
            throws NoSuchFieldException {
        return UNSAFE.getObject(object, UNSAFE.objectFieldOffset(clazz.getDeclaredField(name)));
    }
}
