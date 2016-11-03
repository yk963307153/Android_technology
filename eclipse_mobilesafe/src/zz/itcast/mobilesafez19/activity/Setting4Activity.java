package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Setting4Activity extends SettingBaseActivity {

	private CheckBox cbOpenProtect;
	private TextView tvProtectState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting4);

		cbOpenProtect = (CheckBox) findViewById(R.id.cb_open_protect);
		tvProtectState = (TextView) findViewById(R.id.tv_protect_state);

		// 状态回显
		boolean isOpen = sp.getBoolean(MyResource.KEY_OPEN_PROTECT, false);
		cbOpenProtect.setChecked(isOpen);
		if (isOpen) {
			tvProtectState.setText("防盗保护开启");
			tvProtectState.setTextColor(Color.GREEN);
		} else {
			tvProtectState.setText("防盗保护关闭");
			tvProtectState.setTextColor(Color.RED);
		}

		cbOpenProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			// 当勾选状态改变
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setBooleanValue(MyResource.KEY_OPEN_PROTECT, isChecked);
				if (isChecked) {
					//
					tvProtectState.setText("防盗保护开启");
					tvProtectState.setTextColor(Color.GREEN);
				} else {
					tvProtectState.setText("防盗保护关闭");
					tvProtectState.setTextColor(Color.RED);
				}
			}
		});

	}

	@Override
	public void doNext() {
		// TODO Auto-generated method stub

		// 当点击完成时 标记成已经完成设置向导
		setBooleanValue(MyResource.KEY_IS_FINISH_SETTING, true);

		jumpToActivityAndFinish(PhoneSafeActivity.class);
	}

	@Override
	public void doPre() {
		// TODO Auto-generated method stub
		jumpToActivityAndFinish(Setting3Activity.class);
	}

	// public void next(View view) {
	//
	// jumpToActivity(PhoneSafeActivity.class);
	//
	// }
	//
	// public void pre(View view) {
	//
	// jumpToActivity(Setting3Activity.class);
	// }

}
