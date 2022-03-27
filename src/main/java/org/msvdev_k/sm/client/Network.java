package org.msvdev_k.sm.client;

import org.msvdev_k.sm.CommonConstants;
import org.msvdev_k.sm.server.ServerCommandConstants;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Network {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private final ChatController controller;

    /**
     * Путь к файлу, содержащему историю сообщений.
     */
    private File historyFile;

    /**
     * Константа, определяющая количество последних строк истории чата отображаемых
     * при авторизации пользователя.
     */
    private static final int COUNT_OF_LAST_MESSAGE = 100;


    public Network(ChatController chatController) {
        this.controller = chatController;
    }


    private void startReadServerMessages() throws IOException {
        new Thread(() -> {

            displayHistory();

            try {
                while (true) {
                    String messageFromServer = inputStream.readUTF();
                    System.out.println(messageFromServer);

                    if (messageFromServer.startsWith(ServerCommandConstants.ENTER)) {
                        String[] client = messageFromServer.split("\\s");
                        controller.displayClient(client[1]);
                        displayMessage("Пользователь " + client[1] + " зашел в чат", false);

                    } else if (messageFromServer.startsWith(ServerCommandConstants.EXIT)) {
                        String[] client = messageFromServer.split("\\s");
                        controller.removeClient(client[1]);
                        displayMessage(client[1] + " покинул чат", false);

                    } else if (messageFromServer.startsWith(ServerCommandConstants.CLIENTS)) {
                        String[] client = messageFromServer.split("\\s");
                        for (int i = 1; i < client.length; i++) {
                            controller.displayClient(client[i]);
                        }

                    } else {
                        displayMessage(messageFromServer, true);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }


    /**
     * Отобразить историю сообщений.
     */
    private void displayHistory() {

        LinkedList<String> messageList = new LinkedList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {

            // Считать из файла необходимое количество последних строк истории
            String messageLine;
            while ((messageLine = reader.readLine()) != null) {

                messageList.addLast(messageLine);

                if (messageList.size() > COUNT_OF_LAST_MESSAGE) {
                    messageList.removeFirst();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Отобразить считанные строки на экране
        for (String message : messageList) {
            controller.displayMessage(message);
        }
    }



    /**
     * Отобразить сообщение на экране.
     * @param message отображаемое сообщение.
     * @param addHistory true - сообщение сохраняется в файле истории, false - не сохраняется.
     */
    private void displayMessage(String message, boolean addHistory) {

        controller.displayMessage(message);

        if (addHistory) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true))) {

                writer.write(message);
                writer.write("\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        boolean authenticated = false;

        try {
            if (socket == null || socket.isClosed()) {
                initializeNetwork();
            }
            outputStream.writeUTF(ServerCommandConstants.AUTHORIZATION + " " + login + " " + password);

            String input = inputStream.readUTF();
            if (input.startsWith(ServerCommandConstants.AUTHORIZATION_OK)) {
                authenticated = true;

                String[] message = input.split("\\s");
                historyFile = new File(String.format("history_%s.txt", message[1]));
                System.out.println(historyFile.getAbsolutePath());

                startReadServerMessages();
            }
            return authenticated;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authenticated;
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