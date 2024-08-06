package ShoujoKagekiNana.util;

import java.util.List;
import java.util.function.Predicate;

public class Util {
    public static <T> void removeFirst(List<T> list, Predicate<T> predicate) {
        for (T t : list) {
            if (predicate.test(t)) {
                list.remove(t);
                break;
            }
        }
    }

    public static String repeatString(String str, int repeat) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
