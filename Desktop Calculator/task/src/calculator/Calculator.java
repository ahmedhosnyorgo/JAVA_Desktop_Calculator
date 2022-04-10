package calculator;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Calculator extends JFrame {
    private static final String ADDITION = "\u002B";
    private static final String SUBTRACTION = "-";
    private static final String MULTIPLICATION = "\u00D7";
    private static final String DIVISION = "\u00F7";
    private static final String SQUARE_ROOT = "√(";
    private static final String POWER_TWO = "^(2)";
    private static final String POWER_Y = "^(";
    private static final String DOT = ".";
    private static final String EQUALS = "=";
    private static final String LEFT_PARENTHESES = "(";
    private static final String RIGHT_PARENTHESES = ")";

    transient SmartCalculator smartCalculator = new SmartCalculator();
    JLabel equationLabel = new JLabel("0");
    boolean start = true;
    JLabel resultLabel = new JLabel();

    private final transient List<String> operation = List.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, POWER_TWO, POWER_Y, LEFT_PARENTHESES, RIGHT_PARENTHESES, SQUARE_ROOT);
    //pay attention to case + . +

    Pattern afterDecimalPattern = Pattern.compile("\\d+\\.");
    Pattern beforeDecimalPattern = Pattern.compile("\\.\\d+");
    Pattern decimalPattern = Pattern.compile("\\d+\\.|\\.\\d+");
    Pattern negatingPattern = Pattern.compile("[(]-.+");

    public Calculator() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 450);
        setTitle("Calculator");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    void initComponents() {
        addEquationTextField();
        addButtonsGrid();
        add(new JPanel(), BorderLayout.PAGE_END);
    }

    void addEquationTextField() {
        JPanel equationPanel = new JPanel();
        equationPanel.setPreferredSize(new Dimension(450, 120));

        equationLabel.setName("EquationLabel");
        equationLabel.setPreferredSize(new Dimension(400, 40));
        equationLabel.setFont(new Font("Tahoma", Font.BOLD, 30));

        resultLabel.setName("ResultLabel");
        resultLabel.setPreferredSize(new Dimension(400, 50));
        resultLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        resultLabel.setForeground(Color.GREEN.darker());
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        equationPanel.add(equationLabel);
        equationPanel.add(resultLabel);
        add(equationPanel, BorderLayout.NORTH);
    }

    void addButtonsGrid() {
        JPanel buttonsPanel = new JPanel(new GridLayout(6, 4, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(400, 250));

        JButton zeroButton = new JButton("0");
        zeroButton.setName("Zero");

        JButton oneButton = new JButton("1");
        oneButton.setName("One");

        JButton twoButton = new JButton("2");
        twoButton.setName("Two");

        JButton threeButton = new JButton("3");
        threeButton.setName("Three");

        JButton fourButton = new JButton("4");
        fourButton.setName("Four");

        JButton fiveButton = new JButton("5");
        fiveButton.setName("Five");

        JButton sixButton = new JButton("6");
        sixButton.setName("Six");

        JButton sevenButton = new JButton("7");
        sevenButton.setName("Seven");

        JButton eightButton = new JButton("8");
        eightButton.setName("Eight");

        JButton nineButton = new JButton("9");
        nineButton.setName("Nine");

        JButton divideButton = new JButton(DIVISION);
        divideButton.setName("Divide");

        JButton multiplyButton = new JButton(MULTIPLICATION);
        multiplyButton.setName("Multiply");

        JButton addButton = new JButton(ADDITION);
        addButton.setName("Add");

        JButton dotButton = new JButton(DOT);
        dotButton.setName("Dot");

        JButton subtractButton = new JButton(SUBTRACTION);
        subtractButton.setName("Subtract");


        JButton clearButton = new JButton("C");
        clearButton.setName("Clear");

        JButton deleteButton = new JButton("Del");
        deleteButton.setName("Delete");

        JButton equalsButton = new JButton(EQUALS);
        equalsButton.setName("Equals");

        JButton parenthesesButton = new JButton(LEFT_PARENTHESES + RIGHT_PARENTHESES);
        parenthesesButton.setName("Parentheses");

        JButton squareRootButton = new JButton(SQUARE_ROOT);
        squareRootButton.setName("SquareRoot");

        JButton powerTwoButton = new JButton(POWER_TWO);
        powerTwoButton.setName("PowerTwo");

        JButton powerYButton = new JButton(POWER_Y);
        powerYButton.setName("PowerY");

        JButton ceButton = new JButton("CE");

        JButton plusMinusButton = new JButton("+-");
        plusMinusButton.setName("PlusMinus");


        for (var button : List.of(parenthesesButton, ceButton, clearButton, deleteButton, powerTwoButton, powerYButton,
                squareRootButton, divideButton, sevenButton, eightButton, nineButton, multiplyButton, fourButton, fiveButton, sixButton,
                subtractButton, oneButton, twoButton, threeButton, addButton, plusMinusButton, zeroButton, dotButton, equalsButton)) {
            buttonsPanel.add(button);
            button.setFocusPainted(false);
        }

        add(buttonsPanel, BorderLayout.CENTER);

        for (var button : List.of(sevenButton, eightButton, nineButton, fourButton,
                fiveButton, sixButton, oneButton, twoButton, threeButton, zeroButton)) {
            button.addActionListener(e -> updateEquation(button));
        }

        clearButton.addActionListener(e -> equationLabel.setText(""));

        deleteButton.addActionListener(e -> setDeletion());

        equalsButton.addActionListener(e -> setEquals());

        parenthesesButton.addActionListener(e -> setParentheses());

        dotButton.addActionListener(e -> setDot());

        plusMinusButton.addActionListener(e -> setPlusMinus());

        addButton.addActionListener(e -> setAddition(addButton));
        subtractButton.addActionListener(e -> setAddition(subtractButton));
        multiplyButton.addActionListener(e -> setMultiplication(multiplyButton));
        divideButton.addActionListener(e -> setMultiplication(divideButton));
        powerYButton.addActionListener(e -> setMultiplication(powerYButton));
        powerTwoButton.addActionListener(e -> setMultiplication(powerTwoButton));
        squareRootButton.addActionListener(e -> setSquareRoot(squareRootButton));
    }

    private void setAddition(JButton button) {
        formatEquation();
        String equation = equationLabel.getText();
        if (start) {
            equation = equation + button.getText();
            start = false;
        } else if (equation.length() != 0) {
            if (Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION).anyMatch(equation::endsWith)) {
                equation = equation.substring(0, equation.length() - 1) + button.getText();
            } else {
                equation = equation + button.getText();
            }
        }
        equationLabel.setText(equation);
        equationLabel.setForeground(Color.RED.darker());
    }

    private void setMultiplication(JButton button) {
        formatEquation();
        String equation = equationLabel.getText();
        if (start) {
            equation = equation + button.getText();
            start = false;
        } else if (equation.length() != 0 && !equation.endsWith("(")) {
            if (Stream.of(LEFT_PARENTHESES + ADDITION, LEFT_PARENTHESES + SUBTRACTION).noneMatch(equation::endsWith) &&
                    Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION).anyMatch(equation::endsWith)) {
                equation = equation.substring(0, equation.length() - 1) + button.getText();
            } else if (Stream.of(LEFT_PARENTHESES + SQUARE_ROOT).noneMatch(equation::endsWith) &&
                    Stream.of(SQUARE_ROOT, POWER_Y).anyMatch(equation::endsWith)) {
                equation = equation.substring(0, equation.length() - 2) + button.getText();
            } else {
                equation = equation + button.getText();
            }
        }
        equationLabel.setText(equation);
        if (!button.getText().equals(POWER_TWO)) {
            equationLabel.setForeground(Color.RED.darker());
        }
    }

    private void setSquareRoot(JButton button) {
        formatEquation();
        String equation = equationLabel.getText();
        if (start || equation.length() == 0) {
            equation = button.getText();
            start = false;
        } else if (Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, POWER_Y, LEFT_PARENTHESES, DOT).anyMatch(equation::endsWith)) {
            equation = equation + button.getText();
        }
        equationLabel.setText(equation);
        equationLabel.setForeground(Color.RED.darker());
    }

    private void setParentheses() {
        formatEquation();
        String equation = equationLabel.getText();
        if (start) {
            equationLabel.setText("(");
            start = false;
        } else if (equation.length() == 0) {
            equationLabel.setText("(");
        } else {
            boolean endsWithNumber = !String.valueOf(equation.charAt(equation.length() - 1)).matches("\\d");
            boolean equalParenthesis = ExpressionChecker.checkParenthesesNumber(InputTokenizer.tokenize(equation));
            boolean endsWithOperation = Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, POWER_TWO, POWER_Y, LEFT_PARENTHESES, SQUARE_ROOT, DOT).anyMatch(equation::endsWith);
            if ((equalParenthesis && endsWithNumber) || endsWithOperation) {
                formatEquation();
                equationLabel.setText(equationLabel.getText() + "(");
                equationLabel.setForeground(Color.RED.darker());
            } else if (equation.contains(LEFT_PARENTHESES)) {
                equationLabel.setText(equationLabel.getText() + ")");
            }
        }
    }

    private void setDot() {
        String equation = equationLabel.getText();
        if (start) {
            equation = equation + DOT;
            start = false;
        } else {
            String lastNum = getLastNum();
            Matcher decimalMatcher = decimalPattern.matcher(lastNum);
            if (!decimalMatcher.find()) {
                equation = equationLabel.getText() + DOT;
            }
        }
        equationLabel.setText(equation);
    }

    private void setPlusMinus() {
        formatEquation();
        String equation = equationLabel.getText();
        Matcher negatingMatcher = negatingPattern.matcher(equation);
        if (start) {
            equation = "(-";
            start = false;
        } else if (equation.equals("(-")) {
            equation = "";
        } else if (negatingMatcher.matches()) {
            if (Stream.of(ADDITION, MULTIPLICATION, DIVISION, POWER_TWO, POWER_Y, RIGHT_PARENTHESES, SQUARE_ROOT, DOT).anyMatch(equation::contains)) {
                equation = equation.substring(3);
            } else {
                equation = equation.substring(2);
            }
        } else {
            if (equation.length() == 0) {
                equation = LEFT_PARENTHESES + SUBTRACTION;
            } else if ((Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, POWER_TWO, POWER_Y, LEFT_PARENTHESES, RIGHT_PARENTHESES, SQUARE_ROOT, DOT).anyMatch(equation::contains))) {
                equation = LEFT_PARENTHESES + SUBTRACTION + LEFT_PARENTHESES + equation;
            } else {
                equation = LEFT_PARENTHESES + SUBTRACTION + equation;
            }
        }
        equationLabel.setText(equation);
    }

    private void setDeletion() {
        if (equationLabel.getText().length() != 0) {
            equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 1));
        }
    }

    private void setEquals() {
        String equation = equationLabel.getText();
        boolean endsWithOperation = Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, LEFT_PARENTHESES)
                .anyMatch(equation::endsWith);
        if (!endsWithOperation) {
            var result = getResult();
            if (result != null) {
                resultLabel.setText(result);
            } else {
                equationLabel.setForeground(Color.red.darker());
            }
        }
    }

    private String getResult() {
        String input = equationLabel.getText();
        var result = smartCalculator.calc(input);
        if (result != null) {
            var intPart = result.toBigInteger();
            var decimalPart = result.subtract(new BigDecimal(intPart));
            if (decimalPart.toString().equals("0.00") || decimalPart.equals(BigDecimal.ZERO)) {
                return intPart.toString();
            } else {
                String decimalPartString = decimalPart.toString();
                int length = decimalPartString.length();
                if ((length == 4 && decimalPart.toString().charAt(3) == '0') || length == 3 && decimalPartString.charAt(2) != '0') {
                    return String.format("%.1f", result);
                } else if (length == 3 && decimalPartString.charAt(2) == '0') {
                    return intPart.toString();
                } else {
                    return String.format("%.2f", result);
                }
            }
        }
        return null;
    }

    void formatEquation() {
        String equation = equationLabel.getText();
        int lastNumStart = lastNumStart();
        String lastNum = getLastNum();
        boolean containsOperation = operation.stream().anyMatch(equation::contains);
        Matcher beforeDecimalMatcher = beforeDecimalPattern.matcher(lastNum);
        Matcher afterDecimalMatcher = afterDecimalPattern.matcher(lastNum);
        if (afterDecimalMatcher.matches()) {
            lastNum = lastNum + "0";
        } else if (beforeDecimalMatcher.matches()) {
            lastNum = "0" + lastNum;
        }
        if (!containsOperation) {
            equationLabel.setText(lastNum);
        } else {
            if (lastNumStart != -1) {
                String str = equationLabel.getText().substring(0, lastNumStart + 1) + lastNum;
                equationLabel.setText(str);
            }
        }
    }

    String getLastNum() {
        String equation = equationLabel.getText();
        String lastNum = "";
        int lastNumStart;
        boolean containsOperation = operation.stream().anyMatch(equation::contains);
        if (!containsOperation) {
            lastNum = equation;
        } else {
            lastNumStart = lastNumStart();
            if (lastNumStart != -1) {
                lastNum = equation.substring(lastNumStart + 1);
            }
        }
        return lastNum;
    }

    int lastNumStart() {
        String equation = equationLabel.getText();
        int lastNumStart;
        lastNumStart = equation.lastIndexOf(ADDITION);
        if (lastNumStart == -1) {
            lastNumStart = equation.lastIndexOf(SUBTRACTION);
        }
        if (lastNumStart == -1) {
            lastNumStart = equation.lastIndexOf(MULTIPLICATION);
        }
        if (lastNumStart == -1) {
            lastNumStart = equation.lastIndexOf(DIVISION);
        }
        if (lastNumStart == -1) {
            lastNumStart = equation.lastIndexOf(LEFT_PARENTHESES);
        }
        if (lastNumStart == -1) {
            lastNumStart = equation.lastIndexOf(RIGHT_PARENTHESES);
        }
        return lastNumStart;
    }

    private void updateEquation(JButton button) {
        if (start) {
            equationLabel.setText(button.getText());
            start = false;
        } else {
            if (!equationLabel.getText().endsWith(")")){
                equationLabel.setText(equationLabel.getText() + button.getText());
            }
        }
        updateEquationLabelColor();
    }

    private void updateEquationLabelColor() {
        equationLabel.setForeground(Color.BLACK);
    }
}
