package calculator;

import java.util.Arrays;

public class ExpressionChecker {

    private ExpressionChecker() {
    }

    static boolean checkExpression(String expression) {
        if (expression.contains("(") && expression.contains(")")) {
            if (!checkParenthesesNumber(expression)) {
                return false;
            }
            return expression.indexOf(")") >= expression.indexOf("(");
        } else if (expression.contains("(")) {
            return false;
        } else if (expression.contains(")")) {
            return false;
        } else {
            String operation = "+";
            boolean isOperationsSequence = false;
            for (var element : expression.split(" ")) {
                if (element.matches("[a-zA-Z]+") || element.matches("\\d+")) {
                    isOperationsSequence = false;
                } else if (element.matches("[)+-/*(^]")) {
                    if (isOperationsSequence) {
                        switch (operation) {
                            case "+":
                            case "-":
                                if (!element.matches("[+-]")) {
                                    return false;
                                }
                                break;
                            case "(":
                            case ")":
                                if (!element.matches("[(-)]")) {
                                    return false;
                                }
                                break;
                            default:
                                return false;
                        }
                    } else {
                        operation = element;
                        isOperationsSequence = true;
                    }
                }
            }
        }
        return true;
    }

    private static boolean checkParenthesesNumber(String expression) {
        var openParenthesis = Arrays.stream(expression.split(" ")).filter(e -> e.equals("(")).count();
        var closeParenthesis = Arrays.stream(expression.split(" ")).filter(e -> e.equals(")")).count();
        return openParenthesis == closeParenthesis;
    }
}