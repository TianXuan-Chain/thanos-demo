pragma solidity ^0.4.16;

contract ExecuteResult {
    event successEvent();
}

library SafeMath {
    function add(uint a, uint b) internal returns (uint c) {
        c = a + b;
        require(c >= a);
    }
    function sub(uint a, uint b) internal returns (uint c) {
        require(b <= a);
        c = a - b;
    }
    function mul(uint a, uint b) internal returns (uint c) {
        c = a * b;
        require(a == 0 || c / a == b);
    }
    function div(uint a, uint b) internal returns (uint c) {
        require(b > 0);
        c = a / b;
    }
}

contract Controller is ExecuteResult {

    address[] public admins;
    mapping(address=>uint) adminMap;

    mapping(bytes32 => address) public registry;

    function Controller() public {
        admins.push(tx.origin);
        adminMap[tx.origin] = 1;
    }

    modifier onlyAdmin {
        require(adminMap[tx.origin]==1);
        _;
    }

    modifier checkAdminAmount {
        require(admins.length >=2);
        _;
    }

    function addAdmin(address account) public onlyAdmin{
        require(adminMap[account] == 0);
        admins.push(account);
        adminMap[account] = 1;
        successEvent();
    }

    function deleteAdmin(address account) public onlyAdmin checkAdminAmount {
        for(uint i=0;i<admins.length;i++){
            if(admins[i]==account){
                delete admins[i];
            }
        }
        adminMap[account] = 0;
        successEvent();
    }

    function getAdminList() public constant returns(address[]){
        return admins;
    }

    function checkAdmin(address account) public constant returns(bool){
        return adminMap[account] == 1;
    }

    function stringToBytes32(string memory source) public returns (bytes32 result) {
        bytes memory tempEmptyStringTest = bytes(source);
        if (tempEmptyStringTest.length == 0) {
            return 0x0;
        }
        assembly {
            result := mload(add(source, 32))
        }
    }

    function register(string _name, address _address) public onlyAdmin returns(bool) {
        bytes32 nameBtye = stringToBytes32(_name);
        require(registry[nameBtye]==0);
        registry[nameBtye] = _address;
        successEvent();
        return true;
    }

    function update(string _name, address _address) public onlyAdmin returns(bool) {
        bytes32 nameBtye = stringToBytes32(_name);
        registry[nameBtye] = _address;
        successEvent();
        return true;
    }

    function lookup(string _name) public constant returns (address) {
        bytes32 nameBtye = stringToBytes32(_name);
        return registry[nameBtye];
    }

    function getFTBTokenHandler() public returns(address){
        return lookup("FTBTokenHandler");
    }

    function getFTBToken() public returns(address){
        return lookup("FTBToken");
    }

    function getAwardPool() public returns(address){
        return lookup("AwardPool");
    }

    function getFreeze() public returns(address){
        return lookup("Freeze");
    }

}

contract Controlled is ExecuteResult {

    address public controller;

    address public owner;

    modifier onlyAdmin(address _user){
        require(Controller(controller).checkAdmin(_user));
        _;
    }

    modifier onlyCaller(string name) {
        require(msg.sender == Controller(controller).lookup(name));
        _;
    }

    modifier onlyOwner {
        require(tx.origin == owner);
        _;
    }

    function Controlled() public {
        owner = tx.origin;
    }

    function getOwner() public constant returns(address) {
        return owner;
    }

    function setController(address _controller) public onlyOwner returns(bool) {
        controller = _controller;
        successEvent();
        return true;
    }

}

contract Freeze is Controlled {

    address[] public freezes;
    mapping(address => uint8) frozedMap;

    // 冻结账户
    function freeze(address account) public onlyAdmin(tx.origin) {
        require(frozedMap[account] == 0);
        freezes.push(account);
        frozedMap[account] = 1;
        successEvent();
    }

    // 解冻账户
    function unFreeze(address account) public onlyAdmin(tx.origin) {
        for(uint i=0;i<freezes.length;i++){
            if(freezes[i]==account){
                delete freezes[i];
            }
        }
        frozedMap[account] = 0;
        successEvent();
    }

    // 校验是否冻结账户
    function checkFreeze(address account) public constant returns(bool){
        return frozedMap[account]==1;
    }

}

