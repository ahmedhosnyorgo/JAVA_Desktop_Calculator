package calculator;

import javax.swing.*;
import java.awt.*;

public class Calculator extends JFrame {
    SmartCalculator smartCalculator = new SmartCalculator();
    JTextField equationTextField = new JTextField("2+2");
    JButton solveButton = new JButton("Solve");

    public Calculator() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 120);
        setResizable(false);
        setTitle("Calculator");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    void initComponents() {
        addEquationTextField();
        addSolveButton();
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

    void addSolveButton() {
        JPanel solvePanel = new JPanel();
        solvePanel.setPreferredSize(new Dimension(280, 40));
        solvePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        solveButton.setName("Solve");
        solveButton.setPreferredSize(new Dimension(70, 30));
        solveButton.addActionListener(e -> {
            String input = equationTextField.getText();
            var result = smartCalculator.calc(input);
            if (result != null) {
                equationTextField.setText(input + "=" + result);
            }
        });

        solvePanel.add(solveButton);
        add(solvePanel, BorderLayout.CENTER);
    }
}
