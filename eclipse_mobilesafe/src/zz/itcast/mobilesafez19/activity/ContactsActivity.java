package zz.itcast.mobilesafez19.activity;

import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.ContactsUtils;
import zz.itcast.mobilesafez19.utils.ContactsUtils.Contact;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ContactsActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView lv;
	private ArrayList<Contact> allContacts;
	// private ProgressBar pb;
	private ImageView ivLoading;
	private AnimationDrawable ad;

	// TODO 这个界面的加载框改成一个帧动画 小人在跑

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_contacts);
		// pb = (ProgressBar) findViewById(R.id.pb_loading_contacts);
		ivLoading = (ImageView) findViewById(R.id.iv_loading);
		// 设置一个背景
		ivLoading.setBackgroundResource(R.drawable.running_man);
		ad = (AnimationDrawable) ivLoading.getBackground();
		ad.start();
		lv = (ListView) findViewById(R.id.lv_contacts);
		// 设置数据适配器

		// 获取所有的联系人 假设有很多人 有可能会阻塞 所以应该有可能产生阻塞的放到子线程中执行

		// 凡是ListView的数据都放在子线程中获取
		new Thread() {
			public void run() {

				allContacts = ContactsUtils
						.getAllContacts(ContactsActivity.this);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						lv.setAdapter(new ContactAdapter());
						// 让这个控件消失掉
						// pb.setVisibility(View.GONE);
						ad.stop();
						ivLoading.setVisibility(View.GONE);

					}
				});
			}
		}.start();

		// 设置条目点击事件
		lv.setOnItemClickListener(this);
	}

	private class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allContacts.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = View.inflate(ContactsActivity.this,
						R.layout.item_contact, null);
			} else {
				view = convertView;
			}

			// 去集合中根据位置拿到对应的bean对象
			Contact contact = allContacts.get(position);
			TextView tvName = (TextView) view
					.findViewById(R.id.tv_contact_name);
			TextView tvNumber = (TextView) view
					.findViewById(R.id.tv_contact_number);

			tvName.setText(contact.name);
			tvNumber.setText(contact.number);

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获取到当前点击的号码
		Contact contact = allContacts.get(position);
		String number = contact.number;
		// 回传数据
		Intent data = new Intent();
		data.putExtra("number", number);
		setResult(Setting3Activity.RESULT_FOR_CONTACTS, data);
		// 将自己关闭
		finish();

	}

}
