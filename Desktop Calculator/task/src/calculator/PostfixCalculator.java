package calculator;


import java.math.BigDecimal;
import java.util.*;

public class PostfixCalculator {
    private  String operation = "+";
    private  boolean isOperationsSequence = false;
    private  String lastOperation = "~";

     BigDecimal calc(Map<String, BigDecimal> varMap, String equation) {
        Deque<String> reorderingStack = new ArrayDeque<>();
        Deque<String> postfixStack = new ArrayDeque<>();

        while (true) {
            assert equation != null;
            if (!equation.contains("$")) break;
            equation = calcSquareRoot(varMap, equation);
        }

        while (true) {
            assert equation != null;
            if (!equation.contains("^")) break;
            equation = calcPower(varMap, equation);
        }
        equation = equation.replaceAll("[~]", "^")
                .replaceAll("\\s+", " ").trim();

        for (var element : equation.split(" ")) {
            transElement(varMap, reorderingStack, postfixStack, element);
        }
        if (isOperationsSequence && !reorderingStack.isEmpty()) {
            pushOperation(reorderingStack, postfixStack, operation);
            isOperationsSequence = false;
        }
        if (!reorderingStack.isEmpty()) {
            pushOperation(reorderingStack, postfixStack);
        }
        return postfixAnswer(postfixStack);
    }

    private  void transElement(Map<String, BigDecimal> varMap, Deque<String> reorderingStack, Deque<String> postfixStack, String element) {
        if (element.matches("[a-zA-Z]+") && varMap.containsKey(element)) {
            if (varMap.get(element) != null) {
                BigDecimal num = varMap.get(element);
                transValue(reorderingStack, postfixStack, num);
            }
        } else if (element.matches("\\d*\\.\\d+|\\d+")) {
            BigDecimal num = new BigDecimal(element);
            transValue(reorderingStack, postfixStack, num);
        } else if (element.matches("[)+-/*(^]")) {
            if (isOperationsSequence) {
                switch (element) {
                    case "-" -> {
                        if (operation.matches("-")) {
                            operation = "+";
                        } else {
                            operation = element;
                        }
                    }
                    case "(" -> {
                        pushOperation(reorderingStack, postfixStack, operation);
                        operation = element;
                        lastOperation = "(";
                    }
                    case ")" -> {
                        if (!lastOperation.equals("(")) {
                            operation = element;
                            pushOperation(reorderingStack, postfixStack, operation);
                        }
                    }
                    default -> operation = element;
                }
            } else {
                if (element.equals(")")) {
                    operation = element;
                    pushOperation(reorderingStack, postfixStack, operation);
                } else {
                    operation = element;
                }
                isOperationsSequence = true;
            }
        }
    }

    private  void transValue(Deque<String> reorderingStack, Deque<String> postfixStack, BigDecimal num) {
        if (isOperationsSequence && ((lastOperation.equals("(")) || postfixStack.isEmpty()) && operation.equals("-")) {
            postfixStack.offerLast(String.valueOf(num.negate()));
            lastOperation = "~";
        } else if (isOperationsSequence) {
            pushOperation(reorderingStack, postfixStack, operation);
            postfixStack.offerLast(String.valueOf(num));
            lastOperation = "~";
        } else {
            postfixStack.offerLast(String.valueOf(num));
        }
        isOperationsSequence = false;
    }

