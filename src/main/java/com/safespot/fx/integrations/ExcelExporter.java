package com.safespot.fx.integrations;

import com.safespot.fx.models.Loan;
import com.safespot.fx.models.LoanStatus;
import com.safespot.fx.models.User;
import com.safespot.fx.services.LoanService;
import com.safespot.fx.uicomponents.BootstrapColors;
import com.safespot.fx.utils.BiddingUtils;
import com.safespot.fx.utils.SecurityUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelExporter {

    public static void exportToExcel(String sheetName, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            User currentUser = SecurityUtils.getCurrentUser();
            List<Loan> myLoans = new LoanService().findByBorrowerId(currentUser.getId());
            Sheet sheet = workbook.createSheet(sheetName);
            AtomicInteger rowIdx = new AtomicInteger();
            // Create header row
            Row headerRow = sheet.createRow(rowIdx.get());
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Amount");
            headerRow.createCell(2).setCellValue("Interest");
            headerRow.createCell(3).setCellValue("Term");
            headerRow.createCell(4).setCellValue("Status");
            headerRow.createCell(5).setCellValue("Purpose");
            headerRow.createCell(6).setCellValue("Total Bids");
            headerRow.createCell(7).setCellValue("Accepted Bids");
            headerRow.createCell(8).setCellValue("Rejected Bids");
            headerRow.createCell(9).setCellValue("Accepted Amount from Bids");
            headerRow.createCell(10).setCellValue("Remaining Amount");
            headerRow.createCell(11).setCellValue("Bidding Progress");

            // Fill data
            myLoans.forEach( loan -> {
                rowIdx.getAndIncrement();
                Row row = sheet.createRow(rowIdx.get());
                row.createCell(0).setCellValue(loan.getId());
                row.createCell(1).setCellValue(loan.getAmount().doubleValue() + " TND");
                row.createCell(2).setCellValue(loan.getInterest().doubleValue() + " %");
                row.createCell(3).setCellValue(loan.getTerm() + " months");

                row.createCell(5).setCellValue(loan.getPurpose());
                row.createCell(6).setCellValue(BiddingUtils.totalBidsCount(loan));
                row.createCell(7).setCellValue(BiddingUtils.acceptedBidsCount(loan));
                row.createCell(8).setCellValue(BiddingUtils.rejectedBidsCount(loan));
                row.createCell(9).setCellValue(BiddingUtils.calculateAcceptedBids(loan) + " TND");
                row.createCell(10).setCellValue(BiddingUtils.calculateRemainingBids(loan) + " TND");
                row.createCell(11).setCellValue(BiddingUtils.calculateBiddingProgress(loan) * 100 + " %");

                Cell statusCell = row.createCell(4);
                statusCell.setCellValue(loan.getStatus().toString());
                CellStyle statusCellStyle = sheet.getWorkbook().createCellStyle();
                if (loan.getStatus().equals(LoanStatus.IN_BIDDING))
                    statusCellStyle.setFillForegroundColor(IndexedColors.BLUE.index);
                else if (loan.getStatus().equals(LoanStatus.ACTIVE))
                    statusCellStyle.setFillForegroundColor(IndexedColors.ORANGE.index);
                else
                    statusCellStyle.setFillForegroundColor(IndexedColors.GREEN.index);
                statusCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                statusCell.setCellStyle(statusCellStyle);
            });

            // Save the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }
}