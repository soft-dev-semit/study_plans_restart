package csit.semit.studyplansrestart.service.exportPlans;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class LoadPlans {
    public void loadPlans(Workbook workbook) {
        Sheet autumnSheet = workbook.createSheet("Осень");
        Sheet springSheet = workbook.createSheet("Весна");   
        createSheetHeader(autumnSheet);
        createSheetHeader(springSheet);
    }

    private void createSheetHeader(Sheet sheet) {
        Row firstRow = sheet.createRow(7);
        firstRow.createCell(0).setCellValue("N п/п");
        firstRow.createCell(1).setCellValue("Назва дисципліни");
        firstRow.createCell(2).setCellValue("Кол-во студентів");
        firstRow.createCell(3).setCellValue("Шифр груп");
        firstRow.createCell(4).setCellValue("Кол-во потоков");
        firstRow.createCell(5).setCellValue("Кол-во подгрупп");
        firstRow.createCell(6).setCellValue("Кол-во кредитов");

        Row secondRow = sheet.createRow(8);
        secondRow.createCell(7).setCellValue("Кол-во практик");
        secondRow.createCell(8).setCellValue("Кол-во самостоятельных");
    }

}
