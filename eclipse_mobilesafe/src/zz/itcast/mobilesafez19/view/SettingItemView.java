package zz.itcast.mobilesafez19.view;

import zz.itcast.mobilesafez19.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

// 这个自定义控件 在构造方法中  一上来就决定自己这一生的样子
public class SettingItemView extends LinearLayout {

	private TextView tvTitle, tvDesc;
	private CheckBox cb;

	private String[] descs;

	// 通过XML布局文件 findViewById得到的
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

		// 获取到所有的属性信息

		// 获得属性的个数
		int attributeCount = attrs.getAttributeCount();
		System.out.println(attributeCount + "");
		// for (int i = 0; i < attributeCount; i++) {
		// // 分别获取到属性的键和 值
		// // 属性名字
		// String attributeName = attrs.getAttributeName(i);
		// // 属性值
		// String attributeValue = attrs.getAttributeValue(i);
		// System.out.println(attributeName + "=" + attributeValue);
		//
		// }

		// 可以获取具体的键和值

		// 获取标题
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/zz.itcast.mobilesafez19",
				"title");

		tvTitle.setText(title);
		
		// 获取描述信息
		String descsStr = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/zz.itcast.mobilesafez19",
				"descs");
		descs = descsStr.split("#");
		
	}

	// 自己new的
	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		View view = View.inflate(getContext(), R.layout.item_setting_layout,
				null);
		tvTitle = (TextView) view.findViewById(R.id.tv_setting_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_setting_desc);
		cb = (CheckBox) view.findViewById(R.id.cb_setting_checked);

		addView(view);

	}

	public boolean isChecked() {
		// TODO Auto-generated method stub
		return cb.isChecked();
	}

	public void setChecked(boolean b) {
		cb.setChecked(b);
		if (b) {
			tvDesc.setTextColor(Color.GREEN);
			// 肯定
			tvDesc.setText(descs[0]);
		} else {
			tvDesc.setTextColor(Color.RED);
			tvDesc.setText(descs[1]);
		}

	}

	public void setTitle(String title) {
		tvTitle.setText(title);

	}

	public void setDesc(String[] descs) {
		this.descs = descs;
	}

}
