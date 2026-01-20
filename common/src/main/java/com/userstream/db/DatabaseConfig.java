package com.userstream.db;

import java.sql.*;
import java.util.Optional;

import com.userstream.user.UserData;

public class DatabaseConfig {

    private static final String URL = "jdbc:sqlite:userstream.db";

    public static Connection getConnection()throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTable(){

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    surname TEXT,
                    email, TEXT
                    );
                """;

        try(Connection connection = DatabaseConfig.getConnection();
            Statement statement = connection.createStatement()) {

            statement.execute(sql);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void save(UserData user){
        String sql = "INSERT INTO users (name, surname, email) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1,user.getName());
            statement.setString(2,user.getSurname());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Optional<UserData> findById(int id){
        String sql = "SELECT * FROM users WHERE id = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                UserData user = new UserData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("email")
                );
                return Optional.of(user);

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
