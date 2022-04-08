package calculator;

import java.math.BigInteger;
import java.util.HashMap;

import static calculator.Evaluator.evaluate;

public class SmartCalculator {
    private final HashMap<String, BigInteger> varMap = new HashMap<>();

    BigInteger calc(String input) {
        return evaluate(varMap, InputTokenizer.tokenize(input));
    }
}
