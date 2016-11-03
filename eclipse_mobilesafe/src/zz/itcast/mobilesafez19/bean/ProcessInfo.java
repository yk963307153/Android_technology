package zz.itcast.mobilesafez19.bean;

import android.graphics.drawable.Drawable;

public class ProcessInfo {

	public Drawable icon; // 图标

	public String processName;// 名称

	public long memory; // 因为会牵扯到运算   byte

	public boolean isChecked;

	public boolean isSystemProcress;

	public String packageName;

	public int pid; // 进程的id
	
	

}
