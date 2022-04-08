package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Calculator extends JFrame {
    transient SmartCalculator smartCalculator = new SmartCalculator();
    JTextField equationTextField = new JTextField();


    public Calculator() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 280);
        setResizable(false);
        setTitle("Calculator");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    void initComponents() {
        addEquationTextField();
        addButtonsGrid();
    }

    void addEquationTextField() {
        JPanel equationPanel = new JPanel();
        equationPanel.setPreferredSize(new Dimension(280, 40));
        equationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        equationTextField.setName("EquationTextField");
        equationTextField.setPreferredSize(new Dimension(260, 30));

        equationPanel.add(equationTextField);
        add(equationPanel, BorderLayout.NORTH);
    }

    void addButtonsGrid() {
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(220, 220));
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

        JButton divideButton = new JButton("/");
        divideButton.setName("Divide");

        JButton multiplyButton = new JButton("x");
        multiplyButton.setName("Multiply");

        JButton addButton = new JButton("+");
        addButton.setName("Add");

        JButton subtractButton = new JButton("-");
        subtractButton.setName("Subtract");

        JPanel hSpacer = new JPanel(null);

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
        buttons.add(zeroButton);
        buttons.add(subtractButton);

        Dimension buttonDimension = new Dimension(50, 50);

        JButton equalsButton = new JButton("=");
        equalsButton.setName("Equals");
        equalsButton.setPreferredSize(buttonDimension);

        for (var button : buttons) {
            if (button.getText().equals("+")) {
                buttonsPanel.add(button);
                buttonsPanel.add(hSpacer);
            } else if (button.getText().equals("0")) {
                buttonsPanel.add(button);
                buttonsPanel.add(equalsButton);
            } else {
                buttonsPanel.add(button);
            }
            button.setPreferredSize(buttonDimension);
            button.setFocusPainted(false);
        }

        add(buttonsPanel, BorderLayout.CENTER);

        for (var button : buttons) {
            if (!button.getText().equals("=")) {
                button.addActionListener(e -> equationTextField.setText(equationTextField.getText() + button.getText()));
            }
        }

        equalsButton.addActionListener(e -> {
            String input = equationTextField.getText();
            var result = smartCalculator.calc(input);
            if (result != null) {
                equationTextField.setText(input + "=" + result);
            }
        });
    }
}
