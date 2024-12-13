import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

public class AdminDashboard extends JFrame {
    private UserManager userManager;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextField newUserField;
    private JPasswordField newPassField;
    private JComboBox<String> userTypeCombo;

    // Color scheme
    private Color primaryColor = new Color(103, 58, 183);  // Deep Purple
    private Color backgroundColor = new Color(245, 245, 245);
    private Color cardColor = Color.WHITE;
    private Color textColor = Color.BLACK;
    private boolean isDarkMode = false;

    public AdminDashboard(UserManager userManager) {
        super("Admin Control Panel");
        this.userManager = userManager;
        this.userListModel = new DefaultListModel<>();

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(backgroundColor);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(backgroundColor);

        JLabel titleLabel = new JLabel("Admin Control Panel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);

        JToggleButton darkModeToggle = new JToggleButton(" Dark Mode 🌙");
        darkModeToggle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        darkModeToggle.setFocusPainted(false);
        darkModeToggle.addActionListener(e -> toggleDarkMode());

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(239, 83, 80));
        logoutButton.addActionListener(e -> {
            new LoginFrame().showLogin();
            dispose();
        });

        rightPanel.add(darkModeToggle);
        rightPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(backgroundColor);
        contentPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Add New User Panel
        contentPanel.add(createAddUserPanel());
        
        // User List Panel
        contentPanel.add(createUserListPanel());

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        refreshUserList();
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            backgroundColor = new Color(33, 33, 33);
            cardColor = new Color(66, 66, 66);
            textColor = Color.WHITE;
            primaryColor = new Color(156, 39, 176); // Brighter purple for dark mode
        } else {
            backgroundColor = new Color(245, 245, 245);
            cardColor = Color.WHITE;
            textColor = Color.BLACK;
            primaryColor = new Color(103, 58, 183);
        }

        // Update colors
        getContentPane().setBackground(backgroundColor);
        updateComponentColors(this);
        repaint();
        revalidate();
    }

    private void updateComponentColors(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(c.getParent() == getContentPane() ? backgroundColor : cardColor);
                updateComponentColors((Container) c);
            } else if (c instanceof JLabel) {
                c.setForeground(textColor);
            } else if (c instanceof JTextField || c instanceof JPasswordField) {
                c.setBackground(cardColor);
                c.setForeground(textColor);
            } else if (c instanceof JList) {
                c.setBackground(cardColor);
                c.setForeground(textColor);
            }
            if (c instanceof Container) {
                updateComponentColors((Container) c);
            }
        }
    }

    private JPanel createAddUserPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Add New User");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newUserField = new JTextField(20);
        styleTextField(newUserField);

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPassField = new JPasswordField(20);
        styleTextField(newPassField);

        // User type combo
        JLabel typeLabel = new JLabel("User Type:");
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] types = {"Regular User", "Admin"};
        userTypeCombo = new JComboBox<>(types);
        styleComboBox(userTypeCombo);

        // Add User button
        JButton addButton = new JButton("Add User");
        styleButton(addButton, primaryColor);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.addActionListener(e -> addUser());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(userLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(newUserField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(passLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(newPassField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(typeLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(userTypeCombo);
        panel.add(Box.createVerticalStrut(25));
        panel.add(addButton);

        return panel;
    }

    private JPanel createUserListPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("User List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(primaryColor);

        userList = new JList<>(userListModel);
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userList.setBackground(cardColor);
        userList.setSelectionBackground(primaryColor.brighter());
        userList.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(null);

        JButton deleteButton = new JButton("Delete Selected User");
        styleButton(deleteButton, new Color(239, 83, 80));
        deleteButton.addActionListener(e -> deleteSelectedUser());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.SOUTH);

        return panel;
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 35));
    }

    // Custom rounded border
    private class RoundedBorder extends AbstractBorder {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(cardColor);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    private void addUser() {
        String username = newUserField.getText().trim();
        String password = new String(newPassField.getPassword()).trim();
        String userType = (String) userTypeCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userManager.getUser(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userManager.addUser(username, password, 
            userType.equals("Admin") ? "ADMIN" : "REGULAR");

        if (success) {
            newUserField.setText("");
            newPassField.setText("");
            userTypeCombo.setSelectedIndex(0);
            refreshUserList();
            JOptionPane.showMessageDialog(this, "User added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        String selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = selectedUser.split(" ")[0];
        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Cannot delete admin user", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete user: " + username + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userManager.deleteUser(username)) {
                refreshUserList();
                JOptionPane.showMessageDialog(this, "User deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshUserList() {
        userListModel.clear();
        for (User user : userManager.getAllUsers()) {
            userListModel.addElement(String.format("%s (%s)", 
                user.getUsername(), 
                user instanceof AdminUser ? "Admin" : "Regular User"));
        }
    }
}