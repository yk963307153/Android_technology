package zz.itcast.mobilesafez19.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AVUtils {

	/**
	 * 判断 md5 字符串是否是病毒文件的md5 字符串
	 * @param md5Str
	 * @return
	 */
	public static boolean isVirus(Context ctx,String md5Str){
		
		boolean isViurs = false;
		
		String path = ctx.getFilesDir().getAbsolutePath()+"/antivirus.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.query("datable", null, " md5 = ?", new String[]{md5Str}, null, null, null);
		
		if(cursor.getCount() > 0){ //找到了 ，是病毒
			isViurs = true;
		}
		cursor.close();
		db.close();
		
		return isViurs;
	}

	/**
	 * 获得当前病毒库的版本号
	 * @param ctx
	 * @return
	 */
	public static int getVersion(Context ctx) {
		
		String path = ctx.getFilesDir().getAbsolutePath()+"/antivirus.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.query("version", null, null, null, null, null, null);
		// 查询数据库返回的cursor 默认指向第一行的上一行
		cursor.moveToNext();
		
		int version = cursor.getInt(cursor.getColumnIndex("subcnt"));
		
		cursor.close();
		db.close();
		
		return version;
	}

	/**
	 * 更新版本号
	 * @param ctx
	 * @param version
	 */
	public static void updateVersion(Context ctx, int version) {
		String path = ctx.getFilesDir().getAbsolutePath()+"/antivirus.db";
		// 以读写的方式打开
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE); 
		
		ContentValues values = new ContentValues();
		values.put("subcnt", version);
		
		db.update("version", values, null, null);
		
		db.close();
		
	}

	/**
	 * 更新病毒文件
	 * @param ctx
	 * @param md5Str
	 * @param desc
	 */
	public static void updateVirusMd5(Context ctx, String md5Str, String desc) {
		String path = ctx.getFilesDir().getAbsolutePath()+"/antivirus.db";
		// 以读写的方式打开
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE); 
		
		ContentValues values = new ContentValues();
		
		values.put("md5", md5Str);
		values.put("type", 6);
		
		values.put("name", "Android.muma.a");
		values.put("desc", desc);
		
		db.insert("datable", null, values);
		
		
		
		
	}
}
