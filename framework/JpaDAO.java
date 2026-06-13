import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaDAO implements InterfaceDAO{
    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public void save(Object entity) {
        em.persist(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> find_criteres(Object entity) {
        String alias = "e";
        StringBuilder jpql = new StringBuilder("SELECT ").append(alias).append(" FROM ")
                .append(entity.getClass().getSimpleName()).append(" ").append(alias).append(" WHERE 1=1");
        List<Object> params = new ArrayList<>();
        List<String> paramNames = new ArrayList<>();

        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    String name = field.getName();
                    jpql.append(" AND ").append(alias).append(".").append(name).append(" = :").append(name);
                    params.add(value);
                    paramNames.add(name);
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        TypedQuery<?> query = em.createQuery(jpql.toString(), entity.getClass());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(paramNames.get(i), params.get(i));
        }
        return new ArrayList<>(query.getResultList());
    }

    @Override
    @Transactional(readOnly = true)
    public void findbyId(Object o) {
        Object id = extractId(o);
        if (id == null) {
            throw new IllegalArgumentException("id field est null");
        }
        Object found = em.find(o.getClass(), id);
        if (found == null) {
            return;
        }
        for (Field field : o.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                field.set(o, field.get(found));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public Object extractId(Object o) {
        try {
            Field idField = o.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return idField.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    @Transactional
    public void delete(Object o) {
        Object managed = em.merge(o);
        em.remove(managed);
    }
    @Transactional
    public void update(Object o) {
        em.merge(o);
    }
}