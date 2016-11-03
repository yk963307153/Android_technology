package zz.itcast.mobilesafez19.service;

import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.ProcessInfo;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;

@SuppressLint("NewApi")
public class LockScreenSercivce extends Service {

	private MyScreenReceiver screenReceiver;
	private ActivityManager am;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();

		// 注册一个熄屏的广播

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		screenReceiver = new MyScreenReceiver();

		IntentFilter filter = new IntentFilter();
		// 不需要权限
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		registerReceiver(screenReceiver, filter);

		// 1 2 3 4 5 
		// 让我们的进程提升优先级 成为前台进程 就杀不掉了

		Notification noti = new Notification.Builder(this)
				.setContentTitle("z19前台服务进程")
				.setContentText("强大的 杀不死的")
				.setSmallIcon(R.drawable.app)
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.mengbi)).build();

		startForeground(100, noti);

	}

	private class MyScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			System.out.println("z19 监听到屏幕熄灭");
			// 清理后台进程
			ArrayList<ProcessInfo> allProcess = AppUtils
					.getAllProcess(LockScreenSercivce.this);

			for (ProcessInfo info : allProcess) {
				// 杀进程 为了放置自己的界面没有开启 不要杀死自己的进程
				if (!info.packageName.equals(getPackageName())) {
					try {
						am.killBackgroundProcesses(info.packageName);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// 取消注册广播
		unregisterReceiver(screenReceiver);
		super.onDestroy();
	}

}
