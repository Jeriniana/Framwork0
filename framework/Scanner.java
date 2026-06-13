import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Scanner {
    public List<Class<?>> scan(String pkg) {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = pkg.replace('.', '/');

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (!"file".equals(resource.getProtocol())) {
                    continue;
                }
                File directory = new File(resource.toURI());
                scanDirectory(pkg, directory, classes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private void scanDirectory(String packageName, File directory, List<Class<?>> classes) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(packageName + "." + file.getName(), file, classes);
                continue;
            }

            if (!file.getName().endsWith(".class") || file.getName().contains("$")) {
                continue;
            }

            String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
