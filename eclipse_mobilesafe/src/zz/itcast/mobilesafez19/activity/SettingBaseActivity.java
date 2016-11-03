package zz.itcast.mobilesafez19.activity;

import zz.itcast.mobilesafez19.R;
import zz.itcast.mobilesafez19.utils.MyResource;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public abstract class SettingBaseActivity extends BaseActivity {

	private GestureDetector gd;

	// 在父类中定义sp的引用，只允许子类使用
	protected SharedPreferences sp;
	
	// 知道子类一定会掉执行动画的操作
	// 父类不知道跳到哪个界面

	// 有部分是已知的 有部分是未知的 未知的部分 我们通过抽象方法暴漏给子类实现
	// 模版设计模式
	/*
	 * 当一个方法中有部分是已知的 有部分是未知的 将已知的部分父类帮你实现 未知的部分交给子类实现
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences(MyResource.SP_FILE_NAME, 0);
		
		// 默认的适配器 一般开头的单词 Simple Base Default
		
		gd = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					// 一旦检测我们手指处于飞划状态 会调用onFlying
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {

						// 单位是 :像素/s 速率在 50~8000之内的范围是手机能够感应到范围

						// 如果横轴速率大于0 说明向右滑动 应该是上一页

						// 我们对速率再规定一次 我们规定必须大于500 才认为是有效飞划
						if (Math.abs(velocityX) > 500) {
							if (velocityX > 0) {
								// 上一页
								pre(null);

							} else {
								next(null);
								// 下一页
							}
							// 小于0 下一页

							System.out.println("手速:" + velocityX);
						}

						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	public void next(View view) {

		doNext();

		overridePendingTransition(R.anim.next_in, R.anim.next_out);

	}

	// 这个方法虽说是系统点击按钮之后触发的 但是我们自己也可以调用 View参数不重要 在这里没有被用到 我们可以给一个null
	public void pre(View view) {

		doPre();

		overridePendingTransition(R.anim.pre_in, R.anim.pre_out);

	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// 系统的工具类能够方便的帮助我们进行解析手指事件的操作

		// 我们将手指的事件源源不断的交给系统去执行
		gd.onTouchEvent(event);

		return true;
	}

	// 抽象方法
	public abstract void doNext();

	public abstract void doPre();



}
