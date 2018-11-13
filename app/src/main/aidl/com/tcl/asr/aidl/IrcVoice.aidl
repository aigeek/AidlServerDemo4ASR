package com.tcl.asr.aidl;
import com.tcl.asr.aidl.IPropertiesCallback;
	interface IrcVoice
	{
		  void onShow(int i);
    	  void onRecording(int i);
    	  void onRecognizing();
    	  void onEndRecog();
    	  void onHide();
    	  boolean isShow();
    	  void onShowUserText(String s);
    	  void onShowMiddleText(String s);
    	  void onShowErrorText(String s) ;
    	  boolean setDialects(int type);
    	  int getDialects();
    	  void onResult(String s);
    	  void registerPropertiesCallback(IPropertiesCallback cb);
    	  void unRegisterPropertiesCallback(IPropertiesCallback cb);
          void onShortClick();
          void onUsbDevicePlugOut();
          void onUsbDevicePlugIn(String s);
          void onWakeUp(int i);
          void onShowAppText(String s, int i);
          void isOneShot(int flag);
          void isVadStart();

	}