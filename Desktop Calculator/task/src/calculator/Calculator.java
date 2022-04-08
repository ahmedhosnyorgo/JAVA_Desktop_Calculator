package calculator;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends JFrame {
    transient SmartCalculator smartCalculator = new SmartCalculator();
    JLabel equationLabel = new JLabel("0");
    JLabel resultLabel = new JLabel();

    private final transient List<String> operation = List.of("\u002B", "-", "\u00D7", "\u00F7");

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
        resultLabel.setForeground(Color.GREEN);
        resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        equationPanel.add(equationLabel);
        equationPanel.add(resultLabel);
        add(equationPanel, BorderLayout.NORTH);
    }

    void addButtonsGrid() {
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(400, 250));

        ArrayList<JButton> buttons = new ArrayList<>();

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

        JButton divideButton = new JButton("\u00F7");
        divideButton.setName("Divide");

        JButton multiplyButton = new JButton("\u00D7");
        multiplyButton.setName("Multiply");

        JButton addButton = new JButton("\u002B");
        addButton.setName("Add");

        JButton dotButton = new JButton(".");
        dotButton.setName("Dot");
        dotButton.setFocusPainted(false);

        JButton subtractButton = new JButton("-");
        subtractButton.setName("Subtract");

        buttons.add(sevenButton);
        buttons.add(eightButton);
        buttons.add(nineButton);
        buttons.add(divideButton);
        buttons.add(fourButton);
        buttons.add(fiveButton);
        buttons.add(sixButton);
        buttons.add(multiplyButton);
        buttons.add(oneButton);
        buttons.add(twoButton);
        buttons.add(threeButton);
        buttons.add(addButton);
        buttons.add(dotButton);
        buttons.add(zeroButton);
        buttons.add(subtractButton);

        JButton clearButton = new JButton("C");
        clearButton.setName("Clear");
        clearButton.setFocusPainted(false);

        JButton deleteButton = new JButton("Del");
        deleteButton.setName("Delete");
        deleteButton.setFocusPainted(false);

        JButton equalsButton = new JButton("=");
        equalsButton.setName("Equals");
        equalsButton.setFocusPainted(false);

        buttonsPanel.add(new JPanel(null));
        buttonsPanel.add(new JPanel(null));
        buttonsPanel.add(clearButton);
        buttonsPanel.add(deleteButton);
        for (var button : buttons) {
            if (button.getText().equals("0")) {
                buttonsPanel.add(button);
                buttonsPanel.add(equalsButton);
            } else {
                buttonsPanel.add(button);
            }
            button.setFocusPainted(false);
        }

        add(buttonsPanel, BorderLayout.CENTER);

        for (var button : buttons) {
            button.addActionListener(e -> {
                if (operation.contains(button.getText())) {
                    equationLabel.setText(equationLabel.getText() + button.getText());
                } else {
                    if (equationLabel.getText().equals("0")) {
                        equationLabel.setText("-");
                        equationLabel.setText(button.getText());
                    } else {
                        equationLabel.setText(equationLabel.getText() + button.getText());
                    }
                }

            });
        }

        clearButton.addActionListener(e -> equationLabel.setText(""));

        deleteButton.addActionListener(e -> {
            if (equationLabel.getText().length() != 0) {
                equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 1));
            }
        });

        equalsButton.addActionListener(e -> {

            if (getResult() != null) {
                resultLabel.setText(getResult());
            }
        });
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
}
