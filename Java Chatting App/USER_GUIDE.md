# Java Chat Application - User Guide

This guide will help you get started with the Java Chat Application. Follow these steps to chat with friends on your local network!

## Quick Start

1. **Start the Application**
   - Double-click on the `run_chat.bat` file
   - The Chat Application Launcher will appear

2. **Choose Your Launch Option**
   - **Start Server**: Starts only the chat server (do this if you want to host a chat)
   - **Start Client**: Connects to an existing server (do this to join a chat)
   - **Start Both**: Launches both server and client on your computer (do this for testing)

3. **Enter Your Username**
   - When the client starts, a login dialog will appear
   - Enter your desired username
   - Click "Login" to connect to the chat server

4. **Start Chatting!**
   - Type your message in the text field at the bottom of the window
   - Press Enter or click the "Send" button to send your message
   - Messages from other users will appear in the chat area

## Features

### Theme Selection
- Click the "Theme" button in the top-right corner to open the theme selector
- Choose from multiple themes: Blue Ocean, Dark Mode, Sunset, Forest, and Lavender Dream
- Your theme preference will be saved between sessions

### Emoji Support
- Click the emoji button to open the emoji picker
- Select an emoji to insert it into your message

### Connection Status
- Look at the top-right corner to see your connection status
- "Connected" (green) means you're connected to the chat server
- "Disconnected" (red) means you're not connected

### User Interface
- Messages you send appear on the right side with a different color
- Messages from others appear on the left side
- Each message includes a timestamp and sender name

## Multiple Users

To test with multiple users on the same computer:
1. Start the server (using either "Start Server" or "Start Both")
2. Click "Start Client" again to open additional client windows
3. Enter different usernames for each client
4. Start chatting between the different windows

## Troubleshooting

### Can't Connect to Server
- Make sure the server is running
- Check that you're using "localhost" as the server address
- Verify that port 12345 is not blocked by your firewall

### Text Display Issues
- If you see strange characters, restart the application
- The application uses UTF-8 encoding for proper text display

### Application Won't Start
- Make sure you have Java installed (JDK 8 or higher)
- Run the batch file from the project's root directory

## Exiting the Application
- Close any client windows by clicking the X button
- To fully shut down the server, you'll need to close the command prompt window or terminate the Java process

Enjoy chatting!
