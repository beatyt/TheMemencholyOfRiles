package main.java.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by user on 2016-02-17.
 */
public class DB {
    public void connect() {
        Connection connection = null;
        try {
            // the postgresql driver string
            Class.forName("org.postgresql.StartupHandler");

            // the postgresql url
            String url = "jdbc:postgresql://THE_HOST/THE_DATABASE";

            // get the postgresql database connection
            connection = DriverManager.getConnection(url, "THE_USER", "THE_PASSWORD");

            // now do whatever you want to do with the connection
            // ...

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}
