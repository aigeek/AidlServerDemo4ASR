package com.tcl.asr.serviceHook;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tcl.asr.ui.MainActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AMSHookHelper {

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    // TODO: 2018/10/25 hook失败。。权限问题
    //需要hook ActivityManagerNative中的IActivityManager对象，用于修改其启动的service
    public static void hookAMN() throws ClassNotFoundException {
        // TODO: 2018/10/24 这里会报错：declaration of 'android.app.ActivityManagerNative' appears in /system/framework/framework.jar

        //获取AMN的getDefault单例对象getDefault（final 静态）
        Object gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManagerNative","getDefault");
        //gDefault 是android.util.Singleton<T>的对象，需要获取单例中的mInstance字段，mInstance是AMN的代理IActivityManager
        Object mInstance = RefInvoke.getFieldObject("android.util.Singleton",gDefault,"mInstance");
        //创建此对象(IActivityManager)的自定义代理对象 MockClass1，然后替换字段，使用代理对象实现功能
        Class<?> classB2Interface = Class.forName("android.app.IActivityManager");
        //动态代理的调用
        Object proxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{classB2Interface},
                new MockClass1(mInstance));

        //把gDefault的mInstance字段（是AMN的代理，IActivityManager对象）修改为proxy
        Class class1 = gDefault.getClass();
        RefInvoke.setFieldObject("android.util.Singleton",gDefault,"mInstance",proxy);
    }


    //需要hook  ActivityThread中的handler用于改变发送的消息
    public static void hookActivityThread() throws Exception{
        //获取到当前的ActivityThread对象
        Object currentActivityThread = RefInvoke.getStaticFieldObject(
                "android.app.ActivityThread",
                "sCurrentActivityThread");
        //由于只有一个ActivityThread进程，所以获取该对象的mH用于拦截
        Handler mH = (Handler) RefInvoke.getFieldObject(currentActivityThread,"mH");
        //将mH的mCallback字段替换为自定义的 new MockCallback2(mH)字段
        RefInvoke.setFieldObject(Handler.class,mH,"mCallback",new MockClass2(mH));

    }


    //拦截需要更改的方法；如果是启动service，就是startService和stopService，如果是绑定service，就是bindService和unBindService
    static class MockClass1 implements InvocationHandler{

        private static final String TAG = "MockClass1";

        Object mBase;

        //替身service使用两种，如果是旧版本就使用walleve中的IFLY service，如果是新版本，就使用asrManager中的IFLY service
        //具体使用哪种，，在应用启动时进行判断

        public MockClass1(Object base){
            mBase = base;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i(TAG,"invoke:"+method.getName());
            if ("bindService".equals(method.getName())){
                //找到参数中的第一个Intent对象
                int index = 0;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent){
                        index = i;
                        break;
                    }
                }


                // TODO: 2018/10/24 这需要匹配原来的service

//                Intent rawIntent = (Intent) args[index];
//                //可以获取原service
//                String rawServiceName = rawIntent.getComponent().getClassName();
                String stubPkg="";
                String stubServiceName="";
                if (MainActivity.useWalleveService){
                    stubPkg = MainActivity.useWalleveServicePkg;
                    stubServiceName = MainActivity.useWalleveServiceActionValue;
                }else {
                    stubPkg = MainActivity.useASRManagerServicePkg;
                    stubServiceName = MainActivity.useASRManagerServiceActionValue;
                }

                //替换intent
                ComponentName componentName = new ComponentName(stubPkg,stubServiceName);
                Intent newIntent = new Intent();
                newIntent.setComponent(componentName);
                args[index] = newIntent;
                Log.i(TAG,"hook service success");
                return method.invoke(mBase,args);
            }
            Log.i(TAG,"hook service failed");
            return null;
        }
    }



    private static class MockClass2 implements Handler.Callback{

        public static final String TAG = "MockClass2";
        Handler mBase;
        public MockClass2(Handler mH) {
            mBase = mH;
        }


        @Override
        public boolean handleMessage(Message message) {
            Log.i(TAG,"MockClass2 handleMessage,msg.what="+String.valueOf(message.what));
            switch (message.what){
                //ActivityThread内CREATE_SERVICE是114
                case 114:
                    //bindService使用这个方法也可以，具体情况待分析
                    handleCreateService(message);
                    break;
                    default:
                        break;
            }
            return false;
        }

        private void handleCreateService(Message message) {
            Object obj = message.obj;
            ServiceInfo serviceInfo = (ServiceInfo) RefInvoke.getFieldObject(obj,"info");
            Log.i(TAG,"old service name="+serviceInfo.name);
            String realServiceName = null;
            if (MainActivity.useWalleveService){
                realServiceName = MainActivity.useWalleveServiceName;
            }else {
                realServiceName = MainActivity.useASRManagerServiceName;
            }
            Log.i(TAG,"new service name="+realServiceName);
            serviceInfo.name = realServiceName;
        }
    }




}
