package com.thanos.demo.ftbtoken;

import com.thanos.common.crypto.key.asymmetric.SecureKey;
import com.thanos.demo.DemoBase;
import com.thanos.demo.ftbtoken.contractcode.*;
import com.thanos.web3j.abi.datatypes.Address;
import com.thanos.web3j.abi.datatypes.DynamicArray;
import com.thanos.web3j.abi.datatypes.Utf8String;
import com.thanos.web3j.abi.datatypes.generated.Bytes32;
import com.thanos.web3j.abi.datatypes.generated.Uint256;
import com.thanos.web3j.crypto.Credentials;
import com.thanos.web3j.model.ThanosTransactionReceipt;
import com.thanos.web3j.protocol.Web3j;
import com.thanos.web3j.protocol.core.methods.request.RawTransaction;
import com.thanos.web3j.protocol.core.methods.response.EthSendTransactionList;
import com.thanos.web3j.protocol.manage.Web3Manager;
import com.thanos.web3j.tx.RawTransactionManager;
import com.thanos.web3j.tx.TransactionConstant;
import com.thanos.web3j.utils.SystemConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class FTBTokenDemo extends DemoBase {
    private static final Logger logger = LoggerFactory.getLogger("demo");
    private static Long DECIMALS_VALUE = 100000000L;
    //操作账户
    private static Credentials systemCred;
    private static Credentials distributorCred;

    private static final int USER_COUNT = 100;
    //用户账户列表
    private static List<Credentials> userCreds;

    //controller
    private String controllerAddress;
    //proxy
    private String c2cAddress;
    private String b2cAddress;
    //handler
    private String handlerAddress;
    //token
    private String tokenAddress;
    //award
    private String mineAddress;
    private String freezeAddress;

    private Uint256 oneToken = new Uint256(BigDecimal.ONE.multiply(BigDecimal.valueOf(DECIMALS_VALUE)).longValue());

    static {

        try {
            SecureKey _extractor = SecureKey.fromPrivate(Hex.decode("010001308f761b30da0baa33457550420bb8938d040a0c6f0582d9351fd5cead86ff11"));
            systemCred = Credentials.create(_extractor);
            SecureKey _distributor = SecureKey.fromPrivate(Hex.decode("010001c833689dd53a006191ec84a1764f0359c80f5f714c88ac9b235de8eab3fc1133"));
            distributorCred = Credentials.create(_distributor);
            userCreds = initUserCredentials(USER_COUNT);
        } catch (Exception e) {
            logger.error("FTBTokenTest create systemCred failed.", e);
        }
    }

    public static void start(Web3Manager web3Manager) {

        Web3j web3j = web3Manager.getWeb3jRandomly();
        FTBTokenDemo tokenTest = new FTBTokenDemo();
        //1. 部署合约
        tokenTest.deployContract(web3j);

        //2. 发矿50000
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateStr = formatter.format(date);

        tokenTest.mineToken(dateStr, web3j);
        //3. 用户挖矿（矿池账号给用户发币）
        double targetBalance = 100;
        BigDecimal target = new BigDecimal(targetBalance);
        logger.info("FTBTokenTest userMine date:{}.", dateStr);
        tokenTest.userMine(distributorCred, userCreds, targetBalance, web3j);

        int batchNumber = 1;
        int errorCount = 0;
        while (true) {
            try {
                web3j = web3Manager.getWeb3jRandomly();
                if (web3j == null) {
                    Thread.sleep(5000);
                    continue;
                }
                logger.info("--------FTBTokenTest test with  [{}]users,[{}]balance start. batchNumber={}.------------", USER_COUNT, targetBalance, batchNumber);
//                Thread.sleep(1000);

                //循环转账
                tokenTest.transferCircle(userCreds, 50, web3j);
                tokenTest.checkBalances(userCreds, target, web3j);

//                Thread.sleep(1000);
                //循环批量转账
//                tokenTest.batchTransferCircle(userCreds, 20, web3j);
//                tokenTest.checkBalances(userCreds, target, web3j);
                logger.info("FTBTokenTest test with  [{}]users,[{}]balance success. batchNumber={}.", USER_COUNT, targetBalance, batchNumber);
                Thread.sleep(1500);
                batchNumber++;
            } catch (Exception e) {
                errorCount++;
                logger.error("FTBTokenTest error, batchNumber={}, errorCount={}. e:{}.", batchNumber, errorCount, getStackTrace(e));
            }
        }
    }


    private void deployContract(Web3j web3) {
        try {
            logger.info("FTBTokenTest begin deployContract...");


            // Controller
            FTBControllerFactory nbdControllerFactory = FTBControllerFactory.deploy(web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT, BigInteger.ZERO).get();
            ThanosTransactionReceipt controllerReceipt = nbdControllerFactory.createController().get();
            List<FTBControllerFactory.CreateSuccessEventEventResponse> controllerResponse = FTBControllerFactory.getCreateSuccessEventEvents(controllerReceipt);
            this.controllerAddress = CollectionUtils.isEmpty(controllerResponse) ? null : controllerResponse.get(0).addr.toString();
            logger.info("FTBTokenTest controllerAddress:{}", controllerAddress);

            //proxy
            FTBProxyFactory nbdProxyFactory = FTBProxyFactory.deploy(web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT, BigInteger.ZERO, new Address(controllerAddress)).get();
            ThanosTransactionReceipt c2creceipt = nbdProxyFactory.createC2CProxy().get();
            List<FTBProxyFactory.CreateSuccessEventEventResponse> c2cresponses = FTBProxyFactory.getCreateSuccessEventEvents(c2creceipt);
            this.c2cAddress = CollectionUtils.isEmpty(c2cresponses) ? null : c2cresponses.get(0).addr.toString();
            logger.info("FTBTokenTest c2cAddress:{}", c2cAddress);

            ThanosTransactionReceipt b2creceipt = nbdProxyFactory.createB2CProxy().get();
            List<FTBProxyFactory.CreateSuccessEventEventResponse> b2cresponses = FTBProxyFactory.getCreateSuccessEventEvents(b2creceipt);
            this.b2cAddress = CollectionUtils.isEmpty(b2cresponses) ? null : b2cresponses.get(0).addr.toString();
            logger.info("FTBTokenTest b2cAddress:{}", b2cAddress);


            //handler
            FTBHandlerFactory nbdHandlerFactory = FTBHandlerFactory.deploy(web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT, BigInteger.ZERO, new Address(controllerAddress)).get();
            ThanosTransactionReceipt handlerreceipt = nbdHandlerFactory.createHandler().get();
            List<FTBHandlerFactory.CreateSuccessEventEventResponse> handlerresponses = FTBHandlerFactory.getCreateSuccessEventEvents(handlerreceipt);
            this.handlerAddress = CollectionUtils.isEmpty(handlerresponses) ? null : handlerresponses.get(0).addr.toString();
            logger.info("FTBTokenTest handlerAddress:{}", handlerAddress);


            //token
            FTBTokenFactory nbdTokenFactory = FTBTokenFactory.deploy(web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT, BigInteger.ZERO, new Address(controllerAddress)).get();
            ThanosTransactionReceipt tokenreceipt = nbdTokenFactory.createToken().get();
            List<FTBTokenFactory.CreateSuccessEventEventResponse> tokenresponses = FTBTokenFactory.getCreateSuccessEventEvents(tokenreceipt);
            this.tokenAddress = CollectionUtils.isEmpty(tokenresponses) ? null : tokenresponses.get(0).addr.toString();
            logger.info("FTBTokenTest tokenAddress:{}", tokenAddress);

            //award
            FTBAwardPoolFactory nbdAwardPoolFactory = FTBAwardPoolFactory.deploy(web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT, BigInteger.ZERO, new Address(controllerAddress)).get();

            ThanosTransactionReceipt minereceipt = nbdAwardPoolFactory.createMine().get();
            List<FTBAwardPoolFactory.CreateSuccessEventEventResponse> mineresponses = FTBAwardPoolFactory.getCreateSuccessEventEvents(minereceipt);
            this.mineAddress = CollectionUtils.isEmpty(mineresponses) ? null : mineresponses.get(0).addr.toString();
            logger.info("FTBTokenTest mineAddress:{}", mineAddress);

            ThanosTransactionReceipt freezereceipt = nbdAwardPoolFactory.createFreeze().get();
            List<FTBAwardPoolFactory.CreateSuccessEventEventResponse> freezeresponses = FTBAwardPoolFactory.getCreateSuccessEventEvents(freezereceipt);
            this.freezeAddress = CollectionUtils.isEmpty(freezeresponses) ? null : freezeresponses.get(0).addr.toString();
            logger.info("FTBTokenTest freezeAddress:{}", freezeAddress);

            logger.info("FTBTokenTest deployContract finished.");
        } catch (Exception e) {
            logger.error("FTBTokenTest deployContract error.", e);
            throw new RuntimeException(e);
        }
    }

    private boolean mineToken(String mineDate, Web3j web3) {

        try {
            OperatingTokenProxy proxy = OperatingTokenProxy.load(b2cAddress, web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT);

            ThanosTransactionReceipt receipt = proxy.minerTokens(new Uint256(Long.valueOf(mineDate))).get();
            List<OperatingTokenProxy.MineSuccessEventEventResponse> responses = OperatingTokenProxy.getMineSuccessEventEvents(receipt);
            Boolean success = CollectionUtils.isNotEmpty(responses);
            BigDecimal amount = CollectionUtils.isEmpty(responses) ? BigDecimal.valueOf(0) : BigDecimal.valueOf(responses.get(0).amount.getValue().longValue()).divide(BigDecimal.valueOf(100000000L));
            if (success) {
                logger.info("FTBTokenTest mineToken success, mineDate:{}.", mineDate);
            }
            return success;
        } catch (Exception e) {
            logger.error("FTBTokenTest.mineToken error.", e);
            printAllBalances(userCreds, web3);
            throw new RuntimeException(e);
        }
    }

    private void userMine(Credentials from, List<Credentials> tos, double coin, Web3j web3) {
        try {
            List<RawTransaction> rawTnxList = new ArrayList<>();


            BigDecimal tokens = new BigDecimal(coin);
            Uint256 amount = new Uint256(tokens.multiply(BigDecimal.valueOf(DECIMALS_VALUE)).longValue());
            RawTransactionManager transactionManager = new RawTransactionManager(web3, from);
            Long futureEventNumber = transactionManager.getThanosLatestConsensusNumber() + TransactionConstant.DS_CHECK_MAX_FUTURE_NUM;
            //生成交易字符串
            for (Credentials to : tos) {

//                Random r = new SecureRandom();
//                BigInteger randomid = new BigInteger(250, r);
                UUID uuid = UUID.randomUUID();
                BigInteger randomid = new BigInteger(uuid.toString().replaceAll("-", ""), 16);

                Set<String> exeSta = new HashSet<>();
                exeSta.add(from.getAddress());
                exeSta.add(to.getAddress());
                String transferData = OperatingTokenProxy.transferData(new Address(to.getAddress()), amount, new Utf8String(getRandomString(10)), new Utf8String("20201216"));

                RawTransaction rawTransaction = RawTransaction.createTransaction(
                        randomid,
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(3000000),
                        b2cAddress,
                        transferData,
                        exeSta,
                        futureEventNumber);
                rawTransaction.setPublicKey(from.getSecureKey().getPubKey());
                rawTransaction.setCredentials(from);
                rawTnxList.add(rawTransaction);
            }
            //批量发送交易
            EthSendTransactionList res = transactionManager.SendTransactionList(rawTnxList);
            if (res == null || res.hasError()) {
                String error = "userMine send transaction with error:" + (res == null ? "" : res.getError().toString());
                throw new RuntimeException(error);
            }
            List<ThanosTransactionReceipt> receiptList = waitForTnxReceiptList(transactionManager, res.getTransactionHash());
            for (ThanosTransactionReceipt receipt : receiptList) {
                if (StringUtils.isNotBlank(receipt.getError())) {
                    throw new RuntimeException("userMine tnxReceipt has error:" + receipt.getError());
                }
            }
        } catch (Exception e) {
            logger.error("FTBTokenTest.userMine error.", e);
            printAllBalances(userCreds, web3);
            throw new RuntimeException("FTBTokenTest.userMine failed.");
        }
    }

    private void transferCircle(List<Credentials> userCreds, double coin, Web3j web3) {
        try {
            List<RawTransaction> rawTnxList = new ArrayList<>();


            BigDecimal tokens = new BigDecimal(coin);
            Uint256 amount = new Uint256(tokens.multiply(BigDecimal.valueOf(DECIMALS_VALUE)).longValue());
            RawTransactionManager transactionManager = new RawTransactionManager(web3, userCreds.get(0));
            Long futureEventNumber = transactionManager.getThanosLatestConsensusNumber() + TransactionConstant.DS_CHECK_MAX_FUTURE_NUM;
            //生成交易字符串
            for (int i = 0; i < userCreds.size(); i++) {
                Credentials from = userCreds.get(i);
                Credentials to = userCreds.get((i + 1) < userCreds.size() ? (i + 1) : 0);

//                Random r = new SecureRandom();
//                BigInteger randomid = new BigInteger(250, r);
                UUID uuid = UUID.randomUUID();
                BigInteger randomid = new BigInteger(uuid.toString().replaceAll("-", ""), 16);

                Set<String> exeSta = new HashSet<>();
                exeSta.add(from.getAddress());
                exeSta.add(to.getAddress());
                String transferData = OperatingTokenProxy.transferData(new Address(to.getAddress()), amount, new Utf8String(getRandomString(10)), new Utf8String("20201216"));

                RawTransaction rawTransaction = RawTransaction.createTransaction(
                        randomid,
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(30000000),
                        b2cAddress,
                        transferData,
                        exeSta,
                        futureEventNumber);
                rawTransaction.setPublicKey(from.getSecureKey().getPubKey());
                rawTransaction.setCredentials(from);
                rawTnxList.add(rawTransaction);
            }
            //批量发送交易
            EthSendTransactionList res = transactionManager.SendTransactionList(rawTnxList);
            if (res == null || res.hasError()) {
                String error = "transferCircle send transaction with error:" + (res == null ? "" : res.getError().toString());
                throw new RuntimeException(error);
            }
            Thread.sleep(2000);

            List<ThanosTransactionReceipt> receiptList = waitForTnxReceiptList(transactionManager, res.getTransactionHash());
            for (int i = 0; i < receiptList.size(); i++) {
                ThanosTransactionReceipt receipt = receiptList.get(i);
                if (StringUtils.isNotBlank(receipt.getError())) {
                    throw new RuntimeException("transferCircle tnxReceipt has error:" + receipt.getError());
                }
                List<FTBTokenProxy.TransferEventResponse> responses = FTBTokenProxy.getTransferEvents(receipt);
                if (CollectionUtils.isEmpty(responses)) {
                    throw new RuntimeException("transferCircle failed, receipt[" + i + "] has no transferEvent. ");
                }
            }
        } catch (Exception e) {
            logger.error("FTBTokenTest.transferCircle error.", e);
            printAllBalances(userCreds, web3);
            throw new RuntimeException("FTBTokenTest.transferCircle failed.");
        }
    }

    private void batchTransferCircle(List<Credentials> userCreds, int coin, Web3j web3) {
        try {
            List<RawTransaction> rawTnxList = new ArrayList<>();
            RawTransactionManager transactionManager = new RawTransactionManager(web3, FTBTokenDemo.userCreds.get(1));
            Long futureEventNumber = transactionManager.getThanosLatestConsensusNumber() + TransactionConstant.DS_CHECK_MAX_FUTURE_NUM;
            //生成交易
            for (int i = 0; i < userCreds.size(); i++) {
                Credentials from = userCreds.get(i);
                Credentials to = userCreds.get((i + 1) < FTBTokenDemo.userCreds.size() ? (i + 1) : 0);

                List<Address> toList = new ArrayList<>();
                List<Uint256> tnList = new ArrayList<>();
                List<Bytes32> traceIdList = new ArrayList<>();
                List<Bytes32> realtimeList = new ArrayList<>();
                for (int j = 0; j < coin; j++) {
                    toList.add(new Address(to.getAddress()));
                    tnList.add(oneToken);
                    traceIdList.add(stringToBytes32(getRandomString(10)));
                    realtimeList.add(stringToBytes32("20201215"));
                }
                String transferData = OperatingTokenProxy.batchTransferData(new DynamicArray<Address>(toList), new DynamicArray<Uint256>(tnList), new DynamicArray<Bytes32>(traceIdList), new DynamicArray<Bytes32>(realtimeList));
                Set<String> exeSta = new HashSet<>();
                exeSta.add(from.getAddress());
                exeSta.add(to.getAddress());

//                Random r = new SecureRandom();
//                BigInteger randomid = new BigInteger(250, r);
                UUID uuid = UUID.randomUUID();
                BigInteger randomid = new BigInteger(uuid.toString().replaceAll("-", ""), 16);

                RawTransaction rawTransaction = RawTransaction.createTransaction(
                        randomid,
                        BigInteger.valueOf(1),
                        BigInteger.valueOf(30000000),
                        b2cAddress,
                        transferData,
                        exeSta,
                        futureEventNumber);
                rawTransaction.setPublicKey(from.getSecureKey().getPubKey());
                rawTransaction.setCredentials(from);
                rawTnxList.add(rawTransaction);
            }

            //批量发送交易
            EthSendTransactionList res = transactionManager.SendTransactionList(rawTnxList);
            if (res == null || res.hasError()) {
                String error = "batchTransferCircle send transaction with error:" + (res == null ? "" : res.getError().toString());
                throw new RuntimeException(error);
            }


            List<ThanosTransactionReceipt> receiptList = waitForTnxReceiptList(transactionManager, res.getTransactionHash());
            for (int i = 0; i < receiptList.size(); i++) {
                ThanosTransactionReceipt receipt = receiptList.get(i);
                if (StringUtils.isNotBlank(receipt.getError())) {
                    throw new RuntimeException("batchTransferCircle tnxReceipt has error:" + receipt.getError());
                }
                List<FTBTokenProxy.TransferEventResponse> responses = FTBTokenProxy.getTransferEvents(receipt);
                if (CollectionUtils.isEmpty(responses) || responses.size() != coin) {
                    throw new RuntimeException("batchTransferCircle failed, receipt[" + i + "] miss transferEvent. except:" + coin + ", receive:" + responses.size());
                }
            }
        } catch (Exception e) {
            logger.error("FTBTokenTest.batchTransferCircle error.", e);
            printAllBalances(userCreds, web3);
            throw new RuntimeException("FTBTokenTest.batchTransferCircle failed.");
        }
    }

    private void checkBalances(List<Credentials> userCreds, BigDecimal targetBalance, Web3j web3) {
        for (int i = 0; i < userCreds.size(); i++) {
            Credentials user = userCreds.get(i);
            BigDecimal userBalance = balanceOf(user.getAddress(), web3);
            if (!targetBalance.equals(userBalance)) {
                logger.error("user[{}] balance isn't equal to target. userBalance:{}. targetBalance:{}.", i, userBalance, targetBalance);
            }
            assert targetBalance.equals(userBalance);
        }
    }

    private BigDecimal balanceOf(String account, Web3j web3) {
        try {
            OperatingTokenProxy proxy = OperatingTokenProxy.load(b2cAddress, web3, systemCred, SystemConstant.GAS_PRICE, SystemConstant.GAS_LIMIT);
            return BigDecimal.valueOf(proxy.balanceOf(new Address(account)).get().getValue().longValue()).divide(BigDecimal.valueOf(DECIMALS_VALUE));
        } catch (Exception e) {
            logger.error("FTBTokenTest.balanceOf exception. account: {}. e:{}.", account, e.getMessage());
        }
        return null;
    }

    private static List<Credentials> initUserCredentials(int userCount) {
        List<Credentials> userCreds = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            SecureKey user = SecureKey.getInstance("ECDSA", 1);
            Credentials cred = Credentials.create(user);
            userCreds.add(cred);
        }
        return userCreds;
    }


    public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }

    private void printAllBalances(List<Credentials> userCreds, Web3j web3) {
        for (int i = 0; i < userCreds.size(); i++) {
            BigDecimal balance = balanceOf(userCreds.get(i).getAddress(), web3);
            logger.info("printAllBalances user[{}] has balance:[{}].", i, balance);
        }
    }
}