contract AwardPool is Controlled {

    using SafeMath for uint;

    // 每日tokens
    uint dayTokens;
    // 衰减天数
    uint reductionDays;
    // 衰减率
    uint reductionRate;
    // 已经完成分发天数
    uint digDays;
    // 挖取日期
    mapping(uint=> uint) recordMap;

    // 运营-每日tokens
    uint operateDayTokens;
    // 运营-已经完成分发天数
    uint operateDigDays;
    // 运营-挖取日期
    mapping(uint=> uint) operateRecordMap;

    // 玩家矿池分配账户 nb001
    address _distributor;
    // 伏羲币用户币销毁账户
    address _surpluser;
    // 伏羲币系统提币操作账户
    address _extractor;
    //伏羲币游戏方可支配账号,每日解封6万到该账号
    address _operator;

    /**
     * dayTokens     每天挖取
     * reductionDays 每2年衰减
     * reductionRate 衰减1/2
     * digDays       挖矿天数
     */
    function AwardPool() public {
        dayTokens = 50000 * 100000000;
        reductionDays = 365 * 2;
        reductionRate = 2;
        digDays = 0;
        _extractor = 0xa14e6d3572df85a13ced0c7c4b8ed91240a54dd7;
        _distributor = 0x11b8b06a8ff4f057df6ae9754380314a5a9f476e;
        _surpluser = 0x71830ac09ed6de1ed44c5215fb6bde5d9ebdc820;
        operateDayTokens = 61000 * 100000000;
        operateDigDays = 0;
        _operator = 0x7df8e507f088b122bdad6c5bcf99c7f6f2a619af;
    }

    // 计算应发tokens量
    function calMinerDigTokens() public returns(uint){
        if (digDays <= reductionDays) {
            return dayTokens;
        } else {
            uint t = digDays.div(reductionDays);
            uint rate = 1;
            for (uint i = 0; i < t; i++) {
                rate *= reductionRate;
            }
            return dayTokens.div(rate);
        }
    }

    // 运营-计算应发tokens量
    function calOperateDigTokens() public returns(uint){
        if (operateDigDays <= reductionDays) {
            return operateDayTokens;
        } else {
            uint t = operateDigDays.div(reductionDays);
            uint rate = 1;
            for (uint i = 0; i < t; i++) {
                rate *= reductionRate;
            }
            return operateDayTokens.div(rate);
        }
    }

    /**
     * 仅FTBTokenhandler调用挖矿
     * 幂等控制，每天仅能挖取一次
     * 计算出今天应该挖取得量
     */
    function minerTokens(uint time) public onlyCaller("FTBTokenHandler") returns(uint tokens){
        // 控制每天不能重复挖取
        require(_distributor != address(0));
        require(recordMap[time] == 0);
        digDays++;
        uint tokenAmount = calMinerDigTokens();
        recordMap[time] = tokenAmount;
        return tokenAmount;
    }

    /**
     * 运营 -
     * 仅FTBTokenhandler调用挖矿
     * 幂等控制，每天仅能挖取一次
     * 计算出今天应该挖取得量
     */
    function minerOperatorTokens(uint time) public onlyCaller("FTBTokenHandler") returns(uint tokens){
        // 控制每天不能重复挖取
        require(operateRecordMap[time] == 0);
        operateDigDays++;
        uint tokenAmount = calOperateDigTokens();
        operateRecordMap[time] = tokenAmount;
        return tokenAmount;
    }

    // 获取挖矿信息
    function getMineInfo() public constant returns(address, address, address, uint){
        return (_extractor, _distributor, _surpluser, digDays);
    }

    // 获取挖矿信息
    function getRecord(uint time) public constant returns(uint){
        return recordMap[time];
    }

    // 分发账户
    function getDistributor() public constant returns(address) {
        return _distributor;
    }

    function getExtractor() public constant returns(address){
        return _extractor;
    }

    // 获取运营账户
    function getOperatorMineInfo() public constant returns (address, address, uint){
        return (_extractor, _operator, operateDigDays);
    }

    // 获取运营挖矿信息
    function getOperatorRecord(uint time) public constant returns (uint) {
        return operateRecordMap[time];
    }

    // 获取解封运营账户
    function getOperator() public constant returns (address){
        return _operator;
    }

    // 校验提币账户
    function checkExtractor(address _user) public constant returns(bool){
        return _user == _extractor ? true : false;
    }

}

