package com.ylf.typoraimgclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadPackage {
    String FileName;
    List<String> orgPath;
    List<String> picNames;
    List<File> pics;
}
