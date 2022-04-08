package calculator;


import java.math.BigInteger;
import java.util.Map;

public class IdentifierChecker {
    private IdentifierChecker() {
    }

    static boolean checkIdentifiers(String identifiers) {
        for (var identifier : identifiers.split(" ")) {
            if (identifier.matches(".*[a-zA-Z].*") && !checkIdentifier(identifier)) {
                return false;
            }
        }
        return true;
    }

    static boolean checkIdentifier(String identifier) {
        return identifier.matches("[a-zA-Z]+");
    }

    static boolean isKnownIdentifiers(Map<String, BigInteger> varMap, String input) {
        for (var identifier : input.split(" ")) {
            if (identifier.matches("[a-zA-Z]+") && !varMap.containsKey(identifier)) {
                return false;
            }
        }
        return true;
    }
}
