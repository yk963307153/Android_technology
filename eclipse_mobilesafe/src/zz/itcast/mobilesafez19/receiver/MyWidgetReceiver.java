package zz.itcast.mobilesafez19.receiver;

import zz.itcast.mobilesafez19.service.WidgetService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/*
 * 代言人  代表咱们应用
 */
public class MyWidgetReceiver extends AppWidgetProvider {

	// 第一次添加小控件时
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);

		// 开启一个服务 目的用服务每隔几秒对它刷新一次
		Intent service = new Intent(context, WidgetService.class);
		context.startService(service);
		
	}

	// 最后一次移除时
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		// 关闭服务
		Intent service = new Intent(context, WidgetService.class);
		context.stopService(service);
	}
}
