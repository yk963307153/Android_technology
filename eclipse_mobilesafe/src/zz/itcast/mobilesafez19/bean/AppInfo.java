package zz.itcast.mobilesafez19.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {

	// 应用的封装描述

	// 对图片笼统的封装
	public Drawable icon; // 描述图片 Bitmap（位图 记录了每一个像素点的信息）

	public String appName;// 应用名称

	public String appSize; // 应用大小

	public boolean isSystemApp; // 是否是系统应用

	public String packageName; // 包名

	public boolean isSaveInSD;// 是否安装在SD卡上
	
	public String apkPath;// 应用的安装目录
}
