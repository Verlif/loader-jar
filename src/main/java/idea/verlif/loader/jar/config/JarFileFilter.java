package idea.verlif.loader.jar.config;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 文件过滤器
 *
 * @author Verlif
 */
public class JarFileFilter implements FileFilter {

    /**
     * 文件全路径过滤
     */
    private final Set<Pattern> exclude;

    /**
     * 指定加载文件
     */
    private final Set<Pattern> include;

    public JarFileFilter() {
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

    public Set<Pattern> getExclude() {
        return exclude;
    }

    public Set<Pattern> getInclude() {
        return include;
    }

    @Override
    public boolean accept(File file) {
        if (include.isEmpty()) {
            return exclude.stream()
                    .noneMatch(pattern -> pattern.matcher(file.getAbsolutePath()).matches());
        } else {
            return include.stream()
                    .anyMatch(pattern -> pattern.matcher(file.getAbsolutePath()).matches());
        }
    }
}
