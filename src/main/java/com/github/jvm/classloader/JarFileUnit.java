package com.github.jvm.classloader;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 *  JarFile test
 */
public class JarFileUnit {

    static void jarFileFoo(String jarPath) throws IOException {

        JarFile jarFile = new JarFile(jarPath);
        //URL url = ClassLoader.getSystemResource("JarFileUnit.class");
        Enumeration<JarEntry> entries = jarFile.entries();
        for(; entries.hasMoreElements();){
            System.out.println(entries.nextElement().getName());
        }
    }

    public static void main(String[] args){
        try {
            jarFileFoo("D:\\IdeaSpace\\cr-import\\startup\\target\\cr-import.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
