package zz.itcast.mobilesafez19.fragment;

import java.util.ArrayList;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.bean.AppInfo;
import zz.itcast.mobilesafez19.db.AppLockDao;
import zz.itcast.mobilesafez19.utils.AppUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LockedFragment extends Fragment{

	private TextView tvDesc;
	
	private ListView listView;
	
	private AppLockDao lockDao ; // 当涉及到 上下文时，变量一般都在 onCreate 方法中创建
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// getActivity() 是当前fragment 的宿主activity
//		TextView tv = new TextView(getActivity());
//		tv.setText("未加锁页面");
		
		View view = inflater.inflate(R.layout.fragment_locked, null);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc_fragment);
		listView = (ListView) view.findViewById(R.id.listView);
		
		proDlg = new ProgressDialog(getActivity());
		proDlg.setMessage("正在加载，请稍候...");
		
		lockDao = AppLockDao.getInstance(getActivity());
		
		fillData();
		
		
		return view;
	}

	private ProgressDialog proDlg;
	
	/**
	 * 所有应用信息
	 */
//	private ArrayList<AppInfo> allApps;
	/**
	 * 已加锁的应用信息
	 */
	private ArrayList<AppInfo> lockedApps;
	
	private void fillData() {
		proDlg.show();
		
		new Thread(){
			public void run() {
				// 加载数据
				ArrayList<AppInfo> allApps = AppUtils.getAllApps(getActivity());
				lockedApps = new ArrayList<AppInfo>();
				
				for(AppInfo info : allApps){
					if(lockDao.isLockedApp(info.packageName)){
						// 已锁定的
						lockedApps.add(info);
					}else{
						// 未加锁的
					}
					
				}
				
				// 更新页面
				handler.sendEmptyMessage(FLUSH_UI);
				
			};
		}.start();
		
	}
	
	private final int FLUSH_UI = 100;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case FLUSH_UI:// 更新页面
				
				adapter = new MyAdapter();
				listView.setAdapter(adapter);
				
				proDlg.dismiss();
				break;
			}
			
		};
	};
	
	private class ViewHolder{

		public ImageView ivIcon;
		public TextView appName;
		public ImageView ivlock;
	}
	
	private MyAdapter adapter;
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			tvDesc.setText("已加锁应用:"+lockedApps.size()+"个");
			
			return lockedApps.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder vh;
			if(convertView == null){
				view = View.inflate(getActivity(), R.layout.list_item_locked, null);
				vh = new ViewHolder();
				
				// 找到子view 
				ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon_list_item);
				TextView appName = (TextView) view.findViewById(R.id.tv_app_name_list_item);
				ImageView ivlock = (ImageView) view.findViewById(R.id.iv_lock_list_item);
				// 打包
				vh.ivIcon = ivIcon;
				vh.appName = appName;
				vh.ivlock = ivlock;
				// 背包
				view.setTag(vh);
			}else{
				view = convertView;
				vh = (ViewHolder) view.getTag();
			}
			
			// 获得数据
			AppInfo appInfo = lockedApps.get(position);
			
			vh.ivIcon.setBackgroundDrawable(appInfo.icon);
			
			vh.appName.setText(appInfo.appName);
			
			vh.ivlock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 执行平移动画
					
					TranslateAnimation ta = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1,
							Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF, 0);
					ta.setDuration(300); // 时间
					
//					ta.setFillAfter(true); // 动画执行完以后，保持完成的状态
					
					View parent = (View) v.getParent(); // getParent() 获得父view 即，当前条目对应的view 
					
					parent.startAnimation(ta);
					
					
					ta.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {}
						
						@Override
						public void onAnimationRepeat(Animation animation) {}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// 当动画结束时，
							// 添加至未加锁
							
							String packageName = lockedApps.get(position).packageName; // 获得当前应用的包名
							lockDao.deleteAppLock(packageName);
							
							// 从当前列表中删除
							lockedApps.remove(position);
							notifyDataSetChanged();
						}
					});
					
				}
			});
			
			TranslateAnimation ta = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,Animation.RELATIVE_TO_SELF, 0);
			ta.setDuration(300);
			view.startAnimation(ta);
			
			return view; // 如果返回 null 会报空指针，而且 异常信息中，只有系统代码，没有我们的代码
		}
		
	}
	
	
	
	
	
}
