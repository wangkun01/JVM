package com.xiaoming.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangkun
 * @date 2019-07-25 23:30
 */
public class MyClassLoader extends ClassLoader {

    //自定义的ClassLoader加载的根路径
    private String rootPath;
    //有哪些class被我们自定义的类加载器加载的
    private List<String> clazzs;

    /**
     * 重写构造方法,ClassLoader默认的构造方法加载了系统的ClassLoader,自己写的ClassLoader不需要
     */
    public MyClassLoader(String rootPath, String... classPaths) throws IOException {
        this.rootPath = rootPath;
        clazzs = new ArrayList<>();

        /**
         * 在此处扫描,是为了打破双亲委派机制
         * First, check if the class has already been loaded
         *   ClassLoader 的loadClass方法源码中, Class<?> c = findLoadedClass(name);
         *  这里查询出来不为空 不会走双亲委派的逻辑
         */
        for (String classpath : classPaths) {
            loadClassPath(new File(classpath));
        }
    }


    //扫描路径并且把class加载到JVM中
    public void loadClassPath(File file) throws IOException {
        //判断是文件还是目录
        if (file.isDirectory()) {
            //如果是目录
            for (File files : file.listFiles()) {
                //递归查找
                loadClassPath(files);
            }
        } else {
            //开始加载文件
            String fileName = file.getName();
            String filePath = file.getPath();
            //后缀名
            String endName = fileName.substring(fileName.lastIndexOf('.') + 1);
            if ("class".equals(endName)) {
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                is.read(bytes, 0, bytes.length);
                // //路径的转化 xx\\xx\\xx 转化为 xx.xx.xx
                String className = filePathToClassName(filePath);
                //clazzs记录已被加载过的class
                clazzs.add(className);
                //父类的方法,这时class文件已经进入了JVM，还未加载
                defineClass(className, bytes, 0, bytes.length);
            }
        }
    }

    //路径的转化 xx\\xx\\xx 转化为 xx.xx.xx
    public String filePathToClassName(String filePath) {

        String className = filePath.replace(rootPath, "").replaceAll("\\\\", ".");
        className = className.substring(0, className.lastIndexOf("."));
        //经测试这么写是为了把最前面的空格去掉
        //最后的结果为：com.xiaoming.ClassLoaderDemo
        className = className.substring(1);
        return className;
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //此方法不重写也可以，为了代码严谨
        //TODO此方法需要加锁..
        Class clazz = findLoadedClass(name);
        //判断传入的类是否被加载到了
        if (clazz == null) {
            //不属于我们自定义的类加载器加载
            if (!clazzs.contains(name)) {
                //交给系统的类加载器加载
                clazz = getSystemClassLoader().loadClass(name);
            } else {
                new ClassNotFoundException("加载不到类");
            }
        }
        return clazz;
    }

    //测试类
    public static void main(String[] args) throws Exception {

        /**
         * 这种写法没有实战意义,因为实际使用是用new关键字创建对象
         *
         * 全盘委托规则:new关键字使用的ClassLoader使用的是当前调用new对象的方法的class类
         * 使用的ClassLoader。
         *
         * 解决：怎么改变new 关键字使用的ClassLoader？
         * 改变调用new的方法所在的类的ClassLoader(所以像SpringBoot要单独写一个run方法入口)
         */
/*        while(true) {
            String rootPath = MyClassLoader.class.getResource("/").getPath().replaceAll("%20", " ");
            System.out.println(rootPath); //结果 /E:/study/projects/JVM/target/classes/
            rootPath = new File(rootPath).getPath(); // E:\study\projects\JVM\target\classes
            System.out.println(rootPath);

            MyClassLoader myClassLoader = new MyClassLoader(rootPath, rootPath + "/com");
            Class<?> aClass = myClassLoader.loadClass("com.xiaoming.classloader.ClassLoaderDemo");
            Method method = aClass.getMethod("hello");
            Object o = aClass.newInstance();
            method.invoke(o);
            //new ClassLoaderDemo().hello();
            TimeUnit.MILLISECONDS.sleep(2000);
        }*/

            Application.run(MyClassLoader.class);
    }
}
