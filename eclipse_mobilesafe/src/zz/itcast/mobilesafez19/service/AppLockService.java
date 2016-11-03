package zz.itcast.mobilesafez19.service;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.mobilesafez19.activity.InputPwdActivity;
import zz.itcast.mobilesafez19.db.AppLockDao;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

public class AppLockService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 程序锁的原理：
	 * 不断监视屏幕，如果屏幕中运行需要被锁定的应用时，
	 * 弹出输入密码页面。密码正确才进入打开的应用。
	 * 
	 */
	
	
	private AppLockDao lockDao;
	
	/**
	 * 程序锁数据库发生变化 时的内容观察者
	 */
	private ContentObserver appLockObserver = new ContentObserver(new Handler()) {
		
		public void onChange(boolean selfChange) {
			System.out.println("onChange========");
			allAppLock = lockDao.getAllAppLock();
			
		};
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
//		System.out.println("AppLockService.start");
		ctx = this;
		
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		lockDao = AppLockDao.getInstance(this);
		
		// 所有的需要锁定的应用的包名
		allAppLock = lockDao.getAllAppLock();
		
		// 定义指定程序锁数据库的uri 
		Uri uri = Uri.parse("content://zz.itcast.mobilesafez19.applock.db");
		// 注册 内容观察者
		getContentResolver().registerContentObserver(uri , true, appLockObserver);
		
		
		
		
		allAllowPckageName = new ArrayList<String>();
		
		// 注册 接收输入密码页面，发送的广播 
		inputPwdReceiver = new InputPwdReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("zz.itcast.mobilesafez19.passowrd.ringht.dog.stop");
		registerReceiver(inputPwdReceiver, filter);
		
		// 注册 锁屏广播 ，
		lockScreenReceiver = new LockScreenReceiver();
		IntentFilter filter2 = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(lockScreenReceiver, filter2);
		
		isRunning = true;
		startDog();
		
	}
	
	private ActivityManager am;
	private Context ctx;
	
	/**
	 * 判断看门狗是否运行
	 */
	private boolean isRunning = false;
	
	// 启动看门狗
	private void startDog() {
		// 开启循环，不断获得用户屏幕运行的应用
		new Thread(){
			public void run() {
				while(isRunning){
					
					// 获得正在运行的任务栈信息，最近运行在前面，之前运行的在后面，我们要的是当前正在运行的第一个就够了
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1); 
					RunningTaskInfo taskInfo = runningTasks.get(0);// 取出第一个运行的任务栈信息
					ComponentName act = taskInfo.topActivity; // 栈顶的activity就是屏幕当前显示的页面
					String packageName = act.getPackageName();
					
//					System.out.println("packageName::"+packageName);
					// 判断应用是否需要被锁定
//					if (lockDao.isLockedApp(packageName)) {
					if (allAppLock.contains(packageName)) {

						// 判断进入当前应用的密码是否输入正确
						if (allAllowPckageName.contains(packageName)) {
							// 当前应用密码输入正确，可以进入
						} else {
							// 显示输入密码的页面
							Intent intent = new Intent(ctx, InputPwdActivity.class);
							// 从activity以外的地方开启 activity时，必须添加该flag 该
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("packageName", packageName);

							startActivity(intent);
						}
					}
					
					SystemClock.sleep(500); // 休眠500秒毫
				}
			};
		}.start();
	}
	
	private InputPwdReceiver inputPwdReceiver;
	
	/**
	 * 密码正确，允许进入的应用的包名
	 */
//	private String allowEnterPackageName;
	
	/**
	 * 所有的，允许进入的应用的包名
	 */
	private List<String> allAllowPckageName;
	
	
	/**
	 * 接收 在输入密码页面，密码正确时，发送的广播
	 * @author Administrator
	 *
	 */
	private class InputPwdReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String allowEnterPackageName = intent.getStringExtra("allowEnterPackageName");
			
			allAllowPckageName.add(allowEnterPackageName);
			
		}
	}
	
	private LockScreenReceiver lockScreenReceiver;
	
	/**
	 * 所有的需要锁定的应用的包名
	 */
	private List<String> allAppLock;
	
	/**
	 * 接收锁屏广播
	 * @author Administrator
	 *
	 */
	private class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// 清空允许 进入的应用的集合
			allAllowPckageName.clear();
			
		}
		
	}
	
	

	@Override
	public void onDestroy() {
		super.onDestroy();
//		System.out.println("AppLockService.onDestroy");
		isRunning = false;
		
		unregisterReceiver(inputPwdReceiver); // 取消注册 广播 
		
		unregisterReceiver(lockScreenReceiver); // 取消注册 广播 
		
		
	}
	
	
	
}
