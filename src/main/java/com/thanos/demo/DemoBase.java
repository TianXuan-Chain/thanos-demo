package com.thanos.demo;

import com.thanos.web3j.model.ThanosTransactionReceipt;
import com.thanos.web3j.tx.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 类TestBase.java的实现描述：
 *
 * @author xuhao create on 2020/12/17 10:43
 */

public class DemoBase {
    private static final Logger logger = LoggerFactory.getLogger("demo");

    //length用户要求产生字符串的长度
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public List<ThanosTransactionReceipt> waitForTnxReceiptList(TransactionManager transactionManager, List<String> tnxHashList) {
        if (tnxHashList == null) {
            return null;
        }
        List<ThanosTransactionReceipt> receiptList = new ArrayList<>();
        for (int i = 0; i < tnxHashList.size(); i++) {
            try {
                ThanosTransactionReceipt receipt = transactionManager.waitForTransactionReceipt(tnxHashList.get(i));
                receiptList.add(receipt);
            } catch (Exception e) {
                logger.error("waitForTnxReceiptList failed. index:{}, txHash:{}.", i, tnxHashList.get(i));
                throw new RuntimeException(e);
            }
        }
        return receiptList;
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
