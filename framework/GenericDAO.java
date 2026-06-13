import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class GenericDAO implements InterfaceDAO{
    List<Map> maps;

    public GenericDAO(List<Map> maps){
        this.maps = maps;
    }
    @Override
    public void save(Object obj) {
        String nom_table = obj.getClass().getSimpleName();
        Map map = maps.stream().filter(m -> m.nom_table.equals(nom_table)).findFirst().orElse(null);
        StringBuilder sql = new StringBuilder("INSERT INTO " + nom_table + " (");
        for (String colonne : map.colonnes) {
            sql.append(colonne).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");
        for (String colonne : map.colonnes) {
            sql.append("?, ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");
        try {
            Connection conn = Connexion.dbConnect();
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            int index = 1;
            for (String colonne : map.colonnes) {
                Field field = obj.getClass().getDeclaredField(colonne);
                field.setAccessible(true);
                stmt.setObject(index++, field.get(obj));
            }
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Object> find_criteres(Object o){
        List<Object> result = new ArrayList<>();
        String nom_table = o.getClass().getSimpleName();
        Map map = maps.stream().filter(m -> m.nom_table.equals(nom_table)).findFirst().orElse(null);
        StringBuilder sql = new StringBuilder("SELECT * FROM " + nom_table + " WHERE  1 = 1");
        List<Object> params = new ArrayList<>();

        for(String colonne : map.colonnes){
            try {
                Field field = o.getClass().getDeclaredField(colonne);
                field.setAccessible(true);
                Object value = field.get(o);
                if (value != null) {
                    sql.append(" AND ").append(colonne).append(" = ?"); 
                    params.add(value); 
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            Connection conn = Connexion.dbConnect();
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object instance = o.getClass().getDeclaredConstructor().newInstance();
                for (String colonne : map.colonnes) {
                    Field field = o.getClass().getDeclaredField(colonne);
                    field.setAccessible(true);
                    field.set(instance, rs.getObject(colonne));
                }
                result.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    @Override
    public void findbyId(Object o){
        String nom_table = o.getClass().getSimpleName();
        Map map = maps.stream().filter(m -> m.nom_table.equals(nom_table)).findFirst().orElse(null);
        String sql = "SELECT * FROM " + nom_table + " WHERE id = ?";
        try {
            Connection conn = Connexion.dbConnect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            Field idField = o.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            stmt.setObject(1, idField.get(o));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                for (String colonne : map.colonnes) {
                    Field field = o.getClass().getDeclaredField(colonne);
                    field.setAccessible(true);
                    field.set(o, rs.getObject(colonne));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
    }
    }
}
