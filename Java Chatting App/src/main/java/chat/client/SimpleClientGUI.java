package chat.client;

import chat.model.Message;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import javax.swing.text.*;

/**
 * GUI Client for the chat application
 */
public class SimpleClientGUI extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    
    // UI Components
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JLabel statusLabel;
    
    // Connection components
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    private boolean connected;
    
    public SimpleClientGUI() {
        // Set up the UI
        setTitle("Chat Client");
        setSize(500, 400);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Get username
        username = JOptionPane.showInputDialog(this, "Enter your username:", "Login", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            username = "User" + (int)(Math.random() * 1000);
        }
        
        setTitle("Chat Client - " + username);
        
        // Initialize components
        initComponents();
        
        // Connect to server
        connectToServer();
    }
    
    private void initComponents() {
        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        // Input field
        inputField = new JTextField();
        inputField.addActionListener(e -> sendMessage());
        
        // Send button
        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        
        // Status label
        statusLabel = new JLabel("Disconnected");
        statusLabel.setForeground(Color.RED);
        
        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Status panel at top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Chat area in center
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Input panel at bottom
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
        
        // Add window listener to disconnect on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
    }
    
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // Update UI
            statusLabel.setText("Connected");
            statusLabel.setForeground(new Color(0, 128, 0));
            connected = true;
            
            // Log connection
            displaySystemMessage("Connected to server.");
            
            // Start receiving messages
            new Thread(this::receiveMessages).start();
            
        } catch (Exception e) {
            displayErrorMessage("Could not connect to server: " + e.getMessage());
        }
    }
    
    private void disconnect() {
        if (connected) {
            try {
                connected = false;
                if (socket != null) socket.close();
                if (out != null) out.close();
                if (in != null) in.close();
                
                // Update UI
                statusLabel.setText("Disconnected");
                statusLabel.setForeground(Color.RED);
                
                displaySystemMessage("Disconnected from server.");
            } catch (IOException e) {
                displayErrorMessage("Error disconnecting: " + e.getMessage());
            }
        }
    }
    
    private void receiveMessages() {
        try {
            while (connected) {
                Object received = in.readObject();
                if (received instanceof Message) {
                    Message message = (Message) received;
                    displayMessage(message);
                }
            }
        } catch (EOFException e) {
            // Connection closed
            if (connected) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Disconnected");
                    statusLabel.setForeground(Color.RED);
                    displaySystemMessage("Connection to server lost.");
                });
            }
            connected = false;
        } catch (Exception e) {
            if (connected) {
                displayErrorMessage("Error receiving messages: " + e.getMessage());
            }
        }
    }
    
    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty() && connected) {
            try {
                Message message = new Message(username, text);
                out.writeObject(message);
                out.flush();
                
                // Clear input field
                inputField.setText("");
                
            } catch (IOException e) {
                displayErrorMessage("Error sending message: " + e.getMessage());
            }
        }
    }
    
    private void displayMessage(final Message message) {
        SwingUtilities.invokeLater(() -> {
            String sender = message.getSender();
            String formattedTime = message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"));
            String formattedMessage;
            
            // Format differently for own messages
            if (sender.equals(username)) {
                formattedMessage = String.format("[%s] You: %s%n", formattedTime, message.getContent());
            } else {
                formattedMessage = String.format("[%s] %s: %s%n", formattedTime, sender, message.getContent());
            }
            
            chatArea.append(formattedMessage);
            
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    private void displaySystemMessage(final String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("*** " + message + " ***\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    private void displayErrorMessage(final String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append("ERROR: " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new SimpleClientGUI().setVisible(true);
        });
    }
}
