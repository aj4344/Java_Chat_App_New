package chat.client.gui;

import java.awt.Color;

/**
 * Theme class for storing color schemes for the chat application
 */
public class ChatTheme {
    private String name;
    private Color primaryColor;
    private Color secondaryColor;
    private Color accentColor;
    private Color backgroundColor;
    private Color backgroundEndColor;
    private Color myMessageColor;
    private Color otherMessageColor;
    private Color textColor;
    
    public ChatTheme(String name, 
                    Color primaryColor, 
                    Color secondaryColor, 
                    Color accentColor, 
                    Color backgroundColor,
                    Color backgroundEndColor,
                    Color myMessageColor, 
                    Color otherMessageColor,
                    Color textColor) {
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.accentColor = accentColor;
        this.backgroundColor = backgroundColor;
        this.backgroundEndColor = backgroundEndColor;
        this.myMessageColor = myMessageColor;
        this.otherMessageColor = otherMessageColor;
        this.textColor = textColor;
    }
    
    // Default themes
    public static final ChatTheme BLUE_THEME = new ChatTheme(
        "Blue Ocean",
        new Color(41, 128, 185),   // Primary - Ocean blue
        new Color(44, 62, 80),     // Secondary - Dark blue
        new Color(26, 188, 156),   // Accent - Turquoise
        new Color(255, 255, 255),  // Background start - White
        new Color(240, 242, 245),  // Background end - Light gray
        new Color(213, 245, 227),  // My message - Light green
        new Color(233, 235, 238),  // Other message - Light gray
        new Color(0, 0, 0)         // Text color - Black
    );
    
    public static final ChatTheme DARK_THEME = new ChatTheme(
        "Dark Mode",
        new Color(52, 73, 94),     // Primary - Dark slate
        new Color(30, 39, 46),     // Secondary - Almost black
        new Color(46, 204, 113),   // Accent - Green
        new Color(44, 62, 80),     // Background start - Dark blue
        new Color(20, 30, 40),     // Background end - Even darker
        new Color(46, 204, 113, 90), // My message - Semi-transparent green
        new Color(52, 73, 94, 90), // Other message - Semi-transparent slate
        new Color(236, 240, 241)   // Text color - Off-white
    );
    
    public static final ChatTheme SUNSET_THEME = new ChatTheme(
        "Sunset",
        new Color(230, 126, 34),   // Primary - Orange
        new Color(153, 45, 34),    // Secondary - Dark red
        new Color(241, 196, 15),   // Accent - Yellow
        new Color(248, 194, 145),  // Background start - Light orange
        new Color(255, 236, 210),  // Background end - Very light orange
        new Color(255, 231, 166),  // My message - Light yellow
        new Color(248, 194, 145),  // Other message - Light orange
        new Color(100, 30, 22)     // Text color - Dark red-brown
    );
    
    public static final ChatTheme FOREST_THEME = new ChatTheme(
        "Forest",
        new Color(39, 174, 96),    // Primary - Green
        new Color(27, 79, 114),    // Secondary - Dark blue
        new Color(241, 196, 15),   // Accent - Yellow
        new Color(220, 237, 200),  // Background start - Light green
        new Color(239, 253, 232),  // Background end - Very light green
        new Color(205, 240, 234),  // My message - Mint
        new Color(224, 242, 191),  // Other message - Light green
        new Color(27, 79, 114)     // Text color - Dark blue
    );
    
    public static final ChatTheme LAVENDER_THEME = new ChatTheme(
        "Lavender Dream",
        new Color(142, 68, 173),   // Primary - Purple
        new Color(87, 101, 116),   // Secondary - Slate
        new Color(155, 89, 182),   // Accent - Light purple
        new Color(245, 240, 255),  // Background start - Very light purple
        new Color(237, 231, 246),  // Background end - Light lavender
        new Color(223, 228, 250),  // My message - Light blue-purple
        new Color(237, 231, 246),  // Other message - Light lavender
        new Color(74, 35, 90)      // Text color - Dark purple
    );
    
    // Getters
    public String getName() {
        return name;
    }
    
    public Color getPrimaryColor() {
        return primaryColor;
    }
    
    public Color getSecondaryColor() {
        return secondaryColor;
    }
    
    public Color getAccentColor() {
        return accentColor;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public Color getBackgroundEndColor() {
        return backgroundEndColor;
    }
    
    public Color getMyMessageColor() {
        return myMessageColor;
    }
    
    public Color getOtherMessageColor() {
        return otherMessageColor;
    }
    
    public Color getTextColor() {
        return textColor;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    // Array of all available themes
    public static final ChatTheme[] AVAILABLE_THEMES = {
        BLUE_THEME,
        DARK_THEME,
        SUNSET_THEME,
        FOREST_THEME,
        LAVENDER_THEME
    };
}
