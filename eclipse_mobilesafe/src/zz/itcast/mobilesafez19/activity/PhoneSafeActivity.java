package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.AppUtils;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhoneSafeActivity extends BaseActivity {

	private TextView tvSafeNumber;
	private ImageView ivProtectState;
	private TextView tvReEntrySetting;
	private LinearLayout llBtn;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_phone_safe);

		tvSafeNumber = (TextView) findViewById(R.id.tv_safe_number);
		tvReEntrySetting = (TextView) findViewById(R.id.tv_reentry_setting);
		ivProtectState = (ImageView) findViewById(R.id.iv_open_state);
		llBtn = (LinearLayout) findViewById(R.id.ll_btn_container);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		// 安全号码状态回显
		String safeNumber = sp.getString(MyResource.KEY_SAFE_NUMBER, "");
		tvSafeNumber.setText(safeNumber);
		// 保护状态回显
		boolean isOpen = sp.getBoolean(MyResource.KEY_OPEN_PROTECT, false);
		ivProtectState.setImageResource(isOpen ? R.drawable.lock
				: R.drawable.unlock);

		// 标题状态回显
		String title = sp.getString(MyResource.KEY_MODULE_NAME, "手机防盗");
		tvTitle.setText(title);

		// 重新计入设置向导
		tvReEntrySetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpToActivityAndFinish(Setting1Activity.class);
			}
		});
	}

	// 监听一个键的点击

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 说明点击的是Menu
			// 判断当前菜单是否是显示状态
			if (llBtn.isShown()) {
				// 如果显示 就消失
				Animation animation = AnimationUtils.loadAnimation(act,
						R.anim.menu_out);

				llBtn.startAnimation(animation);
				llBtn.setVisibility(View.GONE);

			} else {
				// 如果消失就显示

				Animation animation = AnimationUtils.loadAnimation(act,
						R.anim.menu_in);

				llBtn.startAnimation(animation);
				llBtn.setVisibility(View.VISIBLE);

			}

			// 返回true代表事件到此为止 不再 向下传递 我拦截了按钮的点击事件
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void changeModuleName(View view) {
		// 弹出一个对话框
		AlertDialog.Builder builder = new Builder(act);
		builder.setTitle("修改名称");
		// 用系统自带的 系统会自动帮你取消

		// 自定义中间的部分

		final EditText et = new EditText(act);
		et.setHint("请输入新的名称");
		// 系统只会添加中间的部分
		builder.setView(et);

		builder.setNegativeButton("取消", null);
		// 确定
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 拿到输入的名称
				String name = et.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					AppUtils.showToast("不能为空", act);
					return;
				}
				// 存入sp
				setStringValue(MyResource.KEY_MODULE_NAME, name);
				// 修改标题
				tvTitle.setText(name);

			}
		});
		// 显示出来
		builder.show();

	}

}
