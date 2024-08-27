package com.thanos.demo.mail.bo;

import java.util.Objects;

/**
 * 类NotarizationMailUpChainBo.java的实现描述：
 *
 * @author xuhao create on 2020/12/14 15:21
 */


public class NotarizationMailUpChainBo {
    /**
     * 邮件识别码
     */
    private String emailNumber;


    /**
     * 邮件指纹数据
     */
    private String emailFingerprint;

    /**
     * 基于邮件特殊属性生成的hash值，采用SHA256算法
     */
    private String emailHash;
    /**
     * 邮件内容字节长度
     */
    private Long emailLength;
    /**
     * 用户唯一编号
     */
    private String userNumber;


    public String getEmailNumber() {
        return emailNumber;
    }

    public void setEmailNumber(String emailNumber) {
        this.emailNumber = emailNumber;
    }

    public String getEmailFingerprint() {
        return emailFingerprint;
    }

    public void setEmailFingerprint(String emailFingerprint) {
        this.emailFingerprint = emailFingerprint;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public Long getEmailLength() {
        return emailLength;
    }

    public void setEmailLength(Long emailLength) {
        this.emailLength = emailLength;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }


    @Override
    public String toString() {
        return "NotarizationMailUpChainBo{" +
                "emailNumber='" + emailNumber + '\'' +
                ", emailFingerprint='" + emailFingerprint + '\'' +
                ", emailHash='" + emailHash + '\'' +
                ", emailLength=" + emailLength +
                ", userNumber='" + userNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotarizationMailUpChainBo that = (NotarizationMailUpChainBo) o;
        return Objects.equals(emailNumber, that.emailNumber) &&
                Objects.equals(emailFingerprint, that.emailFingerprint) &&
                Objects.equals(emailHash, that.emailHash) &&
                Objects.equals(emailLength, that.emailLength) &&
                Objects.equals(userNumber, that.userNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailNumber, emailFingerprint, emailHash, emailLength, userNumber);
    }
}
