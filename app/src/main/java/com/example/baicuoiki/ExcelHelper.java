package com.example.baicuoiki;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

public class ExcelHelper {

    private static final String TAG = "ExcelHelper";

    public static <T> void exportToExcel(Context context, String fileNamePrefix, String sheetName,
                                         String[] headers, List<T> data, BiConsumer<Row, T> rowMapper) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(context, "Danh sách trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cần thiết cho Apache POI hoạt động trên một số phiên bản Android
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            // Tạo dòng Tiêu đề (Header)
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Đổ dữ liệu vào các dòng tiếp theo
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                rowMapper.accept(row, data.get(i));
            }

            // Lưu vào thư mục Downloads công khai (Để người dùng dễ tìm thấy hơn)
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            String fileName = fileNamePrefix + "_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                fileOut.flush();
            }

            Log.d(TAG, "File saved to: " + file.getAbsolutePath());
            Toast.makeText(context, "Đã lưu thành công: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Throwable e) {
            Log.e(TAG, "Lỗi khi xuất Excel", e);
            Toast.makeText(context, "Lỗi: " + e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
