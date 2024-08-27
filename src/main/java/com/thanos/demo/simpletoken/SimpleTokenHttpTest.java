package com.thanos.demo.simpletoken;

import com.thanos.common.crypto.key.asymmetric.SecureKey;
import com.thanos.web3j.config.SystemConfig;
import com.thanos.web3j.crypto.Credentials;
import com.thanos.web3j.protocol.Web3j;
import com.thanos.web3j.protocol.manage.Web3Manager;
import com.thanos.web3j.utils.ConfigResourceUtil;
import com.typesafe.config.ConfigRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;

/**
 * SimpleTokenHttpTest.java description：
 *
 * @Author wanchuanru create on 2024-08-21 17:18:53
 */
public class SimpleTokenHttpTest {
    private static final Logger logger = LoggerFactory.getLogger("test");
    public static void main(String args[]) {
        SystemConfig systemConfig = ConfigResourceUtil.loadSystemConfig();
        ConfigResourceUtil.loadLogConfig(systemConfig.logConfigPath());
        logger.debug("Config trace: " + systemConfig.config.root().render(ConfigRenderOptions.defaults().setComments(false).setJson(false)));
        logger.info("SimpleTokenHttpTest start success!");

        SecureKey secureKey = SecureKey.fromPrivate(Hex.decode("010001308f761b30da0baa33457550420bb8938d040a0c6f0582d9351fd5cead86ff11"));
        Credentials contractOwnerCredentials = Credentials.create(secureKey);
        Web3Manager web3Manager = new Web3Manager(systemConfig);

        try {
            Web3j web3j = web3Manager.getHttpWeb3jRandomly();
            String contractAddress = SimpleTokenHttpDemo.deploy(web3j, contractOwnerCredentials);

            BigInteger amount = BigInteger.valueOf(2);
            List<Credentials> users = SimpleTokenHttpDemo.createUsers(10);
            for (int i = 0; i < users.size(); i++) {
                web3j = web3Manager.getHttpWeb3jRandomly();
                SimpleTokenHttpDemo.transferToken(web3j, contractAddress, contractOwnerCredentials, users.get(i).getAddress(), amount);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
