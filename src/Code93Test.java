import org.junit.Test;
import static org.junit.Assert.*;

public class Code93Test {

    @Test
    public void encodeEmptyString() {
        assertNotNull(Code93.encode(""));
    }

    @Test
    public void encodeSingleDigits() {
        assertEquals(Code93.encode("0"), Code93.encode("0"));
        assertNotNull(Code93.encode("9"));
    }

    @Test
    public void encodeSingleLetters() {
        assertNotNull(Code93.encode("A"));
        assertNotNull(Code93.encode("Z"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeNull() {
        Code93.encode(null);
    }

    @Test
    public void decodeNull() {
        assertNull(Code93.decode(null));
    }

    @Test
    public void decodeEmptyString() {
        assertNull(Code93.decode(""));
    }

    @Test
    public void decodeTooShort() {
        assertNull(Code93.decode("█ █ ████ █"));
    }

    @Test
    public void decodeMissingStartSymbol() {
        assertNull(Code93.decode("█   █ █   ██ █ █   ██ █ █   ██  █ ██ █ █ ████ █"));
    }

    @Test
    public void decodeMissingStopSymbol() {
        assertNull(Code93.decode("█ █ ████ ██ █ █   ██ █ █   ██  █ ██ █   █ █   █"));
    }

    @Test
    public void decodeMissingFinalBar() {
        assertNull(Code93.decode("█ █ ████ ██ █ █   ██ █ █   ██  █ ██ █ █ ████ "));
    }

    @Test
    public void decodeBadChecksumC() {
        assertNull(Code93.decode("█ █ ████ █ ██  █  ██  █  █ █ █ ██   █ █ ██   █  █ ██  █ ██ ██  █  █ ██  ██ ██  █ █ █ ██   ██  █ █  █  ██ █  █ █ ████ █"));
    }

    @Test
    public void decodeBadChecksumK() {
        assertNull(Code93.decode("█ █ ████ █ ██  █  ██  █  █ █ █ ██   █ █ ██   █  █ ██  █ ██ ██  █  █ ██  ██ ██  █ █ █ ██   ██  █ █  █ █ █    █ █ █    █ █ ████ █"));
    }

    @Test
    public void decodeUnknownBlock() {
        assertNull(Code93.decode("█ █ ████ █ ██  █  ██  █  █ █ █ ██ █ █  █ █ ██ █ █  █  █ ██  █  ███ █ ██ ██ █  █ █ ████ █"));
    }

    @Test
    public void roundtripDigits() {
        String input = "0123456789";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripUppercase() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripLowercase() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripStandardSpecialChars() {
        String input = "-. $/+%";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripSlashChars() {
        String input = "!\"#&'()*,:";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripPercentChars() {
        String input = ";<=>?@[\\]^_`{|}~";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripControlChars() {
        String input = "\u0000\u0001\u001A\u001B\u001F\u007F";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripMixedLowerAndUpper() {
        String input = "Hello World";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripMixedAll() {
        String input = "Test-123/ABC_xyz!";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripSingleChar() {
        for (char c = 0; c <= 127; c++) {
            String input = String.valueOf(c);
            String encoded = Code93.encode(input);
            String decoded = Code93.decode(encoded);
            assertEquals("Failed for ASCII " + (int) c, input, decoded);
        }
    }

    @Test
    public void roundtripEmptyString() {
        assertEquals("", Code93.decode(Code93.encode("")));
    }

    @Test
    public void roundtripLongString() {
        String input = "The Quick Brown Fox Jumps Over The Lazy Dog 0123456789!";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripNul() {
        String input = "\u0000";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripDel() {
        String input = "\u007F";
        assertEquals(input, Code93.decode(Code93.encode(input)));
    }

    @Test
    public void roundtripAt() {
        assertEquals("@", Code93.decode(Code93.encode("@")));
    }

    @Test
    public void roundtripBackslash() {
        assertEquals("\\", Code93.decode(Code93.encode("\\")));
    }

    @Test
    public void roundtripTilde() {
        assertEquals("~", Code93.decode(Code93.encode("~")));
    }

    @Test
    public void roundtripBraces() {
        assertEquals("{}", Code93.decode(Code93.encode("{}")));
    }

    @Test
    public void roundtripAllControlChars() {

        for (int i = 1; i <= 26; i++) {
            String input = String.valueOf((char) i);
            assertEquals("Failed for SOH-SUB ASCII " + i,
                    input, Code93.decode(Code93.encode(input)));
        }

        for (int i = 27; i <= 31; i++) {
            String input = String.valueOf((char) i);
            assertEquals("Failed for ESC-US ASCII " + i,
                    input, Code93.decode(Code93.encode(input)));
        }
    }

    @Test
    public void decodeNormalizedA() {
        assertEquals("A", Code93.decode("██████       ██████      █████████████████████████      █████████████      ██████       ██████                   ████████████       ██████      ██████                   █████████████            ██████       ████████████      ███████      ██████      ██████████████████████████      ██████"));
    }

    @Test
    public void decodeNormalizedHello() {
        assertEquals("HELLO", Code93.decode("██████       ██████      █████████████████████████      ███████      ████████████             ██████             ████████████             ██████            ███████      ██████      ███████      ████████████                   ██████       ██████      █████████████                   ██████            ███████      ████████████             ██████             ██████████████████       ██████      █████████████      ████████████       ██████            ███████      ██████      ██████████████████████████      ██████"));
    }
}