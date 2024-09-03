package enigma;

import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Hovan Liu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allRotors = new ArrayList<Rotor>(allRotors);
        _numRotors = numRotors;
        _pawls = pawls;
        _rotors = new ArrayList<Rotor>();
    }

    /** Clear all rotors being used. */
    void resetRotors() {
        _rotors = new ArrayList<Rotor>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotors.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("Duplicate rotor name");
                }
            }
        }

        ArrayList<String> allRotorsNames = new ArrayList<String>();
        for (Rotor r: _allRotors) {
            allRotorsNames.add(r.name());
        }

        for (int rotorIndex = 0; rotorIndex < rotors.length; rotorIndex++) {
            for (int allRotorIndex = 0; allRotorIndex < _allRotors.size();
                 allRotorIndex++) {
                if (!allRotorsNames.contains(rotors[rotorIndex])) {
                    throw new EnigmaException("Bad rotor name");
                }
                Rotor currentRotor = _allRotors.get(allRotorIndex);
                if (rotors[rotorIndex].equals(currentRotor.name())) {
                    if (!currentRotor.reflecting() && rotorIndex == 0) {
                        throw new
                                EnigmaException("Reflector in the wrong place");
                    } else {
                        _rotors.add(currentRotor);
                    }
                }
            }

        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int rotorIndex = 1;
        if (setting.length() > numRotors() - 1) {
            throw new EnigmaException("Wheel settings too long");
        } else if (setting.length() < numRotors() - 1) {
            throw new EnigmaException("Wheel settings too short");
        } else {
            for (int i = 0; i < setting.length(); i++) {
                _rotors.get(rotorIndex).set(setting.charAt(i));
                rotorIndex++;
            }
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        boolean[] shouldMoveRotor = new boolean[numRotors()];
        for (int rotorIndex = numRotors() - 1; rotorIndex > 0; rotorIndex--) {
            Rotor currentRotor = _rotors.get(rotorIndex);
            if (currentRotor.atNotch()) {
                Rotor rotorLeft = _rotors.get(rotorIndex - 1);
                if (rotorLeft.rotates()) {
                    shouldMoveRotor[rotorIndex - 1] = true;
                    shouldMoveRotor[rotorIndex] = true;
                }
            }
            if (rotorIndex == numRotors() - 1) {
                shouldMoveRotor[rotorIndex] = true;
            }
        }

        for (int i = 1; i < numRotors(); i++) {
            if (shouldMoveRotor[i]) {
                _rotors.get(i).advance();
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        int result = c;
        for (int rotorIndex = _rotors.size() - 1;
             rotorIndex >= 0; rotorIndex--) {
            Rotor currentRotor = _rotors.get(rotorIndex);
            result = currentRotor.convertForward(result);
        }
        for (int rotorIndex = 1; rotorIndex < _rotors.size(); rotorIndex++) {
            Rotor currentRotor = _rotors.get(rotorIndex);
            result = currentRotor.convertBackward(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                continue;
            }
            int charInt = _alphabet.toInt(msg.charAt(i));
            result = result + _alphabet.toChar(convert(charInt));
        }
        return result;
    }

    void ringboard(String ringSettings) {
        if (ringSettings.length() == 0) {
            return;
        }
        for (int i = 1; i < _numRotors; i++) {
            char ringSettingChar = ringSettings.charAt(i - 1);
            int ringSettingInt = _alphabet.toInt(ringSettingChar);
            Rotor currentRotor = _rotors.get(i);
            currentRotor.changeSetting(ringSettingInt);
        }
    }


    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** List of all available rotors. */
    private ArrayList<Rotor> _allRotors;

    /** Number of rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _pawls;

    /** List of rotors being used. */
    private ArrayList<Rotor> _rotors;

    /** Permutation of the plugboard. */
    private Permutation _plugboard;
}
