package idea.verlif.loader.jar;

import idea.verlif.loader.jar.config.JarFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * jar文件加载器
 *
 * @author Verlif
 */
public class JarLoader {

    private static final String FILE_JAR_SUFFIX = ".jar";

    private final File file;
    private final List<JarHolder> holders;

    private JarFileFilter jarFileFilter;

    /**
     * 目标文件或文件夹
     *
     * @param file 需要加载的jar文件或包含多个jar文件的文件夹
     */
    public JarLoader(File file) {
        this(file, null);
    }

    public JarLoader(String path) {
        this(new File(path));
    }

    public JarLoader(String path, JarFileFilter filter) {
        this(new File(path), filter);
    }

    public JarLoader(File file, JarFileFilter filter) {
        this.file = file;
        this.holders = new ArrayList<>();
        this.jarFileFilter = filter;
    }

    public void setFileFilter(JarFileFilter jarFileFilter) {
        this.jarFileFilter = jarFileFilter;
    }

    public JarFileFilter getFileFilter() {
        return jarFileFilter;
    }

    /**
     * 重新加载JarHolder。在进行类操作前需要进行加载才可以获取到文件中的类信息。
     */
    public void reload() {
        holders.clear();
        if (file != null) {
            holders.addAll(loadJarFromFile(file));
        }
    }

    /**
     * 加载到的jar包数量
     */
    public int jarFileCount() {
        return holders.size();
    }

    /**
     * 获取所有加载的jar处理器
     */
    public List<JarHolder> getHolders() {
        return holders;
    }

    /**
     * 从文件中获取其对应的JarHolder列表
     *
     * @param file 文件或文件夹
     * @return 目标文件夹或其文件所对应的JarHolder列表
     */
    public List<JarHolder> loadJarFromFile(File file) {
        List<JarHolder> list = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                list.addAll(loadJarFromFile(f));
            }
        } else if (file.getName().endsWith(FILE_JAR_SUFFIX) && (jarFileFilter == null || jarFileFilter.accept(file))) {
            JarHolder holder = new JarHolder(file);
            list.add(holder);
        }
        return list;
    }

    /**
     * 获取所有子类的实例对象。<br/>
     * params表示了目标实例的构造器参数，不填充则调用无参构造器。
     * 若params存在值时，会按照其类型与顺序调用对应的构造器。若没有对应的构造器则无法生成实例。<br/>
     * 例如: <br/>
     * <p> List&lt;Thread> list = jarLoader.getInstances(Thread.class, runnable); </p>
     * 这里，list列表中的实例就是类似于使用new Thread(runnable)所生成的实例。
     *
     * @param cl     父类
     * @param params 构造器参数
     * @param <T>    父类泛型
     * @return 子类实例列表
     */
    public <T> List<T> getSubInstances(Class<T> cl, Object... params) throws IOException, ClassNotFoundException {
        List<T> list = new ArrayList<>();
        for (JarHolder holder : holders) {
            list.addAll(holder.getSubInstances(cl, params));
        }
        return list;
    }

}
