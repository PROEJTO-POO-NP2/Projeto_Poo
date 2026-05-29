import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://192.168.1.244:5432/base_estudos?currentSchema=bd_rsc_poo";
        String user = "postgres";
        String password = "postgres";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            
            System.out.println("--- TABELA responsaveis_saude ---");
            ResultSet rs = stmt.executeQuery("SELECT * FROM responsaveis_saude");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nome: " + rs.getString("nome_completo") + 
                                   " | Email: '" + rs.getString("email") + "' | Senha: '" + rs.getString("senha") + "'");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
