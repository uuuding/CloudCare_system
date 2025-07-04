package com.cloudcare.utils;

import com.cloudcare.entity.SystemLog;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出工具类
 *
 * @author cloudcare
 */
public class ExcelUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出系统日志到Excel
     *
     * @param logs 日志列表
     * @return Excel文件的字节数组
     * @throws IOException IO异常
     */
    public static byte[] exportSystemLogsToExcel(List<SystemLog> logs) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("系统日志");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "日志级别", "模块", "用户名", "操作", "内容", "IP地址", "用户代理", "创建时间"};
            
            // 创建标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 设置标题
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 创建数据样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setWrapText(true);

            // 填充数据
            int rowNum = 1;
            for (SystemLog log : logs) {
                Row row = sheet.createRow(rowNum++);
                
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(log.getId() != null ? log.getId().toString() : "");
                cell0.setCellStyle(dataStyle);
                
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(log.getLevel() != null ? log.getLevel() : "");
                cell1.setCellStyle(dataStyle);
                
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(log.getModule() != null ? log.getModule() : "");
                cell2.setCellStyle(dataStyle);
                
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(log.getUsername() != null ? log.getUsername() : "");
                cell3.setCellStyle(dataStyle);
                
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(log.getOperation() != null ? log.getOperation() : "");
                cell4.setCellStyle(dataStyle);
                
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(log.getContent() != null ? log.getContent() : "");
                cell5.setCellStyle(dataStyle);
                
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(log.getIpAddress() != null ? log.getIpAddress() : "");
                cell6.setCellStyle(dataStyle);
                
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(log.getUserAgent() != null ? log.getUserAgent() : "");
                cell7.setCellStyle(dataStyle);
                
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(log.getCreateTime() != null ? log.getCreateTime().format(DATE_TIME_FORMATTER) : "");
                cell8.setCellStyle(dataStyle);
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最大列宽，避免内容过长导致列宽过大
                int columnWidth = sheet.getColumnWidth(i);
                if (columnWidth > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}