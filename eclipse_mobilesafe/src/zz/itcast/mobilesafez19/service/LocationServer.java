package zz.itcast.mobilesafez19.service;

import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;

public class LocationServer extends Service {

	private LocationManager lm;
	private LocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 服务一打开 就开始进行定位功能
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);

		// 让系统给我推荐一个可用的定位方式 不见得非得是GPS
		// 你的标准是什么样的
		Criteria criteria = new Criteria();
		// 希望精确度越精确越好
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 会产生一些消耗（流量和钱的消耗）
		criteria.setCostAllowed(true);
		String provider = lm.getBestProvider(criteria, true);

		
		// 创建位置的监听者
		listener = new MyLinstner();
		
		// 获取当前能够使用的定位方式
		lm.requestLocationUpdates(provider, 0, 0, listener);

	}
	
	
	@Override
	public void onDestroy() {
		
		// 需要停止对位置的监听
		lm.removeUpdates(listener);
		super.onDestroy();
	}
	
	
	private class MyLinstner implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			double jingdu = location.getLongitude();
			double weidu = location.getLatitude();

			// 通过谁遥控的，就将位置发送给谁
			String sender = MyResource.sender;
			if (!TextUtils.isEmpty(sender)) {
				// 通过短信再将位置发送过来

				SmsManager manager = SmsManager.getDefault();
				manager.sendTextMessage(sender, null,
						"bao gao zhu ren ,wo zai jingdu:" + jingdu
								+ ",weidu:" + weidu, null, null);

			}
			
			// 只需要在获取到位置之后将这个服务停止掉 即可
			// 服务自己将自己停掉  会回调OnDestroy
			stopSelf(); 
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
