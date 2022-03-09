package org.msvdev_k.sm.server.authorization;


import java.sql.*;

/**
 * Класс, реализующий аутентификацию клиента через БД SQLite.
 */
public class SQLiteAuthService implements AuthService {

    /**+
     * Соединение с БД.
     */
    private Connection connection;


    private void configDB() throws SQLException {

        String sql = "CREATE TABLE users (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nick_name TEXT," +
                     "login TEXT," +
                     "password TEXT);" +

                     "INSERT INTO users(nick_name, login,password) VALUES" +
                     "('user1', 'log1', 'pass1')," +
                     "('user2', 'log2', 'pass2')," +
                     "('user3', 'log3', 'pass3')," +
                     "('user4', 'log4', 'pass4');";


        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();

    }


    @Override
    public void start() throws SQLException {

        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        configDB();
    }


    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {

        String nickName = null;
        String sql = "SELECT nick_name FROM users WHERE login = ? AND password = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nickName = resultSet.getString(1);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nickName;
    }


    @Override
    public void end() {

        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
