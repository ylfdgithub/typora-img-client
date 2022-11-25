package com.ylf.typoraimgclient;

import com.ylf.typoraimgclient.utils.HttpClientUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class TyporaImgClientApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TyporaImgClientApplication.class, args);
        //实体化工具类对象
        HttpClientUtils utils = new HttpClientUtils();
        //存放图片路径的数组
        List<String> fileUrls = new ArrayList<>();
        //把输入参数中的路径转存到路径数组
        Collections.addAll(fileUrls, args);
        //请求服务器的api
        String url = "http://124.70.212.7:8081/upload";
        //创建HttpClient 对象，类似于打开一个浏览器窗口
        CloseableHttpClient client = HttpClients.createDefault();
        //用自定义的工具方法构造需要的请求
        HttpPost post = utils.createPost(fileUrls, url);
        //执行请求，获得响应对象
        CloseableHttpResponse response = client.execute(post);
        //获取响应实体对象
        HttpEntity entity = response.getEntity();
        //打印响应内容
        String toStringResult = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        List<String> paths = utils.getPaths(toStringResult);
        for (String path : paths) {
            System.out.println(path);
        }
        //自定义工具方法进行垃圾回收
        utils.consumeAll(entity,client,response);
    }

}
