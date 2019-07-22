package db;

import model.MessageData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {
    private static String SELECT_USER_BY_ID = "SELECT login, password FROM users WHERE id = ?";
    private static String SELECT_USER_BY_LOGIN = "SELECT login, password FROM users WHERE login = ?";
    private static String SELECT_MESSAGE_BY_LOGIN = "SELECT sendto, subject, message FROM messages WHERE userID = " +
            "(SELECT id FROM users WHERE login = ?)";

    public UserData getAccessData(int byID) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, byID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserData(resultSet.getString(1), resultSet.getString(2));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserData getAccessData(String byLogin) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);) {
            preparedStatement.setString(1, byLogin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserData(resultSet.getString(1), resultSet.getString(2));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MessageData> getMessages(String byLogin) {
        List<MessageData> data = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MESSAGE_BY_LOGIN);) {
            preparedStatement.setString(1, byLogin);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                data.add(new MessageData(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }
}
