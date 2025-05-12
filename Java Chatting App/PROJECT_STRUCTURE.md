# Java Chat Application - Project Structure

This document provides an overview of the project structure to help understand the codebase organization.

## Main Components

### 1. Model
- Location: `src/main/java/chat/model`
- Files: `Message.java`
- Purpose: Contains data models shared between the client and server

### 2. Server
- Location: `src/main/java/chat/server`
- Files: `Server.java`, `ClientHandler.java`
- Purpose: The chat server implementation that handles multiple client connections

### 3. Client
- Location: `src/main/java/chat/client`
- Files: 
  - `Client.java` - Command line client
  - `SimpleClientGUI.java` - A simplified GUI client

### 4. GUI Components
- Location: `src/main/java/chat/client/gui`
- Files:
  - `ChatGUI.java` - Main GUI client with theme support
  - `LoginDialog.java` - Login dialog for entering username
  - `ChatTheme.java` - Theme definitions and management

### 5. Launcher
- Location: `src/main/java/chat/launcher`
- Files:
  - `ChatLauncher.java` - Original application launcher
  - `SimpleLauncher.java` - Simplified launcher for better compatibility

## Helper Files

- `run_chat.bat` - Script to compile and run the application
- `update_theme_support.bat` - Script that was used to update the files with theme support
- `project_organizer.bat` - Script to check project organization and cleanup status
- `README.md` - Main project documentation
- `USER_GUIDE.md` - User guide for application usage

## Class File Organization

The `.class` files are compiled Java bytecode files generated from the `.java` source files. They are organized in the same directory structure as their corresponding source files.

## Backup Directory

The `backup` directory contains backups of original files before theme support was added. These are kept for safety but aren't needed for running the application.

## Compiling and Running

Use the `run_chat.bat` file to compile and run the application. It will:
1. Clean previous compilation results
2. Compile all Java source files
3. Launch the application starter
