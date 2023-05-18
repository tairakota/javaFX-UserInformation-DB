package com.example.javafxuserinformation_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDao {
    private Connection connection;
    public ArrayList list = new ArrayList<String>();

    public UserInfoDao(Connection connection) {
        this.connection = connection;

        getCompanies(1);
        getCompanies(2);
        getCompanies(3);

    }

    public void getCompanies(int id) {
        final var SQL = "SELECT name FROM companies WHERE id = ? ORDER BY id";
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
    public String companiesName(int id) {
        final var SQL = "SELECT name FROM companies WHERE id = ?";
        String companyName = null;
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                companyName = rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return companyName;
    }
    public int companyId(String job) {
        final var SQL = "SELECT id FROM companies WHERE name = ?";
        int companyId = 0;
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setString(1, job);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                companyId = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return companyId;
    }

    public List<InfoRecord> getRecord() {
        final var SQL = "SELECT * FROM users ORDER BY id";
        var list = new ArrayList<InfoRecord>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                var infoRecord
                        = new InfoRecord(rs.getInt("id"), rs.getString("name"), companiesName(rs.getInt("company_id")), rs.getInt("score"));
                list.add(infoRecord);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void insertInfo(String job, String name, int score) {
        final var SQL = "INSERT INTO users (name, company_id, score) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setString(1, name);
            stmt.setInt(2, companyId(job));
            stmt.setInt(3, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void deleteInfo(int id) {
        final var SQL = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void updateInfo(int id, String name, String job, int score) {
        final var SQL = "UPDATE users SET name = ?, company_id = ?, score = ? WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setString(1, name);
            stmt.setInt(2, companyId(job));
            stmt.setInt(3, score);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public List<InfoRecord> findInfo(String find) {
        final var SQL = "SELECT * FROM users WHERE name LIKE ? ORDER BY id";
        var list = new ArrayList<InfoRecord>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(SQL);
            stmt.setString(1, find);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                var infoRecord
                        = new InfoRecord(rs.getInt("id"), rs.getString("name"), companiesName(rs.getInt("company_id")), rs.getInt("score"));
                list.add(infoRecord);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return list;
    }
}
