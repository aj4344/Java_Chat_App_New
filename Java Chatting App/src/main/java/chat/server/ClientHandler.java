package chat.server;

import chat.model.Message;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientAddress;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.clientAddress = socket.getInetAddress().getHostAddress();
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error creating streams for client " + clientAddress + ": " + e.getMessage());
            // Consider closing the socket and removing the client here
        }
    }

    @Override
    public void run() {
        try {
            // Initial message or handshake if needed
            // out.writeObject(new Message("Server", "Welcome to the chat!")); 

            Object inputObject;
            while ((inputObject = in.readObject()) != null) {
                if (inputObject instanceof Message) {
                    Message message = (Message) inputObject;
                    System.out.println("Received from " + clientAddress + ": " + message.getContent());
                    Server.broadcastMessage(message, this);
                } else {
                    System.err.println("Received unknown object type from " + clientAddress);
                }
            }
        } catch (EOFException | SocketException e) {
            System.out.println("Client " + clientAddress + " disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection();
            Server.removeClient(this);
        }
    }

    public void sendMessage(Message message) throws IOException {
        if (out != null && !clientSocket.isClosed()) {
            out.writeObject(message);
            out.flush(); // Ensure the message is sent immediately
        }
    }

    public String getClientAddress() {
        return clientAddress;
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection for client " + clientAddress + ": " + e.getMessage());
        }
    }
}
