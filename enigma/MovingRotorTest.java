package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the MovingRotor class.
 *  @author Hovan Liu
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkConvertForward() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
        assertEquals(alpha.indexOf('P'),
                rotor.convertForward(alpha.indexOf('T')));
        assertEquals(alpha.indexOf('A'),
                rotor.convertForward(alpha.indexOf('U')));
        assertEquals(alpha.indexOf('S'),
                rotor.convertForward(alpha.indexOf('S')));
    }

    @Test
    public void checkConvertBackward() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
        assertEquals(alpha.indexOf('T'),
                rotor.convertBackward(alpha.indexOf('P')));
        assertEquals(alpha.indexOf('U'),
                rotor.convertBackward(alpha.indexOf('A')));
        assertEquals(alpha.indexOf('S'),
                rotor.convertBackward(alpha.indexOf('S')));
    }

    @Test
    public void test1() {
        Alphabet alph1 = new Alphabet("abcdef");
        Permutation perm1 = new Permutation("(abd) (cf)", alph1);
        rotor = new MovingRotor("rotor1", perm1, "c");
        assertEquals(6, rotor.size());
        assertEquals(0, rotor.setting());
        rotor.set(2);
        rotor.set('c');
        assertEquals(2, rotor.setting());
        assertEquals(true, rotor.atNotch());
        assertEquals(2, rotor.convertForward(2));
        rotor.advance();
        assertEquals(3, rotor.setting());
        assertEquals(5, rotor.convertForward(2));
        assertEquals(4, rotor.convertBackward(0));
        rotor.advance();
        rotor.advance();
        rotor.advance();
        rotor.advance();
        rotor.advance();
        assertEquals(2, rotor.setting());
        assertEquals(true, rotor.atNotch());
        rotor.advance();
        assertEquals(false, rotor.atNotch());

        alph1 = new Alphabet();
        perm1 = new Permutation("(AELTPHQXRU)"
            + "(BKNW)(CMOY)(DFG)(IV)(JZ)(S)", alph1);
        rotor = new MovingRotor("rotor2", perm1, "Q");
        rotor.set(5);
        assertEquals(8, rotor.convertForward(5));
    }

}
