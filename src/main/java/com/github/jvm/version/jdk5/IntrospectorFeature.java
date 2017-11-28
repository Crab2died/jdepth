package com.github.jvm.version.jdk5;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * jdk1.5 java Bean内省机制
 * commons-beanutils相关使用
 * @author : Crab2Died
 * @since jdk1.5
 * 2017/11/15  14:45:21
 */
public class IntrospectorFeature {

    // 域
    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public static void main(String[] args) {

        IntrospectorFeature introspector = new IntrospectorFeature();
        try {
            //
            PropertyDescriptor descriptor = new PropertyDescriptor("property", IntrospectorFeature.class);
            // setter 函数获取
            Method setMethod = descriptor.getWriteMethod();
            // getter 函数获取
            Method getMethod = descriptor.getReadMethod();
            // 调用 setter
            Object obj = setMethod.invoke(introspector, "setter方法");
            // 调用getter
            Object prop = getMethod.invoke(introspector);

            System.out.println(prop);


            BeanInfo beanInfo = Introspector.getBeanInfo(IntrospectorFeature.class);
            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor _prop : props){
                System.out.println(_prop.getDisplayName());
                if("property".equals(_prop.getName())){
                    System.out.println(_prop.getDisplayName());
                }
            }

        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
