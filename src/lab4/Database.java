package lab4;

import java.sql.*;
import java.util.ArrayList;

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
    }

    public String[] getSubscribedTopics(String usn) {
        ArrayList<String> result = new ArrayList<>();
        String sql = "SELECT t.topic_name FROM subscribes s JOIN topics t ON t.topic_id=s.topic_id WHERE s.user_id=(SELECT user_id FROM credentials c WHERE c.username=?);";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, usn);
            ResultSet r = st.executeQuery();
            while (r.next()) {
                result.add((String) r.getString("topic_name"));
            }

        } catch (SQLException e) {
            System.err.printf("SQL error: %s\n", e.getMessage());
            return null;

        }
        String[] ret = new String[result.size()];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = result.get(i);
        }

        return ret;
    }

    public void subscribeToTopics(String username, String[] topics) {
        String sql_insert = "INSERT INTO topics (topic_name) VALUES (?);";
        String sql_subs = "CALL subscribe_to_topic(?, ?);";

        for (String t : topics) {
            try (PreparedStatement st = connection.prepareStatement(sql_insert)) {
                st.setString(1, t);
                st.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("SQL error: %s\n", e.getMessage());
            }
            try (PreparedStatement st = connection.prepareStatement(sql_subs)) {
                st.setString(1, username);
                st.setString(2, t);
                st.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("SQL error: %s\n", e.getMessage());
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
