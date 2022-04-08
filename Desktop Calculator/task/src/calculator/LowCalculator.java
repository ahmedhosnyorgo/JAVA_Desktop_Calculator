package calculator;

import java.math.BigInteger;

public class LowCalculator {
    private LowCalculator() {
    }

    static BigInteger calc(BigInteger fstNum, BigInteger sndNum, String operation) {
        switch (operation) {
            case "+":
                return fstNum.add(sndNum);
            case "-":
                return fstNum.subtract(sndNum);
            case "x":
                return fstNum.multiply(sndNum);
            case "/":
                return fstNum.divide(sndNum);
            case "^":
                return fstNum.pow(sndNum.intValue());
            default:
                throw new IllegalArgumentException();
        }
    }
}