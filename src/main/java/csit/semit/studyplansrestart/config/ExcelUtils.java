package csit.semit.studyplansrestart.config;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collections;
import java.util.List;

public class ExcelUtils {

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
            default -> {
                return 0;
            }
        }
    }

    public static Object[] getCreditsCell(Cell cell) {
        Object[] creditsObject = new Object[3];
        if (cell.getCellType() == CellType.STRING) {
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("-")) {
                String[] parts = cellValue.split("-");
                creditsObject[0] = Integer.parseInt(parts[0].trim());
                creditsObject[2] = Integer.parseInt(parts[1].trim());
                creditsObject[1] = true;
            } else {
                creditsObject[0] = Integer.parseInt(cellValue.trim());
            }
        } else {
            creditsObject[0] = (int) cell.getNumericCellValue();
        }
        return creditsObject;
    }

    public static Object[] getExamCell(Cell cell) {
        Object[] examObject = new Object[3];
        if (cell.getCellType() == CellType.STRING) {
            String cellValue = cell.getStringCellValue();
            if (cellValue.contains("-")) {
                String[] parts = cellValue.split("-");
                examObject[0] = Integer.parseInt(parts[0].trim());
                examObject[2] = Integer.parseInt(parts[1].trim());
                examObject[1] = true;
            } else {
                examObject[0] = Integer.parseInt(cellValue.trim());
            }
        } else {
            examObject[0] = (int) cell.getNumericCellValue();
        }
        return examObject;
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
}
