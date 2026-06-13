import java.util.*;

public class Map{
    String nom_table;
    List<String> colonnes;

    public Map(String nom_table, List<String> colonnes) {
        this.nom_table = nom_table;
        this.colonnes = colonnes;
    }
    public static List<Map> map(List<Class<?>> classes){
        List<Map> maps = new ArrayList<>();
        for (Class<?> c : classes) {
            String nom_table = c.getSimpleName();
            List<String> colonnes = new ArrayList<>();
            for (var field : c.getDeclaredFields()) {
                colonnes.add(field.getName());
            }
            maps.add(new Map(nom_table, colonnes));
        }
        return maps;
    }
}

