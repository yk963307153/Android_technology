package com.team.group.ourlibrary.utils;

/**
 * 根据实际需要来clean
 * Created by lijiafu on 16/8/4.
 */
public class CleanManager {

//    public static long getCacheSize() {
//        File cached = appContext.getFilesDir();
//        long size = 0;
//        File flist[] = cached.listFiles();
//        for (int i = 0; i < flist.length; i++) {
//            size = size + flist[i].length();
//        }
//        return size;
//    }
//
//    public static String getFormatSize() {
////        long size = getCacheSize();
//        long size = 0;
////        File f = new File(IMG_PATH);
////        Log.e("size", size + "");
////        if (f.exists()) {
////            size += getAutoFileOrFilesSize(IMG_PATH);
////        }
//
//        File fvoice = new File(VOICE_PATH);
//        if (fvoice.exists()) {
//            size += getAutoFileOrFilesSize(VOICE_PATH);
//        }
//
//        File web = new File(WEB_CACHE);
//        if (web.exists()) {
//            size += getAutoFileOrFilesSize(WEB_CACHE);
//        }
//        DecimalFormat df = new DecimalFormat("#.00");
//        String sizeStr = "";
//        if (size < 1024)
//            sizeStr = size + "B";
//        else if (size < 1024 * 1024)
//            sizeStr = df.format((double) size / 1024f) + "KB";
//        else if (size < 1024 * 1024 * 1024)
//            sizeStr = df.format((double) size / 1024f / 1024f) + "MB";
//        else
//            sizeStr = df.format((double) size / 1024f / 1024f / 1024f) + "GB";
//        return "系统缓存" + sizeStr;
//
//    }
//
//    public static void cleanInternalCache(TextView mTvCache) {
//        //清除/data/data/com.xxx.xxx/files下的内容
//        //+ "/voiceDown/"
//        deleteFilesByDirectory(appContext.getFilesDir());
//        //清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
////        deleteFilesByDirectory(appContext.getCacheDir());
//        //清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            deleteFilesByDirectory(appContext.getExternalCacheDir());
////            deleteFilesByDirectory(new File(IMG_PATH));
//            deleteFilesByDirectory(new File(VOICE_PATH));
//            deleteFilesByDirectory(new File(WEB_CACHE));
//        }
//
//        //清理Webview缓存数据库
//        try {
//            getAppContext().deleteDatabase("webview.db");
//            getAppContext().deleteDatabase("webviewCache.db");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mTvCache.setText("系统缓存0B");
//        DialogUtil.closeMyDialog();
//
//    }
//
//
//    public static void deleteFilesByDirectory(File directory) {
//        try {
//            if (directory != null && directory.exists() && directory.isDirectory()) {
//                for (File item : directory.listFiles()) {
//                    item.delete();
//                }
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * 调用此方法自动计算指定文件或指定文件夹的大小
//     *
//     * @param filePath 文件路径
//     */
//    public static long getAutoFileOrFilesSize(String filePath) {
//        File file = new File(filePath);
//        long blockSize = 0;
//        try {
//            if (file.isDirectory()) {
//                blockSize = getFileSizes(file);
//            } else {
//                blockSize = getFileSize(file);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("获取文件大小", "获取失败!");
//        }
//        return blockSize;
//    }
//
//    /**
//     * 获取指定文件夹
//     *
//     * @param f
//     * @return
//     * @throws Exception
//     */
//    private static long getFileSizes(File f) throws Exception {
//        long size = 0;
//        File flist[] = f.listFiles();
//        for (int i = 0; i < flist.length; i++) {
//            if (flist[i].isDirectory()) {
//                size = size + getFileSizes(flist[i]);
//            } else {
//                size = size + getFileSize(flist[i]);
//            }
//        }
//        return size;
//    }
//
//    /**
//     * 获取指定文件大小
//     *
//     * @return
//     * @throws Exception
//     */
//    private static long getFileSize(File file) throws Exception {
//        long size = 0;
//        if (file.exists()) {
//            FileInputStream fis = null;
//            fis = new FileInputStream(file);
//            size = fis.available();
//        } else {
//            file.createNewFile();
//            Log.e("获取文件大小", "文件不存在!");
//        }
//        return size;
//    }
}



