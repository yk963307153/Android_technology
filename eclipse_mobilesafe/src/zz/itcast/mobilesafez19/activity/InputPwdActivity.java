package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InputPwdActivity extends Activity {

	/**
	 * 输入密码要进入的应用的包名
	 */
	private String packageName;

	private ImageView ivIcon;
	private EditText etInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		packageName = getIntent().getStringExtra("packageName");
		if(packageName == null){
			throw new IllegalStateException("你没给我传包名，我就崩了!");
		}
		setContentView(R.layout.activity_input_pwd);
		ivIcon = (ImageView) findViewById(R.id.iv_app_icon);
		etInput = (EditText) findViewById(R.id.et_input_pswd);
		
		try {
			Drawable icon = getPackageManager().getApplicationIcon(packageName);
			ivIcon.setBackgroundDrawable(icon);
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void btnOk(View v){
		
		String pwd = etInput.getText().toString().trim();
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(this, "密码为空", 0).show();
			return ;
		}
		
		if("123".equals(pwd)){ // 密码正确
			
			// 发送广播，通知看门狗，用户输入密码正确，可以进入应用
			Intent intent = new Intent("zz.itcast.mobilesafez19.passowrd.ringht.dog.stop");
			intent.putExtra("allowEnterPackageName", packageName);
			sendBroadcast(intent); // 发出广播
			
			//  关闭当前页面
			finish();
		}else{
			// 密码不正确
			Toast.makeText(this, "密码不正确", 0).show();
			return ;
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是后退按键，启动桌面
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			<action android:name="android.intent.action.MAIN" />
//            <category android:name="android.intent.category.HOME" />
//            <category android:name="android.intent.category.DEFAULT" />
//            <category android:name="android.intent.category.MONKEY"/>
			
			Intent intent = new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addCategory("android.intent.category.MONKEY");
			startActivity(intent);
			
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
