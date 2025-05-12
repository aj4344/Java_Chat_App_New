package chat.client;

import chat.model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private String username;

    public Client(String username) {
        this.username = username;
    }

    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server.");

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Send username or initial handshake message if server expects one
            // For now, we assume the server knows the sender from the Message object

            // Thread to listen for messages from the server
            Thread receivingThread = new Thread(() -> {
                try {
                    Object serverMessage;
                    while ((serverMessage = in.readObject()) != null) {
                        if (serverMessage instanceof Message) {
                            System.out.println(serverMessage); // Uses Message.toString()
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    if (!socket.isClosed()) { // Avoid error message if socket is intentionally closed
                        System.err.println("Error receiving message from server: " + e.getMessage());
                    }
                } finally {
                    System.out.println("Disconnected from server.");
                }
            });
            receivingThread.start();

            // Main thread for sending messages
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your messages (type 'bye' to exit):");
            while (true) {
                String userInput = scanner.nextLine();
                if ("bye".equalsIgnoreCase(userInput)) {
                    break;
                }
                sendMessage(userInput);
            }

        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to server: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void sendMessage(String content) {
        try {
            Message message = new Message(username, content);
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java chat.client.Client <username>");
            return;
        }
        String username = args[0];
        Client client = new Client(username);
        client.start();
    }
}
