package jo.ju.edu.cc.core.util;

public class StringUtil {
    public static boolean isEqual(String str1, String str2) {
        return !(isNullOrEmptyOrWhiteSpace(str1) || isNullOrEmptyOrWhiteSpace(str2)) &&
                str1.trim().toLowerCase().equals(str2.trim().toLowerCase());
    }

    public static boolean isNullOrEmptyOrWhiteSpace(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean hasText(String txt) {
        return !isNullOrEmptyOrWhiteSpace(txt);
    }
}
