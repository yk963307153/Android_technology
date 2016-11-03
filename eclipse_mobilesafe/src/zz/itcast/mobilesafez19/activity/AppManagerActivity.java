package zz.itcast.mobilesafez19.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.AppInfo;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AppManagerActivity extends BaseActivity implements
		OnScrollListener, OnItemClickListener, OnClickListener {

	private TextView tvInner, tvOutter;
	private AppAdapter adapter;

	private ListView lvAppList;
	private ArrayList<AppInfo> allApps;

	private TextView tvTitle;

	private ProgressDialog pd;
	// 为了实现用户应用和系统应用分组 搞两个集合
	private ArrayList<AppInfo> userAppList;
	private ArrayList<AppInfo> systemAppList;

	private int x, y;

	private PopupWindow pw;

	private PackageManager pm;

	private AppInfo info;
	private AppUninstallReceiver uninstallReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_app_manager);
		pm = getPackageManager();
		tvInner = (TextView) findViewById(R.id.tv_inner_space);
		tvOutter = (TextView) findViewById(R.id.tv_outter_space);
		lvAppList = (ListView) findViewById(R.id.lv_app_list);
		tvTitle = (TextView) findViewById(R.id.tv_app_list_title);
		// 初始化存储空间
		initSpaceInfo();

		// 填充listView
		fillData();

		// 给条目设置点击事件
		lvAppList.setOnItemClickListener(this);

		// 给ListView整体设置一个touch事件
		lvAppList.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = (int) event.getRawX();
					y = (int) event.getRawY();
				}
				return false;
			}
		});

		// 在界面打开时注册一个其他应用被卸载的广播事件 用来进行更新UI的处理

		uninstallReceiver = new AppUninstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		// 系统再向外发广播时 会携带i一个data 我们监听时 应该满足系统的条件
		filter.addDataScheme("package");
		registerReceiver(uninstallReceiver, filter);

	}

	private class AppUninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("监听到应用被卸载");
			// 删除集合中的数据
			if (info.isSystemApp) {
				systemAppList.remove(info);
			} else {
				userAppList.remove(info);
			}

			// 更新UI

			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}

		}

	}

	@Override
	protected void onDestroy() {

		// 在界面退出时 取消对应用卸载广播的监听
		unregisterReceiver(uninstallReceiver);
		super.onDestroy();
	}

	private void fillData() {

		pd = new ProgressDialog(act);
		pd.setMessage("加载中...");
		pd.show();

		new Thread() {
			public void run() {
				allApps = AppUtils.getAllApps(act);

				if (allApps != null && allApps.size() > 0) {

					systemAppList = new ArrayList<AppInfo>();
					userAppList = new ArrayList<AppInfo>();

					for (AppInfo info : allApps) {
						if (info.isSystemApp) {
							systemAppList.add(info);
						} else {
							userAppList.add(info);
						}
					}
				}

				flushUI();

			}

		}.start();

	}

	private void flushUI() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				adapter = new AppAdapter();

				lvAppList.setAdapter(adapter);
				pd.dismiss();
				// 在滑动中改变上方固定小标题的文字 监听一个滑动的事件
				lvAppList.setOnScrollListener(AppManagerActivity.this);
			}
		});

	}

	private class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// 这样写表名 我的数据来自于两个集合
			return userAppList.size() + systemAppList.size();
		}

		// 1.复用历史对象 2.减少findViewById次数 ViewHolder 3.有if的地方一定有else
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(act, R.layout.item_app_layout, null);
				holder = new ViewHolder();

				holder.ivIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
				holder.tvName = (TextView) view.findViewById(R.id.tv_app_name);
				holder.tvSize = (TextView) view.findViewById(R.id.tv_app_size);
				holder.tvSave = (TextView) view.findViewById(R.id.tv_app_save);
				holder.tvTitle = (TextView) view
						.findViewById(R.id.tv_app_title);
				// 打包背身上
				view.setTag(holder);

			} else {
				view = convertView;

				// 拿到包袱
				holder = (ViewHolder) view.getTag();

			}

			// 获取bean对象
			// AppInfo appInfo = allApps.get(position);
			AppInfo appInfo = null;
			// 将bean对象通过两个集合获取

			if (position >= userAppList.size()) {
				// 获取系统集合中的
				appInfo = systemAppList.get(position - userAppList.size());
			} else {
				// 获取用户集合中的
				appInfo = userAppList.get(position);
			}

			holder.ivIcon.setImageDrawable(appInfo.icon);
			holder.tvName.setText(appInfo.appName);
			holder.tvSize.setText(appInfo.appSize);
			holder.tvSave.setText(appInfo.isSaveInSD ? "外部存储" : "内部存储");

			// 当第一个用户应用和第一个系统应用
			if (position == 0) {
				// 第一个用户
				holder.tvTitle.setVisibility(View.VISIBLE);
				holder.tvTitle.setText("用户应用");
			} else if (position == userAppList.size()) {
				// 第一个系统应用
				holder.tvTitle.setVisibility(View.VISIBLE);
				holder.tvTitle.setText("系统应用");
			} else {
				// 将小标题隐藏
				holder.tvTitle.setVisibility(View.GONE);
			}

			return view;
		}

		// 下面这两个方法是给我们自己用的
		@Override
		public Object getItem(int position) {

			AppInfo info = null;

			if (position >= userAppList.size()) {
				// 返回系统的集合中的对象
				info = systemAppList.get(position - userAppList.size());
			} else {
				// 用户集合
				info = userAppList.get(position);
			}

			return info;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private void initSpaceInfo() {

		// 内部空间 /data
		File innerFile = Environment.getDataDirectory();
		// 外部空间 /mnt/sdcard
		File outterFile = Environment.getExternalStorageDirectory();

		// 内部可用空间
		long innerUsableSpace = innerFile.getUsableSpace(); // byte
		long outterUsableSpace = outterFile.getUsableSpace();

		// 单位的转换
		String innerSpace = Formatter.formatFileSize(act, innerUsableSpace);
		String outterSpace = Formatter.formatFileSize(act, outterUsableSpace);

		tvInner.append(innerSpace);
		tvOutter.append(outterSpace);

	}

	private class ViewHolder {

		public ImageView ivIcon;
		public TextView tvName;
		public TextView tvSize;
		public TextView tvSave;
		public TextView tvTitle;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 获取第一个可见条目的位置
		// if (userAppList == null || systemAppList == null) {
		// return;
		// }

		if (firstVisibleItem >= userAppList.size()) {
			// 系统
			tvTitle.setText("系统应用");
		} else {
			// 用户
			tvTitle.setText("用户应用");
		}

		// 先判断第二个可见的条目是不是第一个系统应用
		if (firstVisibleItem + 1 == userAppList.size()) {
			View item2 = lvAppList.getChildAt(1);

			int top = item2.getTop();

			if (top <= tvTitle.getHeight()) {
				tvTitle.setTranslationY(top - tvTitle.getHeight());
			}
		} else {
			tvTitle.setTranslationY(0);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// 先记录下当前点击的是哪一个bean

		info = (AppInfo) lvAppList.getItemAtPosition(position);
		if (info == null) {
			System.out.println("null");
		} else {
			System.out.println("info");
		}

		pw = new PopupWindow(this);
		pw.setWidth(LayoutParams.WRAP_CONTENT);
		pw.setHeight(LayoutParams.WRAP_CONTENT);

		View contentView = View.inflate(this, R.layout.popup_window_layout,
				null);
		LinearLayout llUninstallApp = (LinearLayout) contentView
				.findViewById(R.id.ll_uninstall_app);
		LinearLayout llStartApp = (LinearLayout) contentView
				.findViewById(R.id.ll_start_app);
		LinearLayout llSharedApp = (LinearLayout) contentView
				.findViewById(R.id.ll_share_app);
		LinearLayout llSettingApp = (LinearLayout) contentView
				.findViewById(R.id.ll_setting_app);

		llUninstallApp.setOnClickListener(this);
		llStartApp.setOnClickListener(this);
		llSharedApp.setOnClickListener(this);
		llSettingApp.setOnClickListener(this);

		pw.setContentView(contentView);
		// 透明背景
		pw.setBackgroundDrawable(new BitmapDrawable());

		// 获取到焦点 点击以外区域消失掉
		pw.setFocusable(true);

		// 在哪个坐标下去显示
		// 一个空间的高度在没有添加进来之前 都是0
		System.out.println(contentView.getHeight() + "");

		pw.showAtLocation(view, Gravity.TOP | Gravity.LEFT, x, y - 30);
		// 执行动画显示
		Animation animation = AnimationUtils.loadAnimation(act,
				R.anim.popup_anim);

		contentView.startAnimation(animation);

	}

	// 当点击四个button之后调用的方法
	@Override
	public void onClick(View v) {

		if (pw != null) {
			pw.dismiss();
			// 这句话只是让垃圾回收机制知道这个对象没有被引用到
			pw = null;
		}

		switch (v.getId()) {

		case R.id.ll_start_app:

			// 这个其他应用的界面开启的意图可以通过一个类获取到
			// 获取一个应用的启动界面
			Intent intent = pm.getLaunchIntentForPackage(info.packageName);
			if (intent != null) {
				startActivity(intent);
			} else {
				AppUtils.showToast("此应用无法启动", act);
			}

			break;

		case R.id.ll_uninstall_app:
			// 卸载应用 本身也是一个系统的应用
			// 通过隐式意图开启这个界
			/*
			 * <intent-filter>
			 * 
			 * <action android:name="android.intent.action.DELETE" /> <category
			 * android:name="android.intent.category.DEFAULT" /> <data
			 * android:scheme="package" /> </intent-filter>
			 */
			// 先判断是系统应用还是用户应用
			if (info.isSystemApp) {
				
				// 第一先判断是否拥有root权限 Root.jar
				boolean rootAvailable = RootTools.isRootAvailable();
				if (rootAvailable) {
					// 第二再判断当前应用是否赋予了root权限
					boolean accessGiven;
					try {
						accessGiven = RootTools.isAccessGiven();
						if (accessGiven) {
							// 执行删除的命令 模拟命令行的操作 rm /system/app/xxx.apk
							try {
								// 超时3秒
								RootTools.sendShell("rm " + info.apkPath, 3000);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				Intent uninstall = new Intent();
				uninstall.setAction("android.intent.action.DELETE");
				uninstall.setData(Uri.parse("package:" + info.packageName));
				startActivity(uninstall);
			}
			break;

		case R.id.ll_share_app:
			// 通过隐式意图打开一个分享的界面
			// 如果手机中有很多应用都满足一个过滤器 就给用户弹出一个选择框
			// 借助短信的源码 查看分享的隐式意图怎么写

			/*
			 * <intent-filter> <action android:name="android.intent.action.SEND"
			 * /> <category android:name="android.intent.category.DEFAULT" />
			 * <data android:mimeType="text/plain" /> </intent-filter>
			 */

			Intent shared = new Intent();
			shared.setAction("android.intent.action.SEND");
			shared.setType("text/plain");
			// 携带一些文本过去
			shared.putExtra(Intent.EXTRA_TEXT, "给你推荐一个好玩的应用：" + info.appName
					+ ",你可以去www.itcast.cn下载");
			startActivity(shared);

			break;

		case R.id.ll_setting_app:

			// 跳转到系统的对应的设置界面
			/*
			 * <intent-filter> <action
			 * android:name="android.settings.APPLICATION_DETAILS_SETTINGS" />
			 * <category android:name="android.intent.category.DEFAULT" /> <data
			 * android:scheme="package" /> </intent-filter>
			 */

			Intent setting = new Intent();
			setting.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			setting.setData(Uri.parse("package:" + info.packageName));
			startActivity(setting);

			break;

		default:
			break;
		}

	}

}
