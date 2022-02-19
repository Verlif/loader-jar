package idea.verlif.loader.jar.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 文件过滤器
 *
 * @author Verlif
 */
public class FileFilter {

    /**
     * 文件全路径过滤
     */
    private final Set<Pattern> exclude;

    /**
     * 指定加载文件
     */
    private final Set<Pattern> include;

    public FileFilter() {
        exclude = new HashSet<>();
        include = new HashSet<>();
    }

    /**
     * 排斥的路径正则表达式
     *
     * @param regex 正则表达式
     */
    public void exclude(String... regex) {
        for (String s : regex) {
            exclude.add(Pattern.compile(s));
        }
    }

    /**
     * 包括的路径正则表达式，会使得filter
     *
     * @param regex 正则表达式
     */
    public void include(String... regex) {
        for (String s : regex) {
            include.add(Pattern.compile(s));
        }
    }

    /**
     * 清空排除的路径
     */
    public void clearExclude() {
        exclude.clear();
    }

    /**
     * 清空包括的路径
     */
    public void clearInclude() {
        include.clear();
    }

    /**
     * 用于判断文件是否符合加载要求
     *
     * @param file 目标文件
     * @return 该文件对象是否符合加载要求
     */
    public boolean match(File file) {
        if (include.size() == 0) {
            return exclude.stream()
                    .noneMatch(pattern -> pattern.matcher(file.getAbsolutePath()).matches());
        } else {
            return include.stream()
                    .anyMatch(pattern -> pattern.matcher(file.getAbsolutePath()).matches());
        }
    }

    public Set<Pattern> getExclude() {
        return exclude;
    }

    public Set<Pattern> getInclude() {
        return include;
    }
}
