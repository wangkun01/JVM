package com.xiaoming.classloader;

/**
 * @author wangkun
 * @date 2019-07-27 22:08
 */
public class TestPath {
    public static void main(String[] args) {
        String path = "E:\\study\\projects\\JVM\\src\\main\\java\\com\\xiaoming\\ClassLoaderDemo.class";
        String fileName = path.replace("E:\\study\\projects\\JVM\\src\\main\\java\\"," ");
        System.out.println(fileName);
        fileName = fileName.replaceAll("\\\\",".");
        System.out.println(fileName);
        fileName = fileName.substring(0,fileName.lastIndexOf("."));
        System.out.println(fileName);
        fileName = fileName.substring(1);
        System.out.println(fileName);
    }
}
