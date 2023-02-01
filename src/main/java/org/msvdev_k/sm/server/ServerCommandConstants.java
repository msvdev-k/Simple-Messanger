package org.msvdev_k.sm.server;


/**
 * Класс, описывающий команды для управления сервером со
 * стороны клиента.
 */
public class ServerCommandConstants {

    /**
     * Символ с которого начинаются команды сервера.
     */
    public static final String COMMAND_TOKEN = "/";

    /**
     * Команда авторизации.
     * Клиент -> Сервер.
     * Формат: /auth login password
     */
    public static final String AUTHORIZATION = COMMAND_TOKEN + "auth";

    /**
     * Команда отключения клиента от сервера.
     * Клиент -> Сервер.
     * Формат: /end
     */
    public static final String SHUTDOWN = COMMAND_TOKEN + "end";

    /**
     * Команда для пересылки личного сообщения конкретному пользователю.
     * Клиент -> Сервер.
     * Формат: /w nickName message
     */
    public static final String MESSAGE_TO_USER = COMMAND_TOKEN + "w";

    /**
     * Аутентификация прошла успешно.
     * Сервер -> Клиент.
     * Формат: /auth_ok nickName
     */
    public static final String AUTHORIZATION_OK = COMMAND_TOKEN + "auth_ok";

    /**
     * Клиент авторизовался на сервере.
     * Сервер -> Клиенты.
     * Формат: /enter nickName
     */
    public static final String ENTER = COMMAND_TOKEN + "enter";

    /**
     * Клиент отключился.
     * Сервер -> Клиенты.
     * Формат: /exit nickName
     */
    public static final String EXIT = COMMAND_TOKEN + "exit";

    /**
     * Список авторизованных в текущий момент клиентов.
     * Сервер -> Клиенты.
     * Формат: /clients nickName1 nickName2 ...
     */
    public static final String CLIENTS = COMMAND_TOKEN + "clients";

}

