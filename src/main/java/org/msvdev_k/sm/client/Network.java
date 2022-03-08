package org.msvdev_k.sm.client;

import org.msvdev_k.sm.CommonConstants;
import org.msvdev_k.sm.server.ServerCommandConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private final ChatController controller;

    public Network(ChatController chatController) {
        this.controller = chatController;
    }

    private void startReadServerMessages() throws IOException {
        new Thread(() -> {
            try {
                while (true) {
                    String messageFromServer = inputStream.readUTF();
                    System.out.println(messageFromServer);
                    if (messageFromServer.startsWith(ServerCommandConstants.ENTER)) {
                        String[] client = messageFromServer.split("\\s");
                        controller.displayClient(client[1]);
                        controller.displayMessage("Пользователь " + client[1] + " зашел в чат");
                    } else if (messageFromServer.startsWith(ServerCommandConstants.EXIT)) {
                        String[] client = messageFromServer.split("\\s");
                        controller.removeClient(client[1]);
                        controller.displayMessage(client[1] + " покинул чат");
                    } else if (messageFromServer.startsWith(ServerCommandConstants.CLIENTS)) {
                        String[] client = messageFromServer.split("\\s");
                        for (int i = 1; i < client.length; i++) {
                            controller.displayClient(client[i]);
                        }
                    } else {
                        controller.displayMessage(messageFromServer);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    private void initializeNetwork() throws IOException {
        socket = new Socket(CommonConstants.SERVER_ADDRESS, CommonConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }


    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean sendAuth(String login, String password) {
        try {
            if (socket == null || socket.isClosed()) {
                initializeNetwork();
            }
            outputStream.writeUTF(ServerCommandConstants.AUTHORIZATION + " " + login + " " + password);

            boolean authenticated = inputStream.readUTF().startsWith(ServerCommandConstants.AUTHORIZATION_OK);
            if (authenticated) {
                startReadServerMessages();
            }
            return authenticated;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void closeConnection() {
        try {
            outputStream.writeUTF(ServerCommandConstants.EXIT);
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        //System.exit(1);
    }

}