package lab3;

import java.sql.*;

class Database {
    private Connection connection;

    public Database(String url, String usn, String pwd) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, usn, pwd);
        } catch (SQLException e) {
            System.err.printf("Error occurred while establishing connection, message: %s\n", e.getMessage());
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("No driver found");
            System.exit(1);
        }
        initDB();
    }

    private void initDB() {
        // create tables
        String sql = "CREATE TABLE credentials (username VARCHAR(20) PRIMARY KEY, hashed CHAR(32) NOT NULL);";
        try (Statement st = connection.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            if (!"Table 'credentials' already exists".equals(e.getMessage())) {
                System.err.printf("SQL error: %s", e.getMessage());
                System.exit(1);
            }
        }


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
