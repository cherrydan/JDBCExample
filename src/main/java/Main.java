import java.sql.*;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

//Три метода чтобы показать возможности JDBC -
// readFromDB - выводит некоторую информацию из связанных таблиц
// writeToLeatherClothes - добавляет информацию об одной вещи и присваивает ей id 9
// deleteWhereIdIs9 - и так ясно
public class Main {

    private static final String insertQuery = "INSERT INTO leatherclothes\n" +
            "                (id, brand, length, isnatural, description, category_id, color_id, manufacturer_id, size_id, style_id)\n" +
            "                VALUES (9, 'Seduction', 37, true, 'Юбочка из кожи в треш-металлическом стиле с клёпками', 1, 1, 2,\n" +
            "                4, 2);";
    private static final String selectQuery = "SELECT * FROM leatherclothes WHERE id=9";
    private static final String query = "SELECT l.brand, l.length, c.color_name as Цвет,\n" +
            "       siz.size_name as Размер,\n" +
            "       s.style_name as Стиль, l.isnatural as Натуральные,\n" +
            "       cat.category_name AS Категория from leatherclothes l\n" +
            "                                               JOIN style s on l.style_id = s.style_id\n" +
            "                                               JOIN color c on c.color_id = l.color_id\n" +
            "                                               JOIN category cat on l.category_id = cat.category_id\n" +
            "                                               JOIN size siz on siz.size_id = l.size_id\n" +
            "                                               JOIN manufacturer m on l.manufacturer_id = m.manufacturer_id\n" +
            "WHERE m.manufacturer_name = 'Россия';";

    private static final String deleteQuery = "DELETE FROM leatherclothes WHERE id=9";
    private static final String DB_DRIVER =  "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/leathercollection";
    private static final String DB_USER = "danny";
    private static final String DB_PASS = "jw8s0F4";

    public static void main(String[] args) {

        System.out.println("Welcome to console JDBC PostgreSQL driver demo!");
        System.out.println("Choose action: ");
        System.out.println("Read some info from database        press 1");
        System.out.println("Add 9-th record to database         press 2");
        System.out.println("Delete 9-th record from database    press 3");

        System.out.print("--> ");
        try (Scanner sc = new Scanner(System.in)) {
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    readFromDB();
                    break;
                case 2:
                    writeToLeatherClothes();
                    break;
                case 3:
                    deleteWhereIdIs9();
                    break;
                default:
                    System.out.println("You must input 1 or 2 or 3!");

            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input!");
            e.printStackTrace();
        }


    }

    private static void deleteWhereIdIs9() {
        runQuery(deleteQuery);
        System.out.println("Record with id 9 has been deleted succesfully");

    }

    private static void runQuery(String Query) {
        Connection connection = getConnection();
        try {
            loadDriver();

            if (connection != null) {
                System.out.println("You succesfully connected to database now...");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(Query);
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

    private static void writeToLeatherClothes() {

        runQuery(insertQuery);
        ResultSet rs = makeQuery(selectQuery, Objects.requireNonNull(getConnection()));
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                System.out.printf("Id добавленой записи в БД = %d\n", id);

            }
        }
    catch(SQLException e) {
            System.out.println("Cannot load data...");
            e.printStackTrace();
    }

    }

    private static void readFromDB() {
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

    private static ResultSet makeQuery(String Query, Connection connection) {
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(Query);

        } catch (SQLException e) {
            System.out.println("Execution failed :(");
        }
        return  rs;

    }
}
