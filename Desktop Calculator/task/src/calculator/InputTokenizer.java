package calculator;

public class InputTokenizer {
    private static final String ADDITION = "[\u002B]";
    private static final String SUBTRACTION = "[-]";
    private static final String MULTIPLICATION = "[\u00D7]";
    private static final String DIVISION = "[\u00F7]";
    private static final String POWER = "[\\^]";
    private static final String EQUALS = "[=]";
    private static final String LEFT_PARENTHESES = "[(]";
    private static final String NEGATING = "[(]-";
    private static final String RIGHT_PARENTHESES = "[)]";
    private static final String POSITIVE = "[(]\\+";
    private static final char SQUARE_ROOT = '\u221A';

    private InputTokenizer() {
    }

    static String tokenize(String input) {

        String out = input.trim()
                .replaceAll(NEGATING, " ( 0 - ")
                .replaceAll(POSITIVE, " ( 0 + ")
                .replaceAll(ADDITION, " + ")
                .replaceAll(SUBTRACTION, " - ")
                .replaceAll(MULTIPLICATION, " * ")
                .replaceAll(DIVISION, " / ")
                .replaceAll(POWER, " ^ ")
                .replaceAll(LEFT_PARENTHESES, " ( ")
                .replaceAll(RIGHT_PARENTHESES, " ) ")
                .replaceAll(EQUALS, " = ");
        StringBuilder sb = new StringBuilder();
        for (var ch : out.toCharArray()) {
            if (ch != SQUARE_ROOT) {
                sb.append(ch);
            } else {
                sb.append(" $ ");
            }
        }
        return sb.toString().replaceAll("\\s+", " ").trim();
    }
}
