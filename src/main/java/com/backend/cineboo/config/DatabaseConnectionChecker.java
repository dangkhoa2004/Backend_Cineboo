/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.backend.cineboo.config;

import java.sql.*;
/**
 *
 * @author 04dkh
 */
public class DatabaseConnectionChecker {

    public static boolean checkConnection() {
        String url = "jdbc:mysql://localhost:3306/datvexemphimcineboo";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Kết nối MySQL thành công!");
                return true;
            } else {
                System.out.println("Không thể kết nối MySQL.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối MySQL: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        checkConnection();
    }
}
