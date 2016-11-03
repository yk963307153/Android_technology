package zz.itcast.mobilesafez19.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.app.Application;
import android.os.Build;
import android.os.Environment;

/**
 * 
 * 我们在此类中常干二件事
 * 1- 经常在此作一些初始化的工作
 * 2- 做为全局共有的一个类 , 来传递对象
 */
public class MyApp extends Application implements UncaughtExceptionHandler{

	/**
	 * leo 用的缓存对象
	 */
	public Object leoCache;
	
	
	@Override
	/**
	 * 会在所有的activity的onCreate 方法执行之前执行 
	 */
	public void onCreate() {
		super.onCreate();
		// 经常在此作一些初始化的工作
		
		Thread mainThread = Thread.currentThread();
		
		// 设置未捕获异常的统一处理 ,在开发阶段，应该让所有的异常都崩出来，在上线的时候，再打开该开关
//		mainThread.setUncaughtExceptionHandler(this); 
		
		
	}

	@Override
	// 当系统发生异常，并且没有try 时，调用此方法 
	public void uncaughtException(Thread thread, Throwable ex) {
		
		System.out.println("系统发生了异常，但是被哥给抓着了!");
		
		// 当此方法执行的时候，说明系统崩了。
		// 此时，该应用，必死无疑，唯一能干的事，就是留个遗言，我为啥崩了
		

		try {
			// 将出错日志写入文件
			String errorLogPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/error_log_z19.log";
			PrintStream err = new PrintStream(new File(errorLogPath));
			
			//  将手机本身的信息写入日志
			// 用反射的方式将所有的成员变量全部写入日志

			// 获得字节码
			Class clazz = Class.forName("android.os.Build");
			
			Field[] fields = clazz.getFields(); // 获得所有的成变量
			for(Field field : fields){
				String name = field.getName(); // 获得变量的名称
				Object value = field.get(null); // 获得变量的值
				err.println(name+" : "+value);
			}
			err.println("==========我是华丽丽的分隔线====================");
			// 写入出错日志
			ex.printStackTrace(err);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// 早死早超生
		android.os.Process.killProcess(android.os.Process.myPid()); // 杀死当前进程
	}
	
	
	
}
