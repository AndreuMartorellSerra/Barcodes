import org.junit.Test;
import static org.junit.Assert.*;

public class Code11Test {

    @Test(expected = IllegalArgumentException.class)
    public void encodeNull() {
        Code11.encode(null);
    }

    @Test
    public void encodeSingleZero() {
        assertEquals("█ ██  █ █ █ ██ █ ██  █", Code11.encode("*0*"));
    }

    @Test
    public void encodeSingleDash() {
        assertEquals("█ ██  █ █ ██ █ █ ██  █", Code11.encode("*-*"));
    }

    @Test
    public void encodeAllDigits() {
        assertNotNull(Code11.encode("*0123456789*"));
    }

    @Test
    public void encodeDashesOnly() {
        assertNotNull(Code11.encode("*---*"));
    }

    @Test
    public void encodeMixedDashAndDigits() {
        assertNotNull(Code11.encode("*1-2-3*"));
    }

    @Test
    public void decodeNull() {
        assertNull(Code11.decode(null));
    }

    @Test
    public void decodeEmpty() {
        assertNull(Code11.decode(""));
    }

    @Test
    public void decodeInvalidBlock() {
        assertNull(Code11.decode("█ ███  ██ █ █ █ █ ███ █ ███   █"));
    }

    @Test
    public void decodeInvalidBlock2() {
        assertNull(Code11.decode("█ █      ██   █ "));
    }

    @Test
    public void decodeBlockWithLetters() {
        assertNull(Code11.decode("█ ███   █ █ █ █A█ █ █T█   ██"));
    }

    @Test
    public void decodeSingleZero() {
        assertEquals("*0*", Code11.decode("█ ██  █ █ █ ██ █ ██  █"));
    }

    @Test
    public void decodeSingleStar() {
        assertEquals("**", Code11.decode("█ ██  █ █ ██  █"));
    }

    @Test
    public void decodeWithLeadingTrailingSpaces() {
        assertEquals("*0*", Code11.decode("  █ ██  █ █ █ ██ █ ██  █     "));
    }

    @Test
    public void decodeNormalizedDouble() {
        assertEquals("*0*", Code11.decode("█ ███   █ █ █ ███ █ ███   █"));
    }

    @Test
    public void decodeNormalizedTriple() {
        assertEquals("*0*", Code11.decode("███   █████     ███   ███   ███   █████   ███   █████     ███"));
    }

    @Test
    public void decodeNormalizedLarge() {
        assertEquals("*0*", Code11.decode("███████       ██████████           ███████       ███████       ███████       ██████████       ███████       ██████████            ███████"));
    }

    @Test
    public void decodeNormalizedWithExtraLeading() {
        assertEquals("*0*", Code11.decode("██ ███   █ █ █ ███ █ ███   █"));
    }

    @Test
    public void roundtripSingleDigits() {
        for (char c = '0'; c <= '9'; c++) {
            String input = "*" + c + "*";
            assertEquals(input, Code11.decode(Code11.encode(input)));
        }
    }

    @Test
    public void roundtripDash() {
        assertEquals("*-*", Code11.decode(Code11.encode("*-*")));
    }

    @Test
    public void roundtripAllDigits() {
        String input = "*0123456789*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripMixed() {
        String input = "*121-9087547*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripLong() {
        String input = "*2521471-8574-965121000-785*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripDashesOnly() {
        String input = "*---*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripEmpty() {
        String input = "**";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripSingleZero() {
        assertEquals("*0*", Code11.decode(Code11.encode("*0*")));
    }

    @Test
    public void roundtripDashAtStart() {
        String input = "*-123*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripDashAtEnd() {
        String input = "*123-*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }

    @Test
    public void roundtripMultipleDashes() {
        String input = "*1-2-3-4-5*";
        assertEquals(input, Code11.decode(Code11.encode(input)));
    }
}