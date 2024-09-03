package enigma;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectorTest {
    private Rotor rotor;

    @Test
    public void test1() {
        Alphabet alph1 = new Alphabet();
        Permutation perm1 = new Permutation("(AE) (BN) (CK)"
            + "(DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", alph1);
        rotor = new Reflector("reflector1", perm1);
        assertEquals(true, rotor.reflecting());
        assertEquals(4, rotor.convertForward(0));
        assertEquals(0, rotor.convertForward(4));
    }

    @Test(expected = EnigmaException.class)
    public void test2() {
        Alphabet alph1 = new Alphabet();
        Permutation perm1 = new Permutation("(AE) (BN) (CK)"
            + "(DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", alph1);
        rotor = new Reflector("reflector1", perm1);
        rotor.convertBackward(2);
    }

}
