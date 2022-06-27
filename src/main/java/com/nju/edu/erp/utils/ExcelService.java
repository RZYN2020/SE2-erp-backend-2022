package com.nju.edu.erp.utils;

import static java.nio.file.Path.*;

import com.nju.edu.erp.model.vo.warehouse.WarehouseCountingVO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FooService {

  public byte[] WriteWarehouseCountingToExcel(List<WarehouseCountingVO> l) throws IOException {
    //Blank workbook
    XSSFWorkbook workbook = new XSSFWorkbook();

    //Create a blank sheet
    XSSFSheet sheet = workbook.createSheet("WarehouseCounting");

    //This data needs to be written (Object[])
    Map<String, Object[]> data = new TreeMap<String, Object[]>();


    data.put("1", new Object[]{"商品ID", "型号", "数量", "批次", "价格"});

    for (int i = 0; i < l.size(); i++) {
      data.put(String.valueOf(i+2), new Object[]{l.get(i).getId(), l.get(i).getProduct().getType(), l.get(i).getQuantity(), l.get(i).getBatchId(), l.get(i).getPurchasePrice()});
    }

    //Iterate over data and write to sheet
    Set<String> keyset = data.keySet();

    int rownum = 0;
    for (String key : keyset)
    {
      //create a row of excelsheet
      Row row = sheet.createRow(rownum++);

      //get object array of prerticuler key
      Object[] objArr = data.get(key);

      int cellnum = 0;

      for (Object obj : objArr) {
        Cell cell = row.createCell(cellnum++);
        if (obj instanceof String) {
          cell.setCellValue((String) obj);
        }
        else if (obj instanceof Integer) {
          cell.setCellValue((Integer) obj);
        }
        else if (obj instanceof BigDecimal) {
          cell.setCellValue(String.valueOf(obj));
        }
      }
    }
      //Write the workbook in file system
      FileOutputStream out = new FileOutputStream("inventory.xlsx");
      workbook.write(out);
      out.close();
      byte[] exceldata = Files.readAllBytes(of("inventory.xlsx"));
      return exceldata;
  }
}