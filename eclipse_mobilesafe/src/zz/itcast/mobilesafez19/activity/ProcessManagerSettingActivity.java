package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.service.LockScreenSercivce;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessManagerSettingActivity extends BaseActivity {

	private CheckBox cbShowSystem, cbLockClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_process_manager_setting);

		cbShowSystem = (CheckBox) findViewById(R.id.cb_is_show_system);
		cbLockClear = (CheckBox) findViewById(R.id.cb_lock_clear);

		// 回显状态
		boolean isShow = sp.getBoolean(MyResource.KEY_SHOW_SYSTEM, true);
		cbShowSystem.setChecked(isShow);

		// 设置状态改变的监听
		cbShowSystem.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// 保存sp的值
				setBooleanValue(MyResource.KEY_SHOW_SYSTEM, isChecked);
			}
		});

		cbLockClear.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Intent service = new Intent(act, LockScreenSercivce.class);
				if (isChecked) {
					// 开始服务
					startService(service);
				} else {
					// 停止服务
					stopService(service);
				}

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		boolean isRunning = AppUtils.isServiceRunning(LockScreenSercivce.class,
				act);
		cbLockClear.setChecked(isRunning);

	}

}
