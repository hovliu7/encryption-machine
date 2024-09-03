package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Hovan Liu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean atNotch() {
        boolean isAtNotch = false;
        for (int i = 0; i < _notches.length(); i++) {
            char currentNotchChar = _notches.charAt(i);
            int adjustedNotchInt = permutation().wrap
                    (alphabet().toInt(currentNotchChar) - _ringSetting);
            if (adjustedNotchInt == setting()) {
                return true;
            }
        }
        return isAtNotch;
    }

    @Override
    void advance() {
        if (setting() + 1 < alphabet().size()) {
            set(setting() + 1);
        } else {
            set(0);
        }
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    String notches() {
        return _notches;
    }

    /** String of notches in moving rotor. */
    private String _notches;

}
