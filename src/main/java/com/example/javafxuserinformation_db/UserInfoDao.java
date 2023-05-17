package com.example.javafxuserinformation_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserInfoDao {
    private Connection connection;
    public ArrayList list = new ArrayList<String>();
    public User user;

    public UserInfoDao(Connection connection) {
        this.connection = connection;

        getCompanies(1);
        getCompanies(2);
        getCompanies(3);

    }

    public void getCompanies(int id) {
        final var SQL = "SELECT name FROM companies WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String companies(int id) {
        return this.list.get(id - 1).toString();
    }

    public void getRecord(int id) {
        final var SQL = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                this.user = new User(companies(rs.getInt("company_id")), rs.getString("name"), rs.getInt("score"));
                this.user.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertInfo(String job, String name, int score) {
        final var SQL = "INSERT INTO users (name, company_id, score) VALUES (?, ?, ?)";
        final var SQL2 = "SELECT id FROM companies WHERE name = ?";

        try {
            PreparedStatement stmt2 = this.connection.prepareStatement(SQL2);
            stmt2.setString(1, job);
            ResultSet rs = stmt2.executeQuery();
            int jobId = 0;
            while (rs.next()) {
                jobId = rs.getInt("id");
            }

            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setString(1, name);
            stmt.setInt(2, jobId);
            stmt.setInt(3, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
