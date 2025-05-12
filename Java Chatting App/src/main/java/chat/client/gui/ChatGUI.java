package chat.client.gui;

import chat.model.Message;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import javax.swing.text.*;
import java.util.prefs.Preferences;

public class ChatGUI extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final String PREF_THEME = "chatTheme";
    
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton emojiButton;
    private JButton themeButton;
    private JList<String> userList;
    private JLabel statusLabel;
    private DefaultListModel<String> userListModel;
    
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private String username;
    private boolean connected = false;
    private Thread receivingThread;
    
    private Preferences prefs = Preferences.userNodeForPackage(ChatGUI.class);
    private ChatTheme currentTheme;
    private JPanel mainPanel;
    
    public ChatGUI() {
        loadTheme();
        initComponents();
        setupLayout();
        setupListeners();
        promptForUsername();
    }
    
    private void loadTheme() {
        int themeIndex = prefs.getInt(PREF_THEME, 0); // Default to Blue theme
        if (themeIndex < 0 || themeIndex >= ChatTheme.AVAILABLE_THEMES.length) {
            themeIndex = 0;
        }
        currentTheme = ChatTheme.AVAILABLE_THEMES[themeIndex];
    }
    
    private void saveTheme(int themeIndex) {
        prefs.putInt(PREF_THEME, themeIndex);
    }
    
    private void initComponents() {
        setTitle("Modern Chat Application");
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize components
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        ((DefaultCaret)chatArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(currentTheme.getAccentColor());
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        emojiButton = new JButton(":)");
        emojiButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emojiButton.setBackground(currentTheme.getPrimaryColor());
        emojiButton.setForeground(Color.WHITE);
        emojiButton.setFocusPainted(false);
        emojiButton.setBorderPainted(false);
        emojiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        themeButton = new JButton("Theme");
        themeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        themeButton.setBackground(currentTheme.getSecondaryColor());
        themeButton.setForeground(Color.WHITE);
        themeButton.setFocusPainted(false);
        themeButton.setBorderPainted(false);
        themeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBackground(new Color(233, 236, 239));
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        statusLabel = new JLabel("Disconnected");
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void setupLayout() {
        // Set overall layout
        setLayout(new BorderLayout(10, 10));
        
        // Custom panel with gradient background
        mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, currentTheme.getBackgroundColor(), 
                                                          0, getHeight(), currentTheme.getBackgroundEndColor());
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top panel (header)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Chat Room");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(currentTheme.getSecondaryColor());
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);
        statusPanel.add(themeButton);
        statusPanel.add(statusLabel);
        
        // Add top panel components
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);
        
        // Create scrollable chat area
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Create scrollable user list
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        userScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setOpaque(false);
        JLabel usersLabel = new JLabel(" Online Users");
        usersLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersLabel.setForeground(currentTheme.getSecondaryColor());
        userPanel.add(usersLabel, BorderLayout.NORTH);
        userPanel.add(userScrollPane, BorderLayout.CENTER);
        
        // Input panel for sending messages
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setOpaque(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(emojiButton);
        buttonPanel.add(sendButton);
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add all panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(chatScrollPane, BorderLayout.CENTER);
        mainPanel.add(userPanel, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // Add main panel to the frame
        add(mainPanel);
    }
    
    private void setupListeners() {
        // Send message action
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        };
        
        sendButton.addActionListener(sendAction);
        messageField.addActionListener(sendAction); // Enter key
        
        // Emoji button action
        emojiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEmojiPicker();
            }
        });
        
        // Theme button action
        themeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showThemeSelector();
            }
        });
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
    }
    
    private void showThemeSelector() {
        JDialog themeDialog = new JDialog(this, "Select Theme", true);
        themeDialog.setLayout(new BorderLayout());
        themeDialog.setSize(350, 300);
        themeDialog.setLocationRelativeTo(this);
        
        DefaultListModel<ChatTheme> themeListModel = new DefaultListModel<>();
        for (ChatTheme theme : ChatTheme.AVAILABLE_THEMES) {
            themeListModel.addElement(theme);
        }
        
        final JList<ChatTheme> themeList = new JList<>(themeListModel);
        themeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        themeList.setCellRenderer(new ThemeCellRenderer());
        
        // Find current theme index
        for (int i = 0; i < ChatTheme.AVAILABLE_THEMES.length; i++) {
            if (ChatTheme.AVAILABLE_THEMES[i].getName().equals(currentTheme.getName())) {
                themeList.setSelectedIndex(i);
                break;
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(themeList);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton okButton = new JButton("Apply");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = themeList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    currentTheme = ChatTheme.AVAILABLE_THEMES[selectedIndex];
                    saveTheme(selectedIndex);
                    applyTheme();
                    themeDialog.dispose();
                }
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themeDialog.dispose();
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        themeDialog.add(new JLabel("  Choose a theme:"), BorderLayout.NORTH);
        themeDialog.add(scrollPane, BorderLayout.CENTER);
        themeDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        themeDialog.setVisible(true);
    }
    
    private void applyTheme() {
        // Update UI components with new theme colors
        sendButton.setBackground(currentTheme.getAccentColor());
        emojiButton.setBackground(currentTheme.getPrimaryColor());
        themeButton.setBackground(currentTheme.getSecondaryColor());
        
        // Force repaint of the gradient background
        mainPanel.repaint();
        
        // Redraw all messages with new theme colors
        chatArea.setText("");
        
        // If needed, you could store messages and redisplay them with new colors
        
        // Show a subtle theme changed notification
        displaySystemMessage("Theme changed to: " + currentTheme.getName());
    }
    
    private class ThemeCellRenderer extends JPanel implements ListCellRenderer<ChatTheme> {
        private JLabel nameLabel;
        private JPanel colorPreview;
        
        public ThemeCellRenderer() {
            setLayout(new BorderLayout(10, 0));
            
            nameLabel = new JLabel();
            nameLabel.setOpaque(false);
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            colorPreview = new JPanel(new GridLayout(1, 3, 2, 0));
            colorPreview.setPreferredSize(new Dimension(60, 20));
            
            add(nameLabel, BorderLayout.CENTER);
            add(colorPreview, BorderLayout.EAST);
            
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends ChatTheme> list, 
                                                     ChatTheme theme, int index, 
                                                     boolean isSelected, boolean cellHasFocus) {
            nameLabel.setText(theme.getName());
            
            // Clear and re-add color swatches
            colorPreview.removeAll();
            colorPreview.add(createColorSwatch(theme.getPrimaryColor()));
            colorPreview.add(createColorSwatch(theme.getAccentColor()));
            colorPreview.add(createColorSwatch(theme.getMyMessageColor()));
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                nameLabel.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                nameLabel.setForeground(list.getForeground());
            }
            
            return this;
        }
        
        private JPanel createColorSwatch(final Color color) {
            JPanel swatch = new JPanel();
            swatch.setBackground(color);
            swatch.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            return swatch;
        }
    }
    
    private void promptForUsername() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);
        
        if (loginDialog.isLoginSuccessful()) {
            this.username = loginDialog.getUsername();
            setTitle("Chat Application - " + username);
            connectToServer();
        } else {
            System.exit(0);
        }
    }
    
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // Start thread to receive messages
            receivingThread = new Thread(new MessageReceiver());
            receivingThread.start();
            
            // Update UI to reflect connected state
            statusLabel.setText("Connected");
            statusLabel.setForeground(currentTheme.getAccentColor());
            connected = true;
            
            // Add self to user list
            userListModel.addElement(username + " (You)");
            
            displaySystemMessage("Connected to the chat server.");
        } catch (UnknownHostException e) {
            displayErrorMessage("Server not found: " + e.getMessage());
        } catch (IOException e) {
            displayErrorMessage("Couldn't connect to server: " + e.getMessage());
        }
    }
    
    private void disconnect() {
        if (connected) {
            try {
                connected = false;
                
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
                
                if (receivingThread != null) {
                    receivingThread.interrupt();
                }
                
                displaySystemMessage("Disconnected from the chat server.");
            } catch (IOException e) {
                displayErrorMessage("Error while disconnecting: " + e.getMessage());
            }
        }
    }
    
    private void sendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty() && connected) {
            try {
                Message message = new Message(username, content);
                out.writeObject(message);
                out.flush();
                
                displayMyMessage(message);
                messageField.setText("");
            } catch (IOException e) {
                displayErrorMessage("Error sending message: " + e.getMessage());
            }
        }
    }
    
    private void displayMyMessage(Message message) {
        appendToChat(message, true);
    }
    
    private void displayOtherMessage(Message message) {
        appendToChat(message, false);
    }
    
    private void displaySystemMessage(String text) {
        try {
            StyledDocument doc = chatArea.getStyledDocument();
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attributes, "Segoe UI");
            StyleConstants.setFontSize(attributes, 12);
            StyleConstants.setItalic(attributes, true);
            StyleConstants.setForeground(attributes, Color.GRAY);
            
            doc.insertString(doc.getLength(), text + "\n", attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    private void displayErrorMessage(String text) {
        try {
            StyledDocument doc = chatArea.getStyledDocument();
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attributes, "Segoe UI");
            StyleConstants.setFontSize(attributes, 12);
            StyleConstants.setItalic(attributes, true);
            StyleConstants.setForeground(attributes, Color.RED);
            
            doc.insertString(doc.getLength(), "ERROR: " + text + "\n", attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    private void appendToChat(Message message, boolean isMyMessage) {
        try {
            StyledDocument doc = chatArea.getStyledDocument();
            
            // Message container attributes
            SimpleAttributeSet containerAttr = new SimpleAttributeSet();
            StyleConstants.setAlignment(containerAttr, isMyMessage ? 
                StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
            StyleConstants.setSpaceAbove(containerAttr, 5);
            StyleConstants.setSpaceBelow(containerAttr, 5);
            
            // Format timestamp
            String time = message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"));
            
            // Create header (name + time)
            SimpleAttributeSet headerAttr = new SimpleAttributeSet();
            StyleConstants.setFontFamily(headerAttr, "Segoe UI");
            StyleConstants.setFontSize(headerAttr, 10);
            StyleConstants.setForeground(headerAttr, Color.GRAY);
            StyleConstants.setBold(headerAttr, true);
            
            String headerText = isMyMessage ? 
                time + " - You" : 
                message.getSender() + " - " + time;
                
            // Create message bubble
            SimpleAttributeSet bubbleAttr = new SimpleAttributeSet();
            StyleConstants.setFontFamily(bubbleAttr, "Segoe UI");
            StyleConstants.setFontSize(bubbleAttr, 14);
            StyleConstants.setBackground(bubbleAttr, isMyMessage ? 
                                        currentTheme.getMyMessageColor() : 
                                        currentTheme.getOtherMessageColor());
            StyleConstants.setForeground(bubbleAttr, currentTheme.getTextColor());
            
            // Apply alignment to the paragraph
            doc.setParagraphAttributes(doc.getLength(), 1, containerAttr, false);
            
            // Insert header
            doc.insertString(doc.getLength(), headerText + "\n", headerAttr);
            
            // Insert message bubble with content
            doc.insertString(doc.getLength(), message.getContent() + "\n\n", bubbleAttr);
            
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    private void showEmojiPicker() {
        // Use ASCII emoji alternatives to avoid encoding issues
        String[] emojis = {
            ":)", // Smiley face
            ":(", // Frowning face
            "<3", // Heart
            "^_^", // Happy face
            ";)", // Wink
            ":D", // Big smile
            ":/", // Confused
            ":'(", // Cry
            ":P", // Tongue
            "XD"  // Laughing
        };
        
        JPopupMenu emojiMenu = new JPopupMenu();
        emojiMenu.setBackground(Color.WHITE);
        
        for (String emoji : emojis) {
            JMenuItem emojiItem = new JMenuItem(emoji);
            emojiItem.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            emojiItem.addActionListener(e -> {
                messageField.setText(messageField.getText() + emoji);
                messageField.requestFocus();
            });
            emojiMenu.add(emojiItem);
        }
        
        emojiMenu.show(emojiButton, 0, -emojiMenu.getPreferredSize().height);
    }
    
    // Inner class to handle receiving messages
    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                Object input;
                while (connected && (input = in.readObject()) != null) {
                    if (input instanceof Message) {
                        final Message message = (Message) input;
                        
                        // Update UI on EDT
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                displayOtherMessage(message);
                            }
                        });
                    }
                }
            } catch (IOException e) {
                if (connected) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            statusLabel.setText("Disconnected");
                            statusLabel.setForeground(Color.RED);
                            displaySystemMessage("Connection to server lost.");
                        }
                    });
                }
            } catch (ClassNotFoundException e) {
                displayErrorMessage("Error processing incoming message.");
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
                new ChatGUI().setVisible(true);
            }
        });
    }
}
