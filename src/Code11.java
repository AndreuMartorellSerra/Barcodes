import java.util.ArrayList;

public class Code11 {

    private static final String[] codes = {
            "█ █ ██",  // 0
            "██ █ ██", // 1
            "█  █ ██", // 2
            "██  █ █", // 3
            "█ ██ ██", // 4
            "██ ██ █", // 5
            "█  ██ █", // 6
            "█ █  ██", // 7
            "██ █  █", // 8
            "██ █ █",  // 9
            "█ ██ █",  // -
            "█ ██  █"  // *
    };

    static String encode(String s) {
        if (s == null) throw new IllegalArgumentException("Input cannot be null");

        String res = "";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-') {
                res += codes[10];
            } else if (c == '*') {
                res += codes[11];
            } else {
                int index = c - '0';
                res += codes[index];
            }
            res += " ";
        }

        return res.trim();
    }

    static String decode(String s) {
        if (s == null || s.isEmpty()) return null;

        s = s.trim();

        s = normalize(s);

        String[] blocks = extractBlocks(s);

        return decodeString(blocks);
    }

    private static String normalize(String s) {
        int avg = average(s);
        return replaceChars(s, avg);
    }

    private static int average(String s) {
        int max = getMaxChar(s);
        int min = getMinChar(s);
        return (max + min) / 2;
    }

    private static int getMaxChar(String s) {
        int charMax = 0;
        int count = 1;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                if (count > charMax) charMax = count;
                count = 1;
            }
        }
        if (count > charMax) charMax = count;

        return charMax;
    }

    private static int getMinChar(String s) {
        int charMin = s.length();
        int count = 1;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                if (count < charMin) charMin = count;
                count = 1;
            }
        }
        if (count < charMin) charMin = count;

        return charMin;
    }

    private static String replaceChars(String s, int avg) {
        s = s + " ";

        String res = "";
        int count = 1;

        for (int i = 1; i < s.length(); i++) {
            char prevChar = s.charAt(i - 1);

            if (s.charAt(i) == prevChar) {
                count++;
            } else {
                res += prevChar;
                if (count > avg) res += prevChar;
                count = 1;
            }
        }

        return res;
    }

    private static String[] extractBlocks(String s) {
        ArrayList<String> list = new ArrayList<>();
        int spaceCount = 0;
        int start = 0;

        s += " ";

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (i < s.length() - 1) {
                char nextChar = s.charAt(i + 1);
                if (c == ' ' && nextChar == ' ') continue;
            }

            if (c == ' ') spaceCount++;

            if (spaceCount == 3) {
                list.add(s.substring(start, i));
                spaceCount = 0;
                start = i + 1;
            }
        }

        return list.toArray(new String[0]);
    }

    private static String decodeString(String[] blocks) {
        String res = "";
        for (int i = 0; i < blocks.length; i++) {
            int index = getIndexFromBlock(blocks[i]);
            if (index == -1) return null;

            char c = getCharFromIndex(index);
            if (c == (char) -1) return null;

            res += c;
        }
        return res;
    }

    private static int getIndexFromBlock(String block) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(block)) return i;
        }
        return -1;
    }

    private static char getCharFromIndex(int index) {
        if (index >= 0 && index <= 9) return (char) ('0' + index);
        if (index == 10) return '-';
        if (index == 11) return '*';
        return (char) -1;
    }
}
