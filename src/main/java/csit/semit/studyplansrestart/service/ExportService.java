package csit.semit.studyplansrestart.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import csit.semit.studyplansrestart.config.ExcelUtils;
import csit.semit.studyplansrestart.dto.DisciplineCurriculumWithDiscipline;
import csit.semit.studyplansrestart.entity.Semester;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExportService {
    DisciplineService disciplineService;
    DisciplineCurriculumService disciplineCurriculumService;
    SemesterService semesterService;

    public void exportExcel(long curriculum_id) {
        Workbook workbook = new XSSFWorkbook();
        Sheet plansSheet = createHeader(workbook);
        fillCell(plansSheet,curriculum_id);
        try (FileOutputStream fileOut = new FileOutputStream("example.xlsx")) {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        }
        return sheet;
    }

    public  void fillCell(Sheet sheet,long curriculum_id) {
        List<DisciplineCurriculumWithDiscipline> plansInfo = disciplineCurriculumService.getPlansInfo(curriculum_id);
        int index = 11;
        int rowNum = index + 1;
        long ZPcount = plansInfo.stream().filter(d -> d.getDiscipline() != null && d.getDiscipline().getName().startsWith("ЗП")).count();
        long SPcount = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !"Спеціальна (фахова) підготовка".equals(d.getDiscipline().getName()))
                .takeWhile(d -> d.getDiscipline() == null || !"Атестація*".equals(d.getDiscipline().getName())).count();
        long count = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !"Загальна підготовка".equals(d.getDiscipline().getName()))
                .takeWhile(d -> d.getDiscipline() == null || !"Спеціальна (фахова) підготовка".equals(d.getDiscipline().getName())).count();
        long number = plansInfo.stream().dropWhile(d -> d.getDiscipline() == null || !"Профільна підготовка".equals(d.getDiscipline().getName()))
                .takeWhile(d -> d.getDiscipline() == null || !"Дисципліни вільного вибору студента профільної підготовки згідно переліку".equals(d.getDiscipline().getName())).count();
        for (DisciplineCurriculumWithDiscipline list : plansInfo) {
            if("Обов'язкові освітні компоненти".equals(list.getDiscipline().getName())) {
                Row row = sheet.createRow(index);
                row.createCell(0).setCellValue(list.getDiscipline().getShort_name());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                ExcelUtils.fillHeadings(row,index + 2, index + (int) count + 1);
            }
            if ("Спеціальна (фахова) підготовка".equals(list.getDiscipline().getName()) || "Загальна підготовка".equals(list.getDiscipline().getName())) {
                Row row = sheet.createRow(index);
                row.createCell(0).setCellValue(list.getDiscipline().getShort_name());
                row.createCell(1).setCellValue(list.getDiscipline().getName());
                int targetIndex = index + 2;
                if ("Загальна підготовка".equals(list.getDiscipline().getName())) {
                    targetIndex += (int) ZPcount;
                } else {
                    targetIndex += (int) SPcount;
                }
                ExcelUtils.fillSubheading(row,index + 2, targetIndex);
            }
            if("Вибіркові освітні компоненти".equals(list.getDiscipline().getName())) {
                Row row = sheet.createRow(index);
                ExcelUtils.fillSecondHeadings(row,index+2, (int)(index+number+2), (int)(index+number+3));
            }
            if("Профільна підготовка".equals(list.getDiscipline().getName())) {
                Row row = sheet.createRow(index);
                row.createCell(5).setCellFormula("F" + index + 2);
                row.createCell(6).setCellFormula("G" + index + 2);
                row.createCell(7).setCellFormula("H" + index + 2);
                row.createCell(8).setCellFormula("I" + index + 2);
                row.createCell(9).setCellFormula("J" + index + 2);
                row.createCell(10).setCellFormula("K" + index + 2);
                row.createCell(11).setCellFormula("L" + index + 2);
                row.createCell(16).setCellFormula("Q" + index + 2);
                row.createCell(17).setCellFormula("R" + index + 2);
                row.createCell(18).setCellFormula("S" + index + 2);
                row.createCell(19).setCellFormula("T" + index + 2);
                row.createCell(20).setCellFormula("U" + index + 2);
                row.createCell(21).setCellFormula("V" + index + 2);
                row.createCell(22).setCellFormula("W" + index + 2);
                row.createCell(23).setCellFormula("X" + index + 2);
                row.createCell(24).setCellFormula("Y" + index + 2);
                row.createCell(25).setCellFormula("Z" + index + 2);
            }
            List<Integer> examSemesters = new ArrayList<>();
            List<Integer> creditSemesters = new ArrayList<>();
            Row row = sheet.createRow(index);
            row.createCell(0).setCellValue(list.getDiscipline().getShort_name());
            row.createCell(1).setCellValue(list.getDiscipline().getName());
            row.createCell(2).setCellValue(ExcelUtils.formatSemesters(examSemesters));
            row.createCell(3).setCellValue(ExcelUtils.formatSemesters(creditSemesters));
            row.createCell(4).setCellValue(list.getIndividualTaskType());
            ExcelUtils.addFormulaPlansCell(rowNum, row);
            row.createCell(8).setCellValue(list.getLecHours());
            row.createCell(9).setCellValue(list.getLabHours());
            row.createCell(10).setCellValue(list.getPracticeHours());
            for (Semester semester : list.getSemesters()) {
                row.createCell(semester.getSemester() + 11).setCellValue(semester.getAuditHours());
                row.createCell(semester.getSemester() + 12).setCellValue(semester.getCreditsECTS());
                if (semester.isHasExam()) {
                    examSemesters.add(semester.getSemester());
                }
                if (semester.isHasCredit()) {
                    creditSemesters.add(semester.getSemester());
                }
            }

            index++;
        }

    }

}