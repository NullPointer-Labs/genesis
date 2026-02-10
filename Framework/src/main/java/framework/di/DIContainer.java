package framework.di;

import framework.annotations.Repository;
import framework.annotations.RestController;
import framework.annotations.Service;

import java.lang.reflect.Constructor;
import java.util.*;

public class DIContainer {

    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private Set<Class<?>> availableClasses = new HashSet<>();

    public void init(String basePackage) throws Exception {
        this.availableClasses = ClassPathScanner.scan(basePackage);
    }

    public Object getBean(Class<?> clazz) {
        if (singletons.containsKey(clazz)) {
            return singletons.get(clazz);
        }

        Class<?> implementationClass = resolveImplementation(clazz);

        try {
            Object instance = createInstance(implementationClass);
            singletons.put(clazz, instance);
            singletons.put(implementationClass, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + clazz.getName(), e);
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];

        if (constructor.getParameterCount() == 0) {
            return constructor.newInstance();
        }

        List<Object> params = new ArrayList<>();
        for (Class<?> paramType : constructor.getParameterTypes()) {
            Object dependency = getBean(paramType);
            params.add(dependency);
        }

        return constructor.newInstance(params.toArray());
    }

    private Class<?> resolveImplementation(Class<?> type) {
        if (!type.isInterface()) {
            return type;
        }

        return availableClasses.stream()
                .filter(c -> type.isAssignableFrom(c) && !c.isInterface())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No implementation found for " + type.getName()));
    }

    public Collection<Object> getAllBeans() {
        for (Class<?> clazz : availableClasses) {
            if (clazz.isAnnotationPresent(RestController.class) ||
                    clazz.isAnnotationPresent(Service.class) ||
                    clazz.isAnnotationPresent(Repository.class)) {

                getBean(clazz);
            }
        }
        return new HashSet<>(singletons.values());
    }
}