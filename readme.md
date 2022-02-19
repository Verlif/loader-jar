# idea.verlif.loader.jar.JarLoader

jar文件加载器，用来加载jar中的实例。  
例如加载一些jar插件或是实现自己的一些模组化系统还是挺方便的。

## 使用

当我这里有几个jar存在于`F:\test\jar`文件夹中时，可以通过以下方式加载jar包：

```java
JarLoader loader = new JarLoader("F:\\test\\jar");
// 或使用以下方式
File file = new File("F:\\test\\jar")
JarLoader loader = new JarLoader(file);
```

也可以使用`idea.verlif.loader.jar.config.FileFilter`来加载特定的jar包：

```java
// 新建文件过滤器
FileFilter filter = new FileFilter();
// 添加排除的文件总路径名(File.getAbsolutePath())正则表达式
filter.exclude(".*beta.*", ".*test.*");
// 添加包括的文件总路径名(File.getAbsolutePath())正则表达式
// 使用了include(String...)后，exclude(String...)将会被无效化
filter.include(".*release.*", ".*alpha.*");

// 在新建JarLoader时添加过滤器
JarLoader loader = new JarLoader("F:\\test", filter);
// 或是在已有的JarLoader中添加过滤器，这是需要使用reload()方法来重载加载器
oldLoader.setFileFilter(filter);
oldLoader.reload();
```

获取`JarLoader`后，就可以调用其方法了。

```java
// 当想要加载的jar包中的Thread实例时
List<Thread> li = loader.getInstances(
        Thread.class,
        (Runnable) () -> System.out.println("Ok"));
// 这里的第二个参数是可变长度参数，表示了生成实例时使用的构造方法参数
// 然后就可以通过li列表来调用Thread了
```

## 添加

1. 添加Jitpack仓库源

> maven
> ```xml
> <repositories>
>    <repository>
>        <id>jitpack.io</id>
>        <url>https://jitpack.io</url>
>    </repository>
> </repositories>
> ```

> Gradle
> ```text
> allprojects {
>   repositories {
>       maven { url 'https://jitpack.io' }
>   }
> }
> ```

2. 添加依赖

> maven
> ```xml
>    <dependencies>
>        <dependency>
>            <groupId>com.github.Verlif</groupId>
>            <artifactId>loader-jar</artifactId>
>            <version>0.1.3</version>
>        </dependency>
>    </dependencies>
> ```

> Gradle
> ```text
> dependencies {
>   implementation 'com.github.Verlif:loader-jar:0.1.3'
> }
> ```
