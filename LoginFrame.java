import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private UserManager userManager;
    private FinanceManager financeManager;
    private Color primaryColor = new Color(103, 58, 183); // Deep Purple
    private Color accentColor = new Color(255, 171, 64);  // Orange
    private Color backgroundColor = new Color(245, 245, 245);

    public LoginFrame() {
        super("FinanceTracker Pro");
        this.userManager = new UserManager();
        this.financeManager = new FinanceManager();

        // Set custom frame appearance
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        
        // Create gradient background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, primaryColor,
                    0, getHeight(), primaryColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Create main content panel
        JPanel mainPanel = createMainPanel();
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        add(backgroundPanel);

        // Add drag functionality
        addDragListener();

        // Frame settings
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Logo/Icon
        JLabel iconLabel = createLogoLabel();
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(primaryColor);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input fields
        JPanel inputPanel = createInputPanel();

        // Login button
        JButton loginButton = createStyledButton("LOGIN");
        
        // Close button
        JButton closeButton = createStyledButton("EXIT");
        closeButton.setBackground(new Color(239, 83, 80));
        closeButton.addActionListener(e -> System.exit(0));

        // Add components
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(closeButton);

        return panel;
    }

    private JLabel createLogoLabel() {
        JLabel label = new JLabel("💰");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor);
        panel.setMaximumSize(new Dimension(300, 200));

        // Username field
        usernameField = createStyledTextField("Username");
        
        // Password field
        passwordField = new JPasswordField();
        styleTextField(passwordField, "Password");

        // User type combo
        String[] types = {"Regular User", "Admin"};
        userTypeCombo = new JComboBox<>(types);
        styleComboBox(userTypeCombo);

        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(userTypeCombo);

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
        styleTextField(field, placeholder);
        return field;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(300, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(backgroundColor);
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setMaximumSize(new Dimension(300, 40));
        box.setPreferredSize(new Dimension(300, 40));
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(backgroundColor);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private JButton createStyledButton(String text) {
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

        button.setMaximumSize(new Dimension(300, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });

        // Add login functionality
        if (text.equals("LOGIN")) {
            button.addActionListener(e -> handleLogin());
        }

        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        User user = userManager.getUser(username);
        if (user != null && user.authenticate(password)) {
            if (userType.equals("Admin") && user instanceof AdminUser) {
                new AdminDashboard(userManager).setVisible(true);
                dispose();
            } else if (userType.equals("Regular User") && user instanceof RegularUser) {
                UserDashboard dashboard = new UserDashboard(financeManager, (RegularUser) user);
                dashboard.showDashboard();
                dispose();
            } else {
                showError("Invalid user type selected");
            }
        } else {
            showError("Invalid username or password");
        }
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showLogin() {
        setVisible(true);
    }
}