contract ERC20Interface {
    function totalSupply() public constant returns (uint);
    function balanceOf(address tokenOwner) public constant returns (uint balance);
    function transfer(address to, uint tokens) public returns (bool success);
    function allowance(address tokenOwner, address spender) public constant returns (uint remaining);
    function approve(address spender, uint tokens) public returns (bool success);
    function transferFrom(address from, address to, uint tokens) public returns (bool success);

    event Transfer(address indexed from, address indexed to, uint tokens);
    event Approval(address indexed tokenOwner, address indexed spender, uint tokens);
}

contract IEmergency{
    function getEmergency() public constant returns(bool);
    function emergencyStop() public returns(bool);
    function release() public returns(bool);
}

contract FTBToken is IEmergency, Controlled, ERC20Interface {

    using SafeMath for uint;

    string symbol;
    string name;
    uint8 decimals;
    uint _totalSupply;

    mapping(address => uint) balances;
    mapping(address => mapping(address => uint)) allowed;

    bool emergency = false;

    address operator;

    modifier checkEmergency{
        require(emergency == false);
        _;
    }

    modifier checkNonOperator{
        require(tx.origin != operator);
        _;
    }

    function FTBToken() public {
        symbol = "FTB";
        name = "FTB tokens";
        decimals = 8;
        _totalSupply = (73000000 + 90000000) * 100000000;
        balances[this] = 73000000 * 100000000;
        operator = 0x14b70566d9825b8849fb013bd2eb7e0d74503025;
        balances[operator] = 90000000 * 100000000;
        Transfer(address(0), tx.origin, _totalSupply);
    }

    function totalSupply() public constant returns (uint) {
        return _totalSupply;
    }

    function balanceOf(address tokenOwner) public constant returns (uint balance) {
        return balances[tokenOwner];
    }

    function transfer(address to, uint tokens) public checkEmergency checkNonOperator onlyCaller("FTBTokenHandler") returns (bool success) {
        balances[tx.origin] = balances[tx.origin].sub(tokens);
        balances[to] = balances[to].add(tokens);
        Transfer(tx.origin, to, tokens);
        return true;
    }

    function transferWithFee(address to, uint tokens,  address feeAccount, uint fee) public checkEmergency checkNonOperator onlyCaller("FTBTokenHandler") returns (bool success) {
        require(balances[tx.origin] > tokens + fee);
        balances[tx.origin] = balances[tx.origin].sub(tokens);
        balances[to] = balances[to].add(tokens);
        balances[tx.origin] = balances[tx.origin].sub(fee);
        balances[feeAccount] = balances[feeAccount].add(fee);
        Transfer(tx.origin, to, tokens);
        Transfer(tx.origin, feeAccount, fee);
        return true;
    }

    function approve(address spender, uint tokens) public checkEmergency checkNonOperator onlyCaller("FTBTokenHandler") returns (bool success) {
        allowed[tx.origin][spender] = tokens;
        Approval(tx.origin, spender, tokens);
        return true;
    }

    function transferFrom(address from, address to, uint tokens) public checkEmergency onlyCaller("FTBTokenHandler") returns (bool success) {
        balances[from] = balances[from].sub(tokens);
        allowed[from][tx.origin] = allowed[from][tx.origin].sub(tokens);
        balances[to] = balances[to].add(tokens);
        Transfer(from, to, tokens);
        return true;
    }

    function allowance(address tokenOwner, address spender) public constant returns (uint remaining) {
        return allowed[tokenOwner][spender];
    }

    // 提取合约内token到指定账户
    function minerTokens(address to, uint tokens) public checkEmergency onlyCaller("FTBTokenHandler") returns (bool success) {
        balances[this] = balances[this].sub(tokens);
        balances[to] = balances[to].add(tokens);
        Transfer(this, to, tokens);
        return true;
    }

    // 提取运营池内token到指定账户
    function mineOperateTokens(address to, uint tokens) public checkEmergency onlyCaller("FTBTokenHandler") returns (bool success) {
        balances[operator] = balances[operator].sub(tokens);
        balances[to] = balances[to].add(tokens);
        Transfer(operator, to, tokens);
        return true;
    }

    // 销毁指定账户内token
    function burn(address account)public checkEmergency onlyCaller("FTBTokenHandler") returns (bool success){
        balances[address(0)] += balances[account];
        _totalSupply -= balances[account];
        balances[account] = 0;
        return true;
    }

    function getEmergency() public constant returns(bool){
        return emergency;
    }

    function emergencyStop() public onlyCaller("FTBTokenHandler") returns(bool){
        emergency = true;
        return true;
    }

    function release() public onlyCaller("FTBTokenHandler") returns(bool){
        emergency = false;
        return true;
    }

}

