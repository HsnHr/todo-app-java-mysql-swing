import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection baglan() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/todo_app",
                    "root",
                    "SIFRENI_BURAYA_YAZ"
            );

            System.out.println("Bağlantı başarılı!");
            return conn;

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return null;
        }
    }
}