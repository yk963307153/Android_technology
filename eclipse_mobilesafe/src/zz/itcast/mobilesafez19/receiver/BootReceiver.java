package zz.itcast.mobilesafez19.receiver;

import zz.itcast.mobilesafez19.service.SMSService;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	// 当收到开机广播时调用
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences(MyResource.SP_FILE_NAME, 0);

		boolean isOpen = sp.getBoolean(MyResource.KEY_OPEN_PROTECT, false);

		// 如果没有开启防盗保护 下面所有的操作就没有必要做了
		if (!isOpen) {
			return;
		}

		// 如果防盗保护开启 同时打开对短信数据库观察的服务
		Intent service = new Intent(context, SMSService.class);
		context.startService(service);

		// 先获取之前保存的SIM卡的序列号 sp
		String savedSimid = sp.getString(MyResource.KEY_SIM_ID, "");
		if (TextUtils.isEmpty(savedSimid)) {
			return;
		}

		// 再获取当前的

		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 为了模拟SIM卡跟之前不一样 随便加点什么东西
		String currentSimid = tm.getSimSerialNumber() + "asdf";
		// 如果不一样则给安全号码发短信
		if (!savedSimid.equals(currentSimid)) {
			// 说明SIM卡变更 要发送报警短信给安全号码
			String safeNumber = sp.getString(MyResource.KEY_SAFE_NUMBER, "");
			if (!TextUtils.isEmpty(safeNumber)) {
				SmsManager manager = SmsManager.getDefault();
				manager.sendTextMessage(safeNumber, null,
						"bao gao zhu ren wo bei tou le,kuai lai jiu wo", null,
						null);
			}
		}

	}

}
