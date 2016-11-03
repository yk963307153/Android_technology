package zz.itcast.mobilesafez19.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	private static final String SALT = "hasdfhf";

	// 穿过来一个原数据 我得到它的MD5值
	public static String getMD5(String body) {
		String encryptBody = "";
		try {
			
			MessageDigest md5 = MessageDigest.getInstance("md5");
			// 加密的过程我们不用关心 只需要拿到结果即可
			byte[] encryptDigest = md5.digest((body+SALT).getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < encryptDigest.length; i++) {
				byte b = encryptDigest[i];

				// 先转换成正数

				int single = b & 0xFF;
				// 将每个字节转换成16进制
				String hexString = Integer.toHexString(single);
				
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}

				sb.append(hexString);

			}
			// 获得最终加密的结果
			encryptBody = sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encryptBody;
	}

	
	/**
	 * 对文件进行MD5 加密
	 * @param file
	 * @return
	 */
	public static String md5File(File file){
		
		try {
			InputStream fin = new FileInputStream(file);
		
			
			MessageDigest md5 = MessageDigest.getInstance("md5");

			// 将输入流中的内容，读取至加密器
			int len = -1;
			byte [] buffer = new byte[512];
			
			while((len = fin.read(buffer))!=-1){
				// 将读到的内容，更新至加密器
				md5.update(buffer, 0, len);
			}
			
			// 对加密器中的内容进行加密
			byte[] encryptDigest = md5.digest(); 
			
			// 对结果进行标准化输出
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < encryptDigest.length; i++) {
				byte b = encryptDigest[i];
				// 先转换成正数
				int single = b & 0xFF;
				// 将每个字节转换成16进制
				String hexString = Integer.toHexString(single);
				
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			// 获得最终加密的结果
			String encryptBody = sb.toString();
			
			return encryptBody;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	
}
