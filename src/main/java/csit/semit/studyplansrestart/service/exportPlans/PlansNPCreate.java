package csit.semit.studyplansrestart.service.exportPlans;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.returnData.DisciplineCurriculumWithDiscipline;
import csit.semit.studyplansrestart.dto.returnData.SemesterDTO;
import csit.semit.studyplansrestart.service.DisciplineCurriculumService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlansNPCreate {

    DisciplineCurriculumService disciplineCurriculumService;

//    public PlansNPCreate(DisciplineCurriculumService disciplineCurriculumService) {
//        this.disciplineCurriculumService = disciplineCurriculumService;
//    }

    public static Sheet createHeader(Workbook workbook) {

        Sheet sheet = workbook.createSheet("План НП");
        sheet.setColumnWidth(1, 612 * 256 / 7);
        sheet.setColumnWidth(0, 186 * 256 / 7);

        /// Стиль для текста заголовка
        Font fontRotate = workbook.createFont();
        fontRotate.setFontName("Arial");
        fontRotate.setFontHeightInPoints((short) 20);

        ///
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeightInPoints((short) 26);

        // Стиль для повернутого текста
        CellStyle rotatedShifrStyle = workbook.createCellStyle();
        rotatedShifrStyle.setFont(fontRotate);
        rotatedShifrStyle.setRotation((short) 90);
        rotatedShifrStyle.setAlignment(HorizontalAlignment.CENTER);
        rotatedShifrStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        rotatedShifrStyle.setWrapText(true);

        // Стиль для повернутого текста
        CellStyle rotatedStyle = workbook.createCellStyle();
        rotatedStyle.setFont(fontRotate);
        rotatedStyle.setRotation((short) 90);
        rotatedStyle.setAlignment(HorizontalAlignment.CENTER);
        rotatedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        rotatedStyle.setWrapText(true);

        /// Центрированный стиль
        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        centeredStyle.setFont(font);

        /// Центрированный стиль для заголовков
        CellStyle centeredHeaderStyle = workbook.createCellStyle();
        centeredHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        centeredHeaderStyle.setFont(fontRotate);
        centeredHeaderStyle.setWrapText(true);


        Row row = sheet.createRow(0);
        row.createCell(0).setCellFormula("'Основні дані'!A25");
        row.createCell(20).setCellFormula("'Основні дані'!B1");
        sheet.addMergedRegion(new CellRangeAddress(0,0,20,28));

        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("V. ПЛАН НАВЧАЛЬНОГО ПРОЦЕСУ");
        titleCell.setCellStyle(centeredStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 28));

        /// Третья строка
        Row headerRow = sheet.createRow(3);

        headerRow.createCell(0).setCellValue("Шифр за ОПП");
        headerRow.getCell(0).setCellStyle(rotatedShifrStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 9, 0, 0));
        headerRow.createCell(1).setCellValue("Назва навчальної дисципліни");
        headerRow.getCell(1).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 9, 1, 1));
        headerRow.createCell(2).setCellValue("Розподіл за семестрами");
        headerRow.getCell(2).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 4));
        headerRow.createCell(5).setCellValue("Кількість кредитів ECTS");
        headerRow.getCell(5).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 9, 5, 5));
        headerRow.createCell(6).setCellValue("Кількість годин");
        headerRow.getCell(6).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 11));
        headerRow.createCell(12).setCellValue("Розподіл аудиторних годин на тиждень та кредитів ECTS за семестрами");
        headerRow.getCell(12).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 12, 27));
        headerRow.createCell(28).setCellValue("Кафедра");
        headerRow.getCell(28).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 9, 28, 28));

        /// Четвертая строка
        Row subHeaderRow = sheet.createRow(4);

        subHeaderRow.createCell(2).setCellValue("Екзамени");
        subHeaderRow.getCell(2).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 9, 2, 2));
        subHeaderRow.createCell(3).setCellValue("Заліки");
        subHeaderRow.getCell(3).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 9, 3, 3));
        subHeaderRow.createCell(4).setCellValue("Індивідуальні завдання");
        subHeaderRow.getCell(4).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 9, 4, 4));

        subHeaderRow.createCell(6).setCellValue(" ");
        subHeaderRow.getCell(6).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 9, 6, 6));

        subHeaderRow.createCell(7).setCellValue("Аудиторних");
        subHeaderRow.getCell(7).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 7, 10));

        subHeaderRow.createCell(11).setCellValue("Самостійна робота");
        subHeaderRow.getCell(11).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 9, 11, 11));

        subHeaderRow.createCell(12).setCellValue("І курс");
        subHeaderRow.getCell(12).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 12, 15));
        subHeaderRow.createCell(16).setCellValue("ІІ курс");
        subHeaderRow.getCell(16).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 16, 19));
        subHeaderRow.createCell(20).setCellValue("ІІІ курс");
        subHeaderRow.getCell(20).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 20, 23));
        subHeaderRow.createCell(24).setCellValue("ІV курс");
        subHeaderRow.getCell(24).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 24, 27));

        /// Пятая строка
        Row subHeaderRow2 = sheet.createRow(5);
        subHeaderRow2.createCell(7).setCellValue("Всього");
        subHeaderRow2.getCell(7).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(5, 9, 7, 7));
        subHeaderRow2.createCell(8).setCellValue("у тому числі");
        subHeaderRow2.getCell(8).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(5, 6, 8, 10));
        subHeaderRow2.createCell(12).setCellValue("С е м е с т р и");
        subHeaderRow2.getCell(12).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 12, 27));

        /// Шестая строка
        Row subHeaderRow3 = sheet.createRow(6);
        for (int i = 12; i <= 27; i += 2) {
            subHeaderRow3.createCell(i).setCellValue((i - 10) / 2);
            subHeaderRow3.getCell(i).setCellStyle(centeredHeaderStyle);
            sheet.addMergedRegion(new CellRangeAddress(6, 6, i, i + 1));
        }

        /// Седьмая строка
        Row subHeaderRow4 = sheet.createRow(7);
        subHeaderRow4.createCell(8).setCellValue("лекції");
        subHeaderRow4.getCell(8).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(7, 9, 8, 8));
        subHeaderRow4.createCell(9).setCellValue("лабораторні");
        subHeaderRow4.getCell(9).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(7, 9, 9, 9));
        subHeaderRow4.createCell(10).setCellValue("практичні");
        subHeaderRow4.getCell(10).setCellStyle(rotatedStyle);
        sheet.addMergedRegion(new CellRangeAddress(7, 9, 10, 10));
        subHeaderRow4.createCell(12).setCellValue("Кількість тижнів в семестрі");
        subHeaderRow4.getCell(12).setCellStyle(centeredHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 12, 27));

        /// Восьмая строка
        Row subHeaderRow5 = sheet.createRow(8);
        for (int i = 12; i <= 27; i += 2) {
            Cell cell = subHeaderRow5.createCell(i);
            cell.setCellValue(20);
            cell.setCellStyle(centeredHeaderStyle);
            sheet.addMergedRegion(new CellRangeAddress(8, 8, i, i + 1));
        }

        /// Девятая строка
        Row subHeaderRow6 = sheet.createRow(9);
        for (int i = 12; i <= 27; i++) {
            Cell cell = subHeaderRow6.createCell(i);
            if (i % 2 == 0) {
                cell.setCellValue("Аудиторні години");
            } else {
                cell.setCellValue("Кредити ECTS");
            }
            cell.setCellStyle(rotatedStyle);
        }

        /// Десятая строка
        Row numberRow = sheet.createRow(10);
        for (int i = 0; i <= 28; i++) {
            numberRow.createCell(i).setCellValue(i + 1);
            numberRow.getCell(i).setCellStyle(centeredHeaderStyle);
        }
        return sheet;
    }

    public void fillCell(Sheet sheet, long curriculum_id) {
        List<DisciplineCurriculumWithDiscipline> plansInfo = disciplineCurriculumService.getPlansInfo(curriculum_id);
        int index = 11;
        int RDrow = 0;
        int secondComponent = 0;
        int M22 = 0;
        long ZPcount = plansInfo.stream().filter(d -> d.getDiscipline() != null && d.getDiscipline().getShortName().startsWith("ЗП")).count();
        long VDcount = plansInfo.stream().filter(d -> d.getDiscipline() != null && d.getDiscipline().getShortName().startsWith("ВД")).count();
        long SPcount = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !"Спеціальна (фахова) підготовка".equals(d.getDiscipline().getName()))
                .takeWhile(d -> d.getDiscipline() == null || !"Атестація*".equals(d.getDiscipline().getName())).count();
        long count = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !"Загальна підготовка".equals(d.getDiscipline().getName()))
                .takeWhile(d -> d.getDiscipline() == null || !"Спеціальна (фахова) підготовка".equals(d.getDiscipline().getName())).count();
        long number = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !"Профільна підготовка".equals(d.getDiscipline().getName()))
                .takeWhile(d -> d.getDiscipline() == null || !"Дисципліни вільного вибору студента профільної підготовки згідно переліку".equals(d.getDiscipline().getName())).count();
        long RD1count = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !d.getDiscipline().getName().startsWith("Профільований пакет дисциплін 01"))
                .takeWhile(d -> d.getDiscipline() == null || !d.getDiscipline().getName().startsWith("Профільований пакет дисциплін 02")).count();
        long RD2count = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !d.getDiscipline().getName().startsWith("Профільований пакет дисциплін 02"))
                .takeWhile(d -> d.getDiscipline() == null || !d.getDiscipline().getName().startsWith("Профільований пакет дисциплін 03")).count();
        long RD3count = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !d.getDiscipline().getName().startsWith("Профільований пакет дисциплін 03"))
                .takeWhile(d -> d.getDiscipline() == null || !d.getDiscipline().getName().equals("Дисципліни вільного вибору студента профільної підготовки згідно переліку")).count();

        for (DisciplineCurriculumWithDiscipline list : plansInfo) {
            Row row = sheet.createRow(index);
            if("Обов'язкові освітні компоненти".equals(list.getDiscipline().getName())) {
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                ExcelUtils.fillHeadings(row,index + 2, index + (int) count + 2);
                index++;
                continue;
            }
            if ("Спеціальна (фахова) підготовка".equals(list.getDiscipline().getName()) || "Загальна підготовка".equals(list.getDiscipline().getName())) {
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                int targetIndex = index + 1;
                if ("Загальна підготовка".equals(list.getDiscipline().getName())) {
                    targetIndex += (int) ZPcount;
                } else {
                    targetIndex += (int) SPcount;
                    M22 = index;
                }
                ExcelUtils.fillSubheading(row,index + 2, targetIndex);
                index++;
                continue;
            }
            if("Вибіркові освітні компоненти".equals(list.getDiscipline().getName())) {
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                ExcelUtils.fillSecondHeadings(row,index+2, (int)(index+number+2), (int)(index+number+3));
                secondComponent = index+1;
                index++;
                continue;
            }
            if("Профільна підготовка".equals(list.getDiscipline().getName())) {
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                row.createCell(5).setCellFormula("F" + (index + 2));
                row.createCell(6).setCellFormula("G" + (index + 2));
                row.createCell(7).setCellFormula("H" + (index + 2));
                row.createCell(8).setCellFormula("I" + (index + 2));
                row.createCell(9).setCellFormula("J" + (index + 2));
                row.createCell(10).setCellFormula("K" + (index + 2));
                row.createCell(11).setCellFormula("L" + (index + 2));
                row.createCell(16).setCellFormula("Q" + (index + 2));
                row.createCell(17).setCellFormula("R" + (index + 2));
                row.createCell(18).setCellFormula("S" + (index + 2));
                row.createCell(19).setCellFormula("T" + (index + 2));
                row.createCell(20).setCellFormula("U" + (index + 2));
                row.createCell(21).setCellFormula("V" + (index + 2));
                row.createCell(22).setCellFormula("W" + (index + 2));
                row.createCell(23).setCellFormula("X" + (index + 2));
                row.createCell(24).setCellFormula("Y" + (index + 2));
                row.createCell(25).setCellFormula("Z" + (index + 2));
                index++;
                continue;
            }
            if(list.getDiscipline().getName().startsWith("Профільований пакет дисциплін 01")){
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                ExcelUtils.fillSecondSubheading(row,index+2,(int) RD1count + index);
                RDrow = index+1;
                index++;
                continue;
            }
            if(list.getDiscipline().getName().startsWith("Профільований пакет дисциплін 02") || list.getDiscipline().getName().startsWith("Профільований пакет дисциплін 03")) {
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                int targetIndex = index;
                if ("Загальна підготовка".equals(list.getDiscipline().getName())) {
                    targetIndex += (int) RD2count;
                } else {
                    targetIndex += (int) RD3count;
                }
                ExcelUtils.fillProfilePack(row,index+2, targetIndex, RDrow);
                index++;
                continue;
            }
            if("2.2".equals(list.getDiscipline().getShortName())) {
                int rowNum = index + 1;
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                row.createCell(5).setCellFormula("N" + rowNum + "+P" + rowNum + "+R" + rowNum + "+T" + rowNum + "+V" + rowNum + "+X" + rowNum + "+Z" + rowNum + "+AB" + rowNum);
                row.createCell(6).setCellFormula("F" + rowNum + "*30");
                row.createCell(7).setCellFormula("(M" + rowNum + "*Титул!BC$19)+(O" + rowNum + "*Титул!BD$19)+(Q" + rowNum + "*Титул!BE$19)+(S" + rowNum + "*Титул!BF$19)+(U" + rowNum + "*Титул!BG$19)+(W" + rowNum + "*Титул!BH$19)+(Y" + rowNum + "*Титул!BI$19)+(AA" + rowNum + "*Титул!BJ$19)");
                row.createCell(11).setCellFormula("G"+rowNum + "-H" +rowNum );
                fillSemesterCell(row,list);
                index++;
                continue;
            }
            if("2.3".equals(list.getDiscipline().getShortName())) {
                int firstNumber = index + 2;
                int secondNumber = (int) VDcount + index + 1;
                row.createCell(0).setCellValue(list.getDiscipline().getShortName());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                row.createCell(5).setCellFormula("SUM(F" + firstNumber + ":F" + secondNumber + ")");
                row.createCell(6).setCellFormula("SUM(G" +firstNumber + ":G" + secondNumber + ")");
                row.createCell(7).setCellFormula("SUM(H" + firstNumber + ":H" + secondNumber + ")");
                row.createCell(11).setCellFormula("SUM(L" + firstNumber + ":L" + secondNumber + ")");
                row.createCell(24).setCellFormula("SUM(Y" + firstNumber + ":Y" + secondNumber + ")");
                row.createCell(25).setCellFormula("SUM(Z" + firstNumber + ":Z" + secondNumber + ")");
                index++;
                continue;
            }
            fillBasicCell(row,index, list);
            index++;
        }
        ExcelUtils.fillLastRow(sheet,index,M22,secondComponent);
    }

    public static void fillBasicCell(Row row ,int index, DisciplineCurriculumWithDiscipline list) {
        row.createCell(0).setCellValue(list.getDiscipline().getShortName());
        row.createCell(1).setCellValue(list.getDiscipline().getName());
        row.createCell(4).setCellValue(list.getIndividualTaskType());
        ExcelUtils.addFormulaPlansCell(index + 1, row);
        row.createCell(8).setCellValue(list.getLecHours());
        row.createCell(9).setCellValue(list.getLabHours());
        row.createCell(10).setCellValue(list.getPracticeHours());
        fillSemesterCell(row,list);
    }

    public static void fillSemesterCell(Row row , DisciplineCurriculumWithDiscipline list) {
        List<Integer> examSemesters = new ArrayList<>();
        List<Integer> creditSemesters = new ArrayList<>();
        for (SemesterDTO semester : list.getSemesters()) {
            row.createCell(12 + (semester.getSemester() - 1) * 2).setCellValue(semester.getAuditHours());
            row.createCell(12 + (semester.getSemester() - 1) * 2 + 1).setCellValue(semester.getCreditsECTS());
            if (semester.isHasExam()) {
                examSemesters.add(semester.getSemester());
            }
            if (semester.isHasCredit()) {
                creditSemesters.add(semester.getSemester());
            }
        }
        row.createCell(2).setCellValue(ExcelUtils.formatSemesters(examSemesters));
        row.createCell(3).setCellValue(ExcelUtils.formatSemesters(creditSemesters));
    }
}
