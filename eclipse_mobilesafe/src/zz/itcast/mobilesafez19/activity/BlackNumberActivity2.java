package zz.itcast.mobilesafez19.activity;

import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.BlackNumber;
import zz.itcast.mobilesafez19.db.BlackNumberDao;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BlackNumberActivity2 extends BaseActivity {

	private ListView lv;
	private ProgressBar pb;
	private TextView tvLoadingInfo;
	private LinearLayout llPbContainer;
	// private ArrayList<BlackNumber> allBlackNumbers;
	private BlackNumberDao dao;

	private int pageSize = 30;// 每页最多30个

	private ArrayList<BlackNumber> allPageNumber;

	private int currentPageCount = 0; // 当前页 从0开始

	private int allPageCount = 0;

	// 通过这个标记用来表示当前数据是否加载完
	private boolean isLoading;

	private BlackNumberAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number2);
		lv = (ListView) findViewById(R.id.lv_black);
		pb = (ProgressBar) findViewById(R.id.pb_loading_black);
		tvLoadingInfo = (TextView) findViewById(R.id.tv_loading_text);
		llPbContainer = (LinearLayout) findViewById(R.id.ll_pb_container);
		dao = BlackNumberDao.getInstance(act);
		fillData();
		// 需要知道ListView什么时候处于滑动状态
		// 滑动监听
		lv.setOnScrollListener(new MyScollListener());
		// 长按监听
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				showUpdateDialog(position);
				return false;
			}
		});

	}

	private void showUpdateDialog(int position) {

		final BlackNumber bn = allPageNumber.get(position);

		AlertDialog.Builder builder = new Builder(act);
		View view = View
				.inflate(act, R.layout.dialog_update_black_number, null);
		final TextView tvNumber = (TextView) view
				.findViewById(R.id.tv_show_black);
		final CheckBox cbPhone = (CheckBox) view.findViewById(R.id.cb_phone);
		final CheckBox cbSMS = (CheckBox) view.findViewById(R.id.cb_sms);
		// 先做一个数据的回显
		tvNumber.setText(bn.number);
		if (bn.mode == 1) {
			cbPhone.setChecked(true);
			cbSMS.setChecked(false);
		} else if (bn.mode == 2) {
			cbPhone.setChecked(false);
			cbSMS.setChecked(true);
		} else if (bn.mode == 3) {
			cbPhone.setChecked(true);
			cbSMS.setChecked(true);
		}

		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		builder.setView(view);
		// 创建一个对话框
		final AlertDialog dialog = builder.create();

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int mode = 0;
				if (cbPhone.isChecked() && cbSMS.isChecked()) {
					mode = 3;
				} else if (cbPhone.isChecked() && !cbSMS.isChecked()) {
					mode = 1;
				} else if (!cbPhone.isChecked() && cbSMS.isChecked()) {
					mode = 2;
				}
				if (mode == 0) {
					AppUtils.showToast("请至少拦截一项", act);
					return;
				}
				// 修改黑名单 数据库
				dao.update(bn.number, mode);
				// 修改集合中的对象
				bn.mode = mode;
				// 更新UI
				adapter.notifyDataSetChanged();

				dialog.dismiss();

			}
		});

		dialog.show();

	}

	public void add(View view) {
		showAddBlackNumberDialog();
	}

	private void showAddBlackNumberDialog() {
		AlertDialog.Builder builder = new Builder(act);
		View view = View.inflate(act, R.layout.dialog_add_black_number, null);
		final EditText et = (EditText) view.findViewById(R.id.et_add_black);
		final CheckBox cbPhone = (CheckBox) view.findViewById(R.id.cb_phone);
		final CheckBox cbSMS = (CheckBox) view.findViewById(R.id.cb_sms);
		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		builder.setView(view);
		// 创建一个对话框
		final AlertDialog dialog = builder.create();

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = et.getText().toString().trim();
				if (TextUtils.isEmpty(number)) {
					AppUtils.showToast("请输入号码", act);
					return;
				}
				int mode = 0;
				if (cbPhone.isChecked() && cbSMS.isChecked()) {
					mode = 3;
				} else if (cbPhone.isChecked() && !cbSMS.isChecked()) {
					mode = 1;
				} else if (!cbPhone.isChecked() && cbSMS.isChecked()) {
					mode = 2;
				}
				if (mode == 0) {
					AppUtils.showToast("请至少拦截一项", act);
					return;
				}

				// 添加黑名单
				if (dao.add(number, mode)) {
					// 添加到集合
					BlackNumber bn = new BlackNumber();
					bn.number = number;
					bn.mode = mode;
					// 添加到集合的最开头处
					allPageNumber.add(0, bn);
					// 更新UI
					// 这里在第一次没有数据添加时 报出空指针异常
					// adapter = null 原因 是集合没有数据 就不会给它new的机会
					// TODO 自己解决这个问题

					if (adapter == null) {
						adapter = new BlackNumberAdapter();
						lv.setAdapter(adapter);
						lv.setVisibility(View.VISIBLE);
						llPbContainer.setVisibility(View.GONE);

					} else {

						adapter.notifyDataSetChanged();
					}

					dialog.dismiss();

				} else {
					AppUtils.showToast("黑名单已存在", act);
				}

			}
		});

		dialog.show();

	}

	private class MyScollListener implements OnScrollListener {

		// 当滑动状态发生改变
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				System.out.println("停止了");

				// 当滑动停下来 判断一下最后一个可见条目是不是当前集合中最后一个 如果是 认为滑动到底部
				if (lv.getLastVisiblePosition() == allPageNumber.size() - 1) {
					// 加载下一页数据
					if (currentPageCount == allPageCount - 1) {
						// 已经最后一页
						AppUtils.showToast("没有更多数据了", act);
						return;
					}
					currentPageCount++;
					fillData();
				}

				break;

			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				System.out.println("（手指没离开）滑动中");
				break;

			case OnScrollListener.SCROLL_STATE_FLING:
				System.out.println("手指松开（惯性飞划）");
				break;

			default:
				break;
			}

		}

		// 当滑动中
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// System.out.println("手指处于滑动");

		}

	}

	private void fillData() {
		// 去加载黑名单数据
		// 数据的获取应该放在子线程中 网络 数据库
		isLoading = true;
		llPbContainer.setVisibility(View.VISIBLE);

		new Thread() {

			public void run() {
				try {

					// 先获取总个数 计算总页数

					int allCount = dao.getAllCount();
					// 根据是否能够整除来计算总页数
					if (allCount % pageSize == 0) {
						// 整除
						allPageCount = allCount / pageSize;
					} else {
						// 非整除
						allPageCount = allCount / pageSize + 1;
					}

					// 先判断是否是第一次加载 搞一个集合
					if (allPageNumber == null) {
						// 说明是第一次加载
						allPageNumber = dao.getCurrentPageNumbers(
								currentPageCount, pageSize);
					} else {

						// 加载之后的数据 累加给allPageNumber集合

						ArrayList<BlackNumber> tempList = dao
								.getCurrentPageNumbers(currentPageCount,
										pageSize);

						// 将tempList追加到allPageNumber之后
						allPageNumber.addAll(tempList);
					}

				} catch (Exception e) {
					// 偶现
					e.printStackTrace();
				} finally {
					// 最终一定会执行的代码
					flushUI();
					isLoading = false;
				}
			}
		}.start();

	}

	private void flushUI() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (allPageNumber != null && allPageNumber.size() > 0) {
					// 有数据
					// 加载框消失 ListView显示
					// 如果是第一次加载 设置数据适配器

					if (adapter == null) {
						adapter = new BlackNumberAdapter();
						lv.setAdapter(adapter);

					} else {
						// 保持现在List原有的位置
						// 如果已经有了 直接更新
						adapter.notifyDataSetChanged();
					}
					lv.setVisibility(View.VISIBLE);
					llPbContainer.setVisibility(View.GONE);
				} else {
					// 没数据
					// ProgressBar消失 文字内容改变
					pb.setVisibility(View.GONE);
					lv.setVisibility(View.GONE);
					tvLoadingInfo.setText("您当前没有没有黑名单");
				}
			}
		});
	}

	private class BlackNumberAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allPageNumber.size();
		}

		/*
		 * 
		 * 
		 * ListView 的一般优化策略： 1.复用历史对象 2.减少FindViewById的次数 ViewHolder
		 * 
		 * findVIewById是比较消耗性能的操作 原则：已经找到过的控件不在找第二次 找一个对象存起来
		 * 
		 * 3.ListView的高度最好固定下来 * getView方法什么时候调用
		 * 
		 * 第一次加载时调用 滑动期间每当有下个条目出现时也会调用
		 * 
		 * 调几次 fill 1倍 wrap 3-4倍 1-3被做试验 最后一次显示出来
		 * 
		 * 牺牲（内存）换时间
		 * 
		 * 牺牲时间换（内存）
		 */

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			// 让holder的个数和View的个数一致
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(act, R.layout.item_black_number_layout,
						null);
				// 准备一个书包
				holder = new ViewHolder();
				TextView tvNumber = (TextView) view
						.findViewById(R.id.tv_black_number);
				TextView tvMode = (TextView) view
						.findViewById(R.id.tv_black_mode);
				ImageView ivDelete = (ImageView) view
						.findViewById(R.id.iv_black_delete);
				// 将包里存好东西
				holder.tvNumber = tvNumber;
				holder.tvMode = tvMode;
				holder.ivDelete = ivDelete;
				// 将背包背在身上
				view.setTag(holder);

			} else {
				view = convertView;
				// 卸下背包
				holder = (ViewHolder) view.getTag();
			}

			// 先根据位置拿到bean对象
			final BlackNumber blackNumber = allPageNumber.get(position);

			holder.tvNumber.setText(blackNumber.number);
			switch (blackNumber.mode) {
			case 1:
				holder.tvMode.setText("拦截电话");

				break;
			case 2:
				holder.tvMode.setText("拦截短信");
				break;
			case 3:
				holder.tvMode.setText("全部拦截");
				break;

			default:
				break;
			}

			// 处理删除按钮
			holder.ivDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 数据库删除
					dao.delete(blackNumber.number);
					// 集合删除
					allPageNumber.remove(blackNumber);
					// 更新UI
					adapter.notifyDataSetChanged();
				}
			});

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

	/*
	 * 创建一个内部类 用来将之前找到的控件 存下来 业内都起这个名字
	 */
	private class ViewHolder {
		public TextView tvNumber;
		public TextView tvMode;
		public ImageView ivDelete;
	}

}
