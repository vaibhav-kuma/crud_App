package com.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private static final String URL = "jdbc:h2:./data/persons;AUTO_SERVER=TRUE";

    static {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS person (" +
                    "id IDENTITY PRIMARY KEY, name VARCHAR(100), email VARCHAR(150))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, "sa", "");
    }

    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT * FROM person ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Person(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void insert(Person p) {
        String sql = "INSERT INTO person (name, email) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Person p) {
        String sql = "UPDATE person SET name=?, email=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM person WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
