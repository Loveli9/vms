package com.icss.mvp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class IOUtils {

    private static Logger logger = Logger.getLogger(IOUtils.class);

    public static void saveDataToFile(String path,String data) {
        BufferedWriter writer = null;
        File file = new File(path);
        File fileParent = file.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("创建文件异常",e);
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            logger.error("数据写入异常",e);
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("流关闭异常",e);
            }
        }
        System.out.println("文件写入成功！");
    }

    public static String getDatafromFile(String path) {

        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            logger.error("数据读取异常",e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("流关闭异常",e);
                }
            }
        }
        return laststr;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        boolean result = false;

        File file = new File(fileName);
        try {
            /**
             * <pre>
             *     文件不存在 抛java.nio.file.NoSuchFileException
             *     删除非空目录 抛java.nio.file.DirectoryNotEmptyException
             *     删除被占用文件 抛java.nio.file.FileSystemException
             *     其他原因文件无法删除 抛java.io.IOException的具体子类
             * </pre>
             */
            Files.delete(file.toPath());
            result = true;

            logger.info("删除文件：" + fileName + " 成功！");
        } catch (NoSuchFileException e) {
            logger.error("删除文件失败！" + fileName + " 文件不存在");
        } catch (DirectoryNotEmptyException e) {
            logger.error("删除文件失败！" + fileName + " 为非空目录");
        } catch (IOException e) {
            logger.error("删除文件：" + fileName + " 失败！");
        }

        // // File file = new File(fileName);
        // // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        // if (file.exists() && file.isFile()) {
        //
        // if (file.delete()) {
        // logger.info("删除文件" + fileName + "成功！");
        // return true;
        // } else {
        // logger.error("删除文件" + fileName + "失败！");
        // return false;
        // }
        // } else {
        // logger.error("删除文件失败：" + fileName + "不存在！");
        // return false;
        // }

        return result;
    }

    public static void main(String[] args) {

    }
}
