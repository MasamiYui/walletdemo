package cn.edu.cqupt.service;

import cn.edu.cqupt.Contract.ERC20Interface;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import sun.security.krb5.internal.crypto.Nonce;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Service
public class ERC20Service {

    @Autowired
    private Web3j web3j;

    /**
     * 转移token
     * @param tokenAddress
     * @param receiversAddress
     * @param token
     * @param keyStoreFile
     * @param password
     * @return
     * @throws Exception
     */
    public String transferToken(String tokenAddress,String receiversAddress, long token, File keyStoreFile, String password) throws Exception {

        Credentials credentials = WalletUtils.loadCredentials(password, keyStoreFile);
        ERC20Interface erc20Interface = new ERC20Interface(tokenAddress,web3j, credentials, ERC20Interface.GAS_PRICE, ERC20Interface.GAS_LIMIT);
        TransactionReceipt receipt = erc20Interface.transfer(receiversAddress, BigInteger.valueOf(token)).send();//选择同步发送
        return receipt.toString();
    }

    /**
     * 授权token
     * @param tokenAddress
     * @param receiversAddress
     * @param token
     * @param keyStoreFile
     * @param password
     * @return
     * @throws Exception
     */
    public String approveToken(String tokenAddress,String receiversAddress, long token, File keyStoreFile, String password) throws Exception {

        Credentials credentials = WalletUtils.loadCredentials(password, keyStoreFile);
        ERC20Interface erc20Interface = new ERC20Interface(tokenAddress,web3j, credentials, ERC20Interface.GAS_PRICE, ERC20Interface.GAS_LIMIT);
        TransactionReceipt receipt = erc20Interface.approve(receiversAddress, BigInteger.valueOf(token)).send();//选择同步发送
        return receipt.toString();
    }



    /**
     * 获取余额
     * @param tokenAddress
     * @param address
     * @return
     * @throws Exception
     */
    public String getBalance(String tokenAddress, String address) throws Exception {


/*
        ERC20Interface erc20Interface = new ERC20Interface(tokenAddress,web3j, (Credentials) new Object(), ERC20Interface.GAS_PRICE, ERC20Interface.GAS_LIMIT);
        long balance = erc20Interface.balanceOf(address).send().longValue();
*/
        final Function function = new Function(ERC20Interface.FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(address)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(null, tokenAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String returnValue = response.getValue(); //返回16进制余额
        returnValue = returnValue.substring(2);
        long balance = new BigInteger(returnValue, 16).longValue();


        return "tokenAddress:"+tokenAddress+
                "\n"+"searchAddress:"+address+
                "\n"+"balance:"+balance;
    }


    /**
     * 获取发行总量
     * @param tokenAddress
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String getTotalSupply(String tokenAddress) throws ExecutionException, InterruptedException {

        final Function function = new Function(ERC20Interface.FUNC_TOTALSUPPLY, Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(null, tokenAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String returnValue = response.getValue(); //返回16进制余额
        returnValue = returnValue.substring(2);
        long totalSupply = new BigInteger(returnValue, 16).longValue();

        return "tokenAddress:"+tokenAddress+
                "\n"+"totalSupply:"+totalSupply;

    }

    /**
     * 第三方转移
     * @param tokenAddress
     * @param from
     * @param to
     * @param token
     * @param keyStoreFile
     * @param password
     * @return
     * @throws Exception
     */
    public String TransferFrom(String tokenAddress,String from, String to, long token, File keyStoreFile, String password) throws Exception {

        Credentials credentials = WalletUtils.loadCredentials(password, keyStoreFile);
        ERC20Interface erc20Interface = new ERC20Interface(tokenAddress,web3j, credentials, ERC20Interface.GAS_PRICE, ERC20Interface.GAS_LIMIT);
        TransactionReceipt receipt = erc20Interface.transferFrom(from, to, BigInteger.valueOf(token)).send();
        return receipt.toString();
    }


    /**
     * 查询授权量
     * @param tokenAddress
     * @param tokenOwner
     * @param spender
     * @return
     */
    public String getAllowance(String tokenAddress, String tokenOwner, String spender) throws ExecutionException, InterruptedException {
        final Function function = new Function(ERC20Interface.FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(tokenOwner),
                        new org.web3j.abi.datatypes.Address(spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(null, tokenAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String returnValue = response.getValue(); //返回16进制余额
        returnValue = returnValue.substring(2);
        long allowance = new BigInteger(returnValue, 16).longValue();


        return "tokenAddress:"+tokenAddress+
                "\n"+"tokenOwner:"+tokenOwner+
                "\n"+"spender:"+spender+
                "\n"+"allowance:"+allowance;
    }

    public String listenErc20Transaction(String tokenAddress){

        ClientTransactionManager transactionManager = new ClientTransactionManager(web3j,
                tokenAddress);
        ERC20Interface token = ERC20Interface.load(tokenAddress, web3j, transactionManager,
                Contract.GAS_PRICE, Contract.GAS_LIMIT);
        token.transferEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
                .subscribe(tx -> {
                    System.out.println(tx.toString());
                });
        return null;
    }


    public String sendRawTransaction(String raw) throws IOException, ExecutionException, InterruptedException {


        //构造交易
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount( "0x3a00a73A0cF5Fc96f1903Acb2210A04aAA981FFb", DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger value = Convert.toWei("0", Convert.Unit.ETHER).toBigInteger();

        //BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, Contract.GAS_PRICE, Contract.GAS_LIMIT ,"0x02e8f1876713f78edda8704ffcf288c7334c04e0","0xa9059cbb00000000000000000000000002e8f1876713f78edda8704ffcf288c7334c04e00000000000000000000000000000000000000000000000000000000000000001");
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger("F43F2FFF347311B768762F052EF30C45306AAC801976EF4B90CB094919EACD11", 16));
        Credentials credentials = Credentials.create(ecKeyPair);
//签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        System.out.println(hexValue);
//发送
        hexValue = hexValue.substring(0,hexValue.length()-2)+"aa";//篡改
        System.out.println(hexValue);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        String hash = ethSendTransaction.getTransactionHash();
        return hash;
    }




}
