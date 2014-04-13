package me.hch;

import java.io.*;

/**
 * Created by Administrator on 14-4-12.
 */
public class FileUtils {
    public static String getFileContent(String fileName) throws IOException {
        InputStream is = FileUtils.class.getResourceAsStream("/" + fileName);
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b;
        while ((b = bis.read()) != -1) {
            baos.write(b);
        }

        return baos.toString();
    }

    public static void dump(String fileName, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(fileName)
            );
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new WsClientException(e);
        }
    }
}
