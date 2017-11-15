package com.github.jvm.jdk5;

// 静态导入 关键字 static
import static com.github.jvm.jdk5.StaticImport.*;

public class StaticImportFoo
{
    public static void takeStaticFoo(){
        /**
         *  调用静态方法 {@link StaticImport#staticFoo()}
         *  等同 com.github.jvm.jdk5.StaticImport.staticFoo()
         */
        staticFoo();
    }
}
