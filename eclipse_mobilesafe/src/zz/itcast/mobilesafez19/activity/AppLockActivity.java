package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.fragment.LockedFragment;
import zz.itcast.mobilesafez19.fragment.UnLockFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AppLockActivity extends FragmentActivity implements OnClickListener {

	private TextView tvUnlock;
	private TextView tvlocked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_app_lock);
		tvUnlock = (TextView) findViewById(R.id.tv_unlock);
		tvlocked = (TextView) findViewById(R.id.tv_locked);
		
		tvUnlock.setOnClickListener(this);
		tvlocked.setOnClickListener(this);
		
		unLockFragment = new UnLockFragment();
		lockedFragment = new LockedFragment();
		
		// 添加默认的fragment
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.ll_content, unLockFragment)
		.commit();
		
	}

	
	/**
	 * fragment 使用步骤：
	 * 1- 在布局为fragment 设定好容器
	 * 2- 让activity 改为继承 FragmentActivity
	 * 3- 切换页面
	 * 			getSupportFragmentManager() // 获得管理器
				.beginTransaction() // 开启事务
				.replace(R.id.ll_content, unLockFragment) // 替换fragment 
				.commit(); // 提交
	 * 
	 * * 注意事项： 记得把 v4 包也一块打包至 APK 安装包中
	 */
	

	private UnLockFragment unLockFragment;
	
	private LockedFragment lockedFragment;
	
	

	@Override
	public void onClick(View v) {
		
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		
		switch (v.getId()) {
		case R.id.tv_unlock:
			
			tvUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tvlocked.setBackgroundResource(R.drawable.tab_right_default);
			
			// 使用 unLockFragment创建出来的view 替换 指定ID的布局中所有的子view
			trans.replace(R.id.ll_content, unLockFragment);
			
			break;
		case R.id.tv_locked:
			
			tvUnlock.setBackgroundResource(R.drawable.tab_left_default);
			tvlocked.setBackgroundResource(R.drawable.tab_right_pressed);
			
			trans.replace(R.id.ll_content, lockedFragment);
			
			break;
		}
		
		trans.commit(); // 提交
		
	}
}
