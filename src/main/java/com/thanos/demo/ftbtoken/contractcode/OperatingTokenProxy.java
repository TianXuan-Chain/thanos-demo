package com.thanos.demo.ftbtoken.contractcode;

import com.thanos.web3j.abi.FunctionEncoder;
import com.thanos.web3j.channel.client.TransactionSucCallback;
import com.thanos.web3j.abi.EventEncoder;
import com.thanos.web3j.abi.EventValues;
import com.thanos.web3j.abi.TypeReference;
import com.thanos.web3j.abi.datatypes.*;
import com.thanos.web3j.abi.datatypes.generated.Bytes32;
import com.thanos.web3j.abi.datatypes.generated.Uint256;
import com.thanos.web3j.crypto.Credentials;
import com.thanos.web3j.model.ThanosTransactionReceipt;
import com.thanos.web3j.protocol.Web3j;
import com.thanos.web3j.protocol.core.DefaultBlockParameter;
import com.thanos.web3j.protocol.core.methods.request.EthFilter;
import com.thanos.web3j.protocol.core.methods.response.Log;
import com.thanos.web3j.tx.Contract;
import com.thanos.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link com.thanos.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version none.
 */
public final class OperatingTokenProxy extends Contract {
    private static final String BINARY = "606060405232600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061261d806100546000396000f3006060604052361561011b576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806303e9e6091461012057806338b01d361461015757806363a599a41461018e57806370a08231146101a357806372e2f8f5146101f057806383cfab421461024157806386b8bd9e1461027a57806386d1a69f14610382578063893d20e81461039757806389afcb44146103ec5780638d1fdf2f146104255780638da5cb5b1461045e578063906a01d9146104b357806392250bdb146104d657806392eefe9b146105035780639a18b290146105545780639eb6216a14610616578063b865c5db14610639578063e584a6d4146106c8578063e9c02815146107fa578063f77c4791146108da575b600080fd5b341561012b57600080fd5b610141600480803590602001909190505061092f565b6040518082815260200191505060405180910390f35b341561016257600080fd5b6101786004808035906020019091905050610a71565b6040518082815260200191505060405180910390f35b341561019957600080fd5b6101a1610bb3565b005b34156101ae57600080fd5b6101da600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610d10565b6040518082815260200191505060405180910390f35b34156101fb57600080fd5b610227600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610e7e565b604051808215151515815260200191505060405180910390f35b341561024c57600080fd5b610278600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610fec565b005b341561028557600080fd5b610368600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050611170565b604051808215151515815260200191505060405180910390f35b341561038d57600080fd5b610395611356565b005b34156103a257600080fd5b6103aa6114b3565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156103f757600080fd5b610423600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506114dd565b005b341561043057600080fd5b61045c600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050611672565b005b341561046957600080fd5b6104716117f6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156104be57600080fd5b6104d4600480803590602001909190505061181c565b005b34156104e157600080fd5b6104e9611ace565b604051808215151515815260200191505060405180910390f35b341561050e57600080fd5b61053a600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050611c03565b604051808215151515815260200191505060405180910390f35b341561055f57600080fd5b610567611cd6565b604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200194505050505060405180910390f35b341561062157600080fd5b6106376004808035906020019091905050611e2a565b005b341561064457600080fd5b61064c6120dc565b604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390f35b34156106d357600080fd5b6107e0600480803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509190803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091905050612226565b604051808215151515815260200191505060405180910390f35b341561080557600080fd5b6108c0600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050612424565b604051808215151515815260200191505060405180910390f35b34156108e557600080fd5b6108ed6125cc565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156109be57600080fd5b6102c65a03f115156109cf57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166303e9e609836000604051602001526040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1515610a4f57600080fd5b6102c65a03f11515610a6057600080fd5b505050604051805190509050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610b0057600080fd5b6102c65a03f11515610b1157600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166338b01d36836000604051602001526040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1515610b9157600080fd5b6102c65a03f11515610ba257600080fd5b505050604051805190509050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610c4057600080fd5b6102c65a03f11515610c5157600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166363a599a46000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610cc657600080fd5b6102c65a03f11515610cd757600080fd5b50505060405180519050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a1565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610d9f57600080fd5b6102c65a03f11515610db057600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166370a08231836000604051602001526040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1515610e5c57600080fd5b6102c65a03f11515610e6d57600080fd5b505050604051805190509050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610f0d57600080fd5b6102c65a03f11515610f1e57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166372e2f8f5836000604051602001526040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1515610fca57600080fd5b6102c65a03f11515610fdb57600080fd5b505050604051805190509050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561107957600080fd5b6102c65a03f1151561108a57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166383cfab42826040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b151561112d57600080fd5b6102c65a03f1151561113e57600080fd5b5050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a150565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156111ff57600080fd5b6102c65a03f1151561121057600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff1663623454cf888888886000604051602001526040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001945050505050602060405180830381600087803b151561130057600080fd5b6102c65a03f1151561131157600080fd5b50505060405180519050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a1600190509695505050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156113e357600080fd5b6102c65a03f115156113f457600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166386d1a69f6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561146957600080fd5b6102c65a03f1151561147a57600080fd5b50505060405180519050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a1565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561156a57600080fd5b6102c65a03f1151561157b57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166389afcb44826000604051602001526040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b151561162757600080fd5b6102c65a03f1151561163857600080fd5b50505060405180519050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a150565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156116ff57600080fd5b6102c65a03f1151561171057600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16638d1fdf2f826040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b15156117b357600080fd5b6102c65a03f115156117c457600080fd5b5050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a150565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156118ab57600080fd5b6102c65a03f115156118bc57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff1663906a01d9836000604051602001526040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b151561193c57600080fd5b6102c65a03f1151561194d57600080fd5b50505060405180519050506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156119e557600080fd5b6102c65a03f115156119f657600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166338b01d36836000604051602001526040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1515611a7657600080fd5b6102c65a03f11515611a8757600080fd5b5050506040518051905090507f10f45883ab9dcd66f98d47956373560ad8ea291d5d726bd46f4b7ceca37f3dbb816040518082815260200191505060405180910390a15050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515611b5d57600080fd5b6102c65a03f11515611b6e57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166392250bdb6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515611be357600080fd5b6102c65a03f11515611bf457600080fd5b50505060405180519050905090565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16141515611c6157600080fd5b816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a160019050919050565b6000806000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515611d6957600080fd5b6102c65a03f11515611d7a57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16639a18b2906000604051608001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401608060405180830381600087803b1515611def57600080fd5b6102c65a03f11515611e0057600080fd5b50505060405180519060200180519060200180519060200180519050935093509350935090919293565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515611eb957600080fd5b6102c65a03f11515611eca57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16639eb6216a836000604051602001526040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1515611f4a57600080fd5b6102c65a03f11515611f5b57600080fd5b50505060405180519050506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515611ff357600080fd5b6102c65a03f1151561200457600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166303e9e609836000604051602001526040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b151561208457600080fd5b6102c65a03f1151561209557600080fd5b5050506040518051905090507f10f45883ab9dcd66f98d47956373560ad8ea291d5d726bd46f4b7ceca37f3dbb816040518082815260200191505060405180910390a15050565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561216e57600080fd5b6102c65a03f1151561217f57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff1663b865c5db6000604051606001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401606060405180830381600087803b15156121f457600080fd5b6102c65a03f1151561220557600080fd5b50505060405180519060200180519060200180519050925092509250909192565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156122b557600080fd5b6102c65a03f115156122c657600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff166388d695b286866000604051602001526040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019060200280838360005b8381101561236857808201518184015260208101905061234d565b50505050905001838103825284818151815260200191508051906020019060200280838360005b838110156123aa57808201518184015260208101905061238f565b50505050905001945050505050602060405180830381600087803b15156123d057600080fd5b6102c65a03f115156123e157600080fd5b50505060405180519050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a160019050949350505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663ce553b5c6000604051602001526040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156124b357600080fd5b6102c65a03f115156124c457600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff1663a9059cbb86866000604051602001526040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b151561257857600080fd5b6102c65a03f1151561258957600080fd5b50505060405180519050507f15504e11775a2cc1976320e3c666e5965057d329614934f0b707fc711dfb82ec60405160405180910390a160019050949350505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16815600a165627a7a72305820d9aae1806366b2e24aeebb3a4aa6c427a97bd386d9cf9162383f77636bd1bd570029";

