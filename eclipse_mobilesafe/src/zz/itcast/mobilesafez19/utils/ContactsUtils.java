package zz.itcast.mobilesafez19.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;

public class ContactsUtils {

	private static Uri uri = Uri
			.parse("content://com.android.contacts/raw_contacts");
	private static Uri dataUri = Uri
			.parse("content://com.android.contacts/data");

	// 联系人 一共有三张表 raw_contacts(存储有多少个联系人 以及每个人的id) data(存放具体的信息 电话 邮箱等)
	// mimetype(表明对应数据类型的id是什么意思)
	public static ArrayList<Contact> getAllContacts(Context context) {
		ArrayList<Contact> datas = new ArrayList<Contact>();
		Contact con = null;
		// 1.首先查询raw_contacts表 去获取contact_id字段

		Cursor cursor = context.getContentResolver().query(uri,
				new String[] { "contact_id" }, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String id = cursor.getString(0);
				if (TextUtils.isEmpty(id)) {
					// 如果id为null 这次循环直接跳过
					continue;
				}
				System.out.println("当前联系人id:" + id);
				con = new Contact();
				// 在赋值完毕之后 再去添加对象

				// 2.拿着contact_id的值 作为筛选条件去data表中查询 获取到当前这一个人的所有信息
				// 打印出所有的列名 我们发现没有mimetype_id 但是有mimetype
				Cursor dataCursor = context.getContentResolver().query(dataUri,
						new String[] { "mimetype", "data1" },
						"raw_contact_id = ?", new String[] { id }, null);
				if (dataCursor != null && dataCursor.getCount() > 0) {
					while (dataCursor.moveToNext()) {
						// 根据数据类型去判断
						String mimetype = dataCursor.getString(0);
						String data = dataCursor.getString(1);
						// 3.再根据信息的类型确定是姓名还是电话等等 分别存入对象对应的字段中
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							// 说明是一个电话
							con.number = data;
						} else if ("vnd.android.cursor.item/postal-address_v2"
								.equals(mimetype)) {
							con.address = data;
						} else if ("vnd.android.cursor.item/name"
								.equals(mimetype)) {
							con.name = data;
						} else if ("vnd.android.cursor.item/email_v2"
								.equals(mimetype)) {
							con.email = data;
						}

					}

					dataCursor.close();
					// 对象完全可以先添加再赋值 也没问题 因为操作的是同一个对象
					datas.add(con);
				}

			}

			// 最后将cursor关闭
			cursor.close();
		}

		// 4.添加到集合中
		SystemClock.sleep(1500);
		return datas;
	}

	public static class Contact {

		public String number;
		public String address;
		public String email;
		public String name;

		@Override
		public String toString() {
			return "Contact [number=" + number + ", address=" + address
					+ ", email=" + email + ", name=" + name + "]";
		}

	}

}
