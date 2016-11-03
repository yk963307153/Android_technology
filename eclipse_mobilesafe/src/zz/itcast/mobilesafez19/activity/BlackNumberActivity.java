package zz.itcast.mobilesafez19.activity;

import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.BlackNumber;
import zz.itcast.mobilesafez19.db.BlackNumberDao;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BlackNumberActivity extends BaseActivity {

	private ListView lv;
	private ProgressBar pb;
	private TextView tvLoadingInfo;
	private LinearLayout llPbContainer;
	// private ArrayList<BlackNumber> allBlackNumbers;
	private BlackNumberDao dao;

	private int pageSize = 30;// 每页最多30个

	private ArrayList<BlackNumber> currentPageNumbers;

	private int currentPageCount = 0; // 当前页 从0开始

	private int allPageCount = 0;

	private EditText etInputPage;
	private TextView tvPageInfo;
	// 通过这个标记用来表示当前数据是否加载完 
	private boolean isLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		lv = (ListView) findViewById(R.id.lv_black);
		pb = (ProgressBar) findViewById(R.id.pb_loading_black);
		tvLoadingInfo = (TextView) findViewById(R.id.tv_loading_text);
		llPbContainer = (LinearLayout) findViewById(R.id.ll_pb_container);
		etInputPage = (EditText) findViewById(R.id.et_input_page);
		tvPageInfo = (TextView) findViewById(R.id.tv_page_info);
		dao = BlackNumberDao.getInstance(act);
		fillData();

	}

	private void fillData() {
		// 去加载黑名单数据
		// 数据的获取应该放在子线程中 网络 数据库
		isLoading = true;
		lv.setVisibility(View.GONE);
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

					currentPageNumbers = dao.getCurrentPageNumbers(
							currentPageCount, pageSize);
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

	
	
	public void prePage(View view) {
		if (isLoading) {
			AppUtils.showToast("你急啥呢，还没完呢", act);
			return;
		}

		// 先判断是否已经是第一页
		if (currentPageCount == 0) {
			AppUtils.showToast("已经是第一页", act);
			return;
		}

		currentPageCount--;
		fillData();
	}

	public void nextPage(View view) {
		if (isLoading) {
			AppUtils.showToast("你急啥呢，还没完呢", act);
			return;
		}
		if (currentPageCount == allPageCount - 1) {
			AppUtils.showToast("已经是最后一页", act);
			return;
		}

		currentPageCount++;
		fillData();

	}

	public void jumpToPage(View view) {
		if (isLoading) {
			AppUtils.showToast("你急啥呢，还没完呢", act);
			return;
		}
		// 获取到输入的页码数
		String page = etInputPage.getText().toString().trim();
		if (!TextUtils.isEmpty(page)) {
			int pageCount = Integer.parseInt(page);

			if (pageCount >= 1 && pageCount <= allPageCount) {
				currentPageCount = pageCount - 1;
				fillData();
			} else {
				AppUtils.showToast("请输入合适的范围", act);
			}

		}

	}

	private void flushUI() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (currentPageNumbers != null && currentPageNumbers.size() > 0) {
					// 有数据
					// 加载框消失 ListView显示
					lv.setAdapter(new BlackNumberAdapter());
					lv.setVisibility(View.VISIBLE);
					llPbContainer.setVisibility(View.GONE);
					// 显示当前页
					tvPageInfo.setText((currentPageCount + 1) + "/"
							+ allPageCount);
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
			return currentPageNumbers.size();
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
				// 将包里存好东西
				holder.tvNumber = tvNumber;
				holder.tvMode = tvMode;
				// 将背包背在身上
				view.setTag(holder);

			} else {
				view = convertView;
				// 卸下背包
				holder = (ViewHolder) view.getTag();
			}

			// 先根据位置拿到bean对象
			BlackNumber blackNumber = currentPageNumbers.get(position);

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
	}

}
