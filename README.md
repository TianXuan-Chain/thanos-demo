天玄应用示例，详细使用教程见：https://netease-blockchain.gitbook.io/thanos-tech-docs/quick-start/deploy-thanos-app
# 使用
## 环境
- Oracle JDK 1.8
- Maven 3.3.9
- Intelj Idea
- 天玄链 (节点&网关)

## 运行
1. 需要使用到 thanos-web3j ，所以需要先配置 thanos-web3j.conf 文件，在 gateway 条目下，将自己的天玄节点链接填入
   ```
   http.ip.list = [
     #"127.0.0.1:8200"
   ]
   ```
2. 运行 SimpleTokenDemo.main ，看到如下信息
   ```
   SimpleTokenHttpTest.contractAddress:b5df3964d1bfc74d846d3456c9cd6f209c206a16
   ```
   表明应用合约部署成功，合约地址为：b5df3964d1bfc74d846d3456c9cd6f209c206a16
   ```
   transfer from: a14e6d3572df85a13ced0c7c4b8ed91240a54dd7
   tranfer to: 26c3f9eab8bb1348a6501029c6a0fd068bb0a791
   transfer value: 2
   SimpleTokenHttpTest user 0x26c3f9eab8bb1348a6501029c6a0fd068bb0a791, balance 2
   ```
   表明模拟 token 交易成功
