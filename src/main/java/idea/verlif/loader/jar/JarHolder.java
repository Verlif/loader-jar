package idea.verlif.loader.jar;

import idea.verlif.loader.jar.utils.ClassFileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public <T> List<T> getInstances(Class<T> cl, Object... params) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        List<Class<T>> classes = ClassFileUtil.getAllClass(cl, file);

        for (Class<?> cla : classes) {
            List<Constructor<?>> cLi = new ArrayList<>();
            do {
                cLi.addAll(Arrays.asList(cla.getConstructors()));
                cla = cla.getSuperclass();
            } while (cla != null);
            for (Constructor<?> constructor : cLi) {
                if (constructor.getParameterCount() == params.length) {
                    Class<?>[] types = constructor.getParameterTypes();
                    int flag = 0;
                    for (int i = 0; i < types.length; i++) {
                        if (recalculate(types[i]).isAssignableFrom(params[i].getClass())) {
                            flag++;
                        }
                    }
                    if (flag == types.length) {
                        T t = (T) constructor.newInstance(params);
                        list.add(t);
                        break;
                    }
                }
            }
        }
        return list;
    }

    private Class<?> recalculate(Class<?> cl) {
        switch (cl.getSimpleName()) {
            case "int":
                return Integer.class;
            case "double":
                return Double.class;
            case "float":
                return Float.class;
            case "byte":
                return Byte.class;
            case "short":
                return Short.class;
            case "long":
                return Long.class;
            case "boolean":
                return Boolean.class;
            case "char":
                return Character.class;
            default:
                return cl;
        }
    }

    @Override
    public String toString() {
        return "JarHolder{" +
                "file=" + file.getAbsolutePath() +
                '}';
    }
}
