package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.service.AppLockService;
import zz.itcast.mobilesafez19.service.BlackNumberService;
import zz.itcast.mobilesafez19.service.ComingNumberService;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import zz.itcast.mobilesafez19.view.SettingItemView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingCenterActivtiy extends BaseActivity {

	private SettingItemView sivAutoUpdate, sivBlackNumber, sivComingNumber,sivAppLock;

	private RelativeLayout rlChanggeComingStyle;

	private TextView tvComingColor;

	private String[] items = new String[] { "半透明", "天空蓝", "铝合金", "土豪金", "苹果绿" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_center);
		// 处理自动更新
		dealWithAutoUpdate();
		// 处理黑名单
		dealWithBalckNumber();
		// 处理来电显示
		dealWithComingNumber();
		// 处理改变来电风格
		dealWithComingStyle();
		
		dealAppLock();
		
	}

	private void dealAppLock() {
		sivAppLock = (SettingItemView) findViewById(R.id.siv_app_lock);
		
		sivAppLock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(act,AppLockService.class);
				
				if(sivAppLock.isChecked()){
					sivAppLock.setChecked(false);
					stopService(intent);
					
				}else{
					sivAppLock.setChecked(true);
					startService(intent);
				}
			}
		});
		
	}

	private void dealWithComingStyle() {
		rlChanggeComingStyle = (RelativeLayout) findViewById(R.id.rl_channge_coming_style);
		tvComingColor = (TextView) findViewById(R.id.tv_coming_color);
		// 这里需要做两次数据回显 
		final int which = sp.getInt(MyResource.KEY_COMING_STYLE, 1);
		tvComingColor.setText(items[which]);
		rlChanggeComingStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 第二次回显 在点击的瞬间再次获取一次
				
				int newWhich = sp.getInt(MyResource.KEY_COMING_STYLE, 1);
				showChangeStyleDialog(newWhich);

			}

		});

	}

	private void showChangeStyleDialog(int which) {
		AlertDialog.Builder builder = new Builder(act);
		builder.setTitle("请选择风格");

		builder.setSingleChoiceItems(items, which,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 固话存储到sp

						setIntValue(MyResource.KEY_COMING_STYLE, which);
						dialog.dismiss();

						tvComingColor.setText(items[which]);

					}
				});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				String msg = null;
				
				if(msg.endsWith("asdfasdf")){
					
				}
						
						
				
			}
		});
		builder.show();

	}

	private void dealWithComingNumber() {
		sivComingNumber = (SettingItemView) findViewById(R.id.siv_coming_number);
		// sivComingNumber.setTitle("来电显示设置");
		// sivComingNumber.setDesc(new String[] { "来电显示设置已经打开", "来电显示设置没有打开" });
		sivComingNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent service = new Intent(act, ComingNumberService.class);
				// 先判断是否钩上了
				if (sivComingNumber.isChecked()) {
					// 取消勾选
					sivComingNumber.setChecked(false);
					stopService(service);
				} else {
					// 勾选
					sivComingNumber.setChecked(true);
					startService(service);
				}

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 根据服务是否正在运行来判断
		boolean isRunning = AppUtils.isServiceRunning(BlackNumberService.class,
				act);
		sivBlackNumber.setChecked(isRunning);

		// 判断来电显示的服务是否开启
		boolean isComingNumberRunning = AppUtils.isServiceRunning(
				ComingNumberService.class, act);
		sivComingNumber.setChecked(isComingNumberRunning);

		
		boolean isAppLockRunning =  AppUtils.isServiceRunning(
				AppLockService.class, act);
		sivAppLock.setChecked(isAppLockRunning);
		
	}

	private void dealWithBalckNumber() {
		sivBlackNumber = (SettingItemView) findViewById(R.id.siv_black_number);
//		sivBlackNumber.setTitle("黑名单设置");
//		sivBlackNumber.setDesc(new String[] { "黑名单设置已经打开", "黑名单设置没有打开" });

		sivBlackNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent service = new Intent(act, BlackNumberService.class);

				// 先判断是否钩上了
				if (sivBlackNumber.isChecked()) {
					// 取消勾选
					sivBlackNumber.setChecked(false);
					// 停止服务
					stopService(service);

				} else {
					// 勾选
					sivBlackNumber.setChecked(true);
					// 开启服务
					startService(service);
				}

			}
		});

	}

	private void dealWithAutoUpdate() {
		sivAutoUpdate = (SettingItemView) findViewById(R.id.siv_auto_update);

//		sivAutoUpdate.setTitle("自动更新设置");
//		sivAutoUpdate.setDesc(new String[] { "自动更新设置已经打开", "自动更新设置没有打开" });
		// 这里站在开发者角度 我们希望用户自动跟新的 但是这里咱们使用方便false
		boolean isAuto = sp.getBoolean(MyResource.KEY_AUTO_UPDATE, false);
		sivAutoUpdate.setChecked(isAuto);
		sivAutoUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 先判断是否钩上了
				if (sivAutoUpdate.isChecked()) {
					// 取消勾选

					sivAutoUpdate.setChecked(false);
					setBooleanValue(MyResource.KEY_AUTO_UPDATE, false);

				} else {
					// 勾选
					sivAutoUpdate.setChecked(true);
					setBooleanValue(MyResource.KEY_AUTO_UPDATE, true);
				}

			}
		});
	}

}
