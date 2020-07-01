package com.example;

import com.example.entity.User;
import com.example.mapper.test1.UserMapper;
import com.example.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    @Test
    public void contextLoads(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userMapper.insertUser(new User(uuid, "悟空", "2000", "男", "561518181818"));
    }

    void ss() {
        CloseableHttpClient aDefault = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("hh");

        try {
            CloseableHttpResponse response = aDefault.execute(httpGet);
            HttpEntity entity = response.getEntity();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * HttpClient请求
     */
    public void httpClientGet() {
        String url = "http://www.baidu.com";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
            org.apache.http.HttpEntity entity = response.getEntity();
            System.out.println("响应状态" + response.getStatusLine());

            if (entity != null) {
                System.out.println("响应内容长度为:" + entity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(entity));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * list过滤
     */
    public void listFilter(){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(i);
        }
        System.out.println(list);
//        List<Integer> collect = list.stream().filter(i -> (i>= 1)).collect(Collectors.toList());
        List<Integer> collect = list.stream().filter(i -> (i >= 1)).filter(i -> (i <= 1+1)).collect(Collectors.toList());

        List<Integer> collec2 =collect.stream().filter(i -> (i <= 1+1)).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(collec2);
    }

    /**
     * 字符串转化date日期
     */
    public static void strToDate() {
        Date date = null;
        try {
            String time = "20200520";
            time = new StringBuilder(time).insert(4, "-").insert(7, "-").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
    }

    /**
     * 去除Str中的符号和字母
     */
    public static void deleteSymbol() {
        String str = "     ,,,,,,,,,,,,,,,,,,,,,,,,,,,?''''     jdie   dede        ,";
        //去除所有符号
        String str2 = str.replaceAll("[^a-zA-Z_\u4e00-\u9fa5]", "");
        //去除所有字母
        String s = str2.replaceAll("[a-zA-Z]", "");
        System.out.println(s);
        System.out.println(str2);

        //判断字符串st是否包含汉字
        String st = "";
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(st);
    }

    /**
     * 计算时间差并显示
     */
    public static void getTime() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = df.parse("2019-03-26 13:31:40");//当前时间
            Date date = df.parse("2019-03-20 11:30:24");//过去
            long l = now.getTime() - date.getTime();
            System.out.println(l);
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");

        } catch (Exception e) {

        }
    }


}
