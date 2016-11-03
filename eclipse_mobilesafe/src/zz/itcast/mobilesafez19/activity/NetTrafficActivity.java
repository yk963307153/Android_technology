package zz.itcast.mobilesafez19.activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.app.MyApp;
import zz.itcast.mobilesafez19.bean.AppInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class NetTrafficActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_traffic);
		
		
		MyApp app = (MyApp) getApplication(); // 获得MyApp 对象
		AppInfo info = (AppInfo) app.leoCache;
		app.leoCache = null;
		
		
//		TrafficStats.getMobileRxBytes(); // 手机接收到的总的字节数
//		TrafficStats.getMobileTxBytes(); // 手机发送的总的字节数
//		
//		TrafficStats.getTotalRxBytes(); // 手机通过网络接收到的总的字节数
//		
//		// uid 用户ID 其实是系统为每个应用分配的ID 通过 applicationInfo.uid; 获得
//		TrafficStats.getUidTcpRxBytes(123); // 获得指定UID的应用能过 TCP 联接收到的字节数
//		TrafficStats.getUidTcpTxBytes(123);// 获得指定UID的应用能过 TCP 联接发送的字节数
		
	}
	
	/**
	 * 获得应用收到的字节数
	 * @param uid 应用的 uid 
	 * @return
	 */
	public long getUidReceiveBytes(int uid){
		
		// 读取   /proc/uid_stat/uid/tcp_rcv     34234634
		
		try {
			FileInputStream fin = new FileInputStream("/proc/uid_stat/"+uid+"/tcp_rcv");
			
			BufferedReader reader  = new BufferedReader(new InputStreamReader(fin));
			
			String line = reader.readLine();
			
			return Long.parseLong(line);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * 获得应用收到的字节数
	 * @param uid 应用的 uid 
	 * @return
	 */
	public long getUidSendBytes(int uid){
		
		// 读取   /proc/uid_stat/uid/tcp_snd     34234634
		
		try {
			FileInputStream fin = new FileInputStream("/proc/uid_stat/"+uid+"/tcp_snd");
			
			BufferedReader reader  = new BufferedReader(new InputStreamReader(fin));
			
			String line = reader.readLine();
			
			return Long.parseLong(line);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
