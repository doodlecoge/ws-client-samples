package me.hch;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 14-4-12.
 */
public class FileUtils {
    public static String getFileContent(String fileName) throws IOException {
        InputStream is = FileUtils.class.getResourceAsStream("/" + fileName);
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b = -1;
        while ((b = bis.read()) != -1) {
            baos.write(b);
        }

        return baos.toString();
    }
}
