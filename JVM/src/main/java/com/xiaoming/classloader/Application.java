package com.xiaoming.classloader;

import com.xiaoming.classloader.fileutil.FileListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author wangkun
 * @date 2019-07-28 00:37
 */
public class Application {

    public static String rootPath;

    public void start(){
        init();
        new ClassLoaderDemo().hello();
    }

    public  static void start0(MyClassLoader myClassLoader) throws Exception{
        //加载启动类
        Class<?> aClass = myClassLoader.loadClass("com.xiaoming.classloader.Application");
        Method method = aClass.getMethod("start");
        Object o = aClass.newInstance();
        method.invoke(o);
    }

    public void init(){
        System.out.println("初始化项目");
    }

    public static void run(Class clazza) throws Exception{
        String rootPath = clazza.getResource("/").getPath().replaceAll("%20", " ");
        rootPath = new File(rootPath).getPath(); // E:\study\projects\JVM\target\classes
        Application.rootPath = rootPath;
        startFileMino(rootPath);
        MyClassLoader myClassLoader = new MyClassLoader(rootPath,rootPath+"/com");
        start0(myClassLoader);
    }


    //文件变化监听方法配置
    public  static void startFileMino(String rootPath) throws Exception {
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(rootPath);
        fileAlterationObserver.addListener(new FileListener());

        FileAlterationMonitor fileAlterationMonitor =  new FileAlterationMonitor(500);
        fileAlterationMonitor.addObserver(fileAlterationObserver);
        fileAlterationMonitor.start();
    }
}
