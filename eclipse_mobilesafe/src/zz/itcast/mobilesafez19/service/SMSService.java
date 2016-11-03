package zz.itcast.mobilesafez19.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

@SuppressLint("NewApi")
// 这个服务在这里起到对广播没有拦截短信的二次补救 保证短信能够被删掉
public class SMSService extends Service {

	private String GPS = "#*gps*#";
	private String MUSIC = "#*music*#";
	private String LOCK = "#*lock*#";
	private String WIPE = "#*wipe*#";

	private MySMSOberserver smsOberserver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 注册内容观察者

		// 这个onCreate方法只会在第一次启动服务时调用  那么只会注册一次不会重复注册
		smsOberserver = new MySMSOberserver(new Handler());

		getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsOberserver);

	}

	
	
	private class MySMSOberserver extends ContentObserver {

		public MySMSOberserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		// 当短信数据库发生改变
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			super.onChange(selfChange, uri);

			// 先获取到短信内容 如果是遥控指令的其中一个 将它给删除

			// uri只包含当前来的这一条短信
			System.out.println("uri:" + uri.toString());

			Cursor cursor = getContentResolver().query(uri,
					new String[] { "body" }, null, null, null);
			if (cursor != null && cursor.moveToNext()) {
				String body = cursor.getString(0);
				// 只要和遥控指令的内容一直 将短信删掉
				if (MUSIC.equals(body)) {
					getContentResolver().delete(uri, null, null);
					
				} else if (GPS.equals(body)) {
					getContentResolver().delete(uri, null, null);

				} else if (LOCK.equals(body)) {
					getContentResolver().delete(uri, null, null);

				} else if (WIPE.equals(body)) {
					getContentResolver().delete(uri, null, null);

				}
			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// 解除注册
		getContentResolver().unregisterContentObserver(smsOberserver);
		super.onDestroy();
	}

}
