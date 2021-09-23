package com.cetc.pacong.utils;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

public class FileUtil {

    public static String readJsonFile(String filePath) {
        StringBuffer laststr = new StringBuffer();
        File file = new File(filePath);// 打开文件
        BufferedReader reader = null;
        try {
            FileInputStream in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));// 读取文件
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr = laststr.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException el) {
                }
            }
        }
        return laststr.toString();
    }


    public static void list2txt(List<String> list, String fileName) {
//        StringBuilder result = new StringBuilder();
        List<String> link = Lists.newArrayList();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
            list.forEach(s -> {
                try {
                    bw.write(s);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> txt2List(File file) {
//        StringBuilder result = new StringBuilder();
        List<String> link = Lists.newArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                link.add(s.trim());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return link;
    }

    private void extractHan() {
        List<String> link = Lists.newArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Administrator\\Downloads\\langzeyu1024-spring-boot-example-master\\spring-boot-example\\src\\main\\resources\\usa_mili_base.txt"));
            String s = null;
            while ((s = br.readLine()) != null) {
                String reg = "[^\u4e00-\u9fa5]";
                s = s.replaceAll(reg, " ");
                System.out.println(s);
                link.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void copyFile(String srcPathStr, String desPathStr) {
        //1.获取源文件的名称
        String newFileName = srcPathStr.substring(srcPathStr.lastIndexOf("\\") + 1); //目标文件地址
        System.out.println(newFileName);
        desPathStr = desPathStr + File.separator + newFileName; //源文件地址
        System.out.println(desPathStr);

        try {
            File demofile = new File(desPathStr);
            if (!demofile.exists()) demofile.createNewFile();

            //2.创建输入输出流对象
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);

            //创建搬运工具
            byte datas[] = new byte[1024 * 8];
            //创建长度
            int len = 0;
            //循环读取数据
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            //3.释放资源
            fis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        extractHan();
    }

    public static void mkdirs(String filePath) {
        if (!filePath.endsWith("/")) {
            filePath = (filePath + "/");
        }
        File fileTxt = new File(filePath);
        //如果文件夹不存在则创建
        if (!fileTxt.exists() && !fileTxt.isDirectory()) {
            fileTxt.mkdirs();
        }

    }
}
