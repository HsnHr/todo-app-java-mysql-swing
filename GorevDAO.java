import java.sql.*;
import java.util.ArrayList;

public class GorevDAO {

    public static void gorevEkle(String baslik) {
        try {
            Connection conn = Database.baglan();

            String sql = "INSERT INTO gorevler (baslik) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, baslik);

            ps.executeUpdate();
            System.out.println("Görev eklendi.");

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    public static ArrayList<Gorev> gorevleriGetir() {
        ArrayList<Gorev> liste = new ArrayList<>();

        try {
            Connection conn = Database.baglan();

            String sql = "SELECT * FROM gorevler ORDER BY id DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String baslik = rs.getString("baslik");
                boolean tamamlandi = rs.getBoolean("tamamlandi");
                String tarih = rs.getString("tarih");

                liste.add(new Gorev(id, baslik, tamamlandi, tarih));
            }

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }

        return liste;
    }

    public static void gorevTamamla(int id) {
        try {
            Connection conn = Database.baglan();

            String sql = "UPDATE gorevler SET tamamlandi = TRUE WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Görev tamamlandı.");

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    public static void gorevSil(int id) {
        try {
            Connection conn = Database.baglan();

            String sql = "DELETE FROM gorevler WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Görev silindi.");

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}