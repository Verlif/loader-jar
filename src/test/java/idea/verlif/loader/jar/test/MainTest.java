package idea.verlif.loader.jar.test;

import idea.verlif.loader.jar.JarHolder;
import idea.verlif.loader.jar.JarLoader;
import idea.verlif.loader.jar.config.JarFileFilter;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MainTest {

    @Test
    public void loadTest() throws IOException, ClassNotFoundException {
        JarLoader loader = new JarLoader("D:\\Code\\Java\\loader-jar\\target\\loader-jar-0.2.jar");
        loader.reload();
        System.out.println("已加载的数量: " + loader.jarFileCount());
        List<JarHolder> holders = loader.getHolders();
        for (JarHolder holder : holders) {
            System.out.println(holder.getFile().getName());
            Set<String> nameList = holder.listClassName();
            for (String s : nameList) {
                System.out.println("\t--> " + s);
            }
        }

        List<JarFileFilter> subInstances = loader.getSubInstances(JarFileFilter.class);
        for (JarFileFilter subInstance : subInstances) {
            System.out.println(subInstance);
        }
    }
}
