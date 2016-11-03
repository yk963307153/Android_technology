package zz.itcast.mobilesafez19.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {

	public static String getStringFromStream(InputStream is) {
		String body = "";

		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			body = baos.toString();
			// 因为这是写到内存中 关不关无所谓
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return body;
	}

}
