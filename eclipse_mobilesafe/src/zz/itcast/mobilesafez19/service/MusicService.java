package zz.itcast.mobilesafez19.service;

import zz.itcast.mobilesafez19.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

	private MediaPlayer mp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 服务的特点：如果一个服务被意外终止 在内存充足的情况下 会尝试再次启动
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mp = MediaPlayer.create(this, R.raw.zjzy);
		mp.setLooping(true);
		mp.start();
	}

	
	// 这个返回值可以用来表明这个服务被停掉后是否会自启动
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_NOT_STICKY;
	}
	
	
	@Override
	public void onDestroy() {
		mp.stop();
		mp.release();
		mp = null;
		super.onDestroy();
	}

}
