package idea.verlif.loader.jar.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Verlif
 */
public class ClassFileUtil {

    private static final String FILE_PREF = "file:";

    /**
     * 获取jar文件中的所有子类
     *
     * @param cl   目标父类
     * @param path jar文件路径
     * @param <T>  父类泛型
     * @return 查找到的所有子类
     */
    public static <T> List<Class<T>> getAllClass(Class<T> cl, String path) {
        List<Class<T>> list = new ArrayList<>();
        List<String> nameList = getClassNameFromJar(path);
        // 构建url路径
        if (!path.startsWith(FILE_PREF)) {
            path = FILE_PREF + path;
        }
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(path)})) {
            // 获取所有的类
            for (String name : nameList) {
                Class<?> acl = urlClassLoader.loadClass(rebuildClassName(name));
                if (!Modifier.isAbstract(acl.getModifiers())
                        && !Modifier.isInterface(acl.getModifiers())
                        && cl.isAssignableFrom(acl)) {
                    list.add((Class<T>) acl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取jar文件中的所有子类
     *
     * @param cl   目标父类
     * @param file jar文件
     * @param <T>  父类泛型
     * @return 查找到的所有子类
     */
    public static <T> List<Class<T>> getAllClass(Class<T> cl, File file) {
        List<Class<T>> list = new ArrayList<>();
        List<String> nameList = getClassNameFromJar(file);
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(FILE_PREF + file.getPath())})) {
            // 获取所有的类
            for (String name : nameList) {
                Class<?> acl = urlClassLoader.loadClass(rebuildClassName(name));
                if (!Modifier.isAbstract(acl.getModifiers())
                        && !Modifier.isInterface(acl.getModifiers())
                        && cl.isAssignableFrom(acl)) {
                    list.add((Class<T>) acl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 重构类名
    private static String rebuildClassName(String name) {
        if (name.contains("/")) {
            return name.replaceAll("/", ".");
        }
        if (name.contains("\\")) {
            return name.replaceAll("\\\\", ".");
        }
        return name;
    }

    /**
     * 从jar包获取所有的class文件名
     */
    private static List<String> getClassNameFromJar(String path) {
        return getClassNameFromJar(new File(path));
    }

    /**
     * 从jar包获取所有的class文件名
     */
    private static List<String> getClassNameFromJar(File file) {
        List<String> nameList = new ArrayList<>();
        try {
            JarFile jarFile = new JarFile(file);
            // 枚举获得JAR文件内的实体
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                String name = en.nextElement().getName();
                // 通过文件后缀获取类文件
                if (name.endsWith(".class")) {
                    String[] ns = name.split("\\.");
                    nameList.add(ns[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameList;
    }
}
