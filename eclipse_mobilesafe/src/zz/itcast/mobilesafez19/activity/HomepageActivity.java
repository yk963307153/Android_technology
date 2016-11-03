package zz.itcast.mobilesafez19.activity;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.app.MyApp;
import zz.itcast.mobilesafez19.bean.AppInfo;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MD5Utils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomepageActivity extends BaseActivity implements
		OnItemClickListener {

	/*
	 * 手机防盗
	 */
	private static final int PHONE_SAFE = 0;

	/*
	 * 黑名单
	 */
	private static final int BLACK_NUMBER = 1;

	/*
	 * 应用管理
	 */
	private static final int APP_MANAGER = 2;

	/*
	 * 进程管理
	 */
	private static final int PROCESS_MANAGER = 3;
	/*
	 * 高级工具
	 */

	private static final int ADVANCED_TOOLS = 7;
	/*
	 * 设置中心
	 */
	private static final int SETTING_CENTER = 8;

	private GridView gv;

	private boolean isPressedBackOnce = false;
	private long firstTime = 0;
	private long secondTime = 0;

	private String[] titles = new String[] { "手机防盗", "通讯卫士", "应用管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

	private int[] icons = new int[] { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	private GridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 初始化有米广告
		AdManager adm = AdManager.getInstance(this);// 获得广告管理器
		
		// 参数一 是开发者ID ，参数二，是应用密钥，参数三，如果是测试，写true，正式版写false
		adm.init("cf6bc0cc41993204", "1857e45c502127a1", false); // 初始化
		
		
		setContentView(R.layout.activity_homepage);
		
		
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);

		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);

		// 将广告条加入到布局中
		adLayout.addView(adView);
		
		
		// 先实例化SP对象
		gv = (GridView) findViewById(R.id.gv_grid);
		// 需要数据适配器
		adapter = new GridAdapter();

		gv.setAdapter(adapter);

		// 设置条目点击
		gv.setOnItemClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// gv.setAdapter(new GridAdapter());
		// 尽管直接设置Adapter 可以解决这个问题 但是系统不推荐这么做
		// 如果数据改变了 直接通知更新适配器 不用再重新创建新的适配器
		adapter.notifyDataSetChanged(); // 同样会触发 getCount getView

	}

	@Override
	public void onBackPressed() {

		if (isPressedBackOnce) {
			// 第二次点
			secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {
				// 第一次点击作废 现在还是第一次
				AppUtils.showToast("再点一次退出", this);
				isPressedBackOnce = true;
				firstTime = System.currentTimeMillis();
			} else {
				// 2秒之内点的第二次
				finish();
				isPressedBackOnce = false;
				firstTime = 0;
				secondTime = 0;
			}

		} else {
			// 第一次点击
			AppUtils.showToast("再点一次退出", this);
			isPressedBackOnce = true;
			firstTime = System.currentTimeMillis();
		}

	}

	private class GridAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles.length;
		}

		// 每个条目的样子
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// convertView 这里不用复用 因为我们只有9个条目 不需要进行上下滚动 不滚动就不会调用getView方法
			View view = View.inflate(HomepageActivity.this, R.layout.item_grid,
					null);
			TextView tvName = (TextView) view.findViewById(R.id.tv_name);
			ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
			tvName.setText(titles[position]);
			if (position == 0) {
				// 说明是第一个位置
				String text = sp.getString(MyResource.KEY_MODULE_NAME, "");
				if (!TextUtils.isEmpty(text)) {
					tvName.setText(text);
				}
			}
			ivIcon.setImageResource(icons[position]);
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
		switch (position) {
		case PHONE_SAFE:
			// 先判断是否已经设置过了密码
			String pswd = sp.getString(MyResource.KEY_PSWD, "");
			if (TextUtils.isEmpty(pswd)) {
				// 说明还没设置过密码
				// 应该弹出设置密码对话框
				showSettingPswdDialog();
			} else {
				// 弹出输入密码对话框
				showInputPswdDialog();
			}

			break;
		case SETTING_CENTER:

			jumpToActivity(SettingCenterActivtiy.class);

			break;

		case BLACK_NUMBER:

			jumpToActivity(BlackNumberActivity2.class);

			break;

		case APP_MANAGER:

			jumpToActivity(AppManagerActivity.class);

			break;

		case PROCESS_MANAGER:

			jumpToActivity(ProcessManagerActivity.class);

			break;

		case ADVANCED_TOOLS:

			jumpToActivity(AToolsActivity.class);

			break;
		case 5: // 手机杀毒
			
			jumpToActivity(AVActivity.class);
			
			break;
		case 6: // 缓存清理
			
			jumpToActivity(ClearCacheActivity.class);
			
			break;
		case 4: // 流量统计
			// 传递一个对象到流量统计页面
			AppInfo info = new AppInfo();
			
			MyApp app = (MyApp) getApplication(); // 获得MyApp 对象
			app.leoCache = info; // 将要传递的对象设置给临时变量 
			
			jumpToActivity(NetTrafficActivity.class);
			
			break;

		default:
			break;
		}

	}

	private void showSettingPswdDialog() {
		// 显示一个自定义样式的对话框
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setting_pswd_layout,
				null);
		final EditText et1 = (EditText) view.findViewById(R.id.et_setting_pswd);
		final EditText et2 = (EditText) view
				.findViewById(R.id.et_setting_pswd2);

		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		// 确定对话框的样子
		builder.setView(view);
		// 这个创建对话框的操作一定要放在setView之后
		final AlertDialog dialog = builder.create();
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 我们要关闭对话框 但是发现builder中没有cancle或者dismiss这样的方法
				dialog.dismiss();
			}
		});
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pswd1 = et1.getText().toString().trim();
				String pswd2 = et2.getText().toString().trim();

				if (TextUtils.isEmpty(pswd1) || TextUtils.isEmpty(pswd2)) {
					AppUtils.showToast("不能为空", HomepageActivity.this);
					return;
				}
				if (pswd1.equals(pswd2)) {
					// 将密码存储下来 使用封装的方法
					if (setStringValue(MyResource.KEY_PSWD,
							MD5Utils.getMD5(pswd1))) {

						AppUtils.showToast("密码保存成功", HomepageActivity.this);
						dialog.dismiss();
					} else {
						AppUtils.showToast("密码保存失败", HomepageActivity.this);
					}

				} else {
					AppUtils.showToast("两次密码不一致，请重新输入", HomepageActivity.this);
					et1.setText("");
					et2.setText("");
				}

			}
		});
		// 如果上方已经创建了对话框 那么保证显示的和消失的是同一个
		// 因为这个builder的show方法中又创建了一个

		dialog.show();

	}

	private void showInputPswdDialog() {

		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_input_pswd_layout, null);
		final EditText et = (EditText) view.findViewById(R.id.et_input_pswd);

		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);

		builder.setView(view);

		final AlertDialog dialog = builder.create();

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取到之前存的密码
				String savedPswd = sp.getString(MyResource.KEY_PSWD, "");
				// 再获取这次输入的密码
				String inputPswd = et.getText().toString().trim();
				// 密码正确后跳转页面
				// 对用户输入的内容做一次加密
				inputPswd = MD5Utils.getMD5(inputPswd);

				if (savedPswd.equals(inputPswd)) {
					// 密码正确
					AppUtils.showToast("密码正确", HomepageActivity.this);
					dialog.dismiss();

					// 判断是否已经完成了设置 通过sp判断
					boolean isFinish = sp.getBoolean(
							MyResource.KEY_IS_FINISH_SETTING, false);
					if (isFinish) {
						// 进入手机防盗页面 不再使用原始的方式 使用封装的方法
						// Intent intent = new Intent(HomepageActivity.this,
						// PhoneSafeActivity.class);
						// startActivity(intent);
						jumpToActivity(PhoneSafeActivity.class);

					} else {
						// 第一次进入设置向导页面
						// 不再使用原始的方式 使用封装的方法
						// Intent intent = new Intent(HomepageActivity.this,
						// Setting1Activity.class);
						// startActivity(intent);
						jumpToActivityAndFinish(Setting1Activity.class);
					}

				} else {
					// 密码错误
					AppUtils.showToast("密码错误", HomepageActivity.this);

					et.setText("");
				}

			}
		});

		dialog.show();

	}

}
