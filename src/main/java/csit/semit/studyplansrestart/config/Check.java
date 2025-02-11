package csit.semit.studyplansrestart.config;

public class Check {

    public static String getCategory(int row, int col) {
        if (isTeoretic(row, col)) {
            return "Т";
        } else if (isExam(row, col)) {
            return "С";
        } else if (isPractic(row, col)) {
            return "П";
        } else if (isPrepare(row, col)) {
            return "Д";
        } else if (isCredit(row, col)) {
            return "З";
        } else if (isHoliday(row, col)) {
            return "К";
        } else if (isDefense(row, col)) {
            return "А";
        }
        return "";
    }
    //T
    public static boolean isTeoretic(int row, int col) {
        return (1 <= col && col <= 16) || (row == 0 && 25 <= col && col <= 38) || (row < 3 && row > 0 && 23 <= col && col <= 38) || (row == 3 && 23 <= col && col <= 32);
    }
    //C
    public static boolean isExam(int row, int col) {
        return (19 <= col && col <= 21) || (row < 3 && 40 <= col && col <= 42) || (row == 3 && 33 <= col && col <= 34);
    }
    //П
    public static boolean isPractic(int row, int col) {
        return (row == 3 && 35 <= col && col <= 38) || (row == 0 && 23 <= col && col <= 24);
    }
    //Д
    public static boolean isPrepare(int row, int col) {
        return row == 3 && 39 <= col && col <= 40;
    }
    //З
    public static boolean isCredit(int row, int col) {
        return 17 == col || (row < 3 && 39 == col);
    }

    public static boolean isHoliday(int row, int col) {
        return 18 == col  || 22 == col || (row < 3 && 43 <= col && col <= 52);
    }
    public static boolean isDefense(int row, int col) {
        return row == 3 && 41 <= col && 42 >= col;
    }
}
