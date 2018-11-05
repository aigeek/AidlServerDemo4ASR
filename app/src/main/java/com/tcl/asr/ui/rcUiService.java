package com.tcl.asr.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.tcl.asr.aidl.IPropertiesCallback;
import com.tcl.asr.aidl.IrcVoice;


public class rcUiService extends Service{
	
    private static final String TAG = "ASR_UI";// 保存的文件夹名



	//须和ASRManager.apk保持一致
	public static final int   LANGUAGE_CH_PUTONGHUA=1;//普通话
	public static final int   LANGUAGE_CH_SICHUANGHUA=2;//四川话
	public static final int   LANGUAGE_CH_HENANHUA=3;//河南话
	public static final int   LANGUAGE_CH_GUNAGDONGHUA=4;//广东话
	public static final int   LANGUAGE_CH_DONGBEIHUA=5;//东北话
	public static final int   LANGUAGE_CH_END=6;//



	public static com.tcl.asr.ui.rcUiService getRcUiService() {
		return rcUiService;
	}

	private static rcUiService rcUiService;
	//客户端设置属性
	private static final int SET_PROPERTY_KEY_DIALECT = 0x01;
	private RemoteCallbackList<IPropertiesCallback> mCallbackList = new RemoteCallbackList<IPropertiesCallback>();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		rcUiService = this;
		return mBinder;
	}

	public IrcVoice.Stub getBinder() {
		return mBinder;
	}

	/**
	 * IRemote defnition is available here
	 */
	public final IrcVoice.Stub mBinder = new IrcVoice.Stub() 
	{

		@Override
		public void onShow(int i) throws RemoteException 
		{
			 Log.d(TAG, "onShow: ");
			 Message msg = MainActivity.mHandler.obtainMessage(MainActivity.RC_KEY_DOWN);
			 MainActivity.mHandler.sendMessage(msg);
		}
		@Override
		public void onRecording(int i) 
		{
	
			 Log.d(TAG, "onRecording: ");
			 //MainActivity.showText("onRecording");
	
		};
		 @Override
		 public void onRecognizing() 
		 {
			 Log.d(TAG, "onRecognizing: ");
			 //MainActivity.showText("onRecognizing");

		 }
		 @Override
		 public void onEndRecog() 
		 {
			 Log.d(TAG, "onEndRecog: ");
		  }
		 @Override
		 public void onHide() 
		 {
			 Log.d(TAG, "onHide: ");
		     
		 }
		 @Override
		 public boolean isShow() 
		 {

		        return true;
		 }
		 @Override
		 public void onShowUserText(String s)
		 {
			 Log.d(TAG, "onShowUserText: " + s);
			  byte[] buf = s.getBytes(); 
			  int len=s.length();
			  //byte[] buffer = new byte[10240];
		        Log.d(TAG, "onShowUserText: " + s);
		     //   onShowUserText(s);
		   	 //Message msg = MainActivity.mHandler.obtainMessage(MainActivity.RC_KEY_UP);
		        //MainActivity.mHandler.obtainMessage(what, arg1, arg2, obj)
		        Message msg= MainActivity.mHandler.obtainMessage();
		        msg.what=MainActivity.RC_KEY_UP;
		        msg.obj=s;
		 //  	MainActivity.mHandler.obtainMessage(MainActivity.RC_KEY_UP, s.getBytes(),-1,buf).sendToTarget();
		        MainActivity.mHandler.sendMessage(msg);
			// MainActivity.showText("onShowUserText");
		 }

        @Override
        public void onShowMiddleText(String s) throws RemoteException {
            Log.d(TAG, "onShowMiddleText: s="+s);
	        Message msg= MainActivity.mHandler.obtainMessage();
	        msg.what=MainActivity.RC_KEY_DOWN;
	        msg.obj=s;
	        MainActivity.mHandler.sendMessage(msg);
        }

		@Override
		  public void onShowErrorText(String s) 
		  {
			  Log.d(TAG, "onShowErrorText: s="+s);
			  
	
		        //MainActivity.showText("onShowErrorText");
		      
		  }
		  @Override
		  public  boolean setDialects(int type)
		  {
			  Log.d(TAG, "setDialects: ");
			  return setProperty(String.valueOf(SET_PROPERTY_KEY_DIALECT),String.valueOf(type));
		  }

		@Override
		public void registerPropertiesCallback(IPropertiesCallback cb) throws RemoteException {
		 	Log.i(TAG,"registerPropertiesCallback");
		 	if (cb != null){
			    mCallbackList.register(cb);
		    }
		}

		@Override
		public void unRegisterPropertiesCallback(IPropertiesCallback cb) throws RemoteException {
			Log.i(TAG,"unRegisterPropertiesCallback");
			if (cb != null){
				mCallbackList.unregister(cb);
			}
		}


	};
	public void onShowUserText(String s)
	{
		//int a,b;
		//Log.d("LUCK","=====ArithmeticService: =====");
		//add(a,b);
	}

	private boolean setProperty(String key, String value){
		if (mCallbackList ==null){
			Log.i(TAG,"can not set property mCallbackList is null");
			return false;
		}
		Log.i(TAG,"setProperty in server"+":key="+key+",value="+value);
		final int length = mCallbackList.beginBroadcast();
		boolean setDialectResult = false;
		for (int i = 0; i < length; i++) {
			try {
				// TODO: 2018/10/31 判断哪一个是设置语言的回调并返回
				if (!TextUtils.isEmpty(key) && key.equals(String.valueOf(SET_PROPERTY_KEY_DIALECT))) {
					setDialectResult = mCallbackList.getBroadcastItem(i).onPropertiesSet(key, value);
					Log.i(TAG, "set DIALECT,value=" + value + " result=" + setDialectResult);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mCallbackList.finishBroadcast();
		return setDialectResult;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCallbackList.kill();
	}
}