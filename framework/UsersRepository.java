import Modeles.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsersRepository {
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(Users user) {
		em.persist(user);
	}
}
