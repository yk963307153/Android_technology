package zz.itcast.mobilesafez19.service;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ComingNumberService extends Service {

	private OutGoingCallReceiver callReceiver;
	private TelephonyManager tm;
	private MyComingListener comingListener;

	private WindowManager wm;
	// 成员变量 只要对象在 那么它就跟着对象走
	private WindowManager.LayoutParams params;
	private View view;

	private SharedPreferences sp;

	private int[] bgs = new int[] { R.drawable.call_locate_white,
			R.drawable.call_locate_blue, R.drawable.call_locate_gray,
			R.drawable.call_locate_orange, R.drawable.call_locate_green };

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 打出 （广播 ip电话） 和 打入（TelephonyManager）
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp = getSharedPreferences(MyResource.SP_FILE_NAME, 0);
		// 注册去电和
		callReceiver = new OutGoingCallReceiver();
		// 调频
		IntentFilter filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");

		initView();
		registerReceiver(callReceiver, filter);

		// 来电的事件

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		comingListener = new MyComingListener();

		tm.listen(comingListener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	private class MyComingListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

				// 将view删掉
				if (view != null) {
					wm.removeView(view);
				}
				break;

			case TelephonyManager.CALL_STATE_RINGING:

				// 显示来电位置
				String location = AppUtils.getLocationByNumber(incomingNumber,
						ComingNumberService.this);

				// 来电也添加
				addLocationView(location);

				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			default:
				break;
			}

		}

	}

	// 去电的广播
	private class OutGoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 监听到电话打出
			System.out.println("z19 监听到电话打出");

			// 获取到外拨的电话 ip电话拨号器
			String number = getResultData();
			// 根据电话查询位置

			String location = AppUtils.getLocationByNumber(number,
					ComingNumberService.this);

			// 弹出吐司

			// Toast.makeText(ComingNumberService.this, location, 0).show();
			// 咱们自己弹吐司

			// 添加进来
			addLocationView(location);

		}

	}

	private void addLocationView(String location) {

		view = View.inflate(ComingNumberService.this,
				R.layout.view_coming_number_location, null);

		TextView tv = (TextView) view.findViewById(R.id.tv_location);

		tv.setText(location);

		// 添加之前先尝试获取背景
		int which = sp.getInt(MyResource.KEY_COMING_STYLE, 1);

		// 设置背景
		view.setBackgroundResource(bgs[which]);

		// 给这个View设置一个触摸事件
		view.setOnTouchListener(new MyTouchListener());

		// 在添加之前去回显位置
		int x = sp.getInt(MyResource.KEY_COMING_X, 0);
		int y = sp.getInt(MyResource.KEY_COMING_Y, 0);

		// 默认是这个值 
//		params.gravity = Gravity.CENTER;
		
		params.x = x;
		params.y = y;
		wm.addView(view, params);
	}

	private class MyTouchListener implements OnTouchListener {

		int startX = 0;
		int startY = 0;

		int endX = 0;
		int endY = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("ACTION_DOWN");
				// 当手第一次按下
				// 更新

				// getX 代表当前这一点距离控件左上方定点的距离
				// getRawX 代表距离屏幕左上方的距离

				startX = (int) event.getRawX();
				startY = (int) event.getRawY();

				break;

			case MotionEvent.ACTION_MOVE:
				endX = (int) event.getRawX();
				endY = (int) event.getRawY();

				int disX = endX - startX;
				int disY = endY - startY;

				params.x += disX;
				params.y += disY;

				// 更新UI
				wm.updateViewLayout(view, params);

				// 让上次的终点成为下一次的起点
				startX = endX;
				startY = endY;

				break;

			case MotionEvent.ACTION_UP:
				System.out.println("ACTION_UP");
				// 在手指抬起之后 记录位置比较合适
				// sp里
				sp.edit().putInt(MyResource.KEY_COMING_X, params.x).commit();
				sp.edit().putInt(MyResource.KEY_COMING_Y, params.y).commit();

				break;

			default:
				break;
			}

			return true;
		}

	}

	private void initView() {
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		// 准备一些参数

		params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;

		// 我们为了去感应触摸事件 做了三件事：
		// 1、| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 将这个不允许触摸的标记去掉
		// 2、 type意思窗体优先级提升成系统的优先级
		// 3、在清单文件中加入权限<uses-permission
		// android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		params.setTitle("ITCAST");

	}

	@Override
	public void onDestroy() {

		// 取消去电广播监听
		unregisterReceiver(callReceiver);
		// 取消来电监听
		tm.listen(comingListener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

}
