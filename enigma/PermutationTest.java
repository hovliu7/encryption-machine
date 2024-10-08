package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Hovan Liu
 */
public class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    }


    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet() {
        return new Alphabet();
    }

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation(" (BACD) ", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('A', p.invert('C'));
        assertEquals('C', p.invert('D'));
        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(0, p.invert(2));
        assertEquals(2, p.invert(3));
        assertEquals(1, p.invert(4));
        Permutation p2 = getNewPermutation(" (WORLD) ", getNewAlphabet());
        assertEquals('W', p2.invert('O'));
        assertEquals('D', p2.invert('W'));
        assertEquals('A', p2.invert('A'));
        assertEquals(0, p2.invert(0));
    }

    @Test
    public void testSize() {
        Permutation p = getNewPermutation(" (BACD) ", getNewAlphabet("ABCD"));
        assertEquals(4, p.size());
        Permutation p1 = getNewPermutation(" ", getNewAlphabet(""));
        assertEquals(0, p1.size());
        Permutation p2 = getNewPermutation(" (BACD) ", getNewAlphabet());
        assertEquals(26, p2.size());
    }

    @Test
    public void testPermute() {
        Permutation p = getNewPermutation(" (BACD) ", getNewAlphabet("ABCD"));
        assertEquals('B', p.permute('D'));
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('D', p.permute('C'));
        assertEquals(1, p.permute(3));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(3, p.permute(2));
        assertEquals(0, p.permute(5));
        Permutation p2 = getNewPermutation(" (WORLD) ", getNewAlphabet());
        assertEquals('O', p2.permute('W'));
        assertEquals('W', p2.permute('D'));
        assertEquals('A', p2.permute('A'));
        assertEquals(1, p2.permute(1));
        assertEquals(3, p2.permute(11));
    }

    @Test
    public void testDerangement() {
        Permutation p = getNewPermutation(" (BACD) ", getNewAlphabet("ABCD"));
        assertTrue(p.derangement());
        Permutation p2 = getNewPermutation(" (BAC) ", getNewAlphabet("ABCD"));
        assertFalse(p2.derangement());
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation(" (BACD) ", getNewAlphabet("ABCD"));
        p.invert('F');
    }
}
