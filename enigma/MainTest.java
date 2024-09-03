package enigma;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.File;

public class MainTest {

    @Test
    public void testScanner() throws FileNotFoundException {
        File file =
                new File("/Users/hovanliu/repo/proj1/"
                        + "testing/correct/default.conf");
        Scanner config = new Scanner(file);
        System.out.println(config.nextLine());
        System.out.println(config.nextInt());
        System.out.println(config.nextInt());
        config.nextLine();
        String currline = "";
        while (config.hasNextLine()) {
            currline = config.nextLine();
            while (currline.trim().equals("")) {
                currline = config.nextLine();
            }

            while (config.hasNext("\\(.+\\)")) {
                currline = currline + config.nextLine();
            }

            System.out.println(currline);
        }
    }

    @Test
    public void testreadRotor() {
        String line = "Beta N    (ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
        Scanner scan = new Scanner(line);
        String rotorName = scan.next();
        System.out.println("rotor name: " + rotorName);
        String typeAndNotches = scan.next();
        char type = typeAndNotches.charAt(0);
        System.out.println("type: " + type);
        String cycles = scan.nextLine();
        System.out.println("cycles: " + cycles);
        if (type == 'M') {
            String notches = typeAndNotches.substring(1);
            System.out.println("notches: " + notches);
        } else if (type == 'N') {
            System.out.println("It's a fixed rotor");
        } else if (type == 'R') {
            System.out.println("It's a reflector");
        } else {
            System.out.println("Invalid rotor type");
        }
    }

    @Test
    public void testsetUp() {
        String settings = "* B Beta III IV I AXLE (YF) (ZH)";
        Scanner scan = new Scanner(settings);
        String first = scan.next();
        if (first.charAt(0) != '*') {
            System.out.println("Does not start w *");
        } else {
            String[] rotors = new String[5];
            int index = 0;
            if (first.length() > 1) {
                rotors[index] = first.substring(1);
                index++;
            }
            while (index < 5) {
                rotors[index] = scan.next();
                index++;
            }
            String setting = scan.next();
            String plugboard = scan.nextLine();

            System.out.println("Rotors: ");
            for (String rotor: rotors) {
                System.out.println(rotor);
            }
            System.out.println("Rotors setting: " + setting);
            System.out.println("Rotors plugboard: " + plugboard);
        }
    }

    @Test
    public void testPrintMessageLine() {
        String message = "QVPQSOKOILPUBKJZPISFXDW";
        while (message.length() > 5) {
            System.out.print(message.substring(0, 5) + " ");
            message = message.substring(5);
        }
        System.out.println(message);
    }

    @Test
    public void testProcess() {
        String input = "*B Beta III IV I AXLE (HQ) (EX) (IP) (TR) (BY) ";
        Scanner scan = new Scanner(input);
        if (scan.hasNext("\\*\\w*")) {
            System.out.println("correct first line");
        }
    }

    @Test
    public void test1() throws FileNotFoundException {
        File file = new File("/Users"
                + "/hovanliu/repo/proj1/testing/correct/defaultTest.conf");
        Scanner scan = new Scanner(file);
        System.out.println("line 1: " + scan.nextLine());
    }
}