    private  void pushOperation(Deque<String> reorderingStack, Deque<String> postfixStack, String operation) {
        List<String> squareRootPriorities = List.of("&");
        List<String> powerPriorities = List.of("^", "&");
        List<String> multiPriorities = List.of("*", "/", "^");
        List<String> additivePriorities = List.of("+", "-", "*", "/", "^");

        if (reorderingStack.isEmpty() || reorderingStack.peekLast().equals("(")) {
            reorderingStack.offerLast(operation);
            return;
        }
        switch (operation) {
            case "-", "+" -> {      //!reorderingStack.isEmpty() ||
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && additivePriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
            }

            case "/", "*" -> {
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && multiPriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
            }
            case "^" -> {
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && powerPriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
            }
            case "&" -> {
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && squareRootPriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
            }
            case "(" -> reorderingStack.offerLast(operation);
            case ")" -> {
                while (!reorderingStack.isEmpty()) {
                    if (Objects.equals(reorderingStack.peekLast(), "(")) {
                        reorderingStack.pollLast();
                        break;
                    } else {
                        postfixStack.offerLast(reorderingStack.pollLast());
                    }
                }
            }
        }
    }

    private  void pushOperation(Deque<String> reorderingStack, Deque<String> postfixStack) {
        while (!reorderingStack.isEmpty()) {
            if (reorderingStack.peekLast().equals(")") || reorderingStack.peekLast().equals("(")) {
                reorderingStack.pollLast();
            }
            postfixStack.offerLast(reorderingStack.pollLast());
        }
    }

    private  BigDecimal postfixAnswer(Deque<String> postfixStack) {
        Deque<String> answerStack = new ArrayDeque<>();
        while (!postfixStack.isEmpty()) {
            if (postfixStack.peek().matches("-*\\d*\\.\\d+|-*\\d+")) {
                answerStack.offerLast(postfixStack.pollFirst());
            } else if (postfixStack.peek().matches("[+-/*^]")) {
                BigDecimal sndNum = new BigDecimal(Objects.requireNonNull(answerStack.pollLast()));
                BigDecimal fstNum = new BigDecimal(Objects.requireNonNull(answerStack.pollLast()));
                BigDecimal result = LowCalculator.calc(fstNum, sndNum, Objects.requireNonNull(postfixStack.pollFirst()));
                answerStack.offerLast(String.valueOf(result));
            }
        }
        if (answerStack.size() == 1) {
            try {
                return new BigDecimal(answerStack.poll());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private  String calcSquareRoot(Map<String, BigDecimal> varMap, String equation) {
        int squareRootStart = equation.indexOf("$");
        int squareRootEnd = getSubExpressionEnd(equation, squareRootStart);
        String sub = equation.substring(equation.indexOf("(", squareRootStart) + 1, squareRootEnd);
        BigDecimal result = calc(varMap, sub);
        if (result != null) {
            String beforeSquareRoot = equation.substring(0, squareRootStart);
            String afterSquareRoot = equation.substring(squareRootEnd + 1);
            result = result.sqrt(new java.math.MathContext(2));
            return beforeSquareRoot + result + afterSquareRoot;
        } else {
            System.out.println("evaluation error");
            return null;
        }
    }

    private  String calcPower(Map<String, BigDecimal> varMap, String equation) {
        int powerStart = equation.indexOf("^");
        int powerEnd = getSubExpressionEnd(equation, powerStart);
        String sub = equation.substring(equation.indexOf("(", powerStart) + 1, powerEnd);
        BigDecimal result = calc(varMap, sub);
        if (result != null) {
            String beforePower = equation.substring(0, powerStart);
            String afterPower = equation.substring(powerEnd + 1);
            return beforePower + " ~ " + result + afterPower;
        } else {
            System.out.println("evaluation error");
            return null;
        }
    }

    private int getSubExpressionEnd(String equation, int start) {
        String afterPowerSymbol = equation.substring(equation.indexOf("(", start));

        int openedParentheses = -1;
        int powerEnd = -1;
        var chArray = afterPowerSymbol.toCharArray();
        for (int i = 0; i < chArray.length; i++) {
            if (chArray[i] == '(') {
                if (openedParentheses == -1) {
                    openedParentheses = 1;
                } else {
                    openedParentheses++;
                }
            } else if (chArray[i] == ')') {
                openedParentheses--;
            }
            if (openedParentheses == 0) {
                powerEnd = equation.indexOf("(", start) + i;
                break;
            }
        }
        return powerEnd;
    }
}