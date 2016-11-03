package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Setting3Activity extends SettingBaseActivity {

	private static final int REQUEST_FOR_CONTACTS = 1;
	public static final int RESULT_FOR_CONTACTS = 100;
	private EditText etSafeNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting3);

		etSafeNumber = (EditText) findViewById(R.id.et_safe_number);

		// 状态回显
		String safeNumber = sp.getString(MyResource.KEY_SAFE_NUMBER, "");
		// 直接显示 不需要判断
		etSafeNumber.setText(safeNumber);

	}

	public void selectSafeNumber(View view) {

		Intent intent = new Intent(this, ContactsActivity.class);
		startActivityForResult(intent, REQUEST_FOR_CONTACTS);

	}

	@Override
	public void doNext() {

		// 先判断用户是否已经输入了安全号码
		String safeNumber = etSafeNumber.getText().toString().trim();

		if (TextUtils.isEmpty(safeNumber)) {
			AppUtils.showToast("请先输入安全号码", Setting3Activity.this);
		} else {
			// 将安全号码保存下来
			
			setStringValue(MyResource.KEY_SAFE_NUMBER, safeNumber);
			jumpToActivityAndFinish(Setting4Activity.class);
		}

	}

	@Override
	public void doPre() {
		// TODO Auto-generated method stub
		jumpToActivityAndFinish(Setting2Activity.class);
	}

	// public void next(View view) {
	//
	// jumpToActivity(Setting4Activity.class);
	//
	// }
	//
	// public void pre(View view) {
	// jumpToActivity(Setting2Activity.class);
	//
	//
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_FOR_CONTACTS) {
			String number = data.getStringExtra("number");
			etSafeNumber.setText(number);
		}
	}

}
