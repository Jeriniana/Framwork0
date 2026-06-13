import Modeles.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    @Autowired
    private UsersRepository usersRepository;

    public void run() {
        Users user1 = new Users(7, "Doe", "John", 30);
        usersRepository.save(user1);
    }

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            MainApp app = ctx.getBean(MainApp.class);
            app.run();
        }
    }
}
