package chat.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

/**
 * Simple launcher for the chat application.
 * This class provides buttons to launch the server, client, or both.
 */
public class SimpleLauncher {
    
    // Main method - entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Chat Application Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        
        // Create a panel with a gradient background
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 62, 80), 
                                                          0, getHeight(), new Color(0, 0, 0));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Add title
        JLabel titleLabel = new JLabel("Chat Application", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.setOpaque(false);
        
        // Create buttons with modern style
        JButton serverButton = createStyledButton("Start Server");
        JButton clientButton = createStyledButton("Start Client");
        JButton bothButton = createStyledButton("Start Both");
        JButton exitButton = createStyledButton("Exit");
        
        // Style exit button differently
        exitButton.setBackground(new Color(192, 57, 43));
        
        // Add buttons to panel
        buttonPanel.add(serverButton);
        buttonPanel.add(clientButton);
        buttonPanel.add(bothButton);
        buttonPanel.add(exitButton);
        
        // Add padding around button panel
        JPanel paddedButtonPanel = new JPanel(new BorderLayout());
        paddedButtonPanel.setOpaque(false);
        paddedButtonPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        paddedButtonPanel.add(buttonPanel, BorderLayout.CENTER);
        
        panel.add(paddedButtonPanel, BorderLayout.CENTER);
        
        // Add action listeners
        serverButton.addActionListener(e -> startServer());
        clientButton.addActionListener(e -> startClient());
        bothButton.addActionListener(e -> {
            startServer();
            startClient();
        });
        exitButton.addActionListener(e -> System.exit(0));
        
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private static void startServer() {
        new Thread(() -> {
            try {
                chat.server.Server.main(new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting server: " + e.getMessage(), 
                    "Server Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }).start();
        
        JOptionPane.showMessageDialog(null, 
            "Server started successfully!", 
            "Server Status", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static void startClient() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use reflection to handle potential class changes gracefully
                try {
                    Class<?> guiClass = Class.forName("chat.client.gui.ChatGUI");
                    Object guiInstance = guiClass.getDeclaredConstructor().newInstance();
                    
                    // Make the GUI visible using reflection
                    guiClass.getMethod("setVisible", boolean.class).invoke(guiInstance, true);
                } catch (ClassNotFoundException ex) {
                    // Fall back to SimpleClientGUI if ChatGUI is not available
                    Class<?> guiClass = Class.forName("chat.client.SimpleClientGUI");
                    Object guiInstance = guiClass.getDeclaredConstructor().newInstance();
                    
                    // Make the GUI visible
                    guiClass.getMethod("setVisible", boolean.class).invoke(guiInstance, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting client: " + e.getMessage(), 
                    "Client Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
