package idea.verlif.loader.jar;

import idea.verlif.loader.jar.utils.ClassFileUtil;
import idea.verlif.reflection.util.ReflectUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Jar文件持有器
 *
 * @author Verlif
 */
public class JarHolder {

    private final File file;

    public JarHolder(String path) {
        this.file = new File(path);
    }

    public JarHolder(File file) {
        this.file = file;
    }

    public boolean load() throws FileNotFoundException {
        if (file.exists()) {
            if (!file.getName().endsWith(".jar")) {
                throw new FileNotFoundException(file.getName() + " is not a jar file.");
            }
            return true;
        } else {
            throw new FileNotFoundException();
        }
    }

    public File getFile() {
        return file;
    }

    /**
     * 列举所有可以被直接加载的类
     */
    public List<Class<Object>> listClass() throws IOException, ClassNotFoundException {
        return ClassFileUtil.getSubClass(Object.class, file);
    }

    /**
     * 列举所有的包括的类名称
     */
    public Set<String> listClassName() throws IOException {
        return ClassFileUtil.getClassNameFromJar(file);
    }

    /**
     * 获取类的所有子类实例
     *
     * @param cl     目标类
     * @param params 实例构造器参数
     * @param <T>    类泛型
     * @return 实例列表
     */
    public <T> List<T> getSubInstances(Class<T> cl, Object... params) throws IOException, ClassNotFoundException {
        List<T> list = new ArrayList<>();
        List<Class<T>> classes = ClassFileUtil.getSubClass(cl, file);

        for (Class<?> cla : classes) {
            T t = null;
            try {
                t = (T) ReflectUtil.newInstance(cla, params);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            }
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "JarHolder{" +
                "file=" + file.getAbsolutePath() +
                '}';
    }
}
