package com.glimsil.template.vertx.config;

import com.glimsil.template.vertx.config.annotation.Api;
import com.glimsil.template.vertx.config.annotation.Endpoint;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class RouteConfig {

    public Router configRoutes(Vertx vertx ) {
        List<Class<?>> classes = getAnnotatedClassesInPackage("com.glimsil.template.vertx.api", Api.class);
        classes.forEach(c -> System.out.println(c.getName()));
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        for(Class<?> c : classes) {
            Api api = c.getAnnotation(Api.class);
            try {
                final Object instance = c.newInstance();

                for (Method method : c.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Endpoint.class)) {
                        Endpoint endpoint = method.getAnnotation(Endpoint.class);
                        router.route().method(endpoint.method()).path(getFullPath(api.value(), endpoint.path())).
                                handler(r -> {
                            try {
                                method.invoke(instance, r);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return router;
    }

    private List<Class<?>> getAnnotatedClassesInPackage(String packageName, Class<? extends Annotation> ann) {
        String path = packageName.replaceAll("\\.", File.separator);
        List<Class<?>> classes = new ArrayList<>();
        String[] classPathEntries = System.getProperty("java.class.path").split(
                System.getProperty("path.separator")
        );

        String name;
        for (String classpathEntry : classPathEntries) {
            if (classpathEntry.endsWith(".jar")) {
                File jar = new File(classpathEntry);
                try {
                    JarInputStream is = new JarInputStream(new FileInputStream(jar));
                    JarEntry entry;
                    while ((entry = is.getNextJarEntry()) != null) {
                        name = entry.getName();
                        if (name.endsWith(".class")) {
                            if (name.contains(path) && name.endsWith(".class")) {
                                String classPath = name.substring(0, entry.getName().length() - 6);
                                classPath = classPath.replaceAll("[\\|/]", ".");
                                Class<?> clazz = Class.forName(classPath);
                                if(clazz.isAnnotationPresent(ann)) {
                                    classes.add(clazz);
                                }
                            }
                        }
                    }
                } catch (Exception ex) { }
            } else {
                try {
                    File base = new File(classpathEntry + File.separatorChar + path);
                    for (File file : base.listFiles()) {
                        name = file.getName();
                        if (name.endsWith(".class")) {
                            name = name.substring(0, name.length() - 6);
                            Class<?> clazz = Class.forName(packageName + "." + name);
                            if(clazz.isAnnotationPresent(ann)) {
                                classes.add(clazz);
                            }
                        }
                    }
                } catch (Exception ex) { }
            }
        }
        return classes;
    }

    private String getFullPath(String pathA, String pathB) {
        StringBuilder completePath = new StringBuilder();
        completePath.append("/");
        for(String subPath : pathA.split("/")) {
            if(!subPath.equals("")) {
                completePath.append(subPath);
                completePath.append("/");
            }
        }
        for(String subPath : pathB.split("/")) {
            if(!subPath.equals("")) {
                completePath.append(subPath);
                completePath.append("/");
            }
        }
        return completePath.toString();
    }
}
