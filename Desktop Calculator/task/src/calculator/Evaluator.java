package calculator;

import java.math.BigDecimal;
import java.util.Map;

import static calculator.ExpressionChecker.checkExpression;
import static calculator.IdentifierChecker.checkIdentifiers;
import static calculator.IdentifierChecker.isKnownIdentifiers;

public class Evaluator {
    private Evaluator() {
    }

    static BigDecimal evaluate(Map<String, BigDecimal> varMap, String input) {
        if (checkExpression(input) && checkIdentifiers(input) && isKnownIdentifiers(varMap, input)) {
            PostfixCalculator postfixCalculator = new PostfixCalculator();
            BigDecimal result = postfixCalculator.calc(varMap, input);
            if (result != null) {
                return result;
            } else {
                System.out.println("evaluation error");
                return null;
            }
        } else if (!checkIdentifiers(input)) {
            System.out.println("Invalid identifier");
            return null;
        } else if (!checkExpression(input)) {
            System.out.println("Invalid expression");
            return null;
        } else if (!isKnownIdentifiers(varMap, input)) {
            System.out.println("Unknown variable");
            return null;
        }
        return null;
    }
}
