package com.gdufe.health_butler.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author: laichengfeng
 * @Description: 文件工具
 * @Date: 2019/3/5 13:18
 */
public class FileUtils {

    private static final int BATCH_SIZE = 8192;

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[BATCH_SIZE];
            while ((bytesRead = ins.read(buffer, 0, BATCH_SIZE)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
