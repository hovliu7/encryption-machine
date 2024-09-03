package enigma;

import static enigma.EnigmaException.*;
import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Hovan Liu
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        alphabet = new ArrayList<Character>();
        if (chars.contains("*") || chars.contains("(") || chars.contains(")")) {
            throw new EnigmaException("Invalid alphabet");
        }
        for (int i = 0; i < chars.length(); i++) {
            if (alphabet.contains(chars.charAt(i))) {
                throw new EnigmaException("No character may be duplicated");
            } else {
                alphabet.add(chars.charAt(i));
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alphabet.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return alphabet.contains(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return alphabet.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!alphabet.contains(ch)) {
            throw new EnigmaException("Not a valid index.");
        } else {
            return alphabet.indexOf(ch);
        }
    }

    /** alphabet of Machine. */
    private ArrayList<Character> alphabet;

}
