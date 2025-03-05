package csit.semit.studyplansrestart.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import csit.semit.studyplansrestart.service.importPackage.ImportService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import csit.semit.studyplansrestart.dto.StringCellDTO.GetExamOrCreditsCellDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtils {
    private static final Logger log = LoggerFactory.getLogger(ImportService.class);

    public static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING -> {
                return cell.getStringCellValue();
            }
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            }
            case FORMULA -> {
                switch (cell.getCachedFormulaResultType()) {
                    case STRING -> {
                        return cell.getRichStringCellValue().getString();
                    }
                    case NUMERIC -> {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return cell.getDateCellValue().toString();
                        } else {
                            return String.valueOf((int) cell.getNumericCellValue());
                        }
                    }
                    default -> {
                        return "";
                    }
                }
            }
            default -> {
                return "";
            }
        }
    }

    public static int getNumberCellValue(Cell cell) {
        if (cell == null) {
            return 0;
        }
        switch (cell.getCellType()) {
            case NUMERIC -> {
                return (int) cell.getNumericCellValue();
            }
            case STRING -> {
                return Integer.parseInt(cell.getStringCellValue().trim());
            }
            case BLANK -> {
                return 0;
            }
            default -> {
                return 0;
            }
        }
    }

    public static GetExamOrCreditsCellDTO getCreditsAndExamsCell(Cell cell) {
        int first = 0;
        int second = 0;
        boolean has = false;

        try {
            if (cell == null) {
                return new GetExamOrCreditsCellDTO(0, 0, false);
            }

            if (cell.getCellType() == CellType.STRING) {
                String cellValue = getStringValueWithoutSpaces(cell);

                if (cellValue.isEmpty()) {
                    return new GetExamOrCreditsCellDTO(0, 0, false);
                }
                if (cellValue.contains("-")) {
                    String[] parts = cellValue.split("-");
                    try {
                        first = Integer.parseInt(parts[0].trim());
                        second = Integer.parseInt(parts[1].trim());
                        has = true;
                    } catch (NumberFormatException e) {
                        log.warn("Failed to parse credits from string: '{}', parts: {}", cellValue, Arrays.toString(parts));
                        return new GetExamOrCreditsCellDTO(0, 0, false);
                    }
                } else {
                    try {
                        first = Integer.parseInt(cellValue);
                    } catch (NumberFormatException e) {
                        log.warn("Failed to parse credit from string: '{}'", cellValue);
                        return new GetExamOrCreditsCellDTO(0, 0, false);
                    }
                }
            } else if (cell.getCellType() == CellType.NUMERIC) {
                first = (int) cell.getNumericCellValue();
            }

            return new GetExamOrCreditsCellDTO(first, second, has);
        } catch (Exception e) {
            log.error("Error processing cell: {}", cell, e);
            return new GetExamOrCreditsCellDTO(0, 0, false);
        }
    }

    public static String getStringValueWithoutSpaces(Cell stringCell) {
        return stringCell.getStringCellValue().replaceAll("[\\s\\u00A0]+", " ").trim();    
    }

    public static void addFormulaPlansCell(int rowNum, Row row) {
        row.createCell(5).setCellFormula("N" + rowNum + "+P" + rowNum + "+R" + rowNum + "+T" + rowNum + "+V" + rowNum + "+X" + rowNum + "+Z" + rowNum + "+AB" + rowNum);
        row.createCell(6).setCellFormula("F" + rowNum + "*30");
        row.createCell(7).setCellFormula("(M" + rowNum + "*Титул!BC$19)+(O" + rowNum + "*Титул!BD$19)+(Q" + rowNum + "*Титул!BE$19)+(S" + rowNum + "*Титул!BF$19)+(U" + rowNum + "*Титул!BG$19)+(W" + rowNum + "*Титул!BH$19)+(Y" + rowNum + "*Титул!BI$19)+(AA" + rowNum + "*Титул!BJ$19)");
        row.createCell(11).setCellFormula("IF(H"+ rowNum +"=I"+ rowNum + "+J"+ rowNum +"+K"+ rowNum +",G"+rowNum + "-H" +rowNum + ",\"!ОШИБКА!\")");
    }

    public static void fillHeadings(Row row, int from, int to) {
        row.createCell(5).setCellFormula("F" + from + " + F" + to);
        row.createCell(6).setCellFormula("G" + from + " + G" + to);
        row.createCell(7).setCellFormula("H" + from + " + H" + to);
        row.createCell(8).setCellFormula("I" + from + " + I" + to);
        row.createCell(9).setCellFormula("J" + from + " + J" + to);
        row.createCell(10).setCellFormula("K" + from + " + K" + to);
        row.createCell(11).setCellFormula("L" + from + " + L" + to);
        row.createCell(12).setCellFormula("M" + from + " + M" + to);
        row.createCell(13).setCellFormula("N" + from + " + N" + to);
        row.createCell(14).setCellFormula("O" + from + " + O" + to);
        row.createCell(15).setCellFormula("P" + from + " + P" + to);
        row.createCell(16).setCellFormula("Q" + from + " + Q" + to);
        row.createCell(17).setCellFormula("R" + from + " + R" + to);
        row.createCell(18).setCellFormula("S" + from + " + S" + to);
        row.createCell(19).setCellFormula("T" + from + " + T" + to);
        row.createCell(20).setCellFormula("U" + from + " + U" + to);
        row.createCell(21).setCellFormula("V" + from + " + V" + to);
        row.createCell(22).setCellFormula("W" + from + " + W" + to);
        row.createCell(23).setCellFormula("X" + from + " + X" + to);
        row.createCell(24).setCellFormula("Y" + from + " + Y" + to);
        row.createCell(25).setCellFormula("Z" + from + " + Z" + to);
        row.createCell(26).setCellFormula("AA" + from + " + AA" + to);
        row.createCell(27).setCellFormula("AB" + from + " + AB" + to);
    }

    public static void fillSecondHeadings(Row row, int first, int second, int third) {
        row.createCell(5).setCellFormula("F" + first + " + F" + second + " + F" + third);
        row.createCell(6).setCellFormula("G" + first + " + G" + second + " + G" + third);
        row.createCell(7).setCellFormula("H" + first + " + H" + second + " + H" + third);
        row.createCell(8).setCellFormula("I" + first + " + I" + second + " + I" + third);
        row.createCell(9).setCellFormula("J" + first + " + J" + second + " + J" + third);
        row.createCell(10).setCellFormula("K" + first + " + K" + second + " + K" + third);
        row.createCell(11).setCellFormula("L" + first + " + L" + second + " + L" + third);
        row.createCell(16).setCellFormula("Q" + first + " + Q" + second + " + Q" + third);
        row.createCell(17).setCellFormula("R" + first + " + R" + second + " + R" + third);
        row.createCell(18).setCellFormula("S" + first + " + S" + second + " + S" + third);
        row.createCell(19).setCellFormula("T" + first + " + T" + second  + " + T" + third);
        row.createCell(20).setCellFormula("U" + first + " + U" + second + " + U" + third);
        row.createCell(21).setCellFormula("V" + first + " + V" + second + " + V" + third);
        row.createCell(22).setCellFormula("W" + first + " + W" + second  + " + W" + third);
        row.createCell(23).setCellFormula("X" + first + " + X" + second + " + X" + third);
        row.createCell(24).setCellFormula("Y" + first + " + Y" + second + " + Y" + third);
        row.createCell(25).setCellFormula("Z" + first + " + Z" + second + " + Z" + third);
    }

    public static void fillSubheading(Row row, int from, int to) {
        row.createCell(5).setCellFormula("SUM(F" + from + ":F" + to + ")");
        row.createCell(6).setCellFormula("SUM(G" + from + ":G" + to + ")");
        row.createCell(7).setCellFormula("SUM(H" + from + ":H" + to + ")");
        row.createCell(8).setCellFormula("SUM(I" + from + ":I" + to + ")");
        row.createCell(9).setCellFormula("SUM(J" + from + ":J" + to + ")");
        row.createCell(10).setCellFormula("SUM(K" + from + ":K" + to + ")");
        row.createCell(11).setCellFormula("SUM(L" + from + ":L" + to + ")");
        row.createCell(12).setCellFormula("SUM(M" + from + ":M" + to + ")");
        row.createCell(13).setCellFormula("SUM(N" + from + ":N" + to + ")");
        row.createCell(14).setCellFormula("SUM(O" + from + ":O" + to + ")");
        row.createCell(15).setCellFormula("SUM(P" + from + ":P" + to + ")");
        row.createCell(16).setCellFormula("SUM(Q" + from + ":Q" + to + ")");
        row.createCell(17).setCellFormula("SUM(R" + from + ":R" + to + ")");
        row.createCell(18).setCellFormula("SUM(S" + from + ":S" + to + ")");
        row.createCell(19).setCellFormula("SUM(T" + from + ":T" + to + ")");
        row.createCell(20).setCellFormula("SUM(U" + from + ":U" + to + ")");
        row.createCell(21).setCellFormula("SUM(V" + from + ":V" + to + ")");
        row.createCell(22).setCellFormula("SUM(W" + from + ":W" + to + ")");
        row.createCell(23).setCellFormula("SUM(X" + from + ":X" + to + ")");
        row.createCell(24).setCellFormula("SUM(Y" + from + ":Y" + to + ")");
        row.createCell(25).setCellFormula("SUM(Z" + from + ":Z" + to + ")");
        row.createCell(26).setCellFormula("SUM(AA" + from + ":AA" + to + ")");
        row.createCell(27).setCellFormula("SUM(AB" + from + ":AB" + to + ")");
    }
    public static void fillSecondSubheading(Row row, int from, int to) {
        row.createCell(5).setCellFormula("SUM(F" + from + ":F" + to + ")");
        row.createCell(6).setCellFormula("SUM(G" + from + ":G" + to + ")");
        row.createCell(7).setCellFormula("SUM(H" + from + ":H" + to + ")");
        row.createCell(8).setCellFormula("SUM(I" + from + ":I" + to + ")");
        row.createCell(9).setCellFormula("SUM(J" + from + ":J" + to + ")");
        row.createCell(10).setCellFormula("SUM(K" + from + ":K" + to + ")");
        row.createCell(11).setCellFormula("SUM(L" + from + ":L" + to + ")");
        row.createCell(16).setCellFormula("SUM(Q" + from + ":Q" + to + ")");
        row.createCell(17).setCellFormula("SUM(R" + from + ":R" + to + ")");
        row.createCell(18).setCellFormula("SUM(S" + from + ":S" + to + ")");
        row.createCell(19).setCellFormula("SUM(T" + from + ":T" + to + ")");
        row.createCell(20).setCellFormula("SUM(U" + from + ":U" + to + ")");
        row.createCell(21).setCellFormula("SUM(V" + from + ":V" + to + ")");
        row.createCell(22).setCellFormula("SUM(W" + from + ":W" + to + ")");
        row.createCell(23).setCellFormula("SUM(X" + from + ":X" + to + ")");
        row.createCell(24).setCellFormula("SUM(Y" + from + ":Y" + to + ")");
        row.createCell(25).setCellFormula("SUM(Z" + from + ":Z" + to + ")");
    }

    public static void fillProfilePack(Row row, int from, int to, int RDrow) {
        row.createCell(5).setCellFormula("IF(SUM(F" + from + ":F" + to + ")=F$" + RDrow + ",F$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(6).setCellFormula("IF(SUM(G" + from + ":G" + to + ")=G$" + RDrow + ",G$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(7).setCellFormula("IF(SUM(H" + from + ":H" + to + ")=H$" + RDrow + ",H$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(8).setCellFormula("IF(SUM(I" + from + ":I" + to + ")=I$" + RDrow + ",I$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(9).setCellFormula("IF(SUM(J" + from + ":J" + to + ")=J$" + RDrow + ",J$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(10).setCellFormula("IF(SUM(K" + from + ":K" + to + ")=K$" + RDrow + ",K$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(11).setCellFormula("IF(SUM(L" + from + ":L" + to + ")=L$" + RDrow + ",L$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(16).setCellFormula("IF(SUM(Q" + from + ":Q" + to + ")=Q$" + RDrow + ",Q$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(17).setCellFormula("IF(SUM(R" + from + ":R" + to + ")=R$" + RDrow + ",R$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(18).setCellFormula("IF(SUM(S" + from + ":S" + to + ")=S$" + RDrow + ",S$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(19).setCellFormula("IF(SUM(T" + from + ":T" + to + ")=T$" + RDrow + ",T$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(20).setCellFormula("IF(SUM(U" + from + ":U" + to + ")=U$" + RDrow + ",U$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(21).setCellFormula("IF(SUM(V" + from + ":V" + to + ")=V$" + RDrow + ",V$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(22).setCellFormula("IF(SUM(W" + from + ":W" + to + ")=W$" + RDrow + ",W$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(23).setCellFormula("IF(SUM(X" + from + ":X" + to + ")=X$" + RDrow + ",X$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(24).setCellFormula("IF(SUM(Y" + from + ":Y" + to + ")=Y$" + RDrow + ",Y$" + RDrow + ",\"ОШИБКА\")");
        row.createCell(25).setCellFormula("IF(SUM(Z" + from + ":Z" + to + ")=Z$" + RDrow + ",Z$" + RDrow + ",\"ОШИБКА\")");
    }

    public static void fillLastRow(Sheet sheet,int index,int M22, int secondComponent) {
        //first last row
        int secondRowIndex = index + 1;
        Row firstRow = sheet.createRow(index);
        firstRow.createCell(0).setCellValue("");
        firstRow.createCell(1).setCellValue("Загальна кількість за термін підготовки");
        sheet.addMergedRegion(new CellRangeAddress(1, 4, index, index));
        firstRow.createCell(5).setCellFormula("F" +secondComponent + "+ F12");
        firstRow.createCell(6).setCellFormula("G" +secondComponent + "+ G12");
        firstRow.createCell(7).setCellFormula("H" +secondComponent + "+ H12");
        firstRow.createCell(11).setCellFormula("L" +secondComponent + "+ L12");
        firstRow.createCell(12).setCellFormula("M" +secondComponent + "+ M12");
        firstRow.createCell(13).setCellFormula("N" +secondComponent + "+ N12");
        firstRow.createCell(14).setCellFormula("O" +secondComponent + "+ O12");
        firstRow.createCell(15).setCellFormula("P" +secondComponent + "+ P12");
        firstRow.createCell(16).setCellFormula("Q" +secondComponent + "+ Q12");
        firstRow.createCell(17).setCellFormula("R" +secondComponent + "+ R12");
        firstRow.createCell(18).setCellFormula("S" +secondComponent + "+ S12");
        firstRow.createCell(19).setCellFormula("T" +secondComponent + "+ T12");
        firstRow.createCell(20).setCellFormula("U" +secondComponent + "+ U12");
        firstRow.createCell(21).setCellFormula("V" +secondComponent + "+ V12");
        firstRow.createCell(22).setCellFormula("W" +secondComponent + "+ W12");
        firstRow.createCell(23).setCellFormula("X" +secondComponent + "+ X12");
        firstRow.createCell(24).setCellFormula("Y" +secondComponent + "+ Y12");
        firstRow.createCell(25).setCellFormula("Z" +secondComponent + "+ Z12");
        firstRow.createCell(26).setCellFormula("AA" +secondComponent + "+ AA12");
        firstRow.createCell(27).setCellFormula("AB" +secondComponent + "+ AB12");

        //second last row
        Row secondRow = sheet.createRow(secondRowIndex);
        secondRow.createCell(0).setCellValue("");
        sheet.addMergedRegion(new CellRangeAddress( secondRowIndex, index+ 5,0, 0));
        secondRow.createCell(1).setCellValue("Кількість годин на тиждень");
        sheet.addMergedRegion(new CellRangeAddress(index + 1, index + 1,1, 11));
        secondRow.createCell(12).setCellFormula("M" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,12, 13));
        secondRow.createCell(14).setCellFormula("O" + index);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,14, 15));
        secondRow.createCell(16).setCellFormula("Q" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,16, 17));
        secondRow.createCell(18).setCellFormula("S" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,18, 19));
        secondRow.createCell(20).setCellFormula("U" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,20, 21));
        secondRow.createCell(22).setCellFormula("W" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,22, 23));
        secondRow.createCell(24).setCellFormula("Y" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,24, 25));
        secondRow.createCell(26).setCellFormula("AA" + secondRowIndex);
        sheet.addMergedRegion(new CellRangeAddress(secondRowIndex, secondRowIndex,26, 27));

        List<String> stringList = Arrays.asList("Кількість екзаменів","Кількість заліків","Кількість курсових проектів (робіт)");
        int forIndex = index + 2;
        for (int i = 0; i < 3; i++) {
            Row row = sheet.createRow(forIndex  + i);
            row.createCell(1).setCellValue(stringList.get(i));
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,1, 11));
            row.createCell(12).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i , forIndex + i,12, 13));
            row.createCell(14).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,14, 15));
            row.createCell(16).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,16, 17));
            row.createCell(18).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,18, 19));
            row.createCell(20).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,20, 21));
            row.createCell(22).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,22, 23));
            row.createCell(24).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,24, 25));
            row.createCell(26).setCellValue(" ");
            sheet.addMergedRegion(new CellRangeAddress(forIndex + i, forIndex + i,26, 27));
        }

        //sixth last row
        int sixthRowIndex = index+ 5;
        Row sixthRow = sheet.createRow(sixthRowIndex);

        sixthRow.createCell(1).setCellValue("Кількість дисциплін у семестрі");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,1, 11));
        sixthRow.createCell(12).setCellFormula("COUNT(M14:M"+ M22 +")+COUNT(M"+(M22 + 2) +":M"+(secondComponent -1) +")+COUNT(M"+(secondComponent + 3) +":M"+(secondComponent + 8) +")+COUNT(M" + (index -2) +":M"+ index +")+COUNT(M"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,12, 13));
        sixthRow.createCell(14).setCellFormula("COUNT(O14:O"+ M22 +")+COUNT(O"+(M22 + 2) +":O"+(secondComponent -1) +")+COUNT(O"+(secondComponent + 3) +":O"+(secondComponent + 8) +")+COUNT(O" + (index -2) +":O"+ index +")+COUNT(O"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,14, 15));
        sixthRow.createCell(16).setCellFormula("COUNT(Q14:Q"+ M22 +")+COUNT(Q"+(M22 + 2) +":Q"+(secondComponent -1) +")+COUNT(Q"+(secondComponent + 3) +":Q"+(secondComponent + 8) +")+COUNT(Q" + (index -2) +":Q"+ index +")+COUNT(Q"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,16, 17));
        sixthRow.createCell(18).setCellFormula("COUNT(S14:S"+ M22 +")+COUNT(S"+(M22 + 2) +":S"+(secondComponent -1) +")+COUNT(S"+(secondComponent + 3) +":S"+(secondComponent + 8) +")+COUNT(S" + (index -2) +":S"+ index +")+COUNT(S"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,18, 19));
        sixthRow.createCell(20).setCellFormula("COUNT(U14:U"+ M22 +")+COUNT(U"+(M22 + 2) +":U"+(secondComponent -1) +")+COUNT(U"+(secondComponent + 3) +":U"+(secondComponent + 8) +")+COUNT(U" + (index -2) +":U"+ index +")+COUNT(U"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,20, 21));
        sixthRow.createCell(22).setCellFormula("COUNT(W14:W"+ M22 +")+COUNT(W"+(M22 + 2) +":W"+(secondComponent -1) +")+COUNT(W"+(secondComponent + 3) +":W"+(secondComponent + 8) +")+COUNT(W" + (index -2) +":W"+ index +")+COUNT(W"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,22, 23));
        sixthRow.createCell(24).setCellFormula("COUNT(Y14:Y"+ M22 +")+COUNT(Y"+(M22 + 2) +":Y"+(secondComponent -1) +")+COUNT(Y"+(secondComponent + 3) +":Y"+(secondComponent + 8) +")+COUNT(Y" + (index -2) +":Y"+ index +")+COUNT(Y"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,24, 25));
        sixthRow.createCell(26).setCellFormula("COUNT(AA14:AA"+ M22 +")+COUNT(AA"+(M22 + 2) +":AA"+(secondComponent -1) +")+COUNT(AA"+(secondComponent + 3) +":AA"+(secondComponent + 8) +")+COUNT(AA" + (index -2) +":AA"+ index +")+COUNT(AA"+(index -4) +")");
        sheet.addMergedRegion(new CellRangeAddress(sixthRowIndex, sixthRowIndex,26, 27));
        
        // String formula = "=COUNT(M14:M"+ M22 +")+COUNT(M"+(M22 + 2) +":M"+(secondComponent -1) +")+COUNT(M"+(secondComponent + 3) +":M"+(secondComponent + 8) +")+COUNT(M" + (index -1) +":M"+(index +1) +")+COUNT(M"+(index -3) +")";

    }

    public static String formatSemesters(List<Integer> semesters) {
        if (semesters.isEmpty()) return "";
        Collections.sort(semesters);
        if (semesters.size() == 1) {
            return String.valueOf(semesters.get(0));
        }
        StringBuilder result = new StringBuilder();
        int start = semesters.get(0);
        int prev = start;
        for (int i = 1; i < semesters.size(); i++) {
            int current = semesters.get(i);
            if (current != prev + 1) {
                if (start == prev) {
                    result.append(start).append(", ");
                } else {
                    result.append(start).append("-").append(prev).append(", ");
                }
                start = current;
            }
            prev = current;
        }
        if (start == prev) {
            result.append(start);
        } else {
            result.append(start).append("-").append(prev);
        }
        return result.toString();
    }

    public static void setStyle(Workbook workbook) {
        Sheet sheet = workbook.getSheet("План НП");
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 20);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setBorderTop(BorderStyle.THIN);    // Верхняя граница
        cellStyle.setBorderBottom(BorderStyle.THIN); // Нижняя граница
        cellStyle.setBorderLeft(BorderStyle.THIN);   // Левая граница
        cellStyle.setBorderRight(BorderStyle.THIN);  // Правая граница
        /////////////
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(font);
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true);
        textStyle.setBorderTop(BorderStyle.THIN);    // Верхняя граница
        textStyle.setBorderBottom(BorderStyle.THIN); // Нижняя граница
        textStyle.setBorderLeft(BorderStyle.THIN);   // Левая граница
        textStyle.setBorderRight(BorderStyle.THIN);  // Правая граница

        for (int i = 11; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < 29; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    if(j <= 1) {
                        cell.setCellStyle(textStyle);
                    }else {
                        cell.setCellStyle(cellStyle);
                    }

                }
            }
        }
    }

}
