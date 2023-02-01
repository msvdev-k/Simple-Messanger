package org.msvdev_k.sm.server.authorization;


/**
 * Класс описывающий аутентификационные данные клиента.
 */
public class UserData {

    /**
     * Логин для доступа на сервер.
     */
    private final String login;

    /**
     * Пароль для доступа на сервер.
     */
    private final String password;

    /**
     * Ник клиента, отображаемый при общении.
     */
    private final String nickName;


    /**
     * Основной конструктор.
     *
     * @param login    логин.
     * @param password пароль.
     * @param nickName ник.
     */
    public UserData(String login, String password, String nickName) {
        this.login = login;
        this.password = password;
        this.nickName = nickName;
    }


    /**
     * Получить логин.
     *
     * @return строковое представление логина.
     */
    public String getLogin() {
        return login;
    }


    /**
     * Получить пароль.
     *
     * @return строковое представление пароля.
     */
    public String getPassword() {
        return password;
    }


    /**
     * Получить ник.
     *
     * @return ник пользователя.
     */
    public String getNickName() {
        return nickName;
    }

}
