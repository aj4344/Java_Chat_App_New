package chat.client.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JButton loginButton;
    private boolean loginSuccessful = false;
    private String username;
    private JPanel mainPanel;
    
    public LoginDialog(Frame parent) {
        super(parent, "Login to Chat", true);
        initComponents();
        setupLayout();
        setupListeners();
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        setUndecorated(true); // Remove window decorations for modern look
        setShape(new RoundRectangle2D.Double(0, 0, 350, 200, 20, 20)); // Rounded corners
    }
    
    private void initComponents() {
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(25, 118, 210));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setupLayout() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 
                                                          0, getHeight(), new Color(44, 62, 80));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Chat Application");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10);
        inputPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(usernameField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        
        // Add all panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to dialog
        getContentPane().add(mainPanel);
    }
    
    private void setupListeners() {
        ActionListener loginAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        };
        
        loginButton.addActionListener(loginAction);
        usernameField.addActionListener(loginAction); // Allow Enter key to submit
        
        // Add window drag functionality
        MouseDragListener mouseDragListener = new MouseDragListener(this);
        mainPanel.addMouseListener(mouseDragListener);
        mainPanel.addMouseMotionListener(mouseDragListener);
    }
    
    private void attemptLogin() {
        username = usernameField.getText().trim();
        
        if (username.isEmpty()) {
            JLabel message = new JLabel("Please enter a username");
            message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            loginSuccessful = true;
            dispose();
        }
    }
    
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
    
    public String getUsername() {
        return username;
    }
    
    // Inner class for window dragging
    private class MouseDragListener extends MouseAdapter {
        private Point dragPoint;
        private Window window;
        
        public MouseDragListener(Window window) {
            this.window = window;
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                dragPoint = e.getPoint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            dragPoint = null;
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (dragPoint != null) {
                Point currentPoint = e.getLocationOnScreen();
                window.setLocation(currentPoint.x - dragPoint.x, 
                                  currentPoint.y - dragPoint.y);
            }
        }
    }
}
