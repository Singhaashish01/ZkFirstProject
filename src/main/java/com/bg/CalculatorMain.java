package com.bg;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.util.Stack;

public class CalculatorMain extends SelectorComposer<Window> {

    @Wire
    private Textbox display;

    private StringBuilder expression = new StringBuilder();

    @Listen("onClick = button")
    public void handleClick(Event e) {
        Button btn = (Button) e.getTarget();
        String label = btn.getLabel();

        switch (label) {
            case "C":
                clear();
                break;
            case "=":
                calculate();
                break;
            case "⌫":
                backspace();
                break;
            default:
                addToExpression(label);
                break;
        }
    }

    private void addToExpression(String val) {
        if (val.equals("÷")) val = "/";
        expression.append(val);
        display.setValue(expression.toString());
    }

    private void backspace() {
        if (expression.length() > 0) {
            expression.deleteCharAt(expression.length() - 1);
            display.setValue(expression.toString());
        }
    }

    private void clear() {
        expression.setLength(0);
        display.setValue("");
    }

    private void calculate() {
        try {
            double result = evaluateExpression(expression.toString());
            display.setValue(String.valueOf(result));
            expression.setLength(0);
            expression.append(result);
        } catch (Exception ex) {
            display.setValue("Error");
        }
    }

    // Evaluate expression with proper operator precedence
    private double evaluateExpression(String expr) throws Exception {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int n = expr.length();
        for (int i = 0; i < n; i++) {
            char ch = expr.charAt(i);

            if (ch == ' ') continue;

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < n && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%') {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = ops.pop();
                    numbers.push(applyOp(a, b, op));
                }
                ops.push(ch);
            } else {
                throw new Exception("Invalid character: " + ch);
            }
        }

        while (!ops.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char op = ops.pop();
            numbers.push(applyOp(a, b, op));
        }

        return numbers.pop();
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 0;
    }

    private double applyOp(double a, double b, char op) throws Exception {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': 
                if (b == 0) throw new Exception("Division by zero");
                return a / b;
            case '%': return a % b;
        }
        throw new Exception("Unknown operator: " + op);
    }
}
