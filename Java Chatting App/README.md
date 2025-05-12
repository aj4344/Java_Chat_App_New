# Java Chat Application

A modern, feature-rich chat application built with Java that provides both console and GUI interfaces for real-time communication with customizable themes.

## Latest Update: May 12, 2025
- Added theme support with 5 beautiful themes
- Enhanced UI with theme selector
- User preferences saved between sessions

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Architecture](#architecture)
- [GUI Components](#gui-components)
- [Network Communication](#network-communication)
- [Future Enhancements](#future-enhancements)

## Overview

This project is a complete Java-based chat application that supports both text-based and graphical user interfaces. It implements a client-server architecture where multiple clients can connect to a central server and exchange messages in real-time.

## Features

### Server Features
- Multi-threaded server supporting concurrent client connections
- Efficient message broadcasting to all connected clients
- Graceful handling of client connections and disconnections
- Thread pool management for optimal resource usage

### Client Features
- Modern, sleek graphical user interface with a gradient design
- Command-line interface option for headless environments
- Real-time message reception and display
- Visual indicators for connection status
- User-friendly login dialog
- Multiple theme options (Blue Ocean, Dark Mode, Sunset, Forest, Lavender Dream)
- Theme preference saving between sessions using Java Preferences API
- Theme-aware UI components that automatically adjust to the selected theme
- Emoji support for expressive messaging
- Message bubbles with visual distinction between sent and received messages
- Timestamps for all messages

### Launcher Features
- Single-click application startup
- Options to launch server-only, client-only, or both
- Modern UI with gradient background and rounded corners
- Draggable window with custom border

## Project Structure

```
Java Chatting App/
│
├── src/main/java/
│   └── chat/
│       ├── model/
│       │   └── Message.java          # Message data model
│       │
│       ├── server/
│       │   ├── Server.java           # Main server class
│       │   └── ClientHandler.java    # Thread for handling client connections
│       │
│       ├── client/
│       │   ├── Client.java           # Console-based client
│       │   └── SimpleClientGUI.java  # Simplified GUI client
│       │   │
│       │   └── gui/
│       │       ├── ChatGUI.java      # Full-featured GUI client
│       │       └── LoginDialog.java  # Username input dialog
│       │
│       └── launcher/
│           ├── ChatLauncher.java     # GUI application launcher
│           └── SimpleLauncher.java   # Simplified launcher
│
└── run_chat.bat                      # Script to compile and run the application
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Windows OS (for the batch file, though the Java code is cross-platform)

### Running the Application
1. Clone or download the repository
2. Navigate to the project's root directory
3. Run the application using the batch file:
   ```
   run_chat.bat
   ```
4. From the launcher, choose one of the options:
   - Start Server: Launches only the chat server
   - Start Client: Launches a GUI client that can connect to a running server
   - Start Both: Launches both the server and a client

### Project Maintenance
The project includes several utility batch files to help with maintenance:

1. **run_chat.bat**
   - Compiles and runs the application
   - Handles all necessary Java compilation steps

2. **deep_cleanup.bat**
   - Removes unnecessary files like backup files and temporary files
   - Cleans up development artifacts to keep the project tidy

3. **project_organizer.bat**
   - Displays the current project structure
   - Checks for unnecessary files
   - Provides useful organization tips for development

### Using the Chat Application
1. When the client starts, enter your desired username in the login dialog
2. Once connected, you'll see the main chat window
3. Type your message in the text field at the bottom and press Enter or click Send
4. To add emojis, click the emoji button and select one from the popup menu
5. To exit, close the window (the connection will be closed gracefully)

## Architecture

### Client-Server Model
The application follows a classic client-server architecture:
- **Server**: Acts as a central hub that receives messages from clients and broadcasts them to all other connected clients
- **Client**: Connects to the server, sends messages from the user, and receives/displays messages from other clients

### Multi-threading
- The server uses Java's `ExecutorService` to manage a thread pool for client connections
- Each client connection is handled by a separate `ClientHandler` thread
- The GUI client uses a dedicated thread for receiving messages to keep the UI responsive

### Object Serialization
- Messages are serialized using Java's built-in serialization mechanism
- The `Message` class implements `Serializable` to enable transmission over the network

### Theme System
- Implemented using the `ChatTheme` class that defines color schemes
- Each theme contains 8 different colors for various UI elements
- Themes are applied dynamically without application restart
- User preferences are stored using Java's Preferences API
- Built-in themes include: Blue Ocean, Dark Mode, Sunset, Forest, and Lavender Dream

## GUI Components

### Login Dialog
- Gradient background with rounded corners
- Custom-styled input field and button
- Draggable window with no standard window decoration

### Chat Window
- Split-panel design with message area and user list
- Message bubbles with different colors for sent vs. received messages
- Status indicator showing connection state
- Emoji picker for inserting emoticons
- Auto-scrolling message area
- Theme selector with 5 beautiful themes:
  - **Blue Ocean**: Professional blue and white theme with turquoise accents
  - **Dark Mode**: Deep dark theme with green accents for low-light environments
  - **Sunset**: Warm orange and yellow theme inspired by sunset colors
  - **Forest**: Fresh green theme with natural color palette
  - **Lavender Dream**: Soft purple theme with gentle pastel colors

### Launcher
- Simple, intuitive interface with three main options
- Visually pleasing gradient background
- Modern, flat button design

## Network Communication

### Protocol
- Simple object-based protocol using Java serialization
- `Message` objects are sent between client and server
- Each message contains:
  - Sender username
  - Message content
  - Timestamp

### Connection Flow
1. Server starts and listens for connections
2. Client connects and sets up input/output streams
3. Client sends username or initial handshake
4. Messages flow bidirectionally between client and server
5. Server broadcasts received messages to all other clients
6. Client disconnects by closing the socket

## Future Enhancements

Potential improvements for future versions:
- Private messaging between specific users
- File sharing capabilities
- Message history persistence
- User authentication system with encrypted passwords
- Channel/room-based chat groups
- Rich text formatting in messages
- Read receipts and message status indicators
- User presence indicators (typing, away, etc.)
- Cross-platform launcher (replacing the batch file with a more universal solution)
- Android mobile application version
- Additional themes and custom theme creator
- Voice and video chat capabilities
- Screen sharing for collaborative work
- End-to-end encryption for secure communications

## License
This project is licensed under the MIT License - see the LICENSE file for details.
# Java_Chat_App_New
