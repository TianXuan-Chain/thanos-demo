package com.thanos.demo.simpletoken;

import com.thanos.common.crypto.key.asymmetric.SecureKey;
import com.thanos.demo.DemoBase;
import com.thanos.demo.simpletoken.contractcode.SimpleToken;
import com.thanos.web3j.abi.datatypes.Address;
import com.thanos.web3j.abi.datatypes.generated.Uint256;
import com.thanos.web3j.crypto.Credentials;
import com.thanos.web3j.model.ThanosTransactionReceipt;
import com.thanos.web3j.protocol.Web3j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SimpleTokenHttpDemo extends DemoBase {
    private static final Logger logger = LoggerFactory.getLogger("demo");

    public static String deploy(Web3j web3j, Credentials credentials) throws InterruptedException, ExecutionException {
        try {
            String contractAddress;
            try {
                SimpleToken simpleToken = SimpleToken.deploy(web3j, credentials, BigInteger.valueOf(1), BigInteger.valueOf(3000000), BigInteger.valueOf(0)).get();
                contractAddress = simpleToken.getContractAddress();
                logger.info("SimpleTokenHttpTest.contractAddress:{}", contractAddress);
                return contractAddress;
            } finally {
                web3j.close();
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e;
        }
    }

    public static void transferToken(Web3j web3j, String contractAddress, Credentials credentials, String to, BigInteger amount) {
        try {
            SimpleToken proxy = SimpleToken.load(contractAddress, web3j, credentials, BigInteger.ONE, BigInteger.valueOf(3000000));
            ThanosTransactionReceipt receipt = proxy.transfer(new Address(to), new Uint256(amount)).get();
            Assert.assertTrue(StringUtils.isBlank(receipt.getError()));
            List<SimpleToken.TransferEventResponse> event = SimpleToken.getTransferEvents(receipt);
            logger.info("transfer from: {}", event.get(0).from.toString());
            logger.info("transfer to: {}", event.get(0).to.toString());
            logger.info("transfer value: {}", event.get(0).value.getValue());
            Assert.assertEquals(event.get(0).value.getValue(), amount);

            BigInteger value = proxy.balanceOf(new Address(to)).get().getValue();
            logger.info("SimpleTokenHttpTest user {}, balance {}", to, value);
        } catch (Exception e) {
            logger.error("SimpleTokenHttpTest ERROR ... {}", e.getMessage());
        } finally {
            web3j.close();
        }
    }

    public static List<Credentials> createUsers(int count) {
        List<Credentials> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            SecureKey secureKey = SecureKey.getInstance("ECDSA", 1);
            Credentials credentials = Credentials.create(secureKey);
            users.add(credentials);
        }
        return users;
    }

}