contract FTBTokenHandler is Controlled {

    // 校验是否冻结账户
    modifier checkAccountFreeze(address _user){
        require(!Freeze(Controller(controller).getFreeze()).checkFreeze(_user));
        _;
    }

    // 校验是否提币账户
    modifier checkExtractor(address _user){
        require(AwardPool(Controller(controller).getAwardPool()).checkExtractor(_user));
        _;
    }

    function totalSupply() public constant returns (uint) {
        return FTBToken(Controller(controller).getFTBToken()).totalSupply();
    }

    function balanceOf(address tokenOwner) public constant returns (uint balance) {
        return FTBToken(Controller(controller).getFTBToken()).balanceOf(tokenOwner);
    }

    // 转账校验双方是否冻结账户
    function transfer(address to, uint tokens) public  checkAccountFreeze(tx.origin) checkAccountFreeze(to) returns(bool){
        FTBToken(Controller(controller).getFTBToken()).transfer(to, tokens);
        return true;
    }

    function batchTransfer(address[] tos, uint[] tns) public checkAccountFreeze(tx.origin) returns (bool){
        for (uint i = 0; i < tos.length; i++) {
            require(!Freeze(Controller(controller).getFreeze()).checkFreeze(tos[i]));
            FTBToken(Controller(controller).getFTBToken()).transfer(tos[i], tns[i]);
        }
        return true;
    }

    function transferWithFee(address to, uint tokens,  address feeAccount, uint fee) public  checkAccountFreeze(tx.origin) checkAccountFreeze(to) returns(bool){
        FTBToken(Controller(controller).getFTBToken()).transferWithFee(to, tokens, feeAccount, fee);
        return true;
    }

    // 授权校验双方是否冻结账户
    function approve(address spender, uint tokens) public  checkAccountFreeze(tx.origin) checkAccountFreeze(spender) returns(bool){
        FTBToken(Controller(controller).getFTBToken()).approve(spender, tokens);
        return true;
    }

    // 授权转账校验双方是否冻结账户
    function transferFrom(address from, address to, uint tokens) public  checkAccountFreeze(from) checkAccountFreeze(to) returns(bool){
        FTBToken(Controller(controller).getFTBToken()).transferFrom(from, to, tokens);
        return true;
    }

    function allowance(address tokenOwner, address spender) public constant returns (uint remaining) {
        return FTBToken(Controller(controller).getFTBToken()).allowance(tokenOwner, spender);
    }

    // 校验必须提币账户挖矿
    function minerTokens(uint time) public  checkExtractor(tx.origin) returns(bool) {
        AwardPool ap = AwardPool(Controller(controller).getAwardPool());
        uint tokens = ap.minerTokens(time);
        address miner = ap.getDistributor();
        FTBToken(Controller(controller).getFTBToken()).minerTokens(miner, tokens);
        return true;
    }

    // 校验必须提币账户挖矿
    function minerOperatorTokens(uint time) public  checkExtractor(tx.origin) returns(bool) {
        AwardPool ap = AwardPool(Controller(controller).getAwardPool());
        uint tokens = ap.minerOperatorTokens(time);
        address miner = ap.getOperator();
        FTBToken(Controller(controller).getFTBToken()).mineOperateTokens(miner, tokens);
        return true;
    }

    // 仅管理员销毁代币
    function burn(address account) public onlyAdmin(tx.origin) returns(bool) {
        FTBToken(Controller(controller).getFTBToken()).burn(account);
        return true;
    }

    function getEmergency() public constant returns(bool){
        return FTBToken(Controller(controller).getFTBToken()).getEmergency();
    }

    function emergencyStop() public onlyAdmin(tx.origin) returns(bool){
        return FTBToken(Controller(controller).getFTBToken()).emergencyStop();
    }

    function release() public onlyAdmin(tx.origin) returns(bool){
        return FTBToken(Controller(controller).getFTBToken()).release();
    }

    // 冻结账户
    function freeze(address account) public onlyAdmin(tx.origin) {
        Freeze(Controller(controller).getFreeze()).freeze(account);
    }

    // 解冻账户
    function unFreeze(address account) public onlyAdmin(tx.origin) {
        Freeze(Controller(controller).getFreeze()).unFreeze(account);
    }

    // 校验是否冻结账户
    function checkFreeze(address account) public constant returns(bool){
        return Freeze(Controller(controller).getFreeze()).checkFreeze(account);
    }

    // 获取挖矿信息
    function getMineInfo() public constant returns(address, address, address, uint){
        return AwardPool(Controller(controller).getAwardPool()).getMineInfo();
    }

    // 获取挖矿信息
    function getRecord(uint time) public constant returns(uint){
        return AwardPool(Controller(controller).getAwardPool()).getRecord(time);
    }

    function getOperatorMineInfo() public constant returns(address, address, uint){
        return AwardPool(Controller(controller).getAwardPool()).getOperatorMineInfo();
    }

    function getOperatorRecord(uint time) public constant returns(uint){
        return AwardPool(Controller(controller).getAwardPool()).getOperatorRecord(time);
    }

}