    public static final String ABI = "[{\"constant\":true,\"inputs\":[{\"name\":\"time\",\"type\":\"uint256\"}],\"name\":\"getRecord\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"time\",\"type\":\"uint256\"}],\"name\":\"getOperatorRecord\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"emergencyStop\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"tokenOwner\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"checkFreeze\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"unFreeze\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"to\",\"type\":\"address\"},{\"name\":\"tokens\",\"type\":\"uint256\"},{\"name\":\"feeAccount\",\"type\":\"address\"},{\"name\":\"fee\",\"type\":\"uint256\"},{\"name\":\"traceId\",\"type\":\"string\"},{\"name\":\"realtime\",\"type\":\"string\"}],\"name\":\"transferWithFee\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"release\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getOwner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"burn\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"freeze\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"time\",\"type\":\"uint256\"}],\"name\":\"minerOperatorTokens\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getEmergency\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_controller\",\"type\":\"address\"}],\"name\":\"setController\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getMineInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"time\",\"type\":\"uint256\"}],\"name\":\"minerTokens\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getOperatorMineInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"tos\",\"type\":\"address[]\"},{\"name\":\"tns\",\"type\":\"uint256[]\"},{\"name\":\"traceIds\",\"type\":\"bytes32[]\"},{\"name\":\"realtimes\",\"type\":\"bytes32[]\"}],\"name\":\"batchTransfer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"to\",\"type\":\"address\"},{\"name\":\"tokens\",\"type\":\"uint256\"},{\"name\":\"traceId\",\"type\":\"string\"},{\"name\":\"realtime\",\"type\":\"string\"}],\"name\":\"transfer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"controller\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"mineSuccessEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[],\"name\":\"successEvent\",\"type\":\"event\"}]";

