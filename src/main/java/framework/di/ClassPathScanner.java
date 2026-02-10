package framework.di;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> scan(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            return classes;
        }

        File directory = new File(resource.getFile());
        if (directory.exists()) {
            findClasses(directory, packageName, classes);
        }
        return classes;
    }

    private static void findClasses(File directory, String packageName, Set<Class<?>> classes) throws Exception {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
    }
}
