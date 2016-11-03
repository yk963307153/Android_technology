package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class BaseActivity extends Activity {

	protected SharedPreferences sp;

	protected Activity act;

	/*
	 * 抽取积累的好处1.sp不用频繁创建2.跳转界面的方法不用频繁new Intent3.代码简介 方便代码抽取
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences(MyResource.SP_FILE_NAME, 0);
		act = this;
	}

	/**
	 * 抽取两个方法 方便对SP进行写操作
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */

	protected boolean setStringValue(String key, String value) {

		return sp.edit().putString(key, value).commit();

	}
	
	protected boolean setIntValue(String key, int value) {

		return sp.edit().putInt(key, value).commit();

	}

	/**
	 * 抽取两个方法 方便对SP进行写操作
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */

	protected boolean setBooleanValue(String key, boolean value) {

		return sp.edit().putBoolean(key, value).commit();

	}

	/**
	 * 抽取积累的方法跳转界面
	 */
	protected void jumpToActivity(Class<? extends Activity> actClass) {
		Intent intent = new Intent(act, actClass);
		startActivity(intent);
	}

	/**
	 * 抽取积累的方法跳转界面 并且将自己关闭
	 */
	protected void jumpToActivityAndFinish(Class<? extends Activity> actClass) {
		jumpToActivity(actClass);	
		finish();
	}

}
