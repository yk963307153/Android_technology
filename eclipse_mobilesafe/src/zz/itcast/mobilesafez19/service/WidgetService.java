package zz.itcast.mobilesafez19.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.ProcessInfo;
import zz.itcast.mobilesafez19.receiver.MyWidgetReceiver;
import zz.itcast.mobilesafez19.utils.AppUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

@SuppressLint("NewApi")
public class WidgetService extends Service {

	private Timer timer;
	private TimerTask tt;
	private AppWidgetManager awm;
	private ComponentName pn;
	private ActivityManager am;
	private ClearReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		awm = AppWidgetManager.getInstance(WidgetService.this);
		pn = new ComponentName(this, MyWidgetReceiver.class);

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		timer = new Timer();

		tt = new TimerTask() {

			@Override
			public void run() {
				System.out.println("每隔5秒更新一次");
				// 更新这个小控件里的文字内容

				// 四大组件的封装类 标明是哪个应用的

				// 告诉系统你要更新哪个应用的Widget views代表更新成啥样

				// RemoteViews 立即为对View的一个封装 RemoteView是用来跨应用间
				RemoteViews rv = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				// 设置进程个数
				rv.setTextViewText(R.id.process_count, "进程个数："
						+ AppUtils.getAllProcess(WidgetService.this).size()
						+ "个");
				// 设置内存
				MemoryInfo info = new MemoryInfo();
				am.getMemoryInfo(info);
				long availMem = info.availMem;
				long totalMem = info.totalMem;

				rv.setTextViewText(
						R.id.process_memory,
						"内存："
								+ Formatter.formatFileSize(WidgetService.this,
										availMem)
								+ "/"
								+ Formatter.formatFileSize(WidgetService.this,
										totalMem));

				// 处理按钮

				// 我们不可以直接处理按钮的点击行为 ，但是我们可以按钮点击之后让Lanuncher做一件事情
				// 未来做的一件事
				// 构建PendingIntent 实际上是在构建Intent
				Intent intent = new Intent();
				intent.setAction("zz.itcast.clear");
				PendingIntent pIntent = PendingIntent.getBroadcast(
						WidgetService.this, 100, intent,
						PendingIntent.FLAG_ONE_SHOT);

				rv.setOnClickPendingIntent(R.id.btn_clear, pIntent);

				awm.updateAppWidget(pn, rv);

			}
		};

		// 注册广播zz.itcast.clear

		receiver = new ClearReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("zz.itcast.clear");
		registerReceiver(receiver, filter);

		timer.schedule(tt, 0, 5000);

	}

	private class ClearReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 说明用户点击按钮
			System.out.println("点击清理按钮");
			// 清理
			// 清理后台进程
			ArrayList<ProcessInfo> allProcess = AppUtils.getAllProcess(WidgetService.this);

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

		// 不做了
		timer.cancel();
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}
