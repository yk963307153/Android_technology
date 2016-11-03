package zz.itcast.mobilesafez19.receiver;

import zz.itcast.mobilesafez19.service.LocationServer;
import zz.itcast.mobilesafez19.service.MusicService;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	private String GPS = "#*gps*#";
	private String MUSIC = "#*music*#";
	private String LOCK = "#*lock*#";
	private String WIPE = "#*wipe*#";

	private String smsContent = "";
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	public void onReceive(Context context, Intent intent) {
		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

		// ComponentName 是对四大组件的一个封装 看到它就等于看到四大组件

		mDeviceAdminSample = new ComponentName(context, MyDeviceReceiver.class);

		System.out.println("z19 收到清单文件短信广播");
		// 先获取短信内容 如果短信内容是
		getSMSInfo(intent);

		if (MUSIC.equals(smsContent)) {
			// 播放音乐
			System.out.println("z19播放音乐");

			// 我们先收到广播 因为系统短信的优先级是默认值 0
			// 直接停掉广播 有可能只针对原生系统生效
			abortBroadcast();

			// MediaPlayer MP3

			// 让Mp资源和音乐文件关联起来
			// 为了让音乐长期运行 我们可以考虑将音乐放在服务中
			Intent service = new Intent(context, MusicService.class);
			context.startService(service);
		} else if (GPS.equals(smsContent)) {
			// 进行GPS定位
			System.out.println("z19GPS定位");
			abortBroadcast();

			// 去定位 位置有可能过一会拿到 广播的生命周期10秒钟

			// 如果定位的时间过长 有可能拿不到位置了 将比较耗时的这个操作 放在服务中去完成

			// 开服务
			Intent location = new Intent(context, LocationServer.class);
			context.startService(location);

		} else if (LOCK.equals(smsContent)) {

			// 远程锁屏
			System.out.println("z19远程锁屏");
			abortBroadcast();

			boolean active = mDPM.isAdminActive(mDeviceAdminSample);
			if (active) {
				// 设置一个密码

				//
				mDPM.resetPassword("1234", 0);

				mDPM.lockNow();
			}

		} else if (WIPE.equals(smsContent)) {

			// 远程销毁数据（回复出厂设置）
			System.out.println("z19销毁数据");
			abortBroadcast();

			boolean active = mDPM.isAdminActive(mDeviceAdminSample);
			// 先判断当前应用是否已经获取了设备管理员
			if (active) {
				// 这里可以填1或者0 1代表清除所有的数据（包含sd卡）0 代表只清除手机本身
				mDPM.wipeData(1);
			}

		}

	}

	private void getSMSInfo(Intent intent) {
		// 获取短信的模版代码
		Object[] objects = (Object[]) intent.getExtras().get("pdus");

		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
			String single = sms.getMessageBody();
			// 将发件人存起来
			MyResource.sender = sms.getOriginatingAddress();
			sb.append(single);
		}

		smsContent = sb.toString();

	}

}
