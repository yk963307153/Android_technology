package zz.itcast.mobilesafez19.activity;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.ProcessInfo;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ProcessManagerActivity extends BaseActivity implements
		OnItemClickListener {

	private ArrayList<ProcessInfo> allProcess;
	private ArrayList<ProcessInfo> userProcess;
	private ArrayList<ProcessInfo> systemProcess;

	private ProcessAdapter adapter;
	private TextView tvProcessCount, tvMemoryInfo;
	private ListView lvProcessList;
	private ActivityManager am;
	private ProgressDialog pd;

	private int runningProcessCount = 0;
	private MemoryInfo meminfo;
	private long availMemByte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_process_manager);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		tvProcessCount = (TextView) findViewById(R.id.tv_process_count);
		tvMemoryInfo = (TextView) findViewById(R.id.tv_memory_info);

		lvProcessList = (ListView) findViewById(R.id.lv_process_list);

		// 初始化头信息
		initProcessInfo();

		// 初始化列表
		fillData();

		// 给ListView设置条目点击事件
		lvProcessList.setOnItemClickListener(this);

	}

	private void fillData() {
		pd = new ProgressDialog(act);
		pd.setMessage("加载中");
		pd.show();

		// 加载数据 子线程
		new Thread() {
			public void run() {
				allProcess = AppUtils.getAllProcess(act);

				if (allProcess != null && allProcess.size() > 0) {
					userProcess = new ArrayList<ProcessInfo>();
					systemProcess = new ArrayList<ProcessInfo>();
					for (ProcessInfo info : allProcess) {
						if (info.isSystemProcress) {
							systemProcess.add(info);
						} else {
							userProcess.add(info);
						}
					}
				}

				// 刷新UI
				flushUI();
			}

		}.start();

	}

	private void flushUI() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 设置数据适配器
				adapter = new ProcessAdapter();
				lvProcessList.setAdapter(adapter);
				pd.dismiss();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 更新UI

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}

	public void allSelect(View view) {
		// 将所有的都勾选 因为集合中的对象都是固定的 所以直接遍历总集合即可
		for (ProcessInfo info : allProcess) {
			if (!info.packageName.equals(getPackageName())) {

				info.isChecked = true;
			} else {
				info.isChecked = false;
			}
		}

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}

	public void allNotSelect(View view) {
		for (ProcessInfo info : allProcess) {
			info.isChecked = false;
		}

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public void reverseSelect(View view) {
		for (ProcessInfo info : allProcess) {
			if (!info.packageName.equals(getPackageName())) {

				info.isChecked = !info.isChecked;
			} else {
				info.isChecked = false;
			}
		}

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	public void clear(View view) {
		// 1.前台 2.可视 3.服务 4.后台 5.空进程
		long totalClearMemory = 0;
		int totalClearCount = 0;
		for (ProcessInfo info : allProcess) {
			if (info.isChecked
					&& (systemProcess.contains(info) || userProcess
							.contains(info))) {
				try {
					totalClearCount++;
					totalClearMemory += info.memory;
					if (info.isSystemProcress) {
						systemProcess.remove(info);
					} else {
						userProcess.remove(info);
					}
					// 同样不认识C进程
					am.killBackgroundProcesses(info.packageName);

				} catch (Exception e) {

				}
			}

		}

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 更新标题头的进程信息
		runningProcessCount = runningProcessCount - totalClearCount;
		availMemByte = availMemByte + totalClearMemory;
		tvProcessCount.setText("进程个数：" + runningProcessCount + "个");
		tvMemoryInfo.setText("可用内存/总内存："
				+ Formatter.formatFileSize(act, availMemByte) + "/"
				+ Formatter.formatFileSize(act, meminfo.totalMem));

		AppUtils.showToast("这次一共清理了" + totalClearCount + "个进程,一共释放了"
				+ Formatter.formatFileSize(act, totalClearMemory), act);

	}

	public void setting(View view) {

		jumpToActivity(ProcessManagerSettingActivity.class);

	}

	private class ProcessAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			// 获取sp的值

			boolean isShow = sp.getBoolean(MyResource.KEY_SHOW_SYSTEM, true);

			if (isShow) {
				int count = userProcess.size() + systemProcess.size();
				tvProcessCount.setText("进程个数:" + count + "个");

				return count;

			} else {
				int count = userProcess.size();
				tvProcessCount.setText("进程个数:" + count + "个");
				return count;
			}
		}

		// 1.复用历史对象 2。viewHolder 3.有if 必else
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(act, R.layout.item_process_layout, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) view
						.findViewById(R.id.iv_process_icon);
				holder.tvName = (TextView) view
						.findViewById(R.id.tv_process_name);
				holder.tvMemory = (TextView) view
						.findViewById(R.id.tv_process_memory);
				holder.tvTitle = (TextView) view
						.findViewById(R.id.tv_process_title);
				holder.cbChecked = (CheckBox) view
						.findViewById(R.id.cb_process_checked);
				view.setTag(holder);

			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			// 获取bean对象
			// ProcessInfo info = allProcess.get(position);
			ProcessInfo info = null;
			if (position >= userProcess.size()) {
				// 系统
				info = systemProcess.get(position - userProcess.size());
			} else {

				// 用户
				info = userProcess.get(position);
			}

			holder.ivIcon.setImageDrawable(info.icon);
			holder.tvName.setText(info.processName);
			holder.tvMemory.setText("占用内存："
					+ Formatter.formatFileSize(act, info.memory));

			if (position == 0) {
				holder.tvTitle.setText("用户进程");
				holder.tvTitle.setVisibility(View.VISIBLE);
			} else if (position == userProcess.size()) {
				holder.tvTitle.setVisibility(View.VISIBLE);
				holder.tvTitle.setText("系统进程");
			} else {
				holder.tvTitle.setVisibility(View.GONE);
			}

			// 处理checkBox
			// if (info.isChecked) {
			// holder.cbChecked.setChecked(true);
			// } else {
			// holder.cbChecked.setChecked(false);
			// }

			holder.cbChecked.setChecked(info.isChecked);
			// 如果是我们自己的应用 那么就不现实勾选框
			if (info.packageName.equals(getPackageName())) {
				holder.cbChecked.setVisibility(View.GONE);
			} else {
				holder.cbChecked.setVisibility(View.VISIBLE);
			}

			return view;
		}

		@Override
		public Object getItem(int position) {
			ProcessInfo info = null;
			if (position >= userProcess.size()) {
				info = systemProcess.get(position - userProcess.size());
			} else {
				info = userProcess.get(position);
			}

			return info;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private void initProcessInfo() {

		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		runningProcessCount = runningAppProcesses.size();

		tvProcessCount.append(runningProcessCount + "个");
		// 内存使用情况 获取正在运行的一些信息
		// 准备一个盒子 将盒子给它 系统将内存 信息放入盒子中
		meminfo = new MemoryInfo();
		// get开头 返回值没有 这种信息一般从参数中获取
		am.getMemoryInfo(meminfo);
		// 获取可用内存信息
		availMemByte = meminfo.availMem;
		// 总内存信息 单位是字节
		long totalMemByte = meminfo.totalMem;

		// linux 文件系统 I/o操作可以在低版本手机上 获取到内存大小
		// /proc/meminfo 文件中记录了当前手机的内存信息
		String availMemStr = Formatter.formatFileSize(act, availMemByte);
		String totalMemStr = Formatter.formatFileSize(act, totalMemByte);

		tvMemoryInfo.append(availMemStr + "/" + totalMemStr);
	}

	private class ViewHolder {

		public ImageView ivIcon;
		public TextView tvName;
		public TextView tvTitle;
		public TextView tvMemory;
		public CheckBox cbChecked;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 状态跟之前不一样
		ProcessInfo info = (ProcessInfo) lvProcessList
				.getItemAtPosition(position);
		// if (info.isChecked) {
		// info.isChecked = false;
		// } else {
		// info.isChecked = true;
		// }
		// 将状态颠倒
		if (info.packageName.equals(getPackageName())) {
			return;
		}
		info.isChecked = !info.isChecked;

		// 刷新UI
		if (adapter != null) {

			adapter.notifyDataSetChanged();
		}

	}

}
