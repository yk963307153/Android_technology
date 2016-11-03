package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.AppUtils.OnSMSListener;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class AToolsActivity extends BaseActivity implements OnClickListener {

	private LinearLayout llNumberLocation, llBackupSMS, llRecoverySMS,llappLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_atools);
		llNumberLocation = (LinearLayout) findViewById(R.id.ll_number_location);
		llBackupSMS = (LinearLayout) findViewById(R.id.ll_backup_sms);
		llRecoverySMS = (LinearLayout) findViewById(R.id.ll_recovery_sms);
		llappLock = (LinearLayout) findViewById(R.id.ll_app_lock);

		llappLock.setOnClickListener(this);
		llNumberLocation.setOnClickListener(this);
		llBackupSMS.setOnClickListener(this);
		llRecoverySMS.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_number_location:

			jumpToActivity(NumberLocationActivity.class);

			break;

		case R.id.ll_backup_sms:

			// 备份短信 将系统的短信 写到一个文件上 sd卡 xml描述
			// 写XMl 序列化
			// 耗时操作

			final ProgressDialog pd = new ProgressDialog(act);

			// 水平进度样式
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("备份中...");
			pd.show();
			new Thread() {
				public void run() {
					AppUtils.backupSms(act, new OnSMSListener() {
						// 当拿到每个进度
						@Override
						public void onGetProgress(int progress) {
							// TODO Auto-generated method stub
							pd.setProgress(progress);
						}

						// 当拿到所有大小
						@Override
						public void onGetAllCount(int allCount) {
							// TODO Auto-generated method stub
							pd.setMax(allCount);
						}
					});

					pd.dismiss();
				}
			}.start();

			break;

		case R.id.ll_recovery_sms:
			// 从文件上读取短信 写入到系统
			final ProgressDialog pd2 = new ProgressDialog(act);

			// 水平进度样式
			pd2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd2.setMessage("恢复中...");
			pd2.show();

			new Thread() {
				public void run() {
					AppUtils.recoverySms(act, new OnSMSListener() {

						@Override
						public void onGetProgress(int progress) {
							pd2.setProgress(progress);

						}

						@Override
						public void onGetAllCount(int allCount) {
							// TODO Auto-generated method stub
							pd2.setMax(allCount);
						}
					});

					pd2.dismiss();
				}
			}.start();

			break;
		case R.id.ll_app_lock:// 程序锁
			
			jumpToActivity(AppLockActivity.class);
			
			break;

		default:
			break;
		}

	}

}
