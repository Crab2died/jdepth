package com.github.usefultool;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轻量级java bean属性操作工具
 *
 * 极大地提高运行效率
 */
public class LightReflex {

    public static final int SETTER_ALONE = 0B01;

    public static final int GETTER_ALONE = 0B10;

    public static final int SETTER_AND_GETTER = SETTER_ALONE | GETTER_ALONE;

    /**
     * 该方法为批量获取java bean的getter与setter方法
     *
     * @param clazz      操作对象的Class
     * @param flag       获取的方法类型,3种类型选择,
     *                   1:{@link LightReflex#SETTER_ALONE} 只获取setter方法
     *                   2:{@link LightReflex#GETTER_ALONE} 只获取getter方法
     *                   3:{@link LightReflex#SETTER_AND_GETTER} 同时获取getter与setter方法
     * @param fieldsName 指定需要获取getter或setter方法的字段名,可多个
     * @return 返回一个形如{@code Map<String, Method[]>}的集合
     * 集合的map的key为field的字段名, value为{@link Method}的对象数组,
     * 该数组在flag参数为1,2时是一个长度为1的数组,内容为该字段的getter或setter方法,
     * 当参数flag为3时,是一个长度为2的数组，数组的第一个元素放的是setter方法(没有的话为null),
     * 数组的第二个元素放的是getter方法(没有的话为null)
     *
     * @author : Crab2Died
     * 2017/11/29  15:38:15
     */
    public static Map<String, Method[]> getFieldAccessMethods(Class clazz, int flag, String... fieldsName)
            throws IntrospectionException {

        Map<String, Method[]> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        outer:
        for (PropertyDescriptor prop : props) {
            for (String fieldName : fieldsName) {
                // 排除非法项
                if (null == fieldName || "".equals(fieldName))
                    continue;
                if (fieldName.equals(prop.getName())) {
                    switch (flag) {
                        case SETTER_ALONE:
                            Method[] setMethod = new Method[]{
                                    prop.getWriteMethod()
                            };
                            map.put(fieldName, setMethod);
                            continue outer;
                        case GETTER_ALONE:
                            Method[] getMethod = new Method[]{
                                    prop.getWriteMethod(),
                                    prop.getReadMethod()
                            };
                            map.put(fieldName, getMethod);
                            continue outer;
                        case SETTER_AND_GETTER:
                            Method[] methods = new Method[]{
                                    prop.getWriteMethod(),
                                    prop.getReadMethod()
                            };
                            map.put(fieldName, methods);
                            continue outer;
                        default:
                            throw new IllegalArgumentException("The flag = " + flag + " is illegal argument");
                    }
                }
            }
        }
        return map;
    }

    /**
     * 该方法为获取java bean的getter与setter方法
     *
     * @param clazz     操作对象的Class
     * @param flag      获取的方法类型,3种类型选择,
     *                  1:{@link LightReflex#SETTER_ALONE} 只获取setter方法
     *                  2:{@link LightReflex#GETTER_ALONE} 只获取getter方法
     *                  3:{@link LightReflex#SETTER_AND_GETTER} 同时获取getter与setter方法
     * @param fieldName 指定需要获取getter或setter方法的字段名
     * @return 返回一个形如方法的集合, 集合的第一个元素放的是setter方法(没有的话为null),
     * 集合的第二个元素放的是getter方法(没有的话为null)
     *
     * @author : Crab2Died
     * 2017/11/29  15:38:15
     */
    public static List<Method> getFieldAccessMethods(Class clazz, int flag, String fieldName)
            throws IntrospectionException {

        if (null == fieldName)
            throw new IllegalArgumentException("The fileName must be not null");
        List<Method> methods = new ArrayList<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            if (fieldName.equals(prop.getName())) {
                switch (flag) {
                    case SETTER_ALONE:
                        methods.add(prop.getWriteMethod());
                        break;
                    case GETTER_ALONE:
                        methods.add(prop.getReadMethod());
                        break;
                    case SETTER_AND_GETTER:
                        methods.add(prop.getWriteMethod());
                        methods.add(prop.getReadMethod());
                        break;
                    default:
                        throw new IllegalArgumentException("The flag = " + flag + " is illegal argument");
                }
            }
        }
        return methods;
    }

    /**
     * 获取java bean某个字段的getter或setter方法
     *
     * @param clazz 操作对象的Class
     * @param flag  获取的方法类型,3种类型选择,
     *              1:{@link LightReflex#SETTER_ALONE} 只获取setter方法
     *              2:{@link LightReflex#GETTER_ALONE} 只获取getter方法
     * @return 返回该字段的getter或setter方法，没有则返回null
     *
     * @author : Crab2Died
     * 2017/11/29  16:38:04
     */
    public static Method getterOrSetterMethod(Class clazz, int flag, String fieldName)
            throws IntrospectionException {

        if (null == fieldName)
            throw new IllegalArgumentException("The fileName must be not null");
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            if (fieldName.equals(prop.getName())) {
                if (flag == SETTER_ALONE) {
                    return prop.getWriteMethod();
                } else if (flag == GETTER_ALONE) {
                    return prop.getReadMethod();
                } else {
                    throw new IllegalArgumentException("The flag = " + flag + " is illegal argument");
                }
            }
        }
        return null;
    }

    public static Method getterMethod(Class clazz, String fieldName) throws IntrospectionException {

        return getterOrSetterMethod(clazz, 2, fieldName);
    }

    public static Method setterMethod(Class clazz, String fieldName) throws IntrospectionException {

        return getterOrSetterMethod(clazz, 1, fieldName);
    }

}
