package calculator;


import java.math.BigInteger;
import java.util.*;

public class PostfixCalculator {
    private static String operation = "+";
    private static boolean isOperationsSequence = false;
    private static String lastOperation = "~";

    private PostfixCalculator() {
    }

    static BigInteger calc(Map<String, BigInteger> varMap, String input) {
        Deque<String> reorderingStack = new ArrayDeque<>();
        Deque<String> postfixStack = new ArrayDeque<>();

        for (var element : input.split(" ")) {
            transElement(varMap, reorderingStack, postfixStack, element);
        }
        if (isOperationsSequence) {
            pushOperation(reorderingStack, postfixStack, operation);
        }
        pushOperation(reorderingStack, postfixStack);
        return postfixAnswer(postfixStack);
    }

    private static void transElement(Map<String, BigInteger> varMap, Deque<String> reorderingStack, Deque<String> postfixStack, String element) {
        if (element.matches("[a-zA-Z]+") && varMap.containsKey(element)) {
            if (varMap.get(element) != null) {
                BigInteger num = varMap.get(element);
                transValue(reorderingStack, postfixStack, num);
            }
        } else if (element.matches("\\d+")) {
            BigInteger num = new BigInteger(element);
            transValue(reorderingStack, postfixStack, num);
        } else if (element.matches("[)+-/*(^]")) {
            if (isOperationsSequence) {
                switch (element) {
                    case "-":
                        if (operation.matches("-")) {
                            operation = "+";
                        } else {
                            operation = element;
                        }
                        break;
                    case "(":
                        pushOperation(reorderingStack, postfixStack, operation);
                        operation = element;
                        lastOperation = "(";
                        break;
                    case ")":
                        if (!lastOperation.equals("(")) {
                            operation = element;
                            pushOperation(reorderingStack, postfixStack, operation);
                        }
                        break;
                    default:
                        operation = element;
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


    private static void transValue(Deque<String> reorderingStack, Deque<String> postfixStack, BigInteger num) {
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
            case "-":
            case "+": //!reorderingStack.isEmpty() ||
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && additivePriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
                break;
            case "/":
            case "*":
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && multiPriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
                break;
            case "^":
                while (!reorderingStack.isEmpty() && !reorderingStack.peekLast().equals("(") && powerPriorities.contains(reorderingStack.peekLast())) {
                    postfixStack.offerLast(reorderingStack.pollLast());
                }
                reorderingStack.offerLast(operation);
                break;
            case "(":
                reorderingStack.offerLast(operation);
                break;
            case ")":
                while (!reorderingStack.isEmpty()) {
                    if (Objects.equals(reorderingStack.peekLast(), "(")) {
                        reorderingStack.pollLast();
                        break;
                    } else {
                        postfixStack.offerLast(reorderingStack.pollLast());
                    }
                }
                break;
            default:
                break;
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

    private static BigInteger postfixAnswer(Deque<String> postfixStack) {
        Deque<String> answerStack = new ArrayDeque<>();
        while (!postfixStack.isEmpty()) {
            if (postfixStack.peek().matches("-*\\d+")) {
                answerStack.offerLast(postfixStack.pollFirst());
            } else if (postfixStack.peek().matches("[+-/*^]")) {
                BigInteger sndNum = new BigInteger(Objects.requireNonNull(answerStack.pollLast()));
                BigInteger fstNum = new BigInteger(Objects.requireNonNull(answerStack.pollLast()));
                BigInteger result = LowCalculator.calc(fstNum, sndNum, Objects.requireNonNull(postfixStack.pollFirst()));
                answerStack.offerLast(String.valueOf(result));
            }
        }
        if (answerStack.size() == 1) {
            try {
                return new BigInteger(answerStack.poll());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}