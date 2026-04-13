/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

/**
 *
 * @author ASUS
 */
public class TestResultDTO {
    private String testName;
    private String resultValue;
    private String fileUrl;

    /**
     * @return the testName
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName the testName to set
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
     * @return the resultValue
     */
    public String getResultValue() {
        return resultValue;
    }

    /**
     * @param resultValue the resultValue to set
     */
    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    /**
     * @return the fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @param fileUrl the fileUrl to set
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
