package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfigs(initialConfigClasses);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        var initialConfigClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);

        processConfigs(initialConfigClasses.toArray(new Class[0]));
    }

    private void processConfigs(Class<?>... initialConfigClasses) {
        var configClasses = Arrays.stream(initialConfigClasses)
            .filter(method -> method.isAnnotationPresent(AppComponentsContainerConfig.class))
            .sorted(Comparator.comparingInt(m1 -> m1.getAnnotation(AppComponentsContainerConfig.class).order()))
            .toList();

        configClasses.forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        var methods = getMethods(configClass);
        methods.forEach(method -> {
                var component = getObject(method);
                var componentName = method.getAnnotation(AppComponent.class).name();
                if (appComponentsByName.put(componentName, component) != null) {
                    throw new RuntimeException(String.format("A component named %s already exists in the context", componentName));
                }
                appComponents.add(component);
            }
        );
    }

    private List<Method> getMethods(Class<?>... configClasses) {
        return Arrays.stream(configClasses)
            .map(Class::getMethods)
            .map(Arrays::asList)
            .flatMap(Collection::stream)
            .filter(method -> method.isAnnotationPresent(AppComponent.class))
            .sorted(Comparator.comparingInt(m1 -> m1.getAnnotation(AppComponent.class).order()))
            .toList();
    }

    private Object getObject(Method method) {
        try {
            var instance = method.getDeclaringClass().getConstructor().newInstance();
            var parTypes = method.getParameterTypes();
            return parTypes.length > 0
                ? method.invoke(instance, Arrays.stream(parTypes).map(this::getAppComponent).toArray())
                : method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var components = appComponents.stream()
            .filter(e -> e.getClass().equals(componentClass) || Arrays.asList(e.getClass().getInterfaces()).contains(componentClass))
            .toList();
        if (components.size() > 1) {
            throw new RuntimeException(String.format("In the context %d components of type %s", components.size(), componentClass));
        } else if (components.isEmpty()) {
            throw new RuntimeException(String.format("Component of type %s not present in context", componentClass));
        } else {
            return (C) components.getFirst();
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException(String.format("Component named %s not present in context", componentName));
        } else {
            return (C) component;
        }
    }
}
