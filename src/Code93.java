import java.util.ArrayList;

public class Code93 {

    private static final String[] codes = {
            "█   █ █  ", // 0 - 0
            "█ █  █   ", // 1 - 1
            "█ █   █  ", // 2 - 2
            "█ █    █ ", // 3 - 3
            "█  █ █   ", // 4 - 4
            "█  █  █  ", // 5 - 5
            "█  █   █ ", // 6 - 6
            "█ █ █    ", // 7 - 7
            "█   █  █ ", // 8 - 8
            "█    █ █ ", // 9 - 9

            "██ █ █   ", // 10 - A
            "██ █  █  ", // 11 - B
            "██ █   █ ", // 12 - C
            "██  █ █  ", // 13 - D
            "██  █  █ ", // 14 - E
            "██   █ █ ", // 15 - F
            "█ ██ █   ", // 16 - G
            "█ ██  █  ", // 17 - H
            "█ ██   █ ", // 18 - I
            "█  ██ █  ", // 19 - J
            "█   ██ █ ", // 20 - K
            "█ █ ██   ", // 21 - L
            "█ █  ██  ", // 22 - M
            "█ █   ██ ", // 23 - N
            "█  █ ██  ", // 24 - O
            "█   █ ██ ", // 25 - P
            "██ ██ █  ", // 26 - Q
            "██ ██  █ ", // 27 - R
            "██ █ ██  ", // 28 - S
            "██ █  ██ ", // 29 - T
            "██  █ ██ ", // 30 - U
            "██  ██ █ ", // 31 - V
            "█ ██ ██  ", // 32 - W
            "█ ██  ██ ", // 33 - X
            "█  ██ ██ ", // 34 - Y
            "█  ███ █ ", // 35 - Z

            "█  █ ███ ", // 36 - -
            "███ █ █  ", // 37 - .
            "███ █  █ ", // 38 - SPACE
            "███  █ █ ", // 39 - $
            "█ ██ ███ ", // 40 - /
            "█ ███ ██ ", // 41 - +
            "██ █ ███ ", // 42 - %

            "█  █  ██ ", // 43 - ($)
            "███ ██ █ ", // 44 - (%)
            "███ █ ██ ", // 45 - (/)
            "█  ██  █ ", // 46 - (+)
            "█ █ ████ ", // Start/Stop
    };

    static String encode(String str) {
        if (str == null) throw new IllegalArgumentException("Input cannot be null");

        String res = "";
        res += codes[47];

        res = encodeCharacters(str, res);

        res = addChecksums(res);

        res += codes[47];
        res += "█";

        return res;
    }

