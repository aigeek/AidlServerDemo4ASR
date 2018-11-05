package com.tcl.asr.serviceHook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射封装类
 */
public class RefInvoke {
    /**
     * 多个参数的构造方法
     * @param clazz
     * @param pareTyples
     * @param pareValues
     * @return
     */
    private static Object createObject(Class clazz, Class[] pareTyples, Object[] pareValues) {
        try {
            Constructor constructor = clazz.getConstructor(pareTyples);
            return constructor.newInstance(pareValues);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    //多个参数的构造方法
    public static Object createObject(String className,Class[] pareTyples,Object[] pareValues){
        try {
            Class r = Class.forName(className);
            return createObject(r,pareTyples,pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //无参数构造方法
    public static Object createObject(String className){
        Class[] pareTyples = new Class[]{};
        Object[] pareValues = new Object[]{};
        try {
            Class r = Class.forName(className);
            return createObject(r,pareTyples,pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    //无参构造方法
    public static Object createObject(Class clazz){
        Class[] pareTyple = new Class[]{};
        Object[] pareVaules = new Object[]{};
        return createObject(clazz,pareTyple,pareVaules);

    }

    //一个参数的构造方法
    public static Object createObject(String className,Class pareTyple,Object pareValue){
        Class[] pareTyples = new Class[]{pareTyple};
        Object[] pareValues = new Object[]{pareValue};
        try {
            Class r = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //一个参数的构造方法
    public static Object createObject(Class clazz,Class pareTyple,Object pareValue){
        Class[] pareTyples = new Class[]{pareTyple};
        Object[] pareValues = new Object[]{pareValue};
        return createObject(clazz,pareTyples,pareValues);
    }





    //多个参数的普通实例方法
    public static Object invokeInstanceMethod(Object obj,String methodName,Class[] pareTyples,Object[] pareValues){
        if (obj == null){
            return null;
        }
        try {
            //调用private方法//指定类中获取指定参数的方法
            Method method = obj.getClass().getDeclaredMethod(methodName,pareTyples);
            method.setAccessible(true);
            return method.invoke(obj,pareValues);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

   //一个参数的普通实例方法
    public static Object invokeInstanceMethod(Object obj,String methodName,Class pareTyple,Object pareValue){
        Class[] pareTyples = new Class[]{pareTyple};
        Object[] pareValues = new Object[]{pareValue};
        return invokeInstanceMethod(obj,methodName,pareTyples,pareValues);
    }


    //无参数的普通实例方法
    public static Object invokeInstanceMethod(Object obj,String methodName){
        Class[] pareTyples = new Class[]{};
        Object[] pareValues = new Object[]{};
        return invokeInstanceMethod(obj,methodName,pareTyples,pareValues);
    }


    /**
     * 多参数的静态方法
     * @param clazz
     * @param methodName
     * @param pareTyples
     * @param pareValues
     * @return
     */
    public static Object invokeStaticMethod(Class clazz,String methodName,Class[] pareTyples,Object[] pareValues){
        try {
            Method method = clazz.getDeclaredMethod(methodName,pareTyples);
            method.setAccessible(true);
            return method.invoke(null,pareValues);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }



    //多个参数的静态方法
    public static Object invokeStaticMethod(String className,String method_name,Class[] pareTyples,Object[] pareVaules){
        try {
            Class obj = Class.forName(className);
            return invokeStaticMethod(obj,method_name,pareTyples,pareVaules);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //一个参数的静态方法
    public static Object invokeStaticMethod(Class clazz,String methodName,Class pareType,Object pareValue){
        Class[] pareTyples = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return invokeStaticMethod(clazz,methodName,pareTyples,pareValues);
    }

    //一个参数的静态方法
    public static Object invokeStaticMethod(String className,String methodName,Class pareType,Object pareValue){
        Class[] pareTyples = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};
        return invokeStaticMethod(className,methodName,pareTyples,pareValues);
    }


    //无参数的静态方法
    public static Object invokeStaticMethod(Class clazz,String methodName){
        Class[] pareTyples = new Class[]{};
        Object[] pareValues = new Object[]{};
        return invokeStaticMethod(clazz,methodName,pareTyples,pareValues);

    }

    //无参数的静态方法
    public static Object invokeStaticMethod(String className,String methodName){
        Class[] pareTyples = new Class[]{};
        Object[] pareValues = new Object[]{};
        return invokeStaticMethod(className,methodName,pareTyples,pareValues);
    }

    /**
     * 反射调用获取某个成员变量
     * @param clazz
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldObject(Class clazz,Object obj,String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldObject(String className,Object obj,String fieldName){
        try {
            Class obj_class = Class.forName(className);
            return getFieldObject(obj_class,obj,fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldObject(Object obj,String fieldName){
        return getFieldObject(obj.getClass(),obj,fieldName);
    }

    /**
     * 反射设置类的成员变量的值
     * @param clazz
     * @param object 类的实例对象
     * @param fieldName 类的某个变量名
     * @param fieldValue 类的对应变量值
     */
    public static void setFieldObject(Class clazz,Object object,String fieldName,Object fieldValue){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object,fieldValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(String className,Object object,String fieldName,Object fieldValue){
        try {
            Class obj_class = Class.forName(className);
            setFieldObject(obj_class,object,fieldName,fieldValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(Object obj,String fieldName,Object fieldValue){
        setFieldObject(obj.getClass(),obj,fieldName,fieldValue);
    }


    /**
     * 获取静态成员变量值
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Object getStaticFieldObject(Class clazz,String fieldName){
        return getFieldObject(clazz,null,fieldName);
    }

    public static Object getStaticFieldObject(String className, String fieldName){
        return getFieldObject(className,null,fieldName);
    }

    /**
     * 设置静态成员变量值
     * @param className
     * @param fieldName
     * @param fieldValue
     */
    public static void setStaticFieldObject(String className,String fieldName,Object fieldValue){
        setFieldObject(className,null,fieldName,fieldValue);
    }


    public static void setStaticFieldObject(Class clazz,String fieldName,Object fieldValue){
        setFieldObject(clazz,null,fieldName,fieldValue);
    }



















}
