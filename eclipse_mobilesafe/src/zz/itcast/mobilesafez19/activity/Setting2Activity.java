package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Setting2Activity extends SettingBaseActivity {

	private LinearLayout llBindSim;
	private ImageView ivBindState;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting2);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		llBindSim = (LinearLayout) findViewById(R.id.ll_bind_sim);
		ivBindState = (ImageView) findViewById(R.id.iv_bind_state);

		// 先做状态回显
		String saveSimId = sp.getString(MyResource.KEY_SIM_ID, "");
		if (TextUtils.isEmpty(saveSimId)) {
			ivBindState.setImageResource(R.drawable.unlock);
		} else {
			ivBindState.setImageResource(R.drawable.lock);
		}

		llBindSim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 先判断是否已经绑定SIM卡
				String saveSimId = sp.getString(MyResource.KEY_SIM_ID, "");
				// 通过SIM卡序列号能够确定SIM卡的唯一性，将SIM卡的序列号保存下来视为已绑定
				if (TextUtils.isEmpty(saveSimId)) {
					// 之前没绑定 现在应该绑定
					ivBindState.setImageResource(R.drawable.lock);
					// 将SIM卡序列号存下来
					String simId = tm.getSimSerialNumber();
					setStringValue(MyResource.KEY_SIM_ID, simId);
				} else {
					// 之前绑定了 现在应该解绑
					ivBindState.setImageResource(R.drawable.unlock);
					// 调用现成的封装的方法进行设置
					setStringValue(MyResource.KEY_SIM_ID, "");

				}

			}
		});

	}

	@Override
	public void doNext() {
		// 在做下一步之前应该先判断是否已经绑定SIm
		String saveSimId = sp.getString(MyResource.KEY_SIM_ID, "");
		if (TextUtils.isEmpty(saveSimId)) {

			AppUtils.showToast("请先绑定SIM卡", Setting2Activity.this);

		} else {

			jumpToActivityAndFinish(Setting3Activity.class);
		}

	}

	@Override
	public void doPre() {
		// TODO Auto-generated method stub
		jumpToActivityAndFinish(Setting1Activity.class);
	}

	// public void next(View view) {
	//
	//
	//
	//
	// }
	//
	// public void pre(View view) {
	//
	// jumpToActivity(Setting1Activity.class);
	//
	//
	//
	// }

}
