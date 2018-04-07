package lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Database {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private Connection connection;

    public Database(String url, String usn, String pwd) {
        dbUrl = url;
        dbUsername = usn;
        dbPassword = pwd;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.err.printf("Error occurred while establishing connection, message: %s\n", e.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("No driver found");
            System.exit(1);
        }
        initDB();
    }

    public boolean initDB() {
        // create tables
        String sql = "CREATE TABLE credentials (username VARCHAR(20) PRIMARY KEY, hashed CHAR(32) NOT NULL);";
        try (Statement st = (Statement) connection.createStatement()) {
            return st.execute(sql);
        } catch (SQLException e) {
            if (!"Table 'credentials' already exists".equals(e.getMessage())) {
                System.err.printf("SQL error: %s", e.getMessage());
                System.exit(1);
            }
        }


        return false;
    }

    public boolean saveAccountCredentials(String usn, String hashed) {
        // save new account
        String sql = "INSERT INTO credentials (username, hashed) VALUES (?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, usn);
            st.setString(2, hashed);
            if (st.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.err.printf("SQL error: %s\n", e.getMessage());
        }
        return false;
    }

    public String readAccountHashedPassword(String usn) {
        // get hashed password by account
        String sql = "SELECT hashed FROM credentials WHERE username = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, usn);
            ResultSet r = st.executeQuery();
            if (r.next()) {
                return r.getString("hashed");
            }
        } catch (SQLException e) {
            System.err.printf("SQL error: %s\n", e.getMessage());
        }
        return "";
    }
}
