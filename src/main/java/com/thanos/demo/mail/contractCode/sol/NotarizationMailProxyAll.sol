pragma solidity ^0.4.11;

contract ExecuteResult {
    event successEvent();
}

contract Controller is ExecuteResult {

    address[] public admins;
    mapping(address => uint) adminMap;

    mapping(bytes32 => address) public registry;

    function Controller() public {
        admins.push(tx.origin);
        adminMap[tx.origin] = 1;
    }

    modifier onlyAdmin {
        require(adminMap[tx.origin] == 1);
        _;
    }

    modifier checkAdminAmount {
        require(admins.length >= 2);
        _;
    }

    function addAdmin(address account) public onlyAdmin {
        require(adminMap[account] == 0);
        admins.push(account);
        adminMap[account] = 1;
        successEvent();
    }

    function deleteAdmin(address account) public onlyAdmin checkAdminAmount {
        for (uint i = 0; i < admins.length; i++) {
            if (admins[i] == account) {
                delete admins[i];
            }
        }
        adminMap[account] = 0;
        successEvent();
    }

    function getAdminList() public constant returns (address[]){
        return admins;
    }

    function checkAdmin(address account) public constant returns (bool){
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

    function register(string _name, address _address) public onlyAdmin returns (bool) {
        bytes32 nameBtye = stringToBytes32(_name);
        require(registry[nameBtye] == 0);
        registry[nameBtye] = _address;
        successEvent();
        return true;
    }

    function update(string _name, address _address) public onlyAdmin returns (bool) {
        bytes32 nameBtye = stringToBytes32(_name);
        registry[nameBtye] = _address;
        successEvent();
        return true;
    }

    function lookup(string _name) public constant returns (address) {
        bytes32 nameBtye = stringToBytes32(_name);
        return registry[nameBtye];
    }

    function getNotarizationMailStorage() public returns (address){
        return lookup("NotarizationMailStorage");
    }

    function getNotarizationMailHandler() public returns (address){
        return lookup("NotarizationMailHandler");
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

    function getOwner() public constant returns (address) {
        return owner;
    }

    function setController(address _controller) public onlyOwner returns (bool) {
        controller = _controller;
        successEvent();
        return true;
    }

}

//公正邮存储合约
contract NotarizationMailStorage is Controlled {

    struct Info {
        string emailNumber;
        string emailFingerprint;
        string emailHash;
        address createAddress;
        string extendInfo;
    }

    mapping(string => Info) mailMap;

    //底层存储
    function store(string _emailNumber, string _emailFingerprint, string _emailHash, string _extendInfo) public onlyCaller("NotarizationMailHandler") returns (bool){
//        if (mailMap[_emailNumber].createAddress != address(0x0)) {
//            return false;
//        }

        mailMap[_emailNumber] = Info(_emailNumber, _emailFingerprint, _emailHash, tx.origin, _extendInfo);

        successEvent();
        return true;
    }

    //修改指纹信息 非必要
    function update(string _emailNumber, string _emailFingerprint, string _emailHash, string _extendInfo) public onlyCaller("NotarizationMailHandler") returns (bool){
        if (mailMap[_emailNumber].createAddress == address(0x0)) {
            return false;
        }

        mailMap[_emailNumber].emailFingerprint = _emailFingerprint;
        mailMap[_emailNumber].emailHash = _emailHash;
        mailMap[_emailNumber].extendInfo = _extendInfo;

        successEvent();
        return true;
    }

    //底层查询接口
    function query(string _emailNumber) public constant returns (string, string, string, address, string){
        return (mailMap[_emailNumber].emailNumber, mailMap[_emailNumber].emailFingerprint, mailMap[_emailNumber].emailHash,
        mailMap[_emailNumber].createAddress, mailMap[_emailNumber].extendInfo);
    }

    //查询指纹
    function querySample(string _emailNumber) public constant returns (string, string, string){
        return (mailMap[_emailNumber].emailNumber, mailMap[_emailNumber].emailFingerprint, mailMap[_emailNumber].emailHash);
    }

    //存证数据是否存在
    function exist(string _emailNumber) public constant returns (bool){
        return (mailMap[_emailNumber].createAddress != address(0x0));
    }
}

//公正邮handler
contract NotarizationMailHandler is Controlled {

    function store(string _emailNumber, string _emailFingerprint, string _emailHash, string _extendInfo)  returns (bool){
        return NotarizationMailStorage(Controller(controller).getNotarizationMailStorage()).store(_emailNumber, _emailFingerprint, _emailHash, _extendInfo);
    }

    function exist(string _emailNumber) public constant returns (bool){
        return NotarizationMailStorage(Controller(controller).getNotarizationMailStorage()).exist(_emailNumber);
    }
}

//公正邮代理
contract NotarizationMailProxy is Controlled {

    function store(string _emailNumber, string _emailFingerprint, string _emailHash, string _extendInfo) returns (bool){
        return NotarizationMailHandler(Controller(controller).getNotarizationMailHandler()).store(_emailNumber, _emailFingerprint, _emailHash, _extendInfo);
    }

    function exist(string _emailNumber) public constant returns (bool){
        return NotarizationMailHandler(Controller(controller).getNotarizationMailStorage()).exist(_emailNumber);
    }
}

//注册or修改NotarizationMailProxy
contract NotarizationMailProxyFactory {
    event createSuccessEvent(address addr);

    address public controller;

    function NotarizationMailProxyFactory(address _controller){
        controller = _controller;
    }

    function createNotarizationMailProxy() public returns (address){
        NotarizationMailProxy proxy = new NotarizationMailProxy();
        Controller(controller).register("NotarizationMailProxy", proxy);
        proxy.setController(controller);
        createSuccessEvent(proxy);
        return proxy;
    }

    function updateNotarizationMailProxy() public returns (address) {
        NotarizationMailProxy proxy = new NotarizationMailProxy();
        Controller(controller).update("NotarizationMailProxy", proxy);
        proxy.setController(controller);
        createSuccessEvent(proxy);
        return proxy;
    }
}

//注册or修改NotarizationMailHandler
contract NotarizationMailHandlerFactory {
    event createSuccessEvent(address addr);

    address public controller;

    function NotarizationMailHandlerFactory(address _controller){
        controller = _controller;
    }

    function createHandler() public returns (address){
        NotarizationMailHandler handler = new NotarizationMailHandler();
        Controller(controller).register("NotarizationMailHandler", handler);
        handler.setController(controller);
        createSuccessEvent(handler);
        return handler;
    }

    function updateHandler() public returns (address) {
        NotarizationMailHandler handler = new NotarizationMailHandler();
        Controller(controller).update("NotarizationMailHandler", handler);
        handler.setController(controller);
        createSuccessEvent(handler);
        return handler;
    }
}


//注册NotarizationMailStorage
contract NotarizationMailStorageFactory {
    event createSuccessEvent(address addr);

    address public controller;

    function NotarizationMailStorageFactory(address _controller){
        controller = _controller;
    }

    function createMailStorage() public returns (address) {
        NotarizationMailStorage mailStorage = new NotarizationMailStorage();
        Controller(controller).register("NotarizationMailStorage", mailStorage);
        mailStorage.setController(controller);
        createSuccessEvent(mailStorage);
        return mailStorage;
    }
}
