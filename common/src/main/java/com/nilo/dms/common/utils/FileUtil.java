package com.nilo.dms.common.utils;

import com.nilo.dms.common.utils.model.ExcelData;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by admin on 2017/11/10.
 */
public class FileUtil {

    public static String uploadFile(MultipartFile file, String path) throws Exception {
        String name = file.getOriginalFilename();
        String suffixName = name.substring(name.lastIndexOf("."));
        String fileName = IdWorker.getInstance().nextId() + suffixName;
        File saveFile = new File(path, fileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdir();
        }
        if (saveFile.exists()) {
            saveFile.delete();
        }
        saveFile.createNewFile();
        file.transferTo(saveFile);
        return saveFile.getName();
    }

    public static void saveTextFile(String content, String path, String fileName) throws Exception {
        File saveFile = new File(path, fileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdir();
        }
        if (saveFile.exists()) {
            saveFile.delete();
        }
        saveFile.createNewFile();
        try {
            FileWriter fw = new FileWriter(saveFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void saveFile(InputStream in, String path,String fileName) throws Exception {
        DataOutputStream out = null;
        try {
            File checkFile = new File(path + File.separator + fileName);
            if (!checkFile.exists()) {
                out = new DataOutputStream(new FileOutputStream(checkFile));// 存放文件的绝对路径
                byte[] b = new byte[in.available()];
                in.read(b);
                out.write(b);
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
}
