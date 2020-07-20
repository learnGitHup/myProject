package com.example.common.ftp.util;

import com.example.common.ftp.client.FTPClientHelper;
import com.example.utils.DateUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: pbs
 * @description: 校验单元测试
 * @author: lxj
 * @create: 2019-12-03 10:30
 **/
public class UpLoadUtil {

    private static final Logger log = LoggerFactory.getLogger(UpLoadUtil.class);
    //1是对年份操作，2是对月份操作，3是对星期操作，5是对日操作，11是对小时操作，12是对分钟操作，13是对秒操作，14是对毫秒操作
    private static final int calendarField = 5;
    //加减数量 例如:前天-2
    private static final int calendarAmount = -2;
    //加减数量 例如:昨天-1
    private static final int calendarYestaday = -1;
    //压缩文件的数量
    private static final int size = 5000;

    //@Test
    /*public void Test() {
        String localRootPath = "C:/Users/mlamp/Desktop/新建文件夹 (2)/other/111";//本地路劲
        String ftpRootPath = "/home/ftp_recv"; //ftp根路劲
        String filePath = "/other";//业务名称
        String ftpPath = "/testUtil";//fpt路劲
        //删除前一天文件目录
        deleteBeforeDirectory(localRootPath, filePath, calendarAmount);
        localRootPath = localRootPath + "/" + addDays(calendarYestaday) + filePath;
        ftpPath = ftpPath + "/" + addDays(calendarYestaday) + filePath;//ftp文件路劲
        //压缩文件，并发送ftp
        zipFileChannel(localRootPath, ftpPath, 0, size, ftpRootPath);
    }*/

    /**
     * 上传本地文件到Ftp
     *
     * @param localPath           本地存储的前两级路径，例：/RCV/FAIL
     * @param filePath            业务名称，例如：/other  （模块名）
     * @param ftpRootPath         Ftp的根路径
     * @param ftpPath             上传ftp的前两级目录，例：/RCV/FAIL
     * @param size                一次压缩的文件的个数
     * @param ：在所用工程的启动类上注册一个bean --UpLoadUtil
     */
    public void execute(String localPath, String filePath, String ftpRootPath, String ftpPath, int size, FTPClientHelper ftpClientHelper) {

        File file = new File(localPath);
        //判断文件是否存在
        if (!file.exists()) {
            log.info("本地文件路径不存在");
            return;
        }
        //判断文件夹是否为空
        File[] list = file.listFiles();
        if (list == null || list.length < 1) {
            return;
        }
        for (File fn : list) {
            //获取昨天日期
            String addDays = addDays(calendarYestaday);
            if (fn.isDirectory()) {
                //过滤当前日期
                if (fn.getName().compareToIgnoreCase(addDays) < 1) {
                    File fs = new File(fn.getPath());
                    File[] listFiles = fs.listFiles();
                    //判断文件夹是否为空,删除空文件夹
                    if (listFiles.length == 0) {
                        try {
                            FileUtils.deleteDirectory(fs);
                        } catch (Exception e) {
                            log.error("本地文件目录删除失败" + localPath);
                        }
                        continue;
                    }
                    //ftp文件路劲：/RCV/SUCCESS+日期+业务接口名称
                    String ftpFilePath = ftpPath + "/" + fn.getName() + filePath;
                    //本地路劲：/RCV/SUCCESS+日期+业务接口名称
                    String localFilePath = fn.getPath() + filePath;
                    //1、批量压缩文件，并发送ftp     2、压缩文件，直接发送ftp
                    zipFileChannel(localFilePath, ftpFilePath, 0, size, ftpRootPath, ftpClientHelper);
                }
            }
        }
    }

