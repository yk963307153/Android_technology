package zz.itcast.mobilesafez19.utils;

import android.os.Environment;

/**
 * 用来存放一些公用的资源 比如 下载的路径
 * 
 * @author Administrator
 * 
 */
public class MyResource {

	// 我们将跟我们软件相关的所有的文件放在sd卡的同一个路径下
	private static final String ROOT = "mobilesafez19";

	// /mnt/sdcard/mobilesafez19
	public static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/" + ROOT;

	public static String sender = "";

	// 这个目录只跟下载的东西相关
	// /mnt/sdcard/mobilesafez19/download
	public static final String DOWNLOAD_PATH = ROOT_PATH + "/download";

	// 添加一些静态常量字符串
	public static final String SP_FILE_NAME = "config";

	// 我们还需要一个密码的键
	public static final String KEY_PSWD = "pswd";
	// 判断是否已经完成设置向导
	public static final String KEY_IS_FINISH_SETTING = "is_finish_setting";

	// 保存SIM卡的序列号
	public static final String KEY_SIM_ID = "sim_id";

	// 安全号码
	public static final String KEY_SAFE_NUMBER = "safe_number";

	// 防盗保护开启
	public static final String KEY_OPEN_PROTECT = "open_protect";

	// 模块名称
	public static final String KEY_MODULE_NAME = "module_name";

	// 自动更新
	public static final String KEY_AUTO_UPDATE = "auto_update";

	// 来电风格
	public static final String KEY_COMING_STYLE = "coming_style";

	// 来电显示框的位置 X
	public static final String KEY_COMING_X = "coming_x";
	// 来电显示框的位置 Y
	public static final String KEY_COMING_Y = "coming_y";

	// 是否显示系统进程
	public static final String KEY_SHOW_SYSTEM = "show_system";

}