contract FTBTokenProxy is Controlled, ERC20Interface {

    function totalSupply() public constant returns (uint) {
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).totalSupply();
    }

    function balanceOf(address tokenOwner) public constant returns (uint balance) {
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).balanceOf(tokenOwner);
    }

    function transfer(address to, uint tokens) public returns(bool){
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).transfer(to, tokens);
        successEvent();
        return true;
    }

    function approve(address spender, uint tokens) public returns(bool){
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).approve(spender, tokens);
        successEvent();
        return true;
    }

    function transferFrom(address from, address to, uint tokens) public returns(bool){
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).transferFrom(from, to, tokens);
        successEvent();
        return true;
    }

    function allowance(address tokenOwner, address spender) public constant returns (uint remaining) {
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).allowance(tokenOwner, spender);
    }

}

contract OperatingTokenProxy is Controlled {

    event mineSuccessEvent(uint amount);

    function transfer(address to, uint tokens, string traceId, string realtime) public returns(bool){
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).transfer(to, tokens);
        successEvent();
        return true;
    }

    function batchTransfer(address[] tos, uint[] tns, bytes32[] traceIds, bytes32[] realtimes) public returns(bool) {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).batchTransfer(tos, tns);
        successEvent();
        return true;
    }

    function transferWithFee(address to, uint tokens,  address feeAccount, uint fee, string traceId, string realtime) public returns(bool){
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).transferWithFee(to, tokens, feeAccount, fee);
        successEvent();
        return true;
    }

    function balanceOf(address tokenOwner) public constant returns (uint balance) {
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).balanceOf(tokenOwner);
    }

    function minerTokens(uint time) public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).minerTokens(time);
        uint _amount = FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getRecord(time);
        mineSuccessEvent(_amount);
    }

    function minerOperatorTokens(uint time) public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).minerOperatorTokens(time);
        uint _amount = FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getOperatorRecord(time);
        mineSuccessEvent(_amount);
    }

    // 获取挖矿信息
    function getRecord(uint time) public constant returns(uint){
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getRecord(time);
    }

    // 获取挖矿信息
    function getMineInfo() public constant returns(address, address, address, uint){
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getMineInfo();
    }

    function getOperatorRecord(uint time) public constant returns(uint){
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getOperatorRecord(time);
    }

    function getOperatorMineInfo() public constant returns(address, address, uint){
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getOperatorMineInfo();
    }

    function burn(address account) public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).burn(account);
        successEvent();
    }

    function emergencyStop() public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).emergencyStop();
        successEvent();
    }

    function release() public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).release();
        successEvent();
    }

    function getEmergency() public constant returns(bool){
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).getEmergency();
    }

    // 冻结账户
    function freeze(address account) public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).freeze(account);
        successEvent();
    }

    // 解冻账户
    function unFreeze(address account) public {
        FTBTokenHandler(Controller(controller).getFTBTokenHandler()).unFreeze(account);
        successEvent();
    }

    // 校验是否冻结账户
    function checkFreeze(address account) public constant returns(bool){
        return FTBTokenHandler(Controller(controller).getFTBTokenHandler()).checkFreeze(account);
    }

}

