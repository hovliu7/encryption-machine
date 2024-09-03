package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Hovan Liu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        if (_input.hasNext("\\*\\w*")) {
            setUp(m, _input.nextLine());
        } else {
            throw new EnigmaException("First line must be setting");
        }
        while (_input.hasNextLine()) {
            String currLine = _input.nextLine();
            Scanner tempScan = new Scanner(currLine);
            if (tempScan.hasNext("\\*\\w*")) {
                m.resetRotors();
                setUp(m, currLine);
            } else {
                String msg = m.convert(currLine);
                printMessageLine(msg);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine().trim());
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            _config.nextLine();
            while (_config.hasNextLine()) {
                _currLine = _config.nextLine();
                if (!_config.hasNextLine() && _currLine.trim().equals("")) {
                    continue;
                }
                while (_currLine.trim().equals("")) {
                    _currLine = _config.nextLine();
                }
                while (_config.hasNext("\\(.+\\)")) {
                    _currLine = _currLine + _config.nextLine();
                }
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            Scanner scan = new Scanner(_currLine);
            String rotorName = scan.next();
            String typeAndNotches = scan.next();
            char type = typeAndNotches.charAt(0);
            String cycles = scan.nextLine();
            Permutation perm = new Permutation(cycles, _alphabet);
            if (type == 'M') {
                String notches = typeAndNotches.substring(1);
                return new MovingRotor(rotorName, perm, notches);
            } else if (type == 'N') {
                return new FixedRotor(rotorName, perm);
            } else if (type == 'R') {
                return new Reflector(rotorName, perm);
            } else {
                throw new EnigmaException("Invalid rotor type");
            }
        } catch (NoSuchElementException excp) {
            throw error("Bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner scan = new Scanner(settings);
        String first = scan.next();
        if (first.charAt(0) != '*') {
            throw new EnigmaException("Does not start with *");
        } else {
            try {
                String[] rotors = new String[M.numRotors()];
                int index = 0;
                if (first.length() > 1) {
                    String reflectorName = first.substring(1);
                    rotors[index] = reflectorName;
                    index++;
                }
                while (index < M.numRotors()) {
                    rotors[index] = scan.next();
                    index++;
                }
                String setting = scan.next();
                Permutation plugboard = new Permutation("", _alphabet);
                String ringSettings = "";
                while (scan.hasNext()) {
                    if (scan.hasNext("\\(.+\\)")) {
                        plugboard = new Permutation(scan.nextLine(), _alphabet);
                    } else {
                        ringSettings = scan.next();
                    }
                }
                M.setPlugboard(plugboard);
                M.insertRotors(rotors);
                M.setRotors(setting);
                M.ringboard(ringSettings);
            } catch (NoSuchElementException e) {
                throw new EnigmaException("Invalid input line");
            }

        }
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String message = msg;
        while (message.length() > 5) {
            _output.print(message.substring(0, 5) + " ");
            message = message.substring(5);
        }
        _output.println(message);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;

    /** Current line of the _config file. */
    private String _currLine;
}
