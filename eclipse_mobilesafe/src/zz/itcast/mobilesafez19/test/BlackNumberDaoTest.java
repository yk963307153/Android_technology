package zz.itcast.mobilesafez19.test;

import java.util.ArrayList;
import java.util.Random;

import zz.itcast.mobilesafez19.bean.BlackNumber;
import zz.itcast.mobilesafez19.db.BlackNumberDao;
import android.test.AndroidTestCase;

public class BlackNumberDaoTest extends AndroidTestCase {

	private BlackNumberDao dao;

	@Override
	// 初始化操作
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		dao = BlackNumberDao.getInstance(getContext());
	}

	// 结束操作
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	public void testAdd() {

		Random ran = new Random();

		for (int i = 0; i < 200; i++) {
			int mode = ran.nextInt(3) + 1;
			dao.add(""+i, mode);
		}
	}

	public void testDelete() {
		dao.delete("123456");
	}

	public void testUpdate() {
		dao.update("654321", 2);
	}

	public void testGet() {
		ArrayList<BlackNumber> allBlackNumbers = dao.getAllBlackNumbers();

		for (BlackNumber bn : allBlackNumbers) {
			System.out.println(bn.toString());
		}

	}

}
