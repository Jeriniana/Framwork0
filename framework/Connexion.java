import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    public static Connection dbConnect() {
        String url = "jdbc:postgresql://localhost:5432/framework";
        String user = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
            return conn;

        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL introuvable !");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion !");
            e.printStackTrace();
        }

        return null;
    }
}