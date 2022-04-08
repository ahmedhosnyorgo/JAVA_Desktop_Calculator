package calculator;

import java.math.BigDecimal;
import java.util.HashMap;

import static calculator.Evaluator.evaluate;

public class SmartCalculator {
    private final HashMap<String, BigDecimal> varMap = new HashMap<>();

    BigDecimal calc(String input) {
        return evaluate(varMap, InputTokenizer.tokenize(input));
    }
}
