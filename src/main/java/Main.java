import java.sql.*;

public class Main {

    private static final String DB_DRIVER =  "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/leathercollection";
    private static final String DB_USER = "danny";
    private static final String DB_PASS = "jw8s0F4";

    public static void main(String[] args) {

        // readFromDB();
        // writeToLeatherClothes();
    }

    private static void writeToLeatherClothes() {
        String insertQuery = "INSERT INTO leatherclothes\n" +
                "                (id, brand, length, isnatural, description, category_id, color_id, manufacturer_id, size_id, style_id)\n" +
                "                VALUES (9, 'Seduction', 37, true, 'Юбочка из кожи в треш-металлическом стиле с клёпками', 1, 1, 2,\n" +
                "                4, 2);";
        try {
            loadDriver();
            Connection connection = getConnection();
            if (connection != null) {
                System.out.println("You succesfully connected to database now...");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(insertQuery);
                stmt.close();
                connection.close();
            }
            else {
                System.out.println("Failed to make connection to database (");
            }
        }
        catch (SQLException e) {
            System.out.println("Cannot add data to table");
            e.printStackTrace();
        }




    }

    private static void readFromDB() {

        String query = "SELECT l.brand, l.length, c.color_name as Цвет,\n" +
                "       siz.size_name as Размер,\n" +
                "       s.style_name as Стиль, l.isnatural as Натуральные,\n" +
                "       cat.category_name AS Категория from leatherclothes l\n" +
                "                                               JOIN style s on l.style_id = s.style_id\n" +
                "                                               JOIN color c on c.color_id = l.color_id\n" +
                "                                               JOIN category cat on l.category_id = cat.category_id\n" +
                "                                               JOIN size siz on siz.size_id = l.size_id\n" +
                "                                               JOIN manufacturer m on l.manufacturer_id = m.manufacturer_id\n" +
                "WHERE m.manufacturer_name = 'Россия';";
        try {
            loadDriver();


            Connection connection = getConnection();

            if (connection != null) {
                System.out.println("You successfully connected to database now");

                ResultSet rs = makeQuery(query, connection);
                System.out.println("\nВыводим из базы leathercollection те поля в связанных таблицах, где производитель " +
                        " указан как 'Россия':");
                while (rs.next()) {
                    String brand = rs.getString("brand");
                    float length = rs.getFloat("length");
                    String color = rs.getString("Цвет");
                    String size = rs.getString("Размер");
                    String style = rs.getString("Стиль");

                    String cat = rs.getString("Категория");
                    System.out.printf("Бренд: %s\tДлина: %.1f\tЦвет: %s\tРазмер: %s\tСтиль: %s\tКатегория: %s\n",
                            brand, length, color, size, style, cat);

                }
                rs.close();
                connection.close();

            } else {
                System.out.println("Failed to make connection to database");
            }
        } catch (SQLException e) {
            System.out.println("Unable to display dataset :(");
            e.printStackTrace();
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

    private static ResultSet makeQuery(String query, Connection connection) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println("Execution failed :(");
        }
        return  rs;

    }
}
