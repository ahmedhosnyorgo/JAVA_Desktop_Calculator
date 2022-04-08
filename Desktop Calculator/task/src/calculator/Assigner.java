package calculator;

import java.math.BigInteger;
import java.util.Map;

import static calculator.ExpressionChecker.checkExpression;
import static calculator.IdentifierChecker.*;

public class Assigner {
    private Assigner() {
    }

    static void assignVariable(Map<String, BigInteger> varMap, String input) {
        String right = input.substring(0, input.indexOf("=")).trim();
        String left = input.substring(input.indexOf("=") + 1).trim();
        if (!right.contains("(") && !right.contains(")") && checkIdentifier(right)
                && !left.contains("=") && checkExpression(left) && checkIdentifiers(left) && isKnownIdentifiers(varMap, left)) {
            BigInteger leftResult = PostfixCalculator.calc(varMap, left);
            if (leftResult != null) {
                varMap.put(right, leftResult);
            }
        } else if (right.contains("(") || right.contains(")") || !checkIdentifier(right)) {
            System.out.println("Invalid identifier");
        } else if (left.contains("=") || !checkIdentifiers(left)) {
            System.out.println("Invalid assignment");
        } else if (!checkExpression(left)) {
            System.out.println("Invalid expression");
        } else if (!isKnownIdentifiers(varMap, left)) {
            System.out.println("Unknown variable");
        }
    }
}
