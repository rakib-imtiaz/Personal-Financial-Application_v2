import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

public class UserDashboard extends JFrame {
    private FinanceManager financeManager;
    private RegularUser user;
    private DefaultListModel<String> recordModel;
    private JList<String> recordList;
    private JTextField amountField;
    private JComboBox<String> typeCombo;
    private JTextField descField;
    private JComboBox<String> categoryCombo;
    private JLabel balanceLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;

    // Color scheme
    private Color primaryColor = new Color(103, 58, 183);  // Deep Purple
    private Color accentColor = new Color(255, 171, 64);   // Orange
    private Color backgroundColor = new Color(245, 245, 245);
    private Color cardColor = Color.WHITE;
    private Color textColor = new Color(33, 33, 33);

    public UserDashboard(FinanceManager financeManager, RegularUser user) {
        super("FinanceTracker Pro - " + user.getUsername());
        this.financeManager = financeManager;
        this.user = user;

        // Set undecorated and custom border
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, backgroundColor,
                    0, getHeight(), backgroundColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add components
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        add(mainPanel);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add window drag functionality
        addDragListener();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // User info and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, " + user.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setForeground(primaryColor);

        JButton logoutButton = createStyledButton("Logout", new Color(239, 83, 80));
        logoutButton.addActionListener(e -> {
            if (showConfirmDialog("Are you sure you want to logout?")) {
                new LoginFrame().showLogin();
                dispose();
            }
        });

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);

        panel.add(userPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Summary Cards
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(createSummaryPanel(), gbc);

        // Add Transaction Form
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.4;
        panel.add(createAddTransactionPanel(), gbc);

        // Transaction List
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(createTransactionListPanel(), gbc);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);

        balanceLabel = createSummaryCard("Total Balance", "$0.00", primaryColor);
        incomeLabel = createSummaryCard("Total Income", "$0.00", new Color(76, 175, 80));
        expenseLabel = createSummaryCard("Total Expenses", "$0.00", new Color(244, 67, 54));

        panel.add(balanceLabel);
        panel.add(incomeLabel);
        panel.add(expenseLabel);

        return panel;
    }

    private JLabel createSummaryCard(String title, String amount, Color color) {
        JLabel card = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw card background
                g2d.setColor(cardColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                // Draw colored stripe at top
                g2d.setColor(color);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 5, 5, 5));

                super.paintComponent(g);
            }
        };

        card.setPreferredSize(new Dimension(250, 100));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(textColor);

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        amountLabel.setForeground(color);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(amountLabel);

        return card;
    }

    private JPanel createAddTransactionPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(cardColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Add Transaction");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form fields
        amountField = createStyledTextField("Amount");
        
        String[] types = {"INCOME", "EXPENSE"};
        typeCombo = createStyledComboBox(types);
        
        String[] categories = {"Salary", "Food", "Transport", "Entertainment", "Other"};
        categoryCombo = createStyledComboBox(categories);
        
        descField = createStyledTextField("Description");

        JButton addButton = createStyledButton("Add Transaction", primaryColor);
        addButton.addActionListener(e -> addRecord());

        // Add components with spacing
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(createFormRow("Amount:", amountField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("Type:", typeCombo));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("Category:", categoryCombo));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("Description:", descField));
        panel.add(Box.createVerticalStrut(25));
        panel.add(addButton);

        return panel;
    }

    private JPanel createTransactionListPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(cardColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
            }
        };
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Recent Transactions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(primaryColor);
        // Transaction list
        recordModel = new DefaultListModel<>();
        recordList = new JList<>(recordModel);
        recordList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(10, 10, 10, 10));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return label;
            }
        });
        recordList.setBackground(cardColor);
        recordList.setSelectionBackground(primaryColor.brighter());
        recordList.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(recordList);
        scrollPane.setBorder(null);
        scrollPane.setBackground(cardColor);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(component);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 10, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(cardColor);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return box;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else {
                    g2.setColor(getBackground());
                }
                
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }

    private void addDragListener() {
        Point offset = new Point();
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                offset.setLocation(evt.getX(), evt.getY());
            }
        });
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                setLocation(evt.getXOnScreen() - offset.x, evt.getYOnScreen() - offset.y);
            }
        });
    }

    private boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void addRecord() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            String type = (String) typeCombo.getSelectedItem();
            String category = (String) categoryCombo.getSelectedItem();
            String description = descField.getText().trim();

            if (description.isEmpty()) {
                showError("Please enter a description");
                return;
            }

            if (amount <= 0) {
                showError("Amount must be greater than zero");
                return;
            }

            FinancialRecord record = new FinancialRecord(amount, type, description, category, user.getUsername());
            financeManager.addRecord(record);
            refreshRecords();
            clearInputFields();
            showSuccess("Transaction added successfully");
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        }
    }

    private void refreshRecords() {
        recordModel.clear();
        List<FinancialRecord> userRecords = financeManager.getUserRecords(user.getUsername());
        double totalIncome = 0;
        double totalExpense = 0;

        for (FinancialRecord record : userRecords) {
            String prefix = record.getType().equals("INCOME") ? "+" : "-";
            String formattedAmount = String.format("$%.2f", record.getAmount());
            String date = record.getDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            
            recordModel.addElement(String.format("%s %s | %s | %s | %s", 
                prefix, formattedAmount, record.getCategory(), record.getDescription(), date));
            
            if (record.getType().equals("INCOME")) {
                totalIncome += record.getAmount();
            } else {
                totalExpense += record.getAmount();
            }
        }

        // Update summary cards with animations
        updateSummaryLabel(balanceLabel, "Total Balance", totalIncome - totalExpense);
        updateSummaryLabel(incomeLabel, "Total Income", totalIncome);
        updateSummaryLabel(expenseLabel, "Total Expenses", totalExpense);
    }

    private void updateSummaryLabel(JLabel label, String title, double amount) {
        String formattedAmount = String.format("$%.2f", amount);
        SwingUtilities.invokeLater(() -> {
            label.removeAll();
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(textColor);

            JLabel amountLabel = new JLabel(formattedAmount);
            amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            amountLabel.setForeground(amount >= 0 ? new Color(76, 175, 80) : new Color(244, 67, 54));

            label.setLayout(new BoxLayout(label, BoxLayout.Y_AXIS));
            label.add(titleLabel);
            label.add(Box.createVerticalStrut(10));
            label.add(amountLabel);
            
            label.revalidate();
            label.repaint();
        });
    }

    private void clearInputFields() {
        amountField.setText("");
        typeCombo.setSelectedIndex(0);
        categoryCombo.setSelectedIndex(0);
        descField.setText("");
        amountField.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public void showDashboard() {
        setVisible(true);
        refreshRecords();
    }
}