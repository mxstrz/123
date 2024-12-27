import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Calculator {

    private JFrame frame;
    private JTextField textField;
    private String currentInput = "";  // Текущее выражение для вычисления

    public Calculator() {
        // Инициализация окна
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Текстовое поле для вывода
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 30));
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setEditable(false); // Запрещаем ручной ввод
        frame.add(textField, BorderLayout.NORTH);

        // Панель с кнопками
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));

        // Кнопки калькулятора
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", ".", "=", "+"
        };

        // Добавляем кнопки
        for (String buttonText : buttons) {
            JButton button = new JButton(buttonText);
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        // Добавляем панель с кнопками в окно
        frame.add(panel, BorderLayout.CENTER);

        // Отображаем окно
        frame.setVisible(true);
    }

    // Обработчик нажатий кнопок
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("=")) {
                try {
                    // Вычисляем результат
                    currentInput = String.valueOf(evaluate(currentInput));
                } catch (Exception ex) {
                    currentInput = "Error";
                }
            } else if (command.equals("C")) {
                // Очистить ввод
                currentInput = "";
            } else {
                // Добавляем символ к текущему вводу
                currentInput += command;
            }

            // Обновляем текстовое поле
            textField.setText(currentInput);
        }

        // Метод для вычисления выражения
        private double evaluate(String expression) {
            Stack<Double> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();

            int i = 0;
            while (i < expression.length()) {
                char ch = expression.charAt(i);

                if (Character.isDigit(ch) || ch == '.') {
                    // Парсим число
                    StringBuilder sb = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        sb.append(expression.charAt(i));
                        i++;
                    }
                    numbers.push(Double.parseDouble(sb.toString()));
                } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                    // Обрабатываем оператор
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                        performOperation(numbers, operators.pop());
                    }
                    operators.push(ch);
                    i++;
                } else {
                    i++; // Игнорируем лишние символы
                }
            }

            // Выполняем оставшиеся операции
            while (!operators.isEmpty()) {
                performOperation(numbers, operators.pop());
            }

            return numbers.pop();
        }

        // Метод для определения приоритета операторов
        private int precedence(char operator) {
            switch (operator) {
                case '+':
                case '-':
                    return 1;
                case '*':
                case '/':
                    return 2;
                default:
                    return -1;
            }
        }

        // Метод для выполнения операции
        private void performOperation(Stack<Double> numbers, char operator) {
            double b = numbers.pop();
            double a = numbers.pop();
            switch (operator) {
                case '+':
                    numbers.push(a + b);
                    break;
                case '-':
                    numbers.push(a - b);
                    break;
                case '*':
                    numbers.push(a * b);
                    break;
                case '/':
                    if (b == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    numbers.push(a / b);
                    break;
            }
        }
    }


}
