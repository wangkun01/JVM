package com.xiaoming.classloader.fileutil;

import com.xiaoming.classloader.Application;
import com.xiaoming.classloader.MyClassLoader;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;

/**
 * @author wangkun
 * @date 2019-07-28 01:26
 */
public class FileListener extends FileAlterationListenerAdaptor {
    @Override
    public void onFileChange(File file) {
        System.out.println("监控到" + file.getName() +"发生变化！");
        //判断是否是.class文件发生修改
        if(file.getName().indexOf(".class") != -1){
            try{
                MyClassLoader myClassLoader = new MyClassLoader(Application.rootPath, Application.rootPath + "/com");
                Application.start0(myClassLoader);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
