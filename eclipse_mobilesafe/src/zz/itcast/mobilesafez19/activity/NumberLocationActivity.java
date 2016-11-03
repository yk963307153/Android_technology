package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NumberLocationActivity extends BaseActivity {

	private EditText etNumber;
	private TextView tvLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_number_location);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvLocation = (TextView) findViewById(R.id.tv_location);

	}

	public void query(View view) {
		// 查询
		String number = etNumber.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			AppUtils.showToast("不能为空", act);

			// 让输入框抖一抖
			ObjectAnimator oa = ObjectAnimator.ofFloat(etNumber,
					"translationX", 0, 10, -10, 10, -10, 10, -10, 0);
			oa.setDuration(500);
			oa.start();

			// 获取振动器
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			// 开始震动  震动的间隔
			vibrator.vibrate(new long[]{1000,500,1000,500}, 2);
			
			return;
		}
		// 如何确定是一个手机号

		if (!number.matches("^1[3578][0-9]{9}$")) {
			AppUtils.showToast("请输入正确的手机号", act);
			return;
		}

		String location = AppUtils.getLocationByNumber(number, act);
		tvLocation.setText("归属地：" + location);

	}

}