    /**
     * 文件压缩成zip
     *
     * @param filePath 本地备份文件路径，     -------包括时间
     *                 ftpPath ftp文件路径    -------和本地备份的文件路径的最后一个相同
     *                 n 文件的开始位置
     *                 size 每个zip包的数量
     */
    public void zipFileChannel(String filePath, String ftpPath, int n, int size, String ftpRootPath, FTPClientHelper ftpClientHelper) {
        File file = new File(filePath);
        //判断文件是否存在
        if (!file.exists()) {
            log.info("本地文件路径不存在");
            return;
        }
        //初始化
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArray);
        WritableByteChannel writableByteChannel = Channels.newChannel(zipOut);
        zipFileChannel(byteArray, zipOut, writableByteChannel, file, ftpPath, n, size, ftpRootPath, ftpClientHelper);
    }


    public void zipFileChannel(ByteArrayOutputStream byteArray, ZipOutputStream zipOut, WritableByteChannel writableByteChannel,
                               File file, String ftpPath, int n, int size, String ftpRootPath, FTPClientHelper ftpClientHelper) {

        List<String> list = new ArrayList<>();
        File[] fileList = file.listFiles();
        //判断文件夹是否为空,删除空文件夹
        if (fileList.length == 0) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (Exception e) {
                log.error("本地文件夹删除失败" + file.getPath());
            }
            return;
        }
        FileChannel fileChannel = null;
        String name = "";
        try {
            for (int i = n; i < fileList.length; i++) {
                //判断是否为文件夹,并递归遍历文件夹
                if (fileList[i].isDirectory()) {
                    zipFileChannel(fileList[i].getPath(), ftpPath + "/" + fileList[i].getName(), n, size, ftpRootPath, ftpClientHelper);
                } else if (fileList[i].getName().endsWith(".zip")) {
                    n++;
                    name = fileList[i].getName();
                    //zip直接上传ftp
                    boolean result = ftpClientHelper.storeFile(ftpPath, new FileInputStream(fileList[i]), ftpRootPath, name);
                    if (!result) {
                        log.info("上传ftp失败文件名:{},时间:{},上传路径{}", name, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"), ftpPath);
                    } else {
                        try {
                            //删除上传后的zip包
                            fileList[i].delete();
                        } catch (Exception e) {
                            log.error("本地文件删除失败" + fileList[i].getPath());
                        }
                    }
                } else {
                    n++;
                    name = fileList[i].getName();
                    FileInputStream fileInputStream = new FileInputStream(fileList[i]);
                    fileChannel = fileInputStream.getChannel();
                    zipOut.putNextEntry(new ZipEntry(name));
                    fileChannel.transferTo(0, fileList[i].length(), writableByteChannel);
                    fileInputStream.close();
                    //添加文件路劲
                    list.add(fileList[i].getPath());
                    //批量压缩文件后，上传ftp
                    if ((n % size == 0) || n == fileList.length) {
                        fileChannel.close();
                        writableByteChannel.close();
                        zipOut.close();
                        byteArray.close();
                        String zipName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + System.currentTimeMillis() + ".zip";
                        log.info("成功压缩文件:{},压缩到第{}条,文件名:{},压缩最后一条的文件名", zipName, n, name);
                        //上传ftp
                        InputStream inputStream = new ByteArrayInputStream(byteArray.toByteArray());
                        boolean result = ftpClientHelper.storeFile(ftpPath, inputStream, ftpRootPath, zipName);
                        if (!result) {
                            log.info("上传ftp失败,时间:{},上传路径{}", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"), ftpPath);
                        } 
                        inputStream.close();
                        //批量删除文件
                        if (list.size() > 0) {
                            for (String str : list) {
                                File fi = new File(str);
                                try {
                                    fi.delete();
                                } catch (Exception e) {
                                    log.error("本地文件删除失败" + str);
                                }
                            }
                        }
                        if (n != fileList.length) {
                            //初始化
                            byteArray = new ByteArrayOutputStream();
                            zipOut = new ZipOutputStream(byteArray);
                            writableByteChannel = Channels.newChannel(zipOut);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("压缩失败文件名:{},压缩到第{}条", name, n);
            if (n == fileList.length) {
                return;
            }
            zipFileChannel(byteArray, zipOut, writableByteChannel, file, ftpPath, n, size, ftpRootPath, ftpClientHelper);
        }
    }


    /**
     * 基于当前时间的加减
     *
     * @param calendarAmount 例如昨天-1，前天-2
     * @return 返回日期对象   5 ，-2
     */
    public static String addDays(int calendarAmount) {
        //使用默认时区和语言环境获得一个日历。
        Calendar cal = Calendar.getInstance();
        cal.add(calendarField, calendarAmount);
        DateUtil.getDateTimeStr(DateUtil.datePattern);
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.datePattern);
        String dateStr = sdf.format(cal.getTime());
        return dateStr;
    }


}
