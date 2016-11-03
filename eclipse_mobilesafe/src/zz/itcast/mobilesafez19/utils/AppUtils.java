package zz.itcast.mobilesafez19.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.AppInfo;
import zz.itcast.mobilesafez19.bean.ProcessInfo;
import zz.itcast.mobilesafez19.bean.SMS;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Debug.MemoryInfo;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Xml;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AppUtils {

	public static void showToast(final String body, final Activity act) {

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(act, body, 0).show();

			}
		});
	}

	/**
	 * 判断一个服务是否正在运行
	 * 
	 * @param serviceClass
	 * @return
	 */
	public static boolean isServiceRunning(
			Class<? extends Service> serviceClass, Context ctx) {

		// 获取到正在运行的一些服务

		// ActivityManager 一般用于获取到正在运行的一些东西 比如进程 比如服务等
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 穿一个参数 获得正在运行的服务 至多返回多少个服务 将所有正在运行的服务都返回
		List<RunningServiceInfo> runningServices = am.getRunningServices(1000);
		// RunningServiceInfo 可以立即为一个bean对象 是系统封装的
		for (RunningServiceInfo info : runningServices) {
			// 拿到一个四大组件的封装类
			ComponentName service = info.service;
			// 获取类名
			String className = service.getClassName();

			String name = serviceClass.getName();

			if (className.equals(name)) {
				// 说明我们传进来的服务正在运行
				return true;
			}

		}
		return false;

	}

	public static String getLocationByNumber(String phoneNumber, Context ctx) {

		// 手机号先截取前7位 包左不包右
		String substring = phoneNumber.substring(0, 7);

		String location = "未知地址";
		// 查询数据库 查询已有的数据库 /data/data/baoming/files/address.db
		String path = ctx.getFilesDir().getPath() + "/address.db";
		// 打开一个已有的数据库文件
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// 查询
		Cursor cursor = database
				.rawQuery(
						"select * from data2 where id = (select outkey from data1 where id = ?)",
						new String[] { substring });
		if (cursor != null && cursor.moveToNext()) {
			location = cursor.getString(cursor.getColumnIndex("location"));
			cursor.close();
		}
		database.close();
		return location;
	}

	/*
	 * 本身和界面是没有关系的 所以我们希望让逻辑层和显示层
	 */
	public static void backupSms(Context ctx, OnSMSListener listener) {

		// 获取所有短信

		Cursor cursor = ctx.getContentResolver().query(
				Uri.parse("content://sms"),
				new String[] { "address", "date", "type", "read", "body" },
				null, null, null);

		if (cursor != null && cursor.getCount() > 0) {

			if (listener != null) {
				listener.onGetAllCount(cursor.getCount());
			}

			// 有短信才有必要备份
			// 将短信备份到一个xml文件中 sd卡
			XmlSerializer serializer = Xml.newSerializer();
			// 设置要写的地方
			// /mnt/sdcard/mobilesafez19/allsms.xml
			File dir = new File(MyResource.ROOT_PATH);
			if (!dir.exists()) {
				// 深层级创建
				dir.mkdirs();
			}
			File file = new File(MyResource.ROOT_PATH + "/allsms.xml");
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
				serializer.setOutput(fos, "utf-8");
				// 文档的开头 <?xml version="1.0" encoding="utf-8"?>
				serializer.startDocument("utf-8", true);
				// 根开始标签
				serializer.startTag(null, "allsms");
				serializer.attribute(null, "count", cursor.getCount() + "");
				int total = 0;
				while (cursor.moveToNext()) {
					serializer.startTag(null, "sms");
					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");

					serializer.startTag(null, "date");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "date");

					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");

					serializer.startTag(null, "read");
					serializer.text(cursor.getString(3));
					serializer.endTag(null, "read");

					serializer.startTag(null, "body");
					serializer.text(cursor.getString(4));
					serializer.endTag(null, "body");

					serializer.endTag(null, "sms");

					SystemClock.sleep(100);
					total++;
					if (listener != null) {
						listener.onGetProgress(total);
					}
				}

				// 根结束标签
				serializer.endTag(null, "allsms");
				// 文档结尾
				serializer.endDocument();
				// 最后要刷新一下
				serializer.flush();
				fos.close();
				cursor.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void recoverySms(Context ctx, OnSMSListener listener) {

		// 读取XML 解析XML

		XmlPullParser parser = Xml.newPullParser();
		SMS sms = null;
		int total = 0;
		File file = new File(MyResource.ROOT_PATH + "/allsms.xml");
		if (!file.exists()) {
			return;
		}

		try {
			FileInputStream fis = new FileInputStream(file);
			parser.setInput(fis, "utf-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if ("allsms".equals(parser.getName())) {
						// 获取总个数
						String countStr = parser.getAttributeValue(null,
								"count");
						int count = Integer.parseInt(countStr);
						if (listener != null) {
							listener.onGetAllCount(count);
						}
					} else if ("sms".equals(parser.getName())) {
						sms = new SMS();
					} else if ("address".equals(parser.getName())) {
						sms.address = parser.nextText();
					} else if ("date".equals(parser.getName())) {
						sms.date = parser.nextText();
					} else if ("type".equals(parser.getName())) {
						sms.type = parser.nextText();
					} else if ("read".equals(parser.getName())) {
						sms.read = parser.nextText();
					} else if ("body".equals(parser.getName())) {
						sms.body = parser.nextText();
					}

				} else if (eventType == XmlPullParser.END_TAG) {

					if ("sms".equals(parser.getName())) {
						// 写入数据库
						ContentValues values = new ContentValues();
						values.put("address", sms.address);
						values.put("date", sms.date);
						values.put("type", sms.type);
						values.put("read", sms.read);
						values.put("body", sms.body);
						ctx.getContentResolver().insert(
								Uri.parse("content://sms"), values);

						// 通知到外面
						total++;
						if (listener != null) {
							listener.onGetProgress(total);
						}
						SystemClock.sleep(100);
					}

				}
				// 让节点移动到下一个
				eventType = parser.next();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 监听者设计模式 接口
	 * 
	 * 回调的使用
	 */

	public interface OnSMSListener {

		void onGetAllCount(int allCount);

		void onGetProgress(int progress);

	}

	// 获取到手机中安装的所有应用信息
	public static ArrayList<AppInfo> getAllApps(Context ctx) {

		ArrayList<AppInfo> datas = new ArrayList<AppInfo>();

		// 通过包的管理类 获取到所有应用信息
		PackageManager packageManager = ctx.getPackageManager();

		// 写0代表全部拿到
		List<PackageInfo> installedPackages = packageManager
				.getInstalledPackages(0);

		// 将它的比较麻烦的javabean转换成我们自己的简易的方便获取的bean

		for (PackageInfo info : installedPackages) {
			AppInfo appInfo = new AppInfo();
			appInfo.packageName = info.packageName;
			// 对Application节点的封装
			ApplicationInfo applicationInfo = info.applicationInfo;
			
			appInfo.icon = applicationInfo.loadIcon(packageManager);
			appInfo.appName = applicationInfo.loadLabel(packageManager)
					.toString();
			// 应用大小指的是安装包的大小 /data/app 用户自己安装的 /system/app 系统自带的
			String sourceDir = applicationInfo.sourceDir;
			appInfo.apkPath = sourceDir;
			File file = new File(sourceDir);
			long length = file.length(); // 字节
			// 将字节转换成带单位的大小值
			appInfo.appSize = Formatter.formatFileSize(ctx, length);

			// 是否是系统应用 太low了
			if (sourceDir.startsWith("/system")) {
				// 是系统应用
				appInfo.isSystemApp = true;
				System.out.println(appInfo.appName + "通过路径判断是系统应用");
			} else {
				// 用户应用
				appInfo.isSystemApp = false;
				System.out.println(appInfo.appName + "通过路径判断是用户应用");
			}

			// 用一个比较厉害的方式 位运算来判断
			// 位运算 每一位代表的含义 需要知道某一位是否是满足 就可以拿一个标记相与即可
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				// 系统应用
				appInfo.isSystemApp = true;
				System.out.println(appInfo.appName + "通过flag判断是系统应用");
			} else {
				// 用户应用
				appInfo.isSystemApp = false;
				System.out.println(appInfo.appName + "通过flag判断是用户应用");
			}

			// 判断是内部存储还是外部存储
			if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				// 说明是存储在外部空间中
				appInfo.isSaveInSD = true;
			} else {
				// 内部空间中
				appInfo.isSaveInSD = false;
			}

			// 添加进来
			datas.add(appInfo);

		}

		return datas;

	}

	public static ArrayList<ProcessInfo> getAllProcess(Context ctx) {

		ArrayList<ProcessInfo> datas = new ArrayList<ProcessInfo>();
		// 获取正在运行的进程 获取正在运行的一些信息

		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);

		PackageManager pm = ctx.getPackageManager();

		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();

		for (RunningAppProcessInfo info : runningAppProcesses) {
			ProcessInfo processInfo = new ProcessInfo();
			// 对象在加入集合时 先设置数据 后设置数据 不影响 无所谓
			datas.add(processInfo);
			// 进程的id
			processInfo.pid = info.pid;
			// 在系统看来 进程的名称实际上就是包名
			processInfo.packageName = info.processName;
			// 在我们看来 进程的名称实际上应该 应用的名称

			// 进程（正在运行的）的大小 实际上是所占内存的大小 应用的大小 apk安装包的大小 进程的大小是运行期间所占内存的大小
			MemoryInfo[] processMemoryInfos = am
					.getProcessMemoryInfo(new int[] { processInfo.pid });
			// 我们的内存信息直接获取第一个即可 因为参数中数组中只有一个
			MemoryInfo memoryInfo = processMemoryInfos[0];
			// 获取内存信息
			processInfo.memory = memoryInfo.getTotalPrivateDirty() * 1024;// byte

			try {
				// 获取进程不光包括上层的java进程 还会获取到底层的一些C进程 C进程没有图标和名称的
				PackageInfo packageInfo = pm.getPackageInfo(
						processInfo.packageName, 0);

				ApplicationInfo applicationInfo = packageInfo.applicationInfo;

				processInfo.icon = applicationInfo.loadIcon(pm);

				processInfo.processName = applicationInfo.loadLabel(pm)
						.toString();

				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					// 系统进程
					processInfo.isSystemProcress = true;
				} else {
					// 用户进程
					processInfo.isSystemProcress = false;

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// 只要来到这个地方 说明是一个C进程 没有图标和名称 我们可以给它一个默认的

				// 先获取到图片的bitmap
				Bitmap bitmap = BitmapFactory.decodeResource(
						ctx.getResources(), R.drawable.setup1);
				// 将Bitmap转换成Drawable
				BitmapDrawable bd = new BitmapDrawable(bitmap);
				processInfo.icon = bd;

				// 名称 和 包名是一样的
				processInfo.processName = processInfo.packageName;
				// C进程可以看成是系统进程
				processInfo.isSystemProcress = true;
			}

		}

		return datas;
	}

}
