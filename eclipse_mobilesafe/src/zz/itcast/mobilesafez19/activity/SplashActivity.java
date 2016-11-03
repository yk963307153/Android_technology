package zz.itcast.mobilesafez19.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.service.SMSService;
import zz.itcast.mobilesafez19.utils.AVUtils;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import zz.itcast.mobilesafez19.utils.StreamTools;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SplashActivity extends BaseActivity {

	protected static final int REQUEST_FOR_INSTALL = 1;

	private TextView tvVersion;

	// 本地的版本号
	private int versionCode;
	private String versionName;

	private ProgressDialog pd;

	private PackageManager pm;

	private Handler handler = new Handler();

	protected Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		
		tvVersion = (TextView) findViewById(R.id.tv_version);
		// 获取到当前的版本
		getVersionInfo();
		// 展示当前本地的版本
		tvVersion.setText(versionName);

		// 根据是否自动检查更新来判断
		boolean isAuto = sp.getBoolean(MyResource.KEY_AUTO_UPDATE, false);
		if (isAuto) {
			// 检查版本更新 这个功能一定要在1.0版本就开始做 每个应用都是这样
			checkUpdate();
		} else {
			// 过一会做一件事
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					gotoHomepageActivity();

				}
			}, 1500);
		}

		// 如果防盗保护开启 同时打开对短信数据库观察的服务
		Intent service = new Intent(this, SMSService.class);
		startService(service);

		// 将资产目录中的数据库文件 负责到手机的硬盘上（存储空间 /data/data/包名）
		copyDbToLocal("address.db");
		copyDbToLocal("antivirus.db");
		
		// 在闪屏界面一打开  发一个广播 通知系统的Launcher应用去创建快捷方式
		createShortcut();
		
		// 更新病毒库 
		updateVirusDb();

	}

	private void updateVirusDb() {
		new Thread(){
			public void run() {
				
				try {
					int currVersion = AVUtils.getVersion(ctx);
					URL url = new URL("http://192.168.12.75:8080/VirusDbUpdateServer/VirusDbUpdate?version="+currVersion);
					
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					
					conn.setConnectTimeout(5000); // 联接超时时间，
					conn.setReadTimeout(5000); // 读取内容时的超时时间
					
					conn.setRequestMethod("GET");
					
					int responseCode = conn.getResponseCode();
					if(200 == responseCode){
						
						// 获得服务器返回的数据
						InputStream input = conn.getInputStream();
						
						String jsonStr = StreamTools.getStringFromStream(input);
						System.out.println("jsonStr:"+jsonStr);
						
						// 解析 json 数据
						JSONObject jobj = new JSONObject(jsonStr);
						int version = jobj.getInt("version"); // 获得服务器返的版本号
						String md5Str = jobj.getString("md5"); // 新的病毒文件的MD5值
						String desc = jobj.getString("desc");
						
						//  更新病毒库版本
						AVUtils.updateVersion(ctx,version);
						
						// 更新病毒文件
						AVUtils.updateVirusMd5(ctx,md5Str,desc);
						
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			};
		}.start();
	}

	private void createShortcut() {
		
		Intent intent  = new Intent();
		// 频段去Launcher应用清单文件中找
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// 快捷方式  三要素 
		/*
		 * 1. 长啥样  使用bitmap传递
		 * 2.叫啥名
		 * 3.干啥事   跳到哪个界面去    隐士意图
		 * 
		 */
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.mengbi));
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "萌死了");
		// 跳转到首页HomepageActivty  一定要用隐式意图
		Intent home = new Intent();
		home.setAction("zz.itcast19.homepage");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, home);
		// 我们可以告诉Launcher应用如果已经创建过的就不再重复  不允许重复 
		intent.putExtra("duplicate", false);
		// 发广播的操作
		sendBroadcast(intent);
		
	}

	private void copyDbToLocal(final String dbName) {
		// 文件的复制 放在子线程中完成 耗时操作
		new Thread() {
			public void run() {
				
				// /data/data/包名/files
//				getCacheDir(); // /data/data/包名/cache
				File file = new File(getFilesDir(), dbName);
				if(file.exists()){
					return;
				}
				
				AssetManager manager = getAssets();
				try {
					// 源 assets目录中的文件
					InputStream is = manager.open(dbName);
					// 目的 /data/data/包名/files
					FileOutputStream fos = new FileOutputStream(file);

					int len = -1;
					byte[] buf = new byte[1024];

					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					// 关流
					is.close();
					fos.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();

	}

	private void checkUpdate() {
		// 将地址防到一个不容易修改的文件里
		final String path = getString(R.string.update_url);

		// 子线程
		new Thread() {
			public void run() {
				try {
					SystemClock.sleep(2000);
					URL url = new URL(path);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);

					int code = conn.getResponseCode();
					if (code == 200) {
						// 联网成功
						// 将流转换成字符串
						InputStream is = conn.getInputStream();
						String body = StreamTools.getStringFromStream(is);
						// josn解析

						JSONObject jsonObj = new JSONObject(body);
						int newVersionCode = jsonObj.getInt("code");
						String info = jsonObj.getString("info");
						String apkUrl = jsonObj.getString("apkurl");

						System.out.println("新版本号：" + newVersionCode + ","
								+ info + "," + apkUrl);

						// 与本地版本号做判断
						if (newVersionCode == versionCode) {
							// 说明当前已经是最新版本
							// 跳转到主界面
							gotoHomepageActivity();

						} else {
							// 说明有新版本需要更新
							// 弹对话框提示用户去下载新版本

							showUpdateDialog(info, apkUrl);

						}

					} else {
						AppUtils.showToast("服务器忙，请稍后重试", SplashActivity.this);
						gotoHomepageActivity();
					}

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					AppUtils.showToast("服务器忙，请稍后重试", SplashActivity.this);
					gotoHomepageActivity();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 有可能是用户责任
					AppUtils.showToast("请检查网络设置或稍后重试", SplashActivity.this);
					gotoHomepageActivity();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					AppUtils.showToast("服务器忙，请稍后重试", SplashActivity.this);
					gotoHomepageActivity();
				} catch (Exception e) {
					e.printStackTrace();
					AppUtils.showToast("服务器忙，请稍后重试", SplashActivity.this);
					gotoHomepageActivity();
				}

			}

		}.start();

		// 联网检查版本更新 去获取服务器上最新的版本
		// 如果版本号不一致提示用户更新
		// 再获取新版本apk的Url地址
		// 再获取新版本有哪些变动的地方

	}

	private void showUpdateDialog(final String info, final String apkurl) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 普通对对话框
				AlertDialog.Builder builder = new Builder(SplashActivity.this);
				builder.setTitle("更新提示");
				builder.setMessage(info);

				// 如果不想让对话框关闭
				// builder.setCancelable(false);
				// 给对话框设置一个消失的监听
				builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						gotoHomepageActivity();

					}
				});

				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						gotoHomepageActivity();

					}
				});

				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 去下载
						// 自己 HttpUtils // 从哪下 下到哪
						downloadApk(apkurl);

					}
				});

				builder.show();
			}
		});

	}

	private void downloadApk(String apkurl) {

		// 采用XUtils去下载

		HttpUtils http = new HttpUtils();
		// 从哪下 下到哪
		http.download(apkurl, MyResource.DOWNLOAD_PATH + "/temp.apk",
				new RequestCallBack<File>() {

					// 当开始下载
					@Override
					public void onStart() {
						// TODO Auto-generated method stub

						pd = new ProgressDialog(SplashActivity.this);
						pd.setMessage("下载中...");
						// 设置为水平进度条样式
						pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						pd.show();

					}

					// 下载中
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// 更新进度条
						pd.setMax((int) total);
						pd.setProgress((int) current);

					}

					// 当下载成功
					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						// TODO Auto-generated method stub

						AppUtils.showToast("下载成功", SplashActivity.this);

						// 对话框消失掉
						if (pd != null) {
							pd.dismiss();
						}
						// 询问用户是否进行安装 其实会弹出系统的一个界面

						// 隐式意图

						/*
						 * <action android:name="android.intent.action.VIEW" />
						 * <category
						 * android:name="android.intent.category.DEFAULT" />
						 * <data android:scheme="content" /> <data
						 * android:scheme="file" /> <data android:mimeType=
						 * "application/vnd.android.package-archive" />
						 */

						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						// intent.addCategory("android.intent.category.DEFAULT");
						// file:///mnt/sdcar/xxxx
						// intent.setType("application/vnd.android.package-archive");
						// intent.setData(Uri.parse("file://"+MyResource.DOWNLOAD_PATH
						// + "/temp.apk"));
						// 以上两句话互相矛盾 如果同时需要设置data和type则使用下面的方法
						Uri uri = Uri.fromFile(responseInfo.result); // 利用xutils封装好的一个结果
						Uri.parse("file://" + MyResource.DOWNLOAD_PATH
								+ "/temp.apk");
						intent.setDataAndType(uri,
								"application/vnd.android.package-archive");

						// 开启B同时还要索要结果
						startActivityForResult(intent, REQUEST_FOR_INSTALL);

					}

					// 当下载失败
					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub

						AppUtils.showToast("服务器忙，下载失败", SplashActivity.this);

						// 对话框消失掉
						if (pd != null) {
							pd.dismiss();
						}

						gotoHomepageActivity();
					}
				});

	}

	// 在这获取B界面的结果回传
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_FOR_INSTALL) {
			// 说明是我们的界面回传
			// 如果以后再开启系统的一个界面 这个界面包含取消和确定 我想知道点的是取消还是确定 做法都是通用的
			if (resultCode == RESULT_CANCELED) {
				// 说明用户点击的是取消
				System.out.println("点击取消");
				gotoHomepageActivity();
			} else if (resultCode == RESULT_OK) {
				// 说明用户点击的是确定
			}

		}

	}

	private void gotoHomepageActivity() {
		Intent intent = new Intent(this, HomepageActivity.class);
		startActivity(intent);

		finish();

	}

	private void getVersionInfo() {
		// 如何拿到版本信息？
		pm = getPackageManager();
		// 获取到对应的包下面的信息
		// 写0获取到所有的 其他的代表写谁获取谁
		try {
			// PackageInfo 是对整个清单文件的封装
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			// ApplicationInfo 是Application节点的封装
			// ApplicationInfo applicationInfo =
			// pm.getApplicationInfo("zz.itcast.mobilesafez19", 0);
			versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
