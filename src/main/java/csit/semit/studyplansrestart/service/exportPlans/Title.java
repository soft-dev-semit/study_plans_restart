package csit.semit.studyplansrestart.service.exportPlans;

import csit.semit.studyplansrestart.config.Check;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class Title {

    List<String> rowHeaders = Arrays.asList("I", "II", "III", "IV");
    public void titleSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Титул");
        createHeader(sheet);
        createTimeline(sheet);
        createConsolidated(sheet);
    }

    public void createHeader(Sheet sheet) {
        sheet.createRow(0).createCell(44).setCellFormula("'Основні дані'!B1");
        sheet.addMergedRegion(new CellRangeAddress(0,0,44,51));
        sheet.createRow(1).createCell(47).setCellFormula("'Основні дані'!A25");
        sheet.createRow(2).createCell(0).setCellValue("МІНІСТЕРСТВО ОСВІТИ І НАУКИ УКРАЇНИ");
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,52));
        sheet.createRow(3).createCell(0).setCellValue("НАЦІОНАЛЬНИЙ ТЕХНІЧНИЙ УНІВЕРСИТЕТ \"ХАРКІВСЬКИЙ ПОЛІТЕХНІЧНИЙ ІНСТИТУТ\"");
        sheet.addMergedRegion(new CellRangeAddress(3,3,0,52));
        sheet.createRow(4).createCell(0).setCellValue("НАВЧАЛЬНИЙ   ПЛАН");
        sheet.addMergedRegion(new CellRangeAddress(4,4,0,52));
        sheet.createRow(5).createCell(0).setCellValue("освітньо-професійна програма");
        sheet.addMergedRegion(new CellRangeAddress(5,5,9,43));

        Row sixthRow = sheet.createRow(6);
        sixthRow.createCell(1).setCellValue("ЗАТВЕРДЖУЮ");
        sixthRow.createCell(9).setCellFormula("'Основні дані'!B8");
        sheet.addMergedRegion(new CellRangeAddress(6,6,9,43));

        Row sevenRow = sheet.createRow(7);
        sevenRow.createCell(1).setCellValue("Ректор НТУ \"ХПІ\"");
        sevenRow.createCell(11).setCellValue("підготовки ");
        sevenRow.createCell(15).setCellFormula("'Основні дані'!B15");
        sheet.addMergedRegion(new CellRangeAddress(7,7,15,23));
        sevenRow.createCell(24).setCellValue("з галузі знань");
        sevenRow.createCell(28).setCellFormula("'Основні дані'!B9");
        sheet.addMergedRegion(new CellRangeAddress(7,7,28,29));
        sevenRow.createCell(30).setCellFormula("'Основні дані'!B10");
        sheet.addMergedRegion(new CellRangeAddress(7,7,30,41));

        Row eightRow = sheet.createRow(8);
        eightRow.createCell(15).setCellValue("(рівень вищої освіти)");
        sheet.addMergedRegion(new CellRangeAddress(8,8,15,23));
        eightRow.createCell(29).setCellValue("(шифр і назва галузі знань)");

        Row nineRow = sheet.createRow(9);
        nineRow.createCell(1).setCellValue("_______________________________________");
        nineRow.createCell(5).setCellValue("Євген СОКОЛ");
        sheet.addMergedRegion(new CellRangeAddress(9,9,5,11));
        nineRow.createCell(13).setCellValue("за спеціальністю");
        nineRow.createCell(23).setCellValue("-");
        nineRow.createCell(24).setCellFormula("'Основні дані'!B11");
        sheet.addMergedRegion(new CellRangeAddress(9,9,24,27));
        nineRow.createCell(28).setCellFormula("'Основні дані'!B12");
        sheet.addMergedRegion(new CellRangeAddress(9,9,28,39));
        nineRow.createCell(41).setCellValue("Кваліфікація  ");
        nineRow.createCell(46).setCellFormula("'Основні дані'!B16");
        sheet.addMergedRegion(new CellRangeAddress(9,9,46,51));

        Row tenRow = sheet.createRow(10);
        tenRow.createCell(41).setCellValue("Строк навчання");
        tenRow.createCell(47).setCellValue("3 роки 10 місяців");

        Row elevenRow = sheet.createRow(11);
        elevenRow.createCell(1).setCellValue("\"___\"_______________ " + LocalDate.now().getYear() + " р.");
        elevenRow.createCell(41).setCellValue("на основі");
        elevenRow.createCell(47).setCellValue("повної середньої освіти");

        Row twelveRow = sheet.createRow(12);
        twelveRow.createCell(14).setCellValue("Форма навчання ");
        twelveRow.createCell(20).setCellValue("денна");

        Row thirtyoneRow = sheet.createRow(31);
        thirtyoneRow.createCell(4).setCellValue("ІІ. Зведені бюджети часу (у тижнях)");
        thirtyoneRow.createCell(29).setCellValue("III. Практика");
        sheet.addMergedRegion(new CellRangeAddress(31,31,29,32));
        thirtyoneRow.createCell(43).setCellValue("IV. Атестація");
    }

    public void createTimeline(Sheet sheet) {
        sheet.createRow(14).createCell(0).setCellValue("І. Графік навчального процесу");
        sheet.addMergedRegion(new CellRangeAddress(14,14,0,48));

        sheet.createRow(15).createCell(54).setCellFormula("SUM(BC17:BJ17)");

        Row sixthteenRow = sheet.createRow(16);
        sixthteenRow.createCell(0).setCellValue("Курс");
        sheet.addMergedRegion(new CellRangeAddress(16,17,0,0));
        List<String> listOfMonth = Arrays.asList("Вересень","Жовтень","Листопад","Грудень","Січень","Лютий","Березень","Квітень","Травень","Червень","Липень","Серпень");
        int monthIndex = 0;
        for (int i = 1; i <= listOfMonth.size(); i +=4) {
            sixthteenRow.createCell(i).setCellValue(listOfMonth.get(monthIndex));
            sheet.addMergedRegion(new CellRangeAddress(16, 16, i, i + 3));
            monthIndex++;
        }

        sixthteenRow.createCell(54).setCellFormula("SUM(BC19:BD24)");
        sheet.addMergedRegion(new CellRangeAddress(16, 16, 54, 55));
        sixthteenRow.createCell(56).setCellFormula("SUM(BE19:BF24)");
        sheet.addMergedRegion(new CellRangeAddress(16, 16, 56, 57));
        sixthteenRow.createCell(58).setCellFormula("SUM(BG19:BH24)");
        sheet.addMergedRegion(new CellRangeAddress(16, 16, 58, 59));
        sixthteenRow.createCell(60).setCellFormula("SUM(BI19:BJ24)");
        sheet.addMergedRegion(new CellRangeAddress(16, 16, 60, 61));
        fillTimeline(sheet);
        fillTable(sheet);

        Row twentysixthRow = sheet.createRow(26);
        twentysixthRow.createCell(0).setCellValue("Позначення:");
        twentysixthRow.createCell(5).setCellValue("Т");
        twentysixthRow.createCell(6).setCellValue("Теоретичне навчання");

        twentysixthRow.createCell(13).setCellValue("С");
        twentysixthRow.createCell(14).setCellValue("Екзаменаційна сесія");

        twentysixthRow.createCell(20).setCellValue("П");
        twentysixthRow.createCell(21).setCellValue("Практика");

        twentysixthRow.createCell(24).setCellValue("Д");
        twentysixthRow.createCell(25).setCellValue("Підготовка кваліфікаційної роботи");

        twentysixthRow.createCell(34).setCellValue("З");
        twentysixthRow.createCell(35).setCellValue("Заліковий тиждень");

        twentysixthRow.createCell(40).setCellValue("К");
        twentysixthRow.createCell(41).setCellValue("Канікули");

        twentysixthRow.createCell(44).setCellValue("А");
        twentysixthRow.createCell(45).setCellValue("Захист кваліфікаційної роботи");
    }

    public void fillTable(Sheet sheet) {
        Row row18 = sheet.getRow(18);
        row18.createCell(53).setCellValue("Т теор.навчання");
        row18.createCell(63).setCellFormula("F27");

        Row row19 = sheet.getRow(19);
        row19.createCell(53).setCellValue("С сесія");
        row19.createCell(54).setCellFormula("COUNTIF(B19:W19,BL20)+COUNTIF(B19:W19,BM20)+COUNTIF(B19:W19,BN20)");
        row19.createCell(55).setCellFormula("COUNTIF(X19:BA19,BL20)+COUNTIF(X19:BA19,BM20)+COUNTIF(X19:BA19,BN20)");
        row19.createCell(56).setCellFormula("COUNTIF(B20:W20,BL20)+COUNTIF(B20:W20,BM20)+COUNTIF(B20:W20,BN20)");
        row19.createCell(57).setCellFormula("COUNTIF(X20:BA20,BL20)+COUNTIF(X20:BA20,BM20)+COUNTIF(X20:BA20,BN20)");
        row19.createCell(58).setCellFormula("COUNTIF(B21:W21,BL20)+COUNTIF(B21:W21,BM20)+COUNTIF(B21:W21,BN20)");
        row19.createCell(59).setCellFormula("COUNTIF(X21:BA21,BL20)+COUNTIF(X21:BA21,BM20)+COUNTIF(X21:BA21,BN20)");
        row19.createCell(60).setCellFormula("COUNTIF(B22:W22,BL20)+COUNTIF(B22:W22,BM20)+COUNTIF(B22:W22,BN20)");
        row19.createCell(61).setCellFormula("COUNTIF(X22:BA22,BL20)+COUNTIF(X22:AQ22,BM20)+COUNTIF(X22:AQ22,BN20)");
        row19.createCell(62).setCellFormula("SUM(BC20:BJ20)");
        row19.createCell(63).setCellFormula("N27");
        row19.createCell(64).setCellFormula("AI27");

        Row row20 = sheet.getRow(20);
        row20.createCell(53).setCellValue("П практика");
        row20.createCell(63).setCellFormula("U27");

        Row row21 = sheet.getRow(21);
        row21.createCell(53).setCellValue("Д диплом.проект");
        row21.createCell(63).setCellFormula("Y27");

        Row row22 = sheet.createRow(22);
        row22.createCell(53).setCellValue("К каникули");
        row22.createCell(63).setCellFormula("AO27");

        Row row23 = sheet.createRow(23);
        row23.createCell(53).setCellValue("А атестація");
        row23.createCell(63).setCellFormula("AS27");

        for (int i = 0; i < 6; i++) {
            Row row = sheet.getRow(18 + i);
            if (i == 1) {
                row.createCell(62).setCellFormula("SUM(BC" + (19 + i ) + ":BJ" + (19 + i ) + ")");
                continue;
            }
            row.createCell(54).setCellFormula("COUNTIF(B19:W19,BL" + (19 + i) + ")");
            row.createCell(55).setCellFormula("COUNTIF(X19:BA19,BL" + (19 + i ) + ")");
            row.createCell(56).setCellFormula("COUNTIF(B20:W20,BL" + (19 + i ) + ")");
            row.createCell(57).setCellFormula("COUNTIF(X20:BA20,BL" + (19 + i ) + ")");
            row.createCell(58).setCellFormula("COUNTIF(B21:W21,BL" + (19 + i ) + ")");
            row.createCell(59).setCellFormula("COUNTIF(X21:BA21,BL" + (19 + i ) + ")");
            row.createCell(60).setCellFormula("COUNTIF(B22:W22,BL" + (19 + i ) + ")");
            row.createCell(61).setCellFormula("COUNTIF(X22:BA22,BL" + (19 + i ) + ")");
            row.createCell(62).setCellFormula("SUM(BC" + (19 + i ) + ":BJ" + (19 + i ) + ")");
        }

        Row row24 = sheet.createRow(24);
        row24.createCell(53).setCellValue("Всього");
        for (int i = 53; i <= 62; i++) {
            String cellString = CellReference.convertNumToColString(i);
            row24.createCell(i).setCellFormula("SUM("+ cellString + "19:" + cellString + "24)");
        }
    }

    public void fillTimeline(Sheet sheet) {
        Row seventeenRow = sheet.createRow(17);
        seventeenRow.createCell(1).setCellValue(1);
        for (int i = 1; i <= 8; i++) {
            seventeenRow.createCell(i + 53).setCellValue(i);
        }
        seventeenRow.createCell(62).setCellValue("сум");
        seventeenRow.createCell(63).setCellValue("буквы укр");
        sheet.addMergedRegion(new CellRangeAddress(17,17,63,64));
        for (int i = 2; i <= 52; i++) {
            seventeenRow.createCell(i).setCellFormula(CellReference.convertNumToColString(i - 1)+ "18+1");
        }
        for (int i = 0; i < rowHeaders.size(); i++) {
            Row row = sheet.createRow(i + 18);
            row.createCell(0).setCellValue(rowHeaders.get(i));
            for (int j = 1; j <= 52; j++) {
                String category = Check.getCategory(i,j);
                switch (category) {
                    case "Т" -> row.createCell(j).setCellValue("Т");
                    case "С" -> row.createCell(j).setCellValue("С");
                    case "П" -> row.createCell(j).setCellValue("П");
                    case "Д" -> row.createCell(j).setCellValue("Д");
                    case "З" -> row.createCell(j).setCellValue("З");
                    case "К" -> row.createCell(j).setCellValue("К");
                    case "А" -> row.createCell(j).setCellValue("А");
                    default -> row.createCell(j).setCellValue("");
                }
            }
        }
    }
    public void createConsolidated(Sheet sheet) {
        Row row34 = sheet.createRow(34);
        row34.createCell(0).setCellValue("Курс");
        row34.createCell(2).setCellValue("Теоретичне навчання");
        row34.createCell(6).setCellValue("Екзамен. сесія");
        row34.createCell(9).setCellValue("Практика");
        row34.createCell(12).setCellValue("Атестація");
        row34.createCell(15).setCellValue("Виконання кваліфікаційної роботи");
        row34.createCell(19).setCellValue("Канікули");
        row34.createCell(22).setCellValue("Всього");
        fillConsolidated(sheet);
        Row row40 = sheet.createRow(40);
        row40.createCell(0).setCellValue("Разом");
        row40.createCell(2).setCellFormula("SUM(C37:F40)");
        row40.createCell(6).setCellFormula("SUM(G37:I40)");
        row40.createCell(9).setCellFormula("SUM(J37:L40)");
        row40.createCell(12).setCellFormula("SUM(M37:O40)");
        row40.createCell(15).setCellFormula("SUM(P37:S40)");
        row40.createCell(19).setCellFormula("SUM(T37:V40)");
        row40.createCell(22).setCellFormula("SUM(W37:Y40)");
        mergeConsolidated(sheet);
    }

    private void fillConsolidated(Sheet sheet){
        List<Integer> cellIndex = Arrays.asList(2, 6, 9, 12, 15, 19);
        List<String> firstFormulaValues = Arrays.asList("BC", "BE", "BG", "BI");
        List<String> secondFormulaValues = Arrays.asList("+BD", "+BF", "+BH", "+BJ");

        for (int j = 0; j < cellIndex.size(); j++) {
            for (int i = 0; i < rowHeaders.size(); i++) {
                Row row = sheet.getRow(i + 36);
                if (row == null) {
                    row = sheet.createRow(i + 36);
                }
                row.createCell(0).setCellValue(rowHeaders.get(i));
                String formula = createFormula(firstFormulaValues.get(j), secondFormulaValues.get(j), j);
                row.createCell(cellIndex.get(j)).setCellFormula(formula);
            }
        }

        for (int i = 0; i < 4; i++) {
            Row row = sheet.getRow(i + 36);
            row.createCell(22).setCellFormula("SUM(C" + (37 + i) + ":V" + (37 + i) + ")");
        }
    }

    private void mergeCells(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
    }

    private String createFormula(String firstFormulaValue, String secondFormulaValue, int j) {
        int base = (j == 3) ? 24 : (j > 3) ? (18 + j) : (19 + j);
        return firstFormulaValue + base + secondFormulaValue + base;
    }

    private void mergeConsolidated(Sheet sheet) {
        mergeCells(sheet, 34, 35, 0, 1);
        mergeCells(sheet, 34, 35, 2, 5);
        mergeCells(sheet, 34, 35, 6, 8);
        mergeCells(sheet, 34, 35, 9, 11);
        mergeCells(sheet, 34, 35, 12, 14);
        mergeCells(sheet, 34, 35, 15, 18);
        mergeCells(sheet, 34, 35, 19, 21);
        mergeCells(sheet, 34, 35, 22, 24);

        for (int i = 0; i <= 4; i++) {
            mergeCells(sheet, i + 36, i + 36, 0, 1);
            mergeCells(sheet, i + 36, i + 36, 2, 5);
            mergeCells(sheet, i + 36, i + 36, 6, 8);
            mergeCells(sheet, i + 36, i + 36, 9, 11);
            mergeCells(sheet, i + 36, i + 36, 12, 14);
            mergeCells(sheet, i + 36, i + 36, 15, 18);
            mergeCells(sheet, i + 36, i + 36, 19, 21);
            mergeCells(sheet, i + 36, i + 36, 22, 24);
        }
    }
}
