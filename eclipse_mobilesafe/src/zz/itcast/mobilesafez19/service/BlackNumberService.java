package zz.itcast.mobilesafez19.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import zz.itcast.mobilesafez19.db.BlackNumberDao;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BlackNumberService extends Service {

	private SMSReceiver receiver;

	private BlackNumberDao dao;

	private MySMSObserver observer;

	private MyPhoneListner phoneListner;

	private TelephonyManager tm;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 服务创建
	@Override
	public void onCreate() {
		super.onCreate();
		// 注册广播 短信到来的广播
		dao = BlackNumberDao.getInstance(this);
		// 代码注册广播：1.灵活 2.能够比清单文件优先收到,优先级相同时3.优先级可以调成最大21忆
		receiver = new SMSReceiver();
		// 告诉系统你注册的是短信的广播
		IntentFilter filter = new IntentFilter();
		// 注册短信的广播
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		// 设置优先级
		// 优先级在代码设置时 有-1000~1000范围限制 但是 我们可以作弊
		// 设置成最大 能够优先于所有清单文件注册的广播 如果有应用比你先安装 还是我们先收到
		// 代码配置优先级 如果和清单文件一样 还是代码先收到
		filter.setPriority(Integer.MAX_VALUE);
		// 注册广播
		registerReceiver(receiver, filter);

		// 注册内容观察者

		observer = new MySMSObserver(new Handler());
		getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, observer);

		// 去电（广播）和来电（TelephonyManager）

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		
		phoneListner = new MyPhoneListner();

		// 监听电话的到来
		tm.listen(phoneListner, PhoneStateListener.LISTEN_CALL_STATE);

		
	}

	private class MyPhoneListner extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

				System.out.println("电话待机");

				break;

			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("电话响铃");
				// 怎么调用挂断的电话的功能

				// 如果是需要拦截号码 则挂断电话
				int mode = dao.getModeFromNumber(incomingNumber);
				if (mode == 1 || mode == 3) {

					endCall();
					Toast.makeText(BlackNumberService.this,
							"挂断电话:" + incomingNumber, 0).show();
				}
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("电话接听");
				break;

			default:
				break;
			}
		}

	}

	private void endCall() {

		// 挂断电话的操作其实是一个谷歌隐藏的功能

		// 想调用endCall方法 获得 ITelephony 对象

		// IBinder binder =
//		ServiceManager.getService(Context.TELEPHONY_SERVICE);

		//

		try {
			// 反射的作用是为了调用系统隐藏的类和方法
			Class<?> clazz = Class.forName("android.os.ServiceManager");

			Method method = clazz.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,Context.TELEPHONY_SERVICE);

			// 以上三句话的目的是为了调用ServiceManager.getService(Context.TELEPHONY_SERVICE)
			// 这一句代码

			ITelephony telephony = ITelephony.Stub.asInterface(binder);
			
			telephony.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class MySMSObserver extends ContentObserver {

		public MySMSObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			// TODO Auto-generated method stub
			super.onChange(selfChange, uri);
			// 获取短信的号码 address body type read date
			Cursor cursor = getContentResolver().query(uri,
					new String[] { "address" }, null, null, null);
			String number = "";
			if (cursor != null && cursor.moveToNext()) {
				number = cursor.getString(0);
				cursor.close();
			}
			int mode = dao.getModeFromNumber(number);

			if (mode == 2 || mode == 3) {
				// 删除这一条短信即可
				getContentResolver().delete(uri, null, null);
				Toast.makeText(BlackNumberService.this,
						"通过内容观察者删除短信：" + number, 0).show();
			}

		}

	}

	private class SMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// 先获取到号码
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			// 以第一段为例
			Object object = objects[0];
			SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
			String number = msg.getOriginatingAddress();

			// 判断是否存在黑名单数据库中
			int mode = dao.getModeFromNumber(number);
			if (mode == 2 || mode == 3) {
				// 只有当拦截模式是2或3时才拦截
				Toast.makeText(BlackNumberService.this, "拦截短信：" + number, 0)
						.show();
				// 如果存在将广播停止
				abortBroadcast();
			}
		}

	}

	// 服务销毁
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		// 取消注册广播
		unregisterReceiver(receiver);
		// 取消注册内容观察者
		getContentResolver().unregisterContentObserver(observer);
		// 取消对电话到来的监听
		tm.listen(phoneListner, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

}
