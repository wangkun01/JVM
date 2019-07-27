package com.xiaoming.classloader;

/**
 * @author wangkun
 * @date 2019-07-25 23:36
 */
public class ClassLoaderDemo {

    public void hello() {
        System.out.println("-------version6.0---------");
        System.out.println("当前使用的类加载器" + getClass().getClassLoader());
    }
}
