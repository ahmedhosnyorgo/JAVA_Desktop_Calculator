package calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LowCalculator {
    private LowCalculator() {
    }

    static BigDecimal calc(BigDecimal fstNum, BigDecimal sndNum, String operation) {
        return switch (operation) {
            case "+" -> fstNum.add(sndNum);
            case "-" -> fstNum.subtract(sndNum);
            case "*" -> fstNum.multiply(sndNum);
            case "/" -> {
                try {
                    yield fstNum.divide(sndNum, 2, RoundingMode.DOWN);
                } catch (ArithmeticException e) {
                    yield null;
                }
            }
            case "^" -> fstNum.pow(sndNum.intValue());
            default -> throw new IllegalArgumentException();
        };
    }
}