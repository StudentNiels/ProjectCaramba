import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.*;

import java.io.*;


public class ReadExcelFile

{
public static void main(String args[]) throws IOException
{
    //obtaining input bytes from a file
    try (InputStream inp = new FileInputStream("C:\Users\Simon the Aryan\Downloads\student.xlsx"))
    {
        //InputStream inp = new FileInputStream("student.xlsx");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(2);
        Cell cell = row.getCell(3);
        if (cell == null)
            cell = row.createCell(3);
        cell.setCellType(CellType.STRING);
        cell.setCellValue("a test");

        // Write the output to a file
        try (OutputStream fileOut = new FileOutputStream('C:\Users\Simon the Aryan\Downloads\student.xlsx'))
        {
            wb.write(fileOut);
        }
    }
}
}
