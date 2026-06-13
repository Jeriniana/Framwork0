
import java.util.List;

public interface InterfaceDAO{
    public void save(Object obj);
    public List<Object> find_criteres(Object o);
    public void findbyId(Object o);
}