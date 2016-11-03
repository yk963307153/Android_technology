package zz.itcast.mobilesafez19.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import zz.itcast.mobilesafez19.bean.BlackNumber;

public class BlackNumberDao {

	/**
	 * 当一个类中的所有方法 用哪个对象调用 结果都是一样的 这个时候可以考虑做成单例模式 这样可以节省内存
	 * 
	 * 1.构造方法私有化 2.提供一个静态方法 返回自己 3.定义一个静态属性 用于返回
	 * 
	 * 懒汉 线程安全的问题
	 * 
	 * 饿汉
	 * 
	 */

	private BlackNumberDBHelper helper;

	private static BlackNumberDao dao;

	private static final String TABLE_NAME = "black";

	private BlackNumberDao(Context context) {
		helper = new BlackNumberDBHelper(context);
	}

	// 懒汉
	public synchronized static BlackNumberDao getInstance(Context context) {
		if (dao == null) {
			dao = new BlackNumberDao(context);
		}
		return dao;
	}

	/**
	 * 增加一条黑名单
	 * 
	 * @param number
	 *            号码
	 * @param mode
	 *            拦截模式 1电话 2短息 3都拦截
	 */
	// SQLiteOpenHelper // 管理数据库创建和更新的
	// SqliteDatabase // 操作 增删改查
	public boolean add(String number, int mode) {

		SQLiteDatabase database = helper.getWritableDatabase();
		// hashmap
		ContentValues values = new ContentValues();

		values.put("number", number);
		values.put("mode", mode);

		long insert = database.insert(TABLE_NAME, null, values);
		// 记住要关上
		database.close();
		if (insert != -1) {
			return true;
		} else {
			return false;
		}

	}

	public void delete(String number) {

		SQLiteDatabase database = helper.getWritableDatabase();

		database.delete(TABLE_NAME, "number = ?", new String[] { number });

		database.close();

	}

	public void update(String number, int newMode) {
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newMode);

		database.update(TABLE_NAME, values, "number = ?",
				new String[] { number });

		database.close();

	}

	public ArrayList<BlackNumber> getAllBlackNumbers() {
		ArrayList<BlackNumber> datas = new ArrayList<BlackNumber>();
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query(TABLE_NAME, new String[] { "number",
				"mode" }, null, null, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				BlackNumber bn = new BlackNumber();
				bn.number = cursor.getString(0);
				bn.mode = cursor.getInt(1);

				datas.add(bn);

			}
			cursor.close();
		}

		database.close();
		SystemClock.sleep(1500);
		return datas;
	}

	/**
	 * 获取到指定页码的数据
	 * 
	 * @param currentPageCount
	 *            指定的页码数 0代表第一页 1
	 * @param pageSize
	 *            每页有几个
	 * @return
	 */
	public ArrayList<BlackNumber> getCurrentPageNumbers(int currentPageCount,
			int pageSize) {
		ArrayList<BlackNumber> datas = new ArrayList<BlackNumber>();
		SQLiteDatabase database = helper.getReadableDatabase();
		// 以_Id这一列降序排列
		Cursor cursor = database
				.rawQuery(
						"select number,mode from black order by _id desc limit ? offset ?",
						new String[] { pageSize + "",
								(currentPageCount * pageSize) + "" });

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				BlackNumber bn = new BlackNumber();
				bn.number = cursor.getString(0);
				bn.mode = cursor.getInt(1);

				datas.add(bn);

			}
			cursor.close();
		}

		database.close();
		SystemClock.sleep(1000);
		return datas;
	}

	public int getAllCount() {
		int count = 0;
		SQLiteDatabase database = helper.getReadableDatabase();

		// 这个语句只是为了拿总个数 一行一列
		Cursor cursor = database.rawQuery("select count(*) from black", null);
		if (cursor != null && cursor.moveToNext()) {
			count = cursor.getInt(0);
			cursor.close();
		}

		database.close();
		return count;
	}

	public int getModeFromNumber(String number) {
		int mode = 0;
		SQLiteDatabase database = helper.getReadableDatabase();

		Cursor cursor = database.query(TABLE_NAME, new String[] { "mode" },
				"number = ?", new String[] { number }, null, null, null);
		if (cursor != null && cursor.moveToNext()) {
			mode = cursor.getInt(0);
			cursor.close();
		}

		database.close();
		return mode;
	}

}
