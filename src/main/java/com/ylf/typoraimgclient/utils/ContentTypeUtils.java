package com.ylf.typoraimgclient.utils;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ContentTypeUtils {
    //下面是三种判断文件Content-Type的方法，但是每个单独用都有可能出问题
    private String getContentTypeLocal(String fileUrl) {
        String contentType = null;
        try {
            contentType = new MimetypesFileTypeMap().getContentType(new File(fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentType;
    }
    private String getContentTypeByType(String fileUrl) {
        String contentType = null;
        try {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            contentType = fileNameMap.getContentTypeFor(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentType;
    }
    private String getContentTypeByLocal(String fileUrl) {
        String contentType = null;
        Path path = Paths.get(fileUrl);
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }


    //根据可能出现的情况，综合三种方法，尽量得出正确的Content-Type
    public String getContentType(String fileUrl){
        String undifine = "application/octet-stream";
        String contentType1 = getContentTypeLocal(fileUrl);
        String contentType2= getContentTypeByType(fileUrl);
        String contentType3 = getContentTypeByLocal(fileUrl);
        if (contentType1.equals(contentType2)&&contentType2.equals(contentType3)) return contentType1;
        else {
            List<String> list = new ArrayList<>();
            if (!contentType1.equals(undifine)) list.add(contentType1);
            if (!contentType2.equals(undifine)) list.add(contentType2);
            if (!contentType3.equals(undifine)) list.add(contentType3);
            return list.get(0);
        }
    }


}
