package calculator;

public class InputTokenizer {
    private InputTokenizer() {
    }

    static String tokenize(String input) {
        return input.trim()
                .replaceAll("[+]", " + ")
                .replaceAll("[-]", " - ")
                .replaceAll("[x]", " x ")
                .replaceAll("[/]", " / ")
                .replaceAll("[\\^]", " ^ ")
                .replaceAll("[(]", " ( ")
                .replaceAll("[)]", " ) ")
                .replaceAll("[=]", " = ")
                .replaceAll("\\s+", " ");
    }
}
