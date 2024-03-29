package org.msvdev_k.sm.server;

import org.msvdev_k.sm.CommonConstants;
import org.msvdev_k.sm.server.authorization.AuthService;
import org.msvdev_k.sm.server.authorization.SQLiteAuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Класс, описывающий логику сервера.
 */
public class Server {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);


    /**
     * Сервис аутентификации.
     */
    private final AuthService authService = new SQLiteAuthService();

    /**
     * Список авторизованных в текущий момент клиентов.
     */
    private final Map<String, ClientHandler> connectedUsers = new HashMap<>();


    /**
     * Основной конструктор класса.
     */
    public Server() {

        try (ServerSocket server = new ServerSocket(CommonConstants.SERVER_PORT)) {

            authService.start();
            LOGGER.info("Запущен сервер аутентификации");

            while (true) {
                LOGGER.info("Сервер ожидает подключения");
                Socket socket = server.accept();

                LOGGER.info("Клиент подключился");
                new ClientHandler(this, socket);
            }

        } catch (IOException | SQLException exception) {
            LOGGER.error("Ошибка в работе сервера");
            exception.printStackTrace();

        } finally {
            authService.end();
            LOGGER.info("Остановлен сервер аутентификации");
        }
    }


    /**
     * Получить сервис аутентификации.
     *
     * @return объект реализующий интерфейс AuthService.
     */
    public AuthService getAuthService() {
        return authService;
    }


    /**
     * Проверить использования ника подключаемого клиента.
     *
     * @param nickName ник подключаемого клиента.
     * @return true - ник в данный момент занят,
     * false - ник свободный.
     */
    public synchronized boolean isNickNameBusy(String nickName) {
        return connectedUsers.containsKey(nickName);
    }


    /**
     * Отправить сообщение всем авторизованным в текущий
     * момент клиентам.
     *
     * @param message отправляемое сообщение.
     */
    public synchronized void broadcastMessage(String message) {
        for (ClientHandler handler : connectedUsers.values()) {
            handler.sendMessage(message);
        }
    }


    /**
     * Отправить сообщение авторизованному клиенту.
     *
     * @param message  отправляемое сообщение.
     * @param nickName никнейм клиента, которому отправляется сообщение.
     */
    public synchronized void broadcastMessage(String message, String nickName) {

        ClientHandler handler = connectedUsers.get(nickName);

        if (handler != null) {
            handler.sendMessage(message);
        }
    }


    /**
     * Добавить авторизованного пользователя.
     *
     * @param handler объект, отвечающий за обмен сообщениями между клиентом и сервером.
     */
    public synchronized void addConnectedUser(ClientHandler handler) {
        connectedUsers.put(handler.getNickName(), handler);
    }


    /**
     * Отключить авторизованного пользователя.
     *
     * @param handler объект, отвечающий за обмен сообщениями между клиентом и сервером.
     */
    public synchronized void disconnectUser(ClientHandler handler) {
        connectedUsers.remove(handler.getNickName());
    }

}