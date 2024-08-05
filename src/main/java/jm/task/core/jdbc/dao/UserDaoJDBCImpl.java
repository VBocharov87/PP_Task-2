package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_SPREADSHEET_QUERY = "CREATE TABLE IF NOT EXISTS spreadsheet (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), lastname VARCHAR(255), age TINYINT)";
    private static final String DROP_SPREADSHEET_QUERY = "DROP TABLE IF EXISTS spreadsheet";
    private static final String INSERT_QUERY = "INSERT INTO spreadsheet (name, lastname, age) VALUES (?, ?, ?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM spreadsheet WHERE ID = ?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM spreadsheet";
    private static final String DELETE_ALL_DATA_QUERY = "DELETE FROM spreadsheet";


    @Override
    public void createUsersTable() {

        try (Statement statement = Util.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_SPREADSHEET_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {

        try (Statement statement = Util.getConnection().createStatement()) {
            statement.executeUpdate(DROP_SPREADSHEET_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {

        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(DELETE_BY_ID_QUERY)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Statement statement = Util.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_QUERY);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {

        try (Statement statement = Util.getConnection().createStatement()) {
            statement.executeUpdate(DELETE_ALL_DATA_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
