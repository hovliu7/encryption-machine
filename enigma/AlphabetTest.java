package enigma;

public class AlphabetTest {
    public static void main(String[] args) {
        Alphabet test = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        System.out.println(test.size());
        System.out.println(test.contains('A'));
        System.out.println(test.toChar(0));
        System.out.println(test.toInt('B'));
    }
}
