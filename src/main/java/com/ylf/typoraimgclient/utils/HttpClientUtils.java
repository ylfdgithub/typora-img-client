package com.ylf.typoraimgclient.utils;

import com.ylf.typoraimgclient.entity.UploadPackage;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HttpClientUtils {
    //制造需要的请求
    public HttpPost createPost(String[] args, String url){
        //构造用于判断文件Content-Type的工具类对象
        ContentTypeUtils utils = new ContentTypeUtils();
        //生成请求中的分割内容的字符串
        String boundary = "-------------------------" + UUID.randomUUID().toString();
        //创建httPost对象
        HttpPost httpPost = new HttpPost(url);
        //设置一些固定的请求头内容
        httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
        //接下来构造请求实体对象
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        UploadPackage uploadPackage = analysisArgs(args);
        List<File> pics = uploadPackage.getPics();
        List<String> orgPath = uploadPackage.getOrgPath();
        String fileName = uploadPackage.getFileName();
        List<String> picNames = uploadPackage.getPicNames();
        //设置
        builder.setCharset(Consts.UTF_8)
                .setBoundary(boundary)
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (int i = 0; i < pics.size(); i++) {
            builder.addBinaryBody("files",pics.get(i),ContentType.create(utils.getContentType(orgPath.get(i))),picNames.get(i));
        }
        StringBody textBody = new StringBody(fileName, ContentType.create("text/plain", Consts.UTF_8));
        builder.addPart("fileName",textBody);
        httpPost.setEntity(builder.build());
        return httpPost;
    }

    //file
    //主要是解析输入参数，因为默认第一个参数中带有文件名字信息
    public UploadPackage analysisArgs(String[] args){
        List<String> picNames = new ArrayList<>();
        List<File> pics = new ArrayList<>();
        List<String> orgPath = new ArrayList<>();
        String fileName;
        String arg = args[0];//default is "file"
        if (arg.length()>4){
            fileName = arg.substring(4);
        }else {
            fileName = "unnamed";
        }
        for (int i = 1; i < args.length; i++) {
            orgPath.add(args[i]);
            pics.add(new File(args[i]));
            String[] split = args[i].split("\\\\");
            picNames.add(split[split.length-1]);
        }
        return new UploadPackage(fileName,orgPath,picNames,pics);
    }

    //垃圾回收
    public void consumeAll(HttpEntity entity, CloseableHttpClient client, CloseableHttpResponse response) throws IOException {
        EntityUtils.consume(entity);
        if (client != null) client.close();
        if (response != null) response.close();
    }
    //{"urls":["http://39.170.47.228:8080/30514820b75849dea70e2b225ded8767.png","http://39.170.47.228:8080/694c50f331b54179937952f963f02f98.png"]}
    //{"urls":["http://39.170.47.228:8080/30514820b75849dea70e2b225ded8767.png"]}
    public List<String> getPaths(String str){
        List<String> paths = new ArrayList<>();
        String[] split = str.split("\"");
        int length = split.length;
        int count = (length-3)/2;
        int index = 3;
        for (int i = 0; i < count; i++) {
            paths.add(split[index]);
            index+=2;
        }
        return paths;
    }
}
