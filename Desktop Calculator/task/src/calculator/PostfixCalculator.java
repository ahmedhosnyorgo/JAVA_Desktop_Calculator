package calculator;


import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.*;

public class PostfixCalculator {
    private static String operation = "+";
    private static boolean isOperationsSequence = false;
    private static String lastOperation = "~";

    private PostfixCalculator() {
    }

    static BigDecimal calc(Map<String, BigDecimal> varMap, String input) {
        Deque<String> reorderingStack = new ArrayDeque<>();
        Deque<String> postfixStack = new ArrayDeque<>();

        for (var element : input.split(" ")) {
            transElement(varMap, reorderingStack, postfixStack, element);
        }
        if (isOperationsSequence) {
            pushOperation(reorderingStack, postfixStack, operation);
            isOperationsSequence = false;
        }
        pushOperation(reorderingStack, postfixStack);
        return postfixAnswer(postfixStack);
    }

    private static void transElement(Map<String, BigDecimal> varMap, Deque<String> reorderingStack, Deque<String> postfixStack, String element) {
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


    private static void transValue(Deque<String> reorderingStack, Deque<String> postfixStack, BigDecimal num) {
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

    private static void pushOperation(Deque<String> reorderingStack, Deque<String> postfixStack, String operation) {
        List<String> powerPriorities = List.of("^");
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
            default -> {
            }
        }
    }

    private static void pushOperation(Deque<String> reorderingStack, Deque<String> postfixStack) {
        while (!reorderingStack.isEmpty()) {
            if (reorderingStack.peekLast().equals(")") || reorderingStack.peekLast().equals("(")) {
                reorderingStack.pollLast();
            }
            postfixStack.offerLast(reorderingStack.pollLast());
        }
    }

    private static BigDecimal postfixAnswer(Deque<String> postfixStack) {
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
}