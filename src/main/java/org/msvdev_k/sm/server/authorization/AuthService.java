package org.msvdev_k.sm.server.authorization;


import java.sql.SQLException;

/**
 * Интерфейс, описывающий сервис аутентификации на стороне сервера.
 */
public interface AuthService {

    /**
     * Запуск сервиса.
     */
    void start() throws SQLException;


    /**
     * Получение ника по логину и паролю.
     *
     * @param login    логин пользователя.
     * @param password пароль пользователя.
     * @return ник пользователя либо null, если пары логин/пароль не существует.
     */
    String getNickNameByLoginAndPassword(String login, String password);


    /**
     * Остановка сервиса.
     */
    void end();
}
