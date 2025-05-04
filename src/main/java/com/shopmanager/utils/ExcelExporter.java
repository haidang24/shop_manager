package com.shopmanager.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Lớp tiện ích để xuất dữ liệu từ JTable sang file Excel
 */
public class ExcelExporter {

    /**
     * Xuất dữ liệu từ JTable sang file Excel
     * 
     * @param table     Bảng dữ liệu cần xuất
     * @param fileName  Tên file xuất ra (không cần đuôi .xlsx)
     * @param sheetName Tên sheet trong file Excel
     * @param title     Tiêu đề cho báo cáo Excel
     * @return true nếu xuất thành công, false nếu có lỗi
     */
    public static boolean exportToExcel(JTable table, String fileName, String sheetName, String title) {
        try {
            TableModel model = table.getModel();

            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();

            // Tạo sheet mới
            Sheet sheet = workbook.createSheet(sheetName);

            // Tạo font cho tiêu đề
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);

            // Tạo style cho tiêu đề
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(headerFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // Thêm tiêu đề báo cáo
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(titleStyle);

            // Merge các ô cho tiêu đề
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, model.getColumnCount() - 1));

            // Tạo font cho header
            Font columnHeaderFont = workbook.createFont();
            columnHeaderFont.setBold(true);

            // Tạo style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(columnHeaderFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Tạo style cho các ô dữ liệu
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // Tạo hàng header (tên cột)
            Row headerRow = sheet.createRow(2); // Để trống 1 hàng sau tiêu đề

            // Thêm tên các cột
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(headerStyle);
            }

            // Thêm dữ liệu từng hàng
            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 3); // +3 vì có tiêu đề và header và 1 hàng trống

                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = dataRow.createCell(col);

                    // Lấy giá trị từ model
                    Object value = model.getValueAt(row, col);

                    // Xử lý các kiểu dữ liệu khác nhau
                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof Date) {
                        cell.setCellValue((Date) value);

                        // Tạo định dạng ngày tháng
                        CellStyle dateStyle = workbook.createCellStyle();
                        dateStyle.cloneStyleFrom(cellStyle);
                        CreationHelper createHelper = workbook.getCreationHelper();
                        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                        cell.setCellStyle(dateStyle);
                    } else {
                        cell.setCellValue(value.toString());
                    }

                    cell.setCellStyle(cellStyle);
                }
            }

            // Tự động điều chỉnh kích thước các cột
            for (int col = 0; col < model.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
            }

            // Thêm ngày xuất báo cáo
            Row dateRow = sheet.createRow(model.getRowCount() + 4);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue(
                    "Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

            // Thêm thông tin người xuất báo cáo (nếu có)
            String currentUser = SessionManager.getCurrentUsername();
            if (currentUser != null && !currentUser.isEmpty()) {
                Row userRow = sheet.createRow(model.getRowCount() + 5);
                Cell userCell = userRow.createCell(0);
                userCell.setCellValue("Người xuất báo cáo: " + currentUser);
            }

            // Tạo tên file với timestamp để tránh trùng lặp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = fileName + "_" + timestamp + ".xlsx";

            // Xuất file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                workbook.close();
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel: " + e.getMessage(),
                    "Lỗi xuất Excel", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Mở thư mục chứa file sau khi xuất
     * 
     * @param filePath Đường dẫn đến file đã xuất
     */
    public static void openFileLocation(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                File directory = file.getParentFile();
                if (directory != null && directory.exists()) {
                    java.awt.Desktop.getDesktop().open(directory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}