package enigma;

import static enigma.EnigmaException.*;
import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Hovan Liu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not
     *  included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _permutations = new ArrayList<ArrayList<Character>>();
        _cycles = cycles;
        ArrayList<Character> permutation = new ArrayList<Character>();
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                permutation = new ArrayList<Character>();
            } else if (cycles.charAt(i) == ')') {
                _permutations.add(permutation);
            } else if (cycles.charAt(i) != ' ') {
                permutation.add(cycles.charAt(i));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        ArrayList<Character> permutation = new ArrayList<Character>();
        for (int i = 0; i < cycle.length(); i++) {
            permutation.add(cycle.charAt(i));
        }
        _permutations.add(permutation);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int index = wrap(p);
        char preperm = _alphabet.toChar(index);
        if (_cycles.indexOf(preperm) < 0) {
            return index;
        }
        char postperm = ' ';
        for (ArrayList<Character> perm : _permutations) {
            if (perm.contains(preperm)) {
                if (perm.indexOf(preperm) == perm.size() - 1) {
                    postperm = perm.get(0);
                } else {
                    postperm = perm.get(perm.indexOf(preperm) + 1);
                }
            }
        }
        return _alphabet.toInt(postperm);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int index = wrap(c);
        char preperm = _alphabet.toChar(index);
        if (_cycles.indexOf(preperm) < 0) {
            return index;
        }
        char postperm = ' ';
        for (ArrayList<Character> perm : _permutations) {
            if (perm.contains(preperm)) {
                if (perm.indexOf(preperm) == 0) {
                    postperm = perm.get(perm.size() - 1);
                } else {
                    postperm = perm.get(perm.indexOf(preperm) - 1);
                }
            }
        }
        return _alphabet.toInt(postperm);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int preIndex = _alphabet.toInt(p);
        int postIndex = permute(preIndex);
        return _alphabet.toChar(postIndex);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int preIndex = _alphabet.toInt(c);
        int postIndex = invert(preIndex);
        return _alphabet.toChar(postIndex);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        boolean noSelfMap = true;
        for (int i = 0; i < _alphabet.size(); i++) {
            if (_cycles.indexOf(_alphabet.toChar(i)) < 0) {
                noSelfMap = false;
            }
        }
        return noSelfMap;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** List of list of permutations. */
    private ArrayList<ArrayList<Character>> _permutations;

    /** Permutation cycles. */
    private String _cycles;
}