    private static String encodeCharacters(String str, String res) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                res += codes[c - '0'];
            } else if (c >= 'A' && c <= 'Z') {
                res += codes[(c - 'A') + 10];
            } else if (c >= 'a' && c <= 'z') {
                res += codes[46];
                res += codes[(c - 'a') + 10];
            } else {
                res += encodeSpecialChar(c);
            }
        }
        return res;
    }

    private static String encodeSpecialChar(char c) {
        int ascii = c;

        // NUL (0): %U
        if (ascii == 0) return codes[44] + codes[30];

        // SOH–SUB (1–26): ($)A – ($)Z
        if (ascii >= 1 && ascii <= 26) return codes[43] + codes[9 + ascii];

        // ESC–US (27–31): (%)A – (%)E
        if (ascii >= 27 && ascii <= 31) return codes[44] + codes[10 + (ascii - 27)];

        switch (c) {
            case ' ':  return codes[38];
            case '$':  return codes[39];
            case '%':  return codes[42];
            case '+':  return codes[41];
            case '-':  return codes[36];
            case '.':  return codes[37];
            case '/':  return codes[40];

            case '!':  return codes[45] + codes[10]; // /A  33
            case '"':  return codes[45] + codes[11]; // /B  34
            case '#':  return codes[45] + codes[12]; // /C  35
            case '&':  return codes[45] + codes[15]; // /F  38
            case '\'': return codes[45] + codes[16]; // /G  39
            case '(':  return codes[45] + codes[17]; // /H  40
            case ')':  return codes[45] + codes[18]; // /I  41
            case '*':  return codes[45] + codes[19]; // /J  42
            case ',':  return codes[45] + codes[21]; // /L  44
            case ':':  return codes[45] + codes[35]; // /Z  58

            case ';':  return codes[44] + codes[15]; // %F  59
            case '<':  return codes[44] + codes[16]; // %G  60
            case '=':  return codes[44] + codes[17]; // %H  61
            case '>':  return codes[44] + codes[18]; // %I  62
            case '?':  return codes[44] + codes[19]; // %J  63
            case '@':  return codes[44] + codes[31]; // %V  64
            case '[':  return codes[44] + codes[20]; // %K  91
            case '\\': return codes[44] + codes[21]; // %L  92
            case ']':  return codes[44] + codes[22]; // %M  93
            case '^':  return codes[44] + codes[23]; // %N  94
            case '_':  return codes[44] + codes[24]; // %O  95
            case '`':  return codes[44] + codes[32]; // %W  96
            case '{':  return codes[44] + codes[25]; // %P 123
            case '|':  return codes[44] + codes[26]; // %Q 124
            case '}':  return codes[44] + codes[27]; // %R 125
            case '~':  return codes[44] + codes[28]; // %S 126
            case (char) 127: return codes[44] + codes[29]; // %T DEL

            default: return "";
        }
    }

    private static String addChecksums(String res) {
        res = calculateChecksumChar(res, 20);
        return calculateChecksumChar(res, 15);
    }

    private static String calculateChecksumChar(String res, int maxWeight) {
        String[] blocks = extractBlocks(res);
        int total = calculateWeightedSum(blocks, blocks.length - 1, 0, maxWeight);

        int remainder = total % 47;
        res += codes[remainder];

        return res;
    }

    private static String[] extractBlocks(String str) {
        return str.split("(?<=\\G.{9})");
    }

    private static int calculateWeightedSum(String[] blocks, int from, int to, int maxWeight) {
        int total = 0;
        int weight = 1;

        for (int i = from; i >= to; i--) {
            int val = getIndexFromBlock(blocks[i]);
            total += val * weight;

            weight++;
            if (weight > maxWeight) {
                weight = 1;
            }
        }

        return total;
    }

    private static int getIndexFromBlock(String block) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(block)) return i;
        }
        return -1;
    }

    static String decode(String str) {
        if (str == null || str.isEmpty()) return null;

        str = normalize(str);

        String[] blocks = extractBlocks(str);

        if (!checkMinLength(blocks))    return null;
        if (!checkStartStop(blocks))    return null;
        if (!checkFinalBar(blocks))     return null;
        if (!checkChecksum(blocks))     return null;

        return decodeString(blocks);
    }

    private static String normalize(String str) {
        String[] blocks = getCharBlocks(str);
        int min = findMinBlock(blocks, str.length());

        String result = "";
        for (int i = 0; i < blocks.length; i++) {
            int units = Math.round((float) blocks[i].length() / min);

            char symbol;
            if (i % 2 == 0) {
                symbol = '█';
            } else {
                symbol = ' ';
            }

            result += repeatSymbol(symbol, units);
        }

        return result;
    }

    private static String[] getCharBlocks(String str) {
        ArrayList<String> list = new ArrayList<>();
        String block = String.valueOf(str.charAt(0));

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != str.charAt(i - 1)) {
                list.add(block);
                block = "";
            }
            block += str.charAt(i);
        }
        list.add(block);

        return list.toArray(new String[0]);
    }

    private static int findMinBlock(String[] blocks, int min) {
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i].length() < min) {
                min = blocks[i].length();
            }
        }
        return min;
    }

    private static String repeatSymbol(char symbol, int units) {
        String res = "";
        for (int j = 0; j < units; j++) {
            res += symbol;
        }
        return res;
    }

    private static boolean checkMinLength(String[] blocks) {
        return blocks.length >= 5;
    }

    private static boolean checkStartStop(String[] blocks) {
        return blocks[0].equals(codes[47]) && blocks[blocks.length - 2].equals(codes[47]);
    }

    private static boolean checkFinalBar(String[] blocks) {
        return blocks[blocks.length - 1].equals("█");
    }

    private static boolean checkChecksum(String[] blocks) {
        int len = blocks.length;

        int totalC = calculateWeightedSum(blocks, len - 5, 1, 20);
        int realIndexC = getIndexFromBlock(blocks[len - 4]);
        int expectedIndexC = totalC % 47;

        if (realIndexC != expectedIndexC) return false;

        int totalK = calculateWeightedSum(blocks, len - 4, 1, 15);
        int realIndexK = getIndexFromBlock(blocks[len - 3]);
        int expectedIndexK = totalK % 47;

        if (realIndexK != expectedIndexK) return false;

        return true;
    }

    private static String decodeString(String[] blocks) {
        String res = "";
        for (int i = 1; i < blocks.length - 4; i++) {
            int index = getIndexFromBlock(blocks[i]);
            if (index == -1) return null;

            if (index >= 43 && index <= 46) {
                i++;
                if (i >= blocks.length - 4) return null;
                int next = getIndexFromBlock(blocks[i]);
                if (next == -1) return null;
                char decoded = decodeShiftedChar(index, next);
                if (decoded == (char) -1) return null;
                res += decoded;
            } else {
                char c = getCharFromIndex(index);
                if (c == (char) -1) return null;
                res += c;
            }
        }
        return res;
    }

    private static char decodeShiftedChar(int shift, int next) {
        switch (shift) {
            case 43:
                if (next >= 10 && next <= 35) return (char) (next - 10 + 1);
                break;
            case 44: // (%)
                if (next >= 10 && next <= 14) return (char) (27  + (next - 10)); // %A–E → ESC–US
                if (next >= 15 && next <= 19) return (char) (59  + (next - 15)); // %F–J → ;–?
                if (next >= 20 && next <= 24) return (char) (91  + (next - 20)); // %K–O → [–_
                if (next >= 25 && next <= 29) return (char) (123 + (next - 25)); // %P–T → {–DEL
                if (next == 30) return (char) 0;    // %U → NUL
                if (next == 31) return '@';         // %V → @
                if (next == 32) return '`';         // %W → `
                break;
            case 45: // (/)
                if (next >= 10 && next <= 12) return (char) (33 + (next - 10)); // /A–C → !–#
                if (next >= 15 && next <= 19) return (char) (38 + (next - 15)); // /F–J → &–*
                if (next == 21) return ',';  // /L → ,
                if (next == 35) return ':';  // /Z → :
                break;
            case 46: // (+)A–Z → a–z
                if (next >= 10 && next <= 35) return (char) ('a' + (next - 10));
                break;
        }
        return (char) -1;
    }

    private static char getCharFromIndex(int index) {
        if (index >= 0 && index <= 9) return (char) ('0' + index);
        if (index >= 10 && index <= 35) return (char) ('A' + (index - 10));
        switch (index) {
            case 36: return '-';
            case 37: return '.';
            case 38: return ' ';
            case 39: return '$';
            case 40: return '/';
            case 41: return '+';
            case 42: return '%';
        }
        return (char) -1;
    }
}
