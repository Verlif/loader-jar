package idea.verlif.loader.jar.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Verlif
 */
public class ClassFileUtil {

    private static final String FILE_PREF = "file:";
    private static final String FILE_CLASS_SUFFIX = ".class";

    private ClassFileUtil() {
    }

    /**
     * 获取jar文件中的所有子类
     *
     * @param interfaceClass 目标父类
     * @param path           jar文件路径
     * @param <T>            父类泛型
     * @return 查找到的所有子类
     */
    public static <T> List<Class<T>> getSubClass(Class<T> interfaceClass, String path) throws IOException, ClassNotFoundException {
        Set<String> nameList = getClassNameFromJar(path);
        // 构建url路径
        if (!path.startsWith(FILE_PREF)) {
            path = FILE_PREF + path;
        }
        return getSubClass(interfaceClass, path, nameList);
    }

    /**
     * 获取jar文件中的所有子类
     *
     * @param interfaceClass 目标父类
     * @param file           jar文件
     * @param <T>            父类泛型
     * @return 查找到的所有子类
     */
    public static <T> List<Class<T>> getSubClass(Class<T> interfaceClass, File file) throws IOException, ClassNotFoundException {
        Set<String> nameList = getClassNameFromJar(file);
        return getSubClass(interfaceClass, FILE_PREF + file.getPath(), nameList);
    }

    private static <T> List<Class<T>> getSubClass(Class<T> interfaceClass, String url, Collection<String> nameList) throws IOException, ClassNotFoundException {
        List<Class<T>> list = new ArrayList<>();
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(url)})) {
            // 获取所有的类
            for (String name : nameList) {
                try {
                    Class<?> acl = urlClassLoader.loadClass(name);
                    if (!Modifier.isAbstract(acl.getModifiers())
                            && !Modifier.isInterface(acl.getModifiers())
                            && interfaceClass.isAssignableFrom(acl)) {
                        list.add((Class<T>) acl);
                    }
                } catch (NoClassDefFoundError | UnsupportedClassVersionError ignored) {
                    // 失败的类进行过滤
                }
            }
        }
        return list;
    }

    /**
     * 重构类名
     */
    private static String rebuildClassName(String name) {
        if (name.contains("/")) {
            return name.replace("/", ".");
        }
        if (name.contains("\\")) {
            return name.replace("\\\\", ".");
        }
        return name;
    }

    /**
     * 从jar包获取所有的class文件名
     */
    public static Set<String> getClassNameFromJar(String path) throws IOException {
        return getClassNameFromJar(new File(path));
    }

    /**
     * 从jar包获取所有的class文件名
     */
    public static Set<String> getClassNameFromJar(File file) throws IOException {
        Set<String> nameList = new HashSet<>();
        try (JarFile jarFile = new JarFile(file)) {
            // 枚举获得JAR文件内的实体
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                String name = en.nextElement().getName();
                // 通过文件后缀获取类文件
                if (name.endsWith(FILE_CLASS_SUFFIX)) {
                    String className = rebuildClassName(name.substring(0, name.length() - FILE_CLASS_SUFFIX.length()));
                    nameList.add(className);
                }
            }
        }
        return nameList;
    }
}
