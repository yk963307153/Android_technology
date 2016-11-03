package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class Setting1Activity extends SettingBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting1);
	}

	@Override
	public void doNext() {
		// TODO Auto-generated method stub
		jumpToActivityAndFinish(Setting2Activity.class);
	}

	@Override
	public void doPre() {
		
		
	}

//	public void next(View view) {
//		jumpToActivity(Setting2Activity.class);
//
//	}

}