contract FTBControllerFactory{

    address public controller;
    event createSuccessEvent(address addr);

    function getController() public returns(address){
        return controller;
    }

    function createController() public returns(address){
        Controller ct = new Controller();
        controller = ct;
        createSuccessEvent(ct);
        return controller;
    }

}

contract FTBProxyFactory {

    address public controller;
    event createSuccessEvent(address addr);

    function FTBProxyFactory(address _controller){
        controller = _controller;
    }

    function createC2CProxy() public returns(address){
        FTBTokenProxy proxy = new FTBTokenProxy();
        Controller(controller).register("FTBTokenProxy", proxy);
        proxy.setController(controller);
        createSuccessEvent(proxy);
        return proxy;
    }

    function createB2CProxy() public returns(address){
        OperatingTokenProxy proxy = new OperatingTokenProxy();
        Controller(controller).register("OperatingTokenProxy", proxy);
        proxy.setController(controller);
        createSuccessEvent(proxy);
        return proxy;
    }

}

contract FTBHandlerFactory{

    address public controller;
    event createSuccessEvent(address addr);

    function FTBHandlerFactory(address _controller){
        controller = _controller;
    }

    function createHandler() public returns(address){
        FTBTokenHandler handler = new FTBTokenHandler();
        Controller(controller).register("FTBTokenHandler", handler);
        handler.setController(controller);
        createSuccessEvent(handler);
        return handler;
    }

}

contract FTBTokenFactory{

    address public controller;
    event createSuccessEvent(address addr);

    function FTBTokenFactory(address _controller){
        controller = _controller;
    }

    function createToken() public returns(address) {
        FTBToken token = new FTBToken();
        token.setController(controller);
        Controller(controller).register("FTBToken", token);
        createSuccessEvent(token);
        return token;
    }

}

contract FTBAwardPoolFactory {

    address public controller;
    event createSuccessEvent(address addr);

    function FTBAwardPoolFactory(address _controller){
        controller = _controller;
    }

    function createMine() public returns(address){
        AwardPool ap = new AwardPool();
        Controller(controller).register("AwardPool", ap);
        ap.setController(controller);
        createSuccessEvent(ap);
        return ap;
    }

    function createFreeze() public returns(address){
        Freeze f = new Freeze();
        f.setController(controller);
        Controller(controller).register("Freeze", f);
        createSuccessEvent(f);
        return f;
    }

}



