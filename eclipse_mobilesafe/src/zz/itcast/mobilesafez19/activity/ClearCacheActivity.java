package zz.itcast.mobilesafez19.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.AppInfo;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ClearCacheActivity extends BaseActivity {

	private LinearLayout llScanResult;
	private LinearLayout llScaning;
	private ProgressBar progressBar;
	private TextView tvDesc;
	protected Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clearcache);
		ctx = this;
		
		llScanResult = (LinearLayout) findViewById(R.id.ll_scan_result);
		llScaning = (LinearLayout) findViewById(R.id.ll_scaning);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		tvDesc = (TextView) findViewById(R.id.tv_desc_progress);
		
		pm = getPackageManager();
		
//		//  zz.itcast.virus
//		// mPm.getPackageSizeInfo(mCurComputingSizePkg, mStatsObserver);
//		// 隐藏方法，不能直接调用，要用反射的方式调用
////	    public abstract void getPackageSizeInfo(String packageName,IPackageStatsObserver observer);
////		pm.getPackageSizeInfo("zz.itcast.virus", mStatsObserver);
//		

		
		startScan();
		
	}
	
	private void startScan() {
		
		new Thread(){
			public void run() {
				
				ArrayList<AppInfo> allApps = AppUtils.getAllApps(ctx);
				
				for(AppInfo appInfo :allApps){
					
					getAppCache(appInfo.packageName);
					
					Message msg = Message.obtain();
					msg.what = SCAN_ING;
					msg.obj = appInfo;
					handler.sendMessage(msg);
					
					SystemClock.sleep(200); // 休眠一会儿
				}
				
				handler.sendEmptyMessage(SCAN_END);
				
			};
		}.start();
	}
	
	public void getAppCache(String packageName){
		try {
			// 一： 获得字节码
			Class clazz = pm.getClass();
			// 二：获得方法 
			// 参数一 是方法的名称 ，参数二及以后，是getPackageSizeInfo 方法 的参数的字节码类型
			Method method = clazz.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			// 三： 调用方法 
			// 参数一：是调用方法 的对象 ，参数二及以后的参数是 getPackageSizeInfo 方法执行时的真实的参数
			method.invoke(pm, packageName, mStatsObserver);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final int SCAN_ING = 100;
	private final int ADD_RESULT = 101;
	
	private final int SCAN_END = 102;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case SCAN_ING:
				AppInfo appInfo = (AppInfo) msg.obj;
				tvDesc.setText("正在扫描:"+appInfo.appName);
				
				break;
			case ADD_RESULT:
				
				String result = (String) msg.obj;
				
				TextView textView = new TextView(ctx);
				textView.setText(result);
				
				// 添加至结果区域
				llScanResult.addView(textView);
				
				break;
			case SCAN_END:
				
				// 获得llScanResult 子view的数量
				int cacheAppCount = llScanResult.getChildCount(); 
				
				if(cacheAppCount > 0 ){ // 有缓存
					llScaning.setVisibility(View.GONE);
				}else{
					// 没有缓存
					progressBar.setVisibility(View.GONE);
					tvDesc.setText("您的手机很干净，没有缓存.");
					
				}
				break;
			}
			
			
		};
	};
	
	

	
	IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			// 如果缓存为0 什么都不干
			if(pStats.cacheSize == 0){
				return ;
			}
			
			try {
				String appName = pm.getApplicationInfo(pStats.packageName, 0).loadLabel(pm).toString();
				
				long cache = pStats.cacheSize;
				String result = appName+"缓存大小为:"+cache;
				
				Message msg = Message.obtain();
				msg.what = ADD_RESULT;
				msg.obj = result;
				
				handler.sendMessage(msg);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private PackageManager pm;
	
	// 清理所有的缓存
	public void clearCache(View v){
		
		// 调用此方法 ，传递一个足够大的数字，就可以达到释放所有的缓存的功能
		// 下面这个为隐藏方法 ，要用反射的方式去调用
		// public abstract void freeStorageAndNotify(long freeStorageSize, IPackageDataObserver observer);
		
		try {
			//一 获得字节码
			Class clazz = pm.getClass();
			
			// 获得方法 
			Method method = clazz.getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
			
			// 调用方法 
			method.invoke(pm, Long.MAX_VALUE,dataObserver);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	private IPackageDataObserver.Stub dataObserver = new IPackageDataObserver.Stub(){

		@Override
		// 清理缓存后，执行此方法 
		public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ctx, "缓存清理完成", 0).show();
					
					// 删除所有的子view 
					llScanResult.removeAllViews();
					
				}
			});
			
			
		}
		
	};
	
	
	
	
}
