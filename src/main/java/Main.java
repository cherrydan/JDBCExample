import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    private static final String DB_DRIVER =  "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/leathercollection";
    private static final String DB_USER = "danny";
    private static final String DB_PASS = "jw8s0F4";

    public static void main(String[] args) {

        loadDriver();
        Connection connection;
        connection = getConnection();

        if (connection != null) {
            System.out.println("You successfully connected to database now");

        } else {
            System.out.println("Failed to make connection to database");
        }


    }

    private static void loadDriver() {
        try {
            Class.forName(Main.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();

        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");
    }

    private static Connection getConnection() {
        Connection connection;

        try {
            connection = DriverManager
                    .getConnection(Main.DB_URL, Main.DB_USER, Main.DB_PASS);
            return connection;

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return null;
        }
    }


}
