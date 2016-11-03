package zz.itcast.mobilesafez19.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {

	private Context ctx;

	private AppLockDao(Context ctx){
		this.ctx = ctx;
		dbHelper = new AppLockDbHelper(ctx, "app_lock.db", 1);
	}
	
	private static AppLockDao instance;
	
	public static synchronized AppLockDao getInstance(Context ctx){
		if(instance == null){
			instance = new AppLockDao(ctx);
		}
		return instance;
	}
	
	private AppLockDbHelper dbHelper;
	
	private String table_app_lock = "app_lock";
	
	/**
	 * 定义一个指向程序锁数据库的 uri 
	 */
	Uri uri = Uri.parse("content://zz.itcast.mobilesafez19.applock.db");
	
	//// 添加工具方法
	
	// 添中程序锁
	public void addAppLock(String packageName){
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("package_name", packageName); ///  参数一是列的名称，参数二是该列的值
		
		db.insert(table_app_lock, null, values);// 只要保证 values 有内容，第二个参数可以忽略
		
		// 数据库发生变化时，发出通知
		ctx.getContentResolver().notifyChange(uri, null);
		
	}
	
	// 删除程锁
	public void deleteAppLock(String packageName){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(table_app_lock, " package_name = ? ", new String[]{packageName});
		
		// 数据库发生变化时，发出通知
		ctx.getContentResolver().notifyChange(uri, null);
		
	}
	
	
	/**
	 * 获得所有的应用的包名
	 * @return
	 */
	public List<String> getAllAppLock(){
		List<String> allAppLock = new ArrayList<String>();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		// 查询数据库返回的cursor 默认指向第一行的上一行
		Cursor cursor = db.query(table_app_lock, null, null, null, null, null, null);
		
		while(cursor.moveToNext()){
			String package_name = cursor.getString(cursor.getColumnIndex("package_name"));
			allAppLock.add(package_name);
		}
		cursor.close();
		
		return allAppLock;
	}
	
	/**
	 * 判断应用是否需要被锁定
	 * @param packageName 包名
	 * @return
	 */
	public boolean isLockedApp(String packageName){
		
		boolean isAppLock = false;;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(table_app_lock, null, " package_name = ? ",
				new String[]{packageName}, null, null, null);
		
		if(cursor.getCount() > 0 ){ // 找到了
			isAppLock = true;
		}else{
			// 没有找到
			isAppLock = false;
		}
		
		cursor.close();
		
//		db.close();
		
		return isAppLock;
	}
	
	
	
	
}