    private OperatingTokenProxy(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Boolean isInitByName) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit, isInitByName);
    }

    private OperatingTokenProxy(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Boolean isInitByName) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit, isInitByName);
    }

    private OperatingTokenProxy(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit, false);
    }

    private OperatingTokenProxy(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit, false);
    }

    public static List<MineSuccessEventEventResponse> getMineSuccessEventEvents(ThanosTransactionReceipt transactionReceipt) {
        final Event event = new Event("mineSuccessEvent",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<MineSuccessEventEventResponse> responses = new ArrayList<MineSuccessEventEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            MineSuccessEventEventResponse typedResponse = new MineSuccessEventEventResponse();
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<MineSuccessEventEventResponse> mineSuccessEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("mineSuccessEvent",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, MineSuccessEventEventResponse>() {
            @Override
            public MineSuccessEventEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                MineSuccessEventEventResponse typedResponse = new MineSuccessEventEventResponse();
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public static List<SuccessEventEventResponse> getSuccessEventEvents(ThanosTransactionReceipt transactionReceipt) {
        final Event event = new Event("successEvent",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<SuccessEventEventResponse> responses = new ArrayList<SuccessEventEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            SuccessEventEventResponse typedResponse = new SuccessEventEventResponse();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<SuccessEventEventResponse> successEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("successEvent",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, SuccessEventEventResponse>() {
            @Override
            public SuccessEventEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                SuccessEventEventResponse typedResponse = new SuccessEventEventResponse();
                return typedResponse;
            }
        });
    }

    public Future<Uint256> getRecord(Uint256 time) {
        Function function = new Function("getRecord",
                Arrays.<Type>asList(time),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Uint256> getOperatorRecord(Uint256 time) {
        Function function = new Function("getOperatorRecord",
                Arrays.<Type>asList(time),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> emergencyStop() {
        Function function = new Function("emergencyStop", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void emergencyStop(TransactionSucCallback callback) {
        Function function = new Function("emergencyStop", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<Uint256> balanceOf(Address tokenOwner) {
        Function function = new Function("balanceOf",
                Arrays.<Type>asList(tokenOwner),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Bool> checkFreeze(Address account) {
        Function function = new Function("checkFreeze",
                Arrays.<Type>asList(account),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> unFreeze(Address account) {
        Function function = new Function("unFreeze", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void unFreeze(Address account, TransactionSucCallback callback) {
        Function function = new Function("unFreeze", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<ThanosTransactionReceipt> transferWithFee(Address to, Uint256 tokens, Address feeAccount, Uint256 fee, Utf8String traceId, Utf8String realtime) {
        Function function = new Function("transferWithFee", Arrays.<Type>asList(to, tokens, feeAccount, fee, traceId, realtime), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void transferWithFee(Address to, Uint256 tokens, Address feeAccount, Uint256 fee, Utf8String traceId, Utf8String realtime, TransactionSucCallback callback) {
        Function function = new Function("transferWithFee", Arrays.<Type>asList(to, tokens, feeAccount, fee, traceId, realtime), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<ThanosTransactionReceipt> release() {
        Function function = new Function("release", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void release(TransactionSucCallback callback) {
        Function function = new Function("release", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<Address> getOwner() {
        Function function = new Function("getOwner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> burn(Address account) {
        Function function = new Function("burn", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void burn(Address account, TransactionSucCallback callback) {
        Function function = new Function("burn", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<ThanosTransactionReceipt> freeze(Address account) {
        Function function = new Function("freeze", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void freeze(Address account, TransactionSucCallback callback) {
        Function function = new Function("freeze", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<Address> owner() {
        Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> minerOperatorTokens(Uint256 time) {
        Function function = new Function("minerOperatorTokens", Arrays.<Type>asList(time), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void minerOperatorTokens(Uint256 time, TransactionSucCallback callback) {
        Function function = new Function("minerOperatorTokens", Arrays.<Type>asList(time), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<Bool> getEmergency() {
        Function function = new Function("getEmergency",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> setController(Address _controller) {
        Function function = new Function("setController", Arrays.<Type>asList(_controller), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void setController(Address _controller, TransactionSucCallback callback) {
        Function function = new Function("setController", Arrays.<Type>asList(_controller), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<List<Type>> getMineInfo() {
        Function function = new Function("getMineInfo",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> minerTokens(Uint256 time) {
        Function function = new Function("minerTokens", Arrays.<Type>asList(time), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void minerTokens(Uint256 time, TransactionSucCallback callback) {
        Function function = new Function("minerTokens", Arrays.<Type>asList(time), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<List<Type>> getOperatorMineInfo() {
        Function function = new Function("getOperatorMineInfo",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<ThanosTransactionReceipt> batchTransfer(DynamicArray<Address> tos, DynamicArray<Uint256> tns, DynamicArray<Bytes32> traceIds, DynamicArray<Bytes32> realtimes) {
        Function function = new Function("batchTransfer", Arrays.<Type>asList(tos, tns, traceIds, realtimes), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static String batchTransferData(DynamicArray<Address> tos, DynamicArray<Uint256> tns, DynamicArray<Bytes32> traceIds, DynamicArray<Bytes32> realtimes) {
        Function function = new Function("batchTransfer", Arrays.<Type>asList(tos, tns, traceIds, realtimes), Collections.<TypeReference<?>>emptyList());
        return FunctionEncoder.encode(function);
    }

    public void batchTransfer(DynamicArray<Address> tos, DynamicArray<Uint256> tns, DynamicArray<Bytes32> traceIds, DynamicArray<Bytes32> realtimes, TransactionSucCallback callback) {
        Function function = new Function("batchTransfer", Arrays.<Type>asList(tos, tns, traceIds, realtimes), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<ThanosTransactionReceipt> transfer(Address to, Uint256 tokens, Utf8String traceId, Utf8String realtime) {
        Function function = new Function("transfer", Arrays.<Type>asList(to, tokens, traceId, realtime), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static String transferData(Address to, Uint256 tokens, Utf8String traceId, Utf8String realtime) {
        Function function = new Function("transfer", Arrays.<Type>asList(to, tokens, traceId, realtime), Collections.<TypeReference<?>>emptyList());
        return FunctionEncoder.encode(function);
    }

    public void transfer(Address to, Uint256 tokens, Utf8String traceId, Utf8String realtime, TransactionSucCallback callback) {
        Function function = new Function("transfer", Arrays.<Type>asList(to, tokens, traceId, realtime), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<Address> controller() {
        Function function = new Function("controller",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<OperatingTokenProxy> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(OperatingTokenProxy.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<OperatingTokenProxy> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(OperatingTokenProxy.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static OperatingTokenProxy load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OperatingTokenProxy(contractAddress, web3j, credentials, gasPrice, gasLimit, false);
    }

    public static OperatingTokenProxy load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OperatingTokenProxy(contractAddress, web3j, transactionManager, gasPrice, gasLimit, false);
    }

    public static OperatingTokenProxy loadByName(String contractName, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OperatingTokenProxy(contractName, web3j, credentials, gasPrice, gasLimit, true);
    }

    public static OperatingTokenProxy loadByName(String contractName, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OperatingTokenProxy(contractName, web3j, transactionManager, gasPrice, gasLimit, true);
    }

    public static class MineSuccessEventEventResponse {
        public Uint256 amount;
    }

    public static class SuccessEventEventResponse {
    }
}
