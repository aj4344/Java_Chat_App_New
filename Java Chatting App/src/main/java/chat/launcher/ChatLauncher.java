package chat.launcher;

import chat.client.gui.ChatGUI;
import chat.server.Server;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ChatLauncher extends JFrame {
    private JButton startServerButton;
    private JButton startClientButton;
    private JButton startBothButton;
    private JButton exitButton;
    private JPanel mainPanel;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(44, 62, 80);
    private Color accentColor = new Color(26, 188, 156);
    private Thread serverThread;
    
    public ChatLauncher() {
        initComponents();
        setupLayout();
        setupListeners();
        
        setUndecorated(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 300, 20, 20));
    }
    
    private void initComponents() {
        startServerButton = createStyledButton("Start Server", new Color(52, 152, 219));
        startClientButton = createStyledButton("Start Client", new Color(26, 188, 156));
        startBothButton = createStyledButton("Start Both", new Color(155, 89, 182));
        exitButton = createStyledButton("Exit", new Color(231, 76, 60));
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void setupLayout() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(0, 0, secondaryColor, 
                                                          0, getHeight(), new Color(22, 31, 40));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add a subtle pattern or texture if desired
            }
        };
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Chat Application Launcher");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(startServerButton);
        buttonPanel.add(startClientButton);
        buttonPanel.add(startBothButton);
        buttonPanel.add(exitButton);
        
        // Wrap the button panel with some padding
        JPanel buttonWrapperPanel = new JPanel(new BorderLayout());
        buttonWrapperPanel.setOpaque(false);
        buttonWrapperPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        buttonWrapperPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonWrapperPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
    }
    
    private void setupListeners() {
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
                JOptionPane.showMessageDialog(
                    ChatLauncher.this, 
                    "Server started successfully!", 
                    "Server Status", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        startClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startClient();
            }
        });
        
        startBothButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
                startClient();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Add window drag functionality
        MouseDragListener mouseDragListener = new MouseDragListener(this);
        mainPanel.addMouseListener(mouseDragListener);
        mainPanel.addMouseMotionListener(mouseDragListener);
    }
    
    private void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Server.main(new String[0]);
            }
        });
        serverThread.setDaemon(true); // Daemon thread will not prevent JVM from exiting
        serverThread.start();
    }
    
    private void startClient() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatGUI().setVisible(true);
            }
        });
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
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Enable antialiasing for Swing components
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatLauncher().setVisible(true);
            }
        });
    }
}
