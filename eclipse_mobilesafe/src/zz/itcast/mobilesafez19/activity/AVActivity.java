package zz.itcast.mobilesafez19.activity;

import java.io.File;
import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.AppInfo;
import zz.itcast.mobilesafez19.utils.AVUtils;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MD5Utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AVActivity extends Activity {

	private ImageView ivScan;

	/**
	 * 水平进度条上的描述文字
	 */
	private TextView tvDescProgress;
	
	/**
	 * 扫描结果描述文字
	 */
	private TextView tvDesc;
	
	private ProgressBar progressBar;
	
	private LinearLayout llScanResult;

	protected Context ctx;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_av);
		ctx = this;
		
		ivScan = (ImageView) findViewById(R.id.iv_scanning);
		tvDescProgress = (TextView) findViewById(R.id.tv_desc_progress);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		llScanResult = (LinearLayout) findViewById(R.id.ll_scan_result);
		
		startScan();
		
	}


	private void startScan() {
		
		new Thread(){
			public void run() {
				// 初始化引擎
				SystemClock.sleep(2000); // 休眠 2秒
				handler.sendEmptyMessage(SCAN_START);
				
				// 扫描文件
				ArrayList<AppInfo> allApps = AppUtils.getAllApps(ctx);
				progressBar.setMax(allApps.size());
				
				for(AppInfo info : allApps){
					// 获得 apk文件的md5值
					String fileMd5 = MD5Utils.md5File(new File(info.apkPath));
					// 判断是否是病毒
					boolean isVirus = AVUtils.isVirus(ctx, fileMd5); 
					
					// 包装扫描结果
					MyScanResult result = new MyScanResult();
					result.isVirus = isVirus;
					result.appInfo = info;
					
					// 将结果发送至handler 
					Message msg = Message.obtain();
					msg.what = SCAN_ING;
					msg.obj = result;
					handler.sendMessage(msg);
					
					SystemClock.sleep(200); // 休眠一会儿，让用记看到，我们在干活
					
				}
				
				// 扫描完成
				handler.sendEmptyMessage(SCAN_END);
				
			};
		}.start();
	}
	
	// 扫描病毒的结果，
	private class MyScanResult{
		public boolean isVirus;
		public AppInfo appInfo;
	}
	
	
	
	private final int SCAN_START = 100;
	
	private final int SCAN_ING = 101;
	
	private final int SCAN_END = 102;

	/**
	 * 发现的病毒数量
	 */
	protected int viursCount;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_START: // 开始扫描
				
				tvDescProgress.setText("正在扫描...");
				
				// 以自已的中心点为园点旋转
				RotateAnimation ra = new RotateAnimation(0, 3600,
						Animation.RELATIVE_TO_SELF,0.5f,
						Animation.RELATIVE_TO_SELF,0.5f);
				
				ra.setDuration(10000);
				// 设置重复次数
				ra.setRepeatCount(Animation.INFINITE); // 英菲尼迪，无穷，无限的
				
				ivScan.startAnimation(ra);
				
				viursCount = 0;
				
				break;
			case SCAN_ING: // 扫描中
				
				// 让进度增加 1 ，相对于最大值
				progressBar.incrementProgressBy(1);
				
				MyScanResult result = (MyScanResult) msg.obj;
				tvDesc.setText("正在扫描："+result.appInfo.appName);
				
				// 展示扫描结果
				
				TextView textView  = new TextView(ctx);
				textView.setTextSize(16);
				if(result.isVirus){
					viursCount++;
					
					textView.setText(result.appInfo.appName+" 发现病毒");
					textView.setTextColor(Color.RED);
				}else{
					textView.setText(result.appInfo.appName+" 扫描安全");
					textView.setTextColor(Color.GREEN);
				}
				
				llScanResult.addView(textView,0); // 将 textView 添加至 线性布局 ,并指定下标，做为第一个子view
				
				
				break;
			case SCAN_END: // 扫描结束
				
				ivScan.clearAnimation(); // 清除动画效果
				
				tvDescProgress.setText("扫描完成");
				
				int totalCount = llScanResult.getChildCount(); // 获得 llScanResult 子view 的数量
				
				tvDesc.setText("扫描了"+totalCount+"个应用，发现了"+viursCount+"个病毒");
				
				break;
			default:
				break;
			}
			
			
		};
	};
	
}
