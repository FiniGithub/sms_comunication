package com.dzd.phonebook.entity;

import java.io.File;

public class UploadFile {
    private String saveDirectory;
    private String fileName;
    private String contentType;
    private String prePath;
    private String completeSavePath;
    private String relativeSavePath;

    public UploadFile(String saveDirectory, String filesystemName) {
        this.saveDirectory = saveDirectory;
        this.fileName = filesystemName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSaveDirectory() {
        return saveDirectory;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPrePath() {
        if (prePath == null) {
            return "";
        }
        return prePath;
    }

    public void setPrePath(String prePath) {
        this.prePath = prePath;
        setCompleteSavePath(prePath + getRelativeSavePath());
    }

    public String getCompleteSavePath() {
        return completeSavePath;
    }

    public void setCompleteSavePath(String completeSavePath) {
        this.completeSavePath = completeSavePath;
    }

    public String getRelativeSavePath() {
        return relativeSavePath;
    }

    public void setRelativeSavePath(String relativeSavePath) {
        this.relativeSavePath = relativeSavePath;
    }

    public void setSaveDirectory(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public File getFile() {
        if (getSaveDirectory() == null || getFileName() == null) {
            return null;
        } else {
//            setRelativeSavePath(Variables.ctx + "/" + Variables.upload + "/" + getFileName());
            return new File(getSaveDirectory() + "/" + getFileName());
        }
    }
}
