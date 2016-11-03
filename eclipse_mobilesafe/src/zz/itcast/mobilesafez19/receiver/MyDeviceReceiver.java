package zz.itcast.mobilesafez19.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

// 广播  代言人
public class MyDeviceReceiver extends DeviceAdminReceiver {

	
	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onPasswordChanged(context, intent);
	}
	
	
	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onPasswordFailed(context, intent);
	}
	
}
