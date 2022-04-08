package calculator;

public class InputTokenizer {
    private InputTokenizer() {
    }

    static String tokenize(String input) {
        return input.trim()
                .replaceAll("[\u002B]", " + ")
                .replaceAll("[-]", " - ")
                .replaceAll("[\u00D7]", " * ")
                .replaceAll("[\u00F7]", " / ")
                .replaceAll("[\\^]", " ^ ")
                .replaceAll("[(]", " ( ")
                .replaceAll("[)]", " ) ")
                .replaceAll("[=]", " = ")
                .replaceAll("\\s+", " ");
    }
}
