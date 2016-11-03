package zz.itcast.mobilesafez19.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockDbHelper extends SQLiteOpenHelper {

	public AppLockDbHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	// 创建数据库时调用
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table app_lock(_id integer primary key autoincrement,package_name varchar(40));");

	}

	@Override
	//数据库更新时，调用
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
