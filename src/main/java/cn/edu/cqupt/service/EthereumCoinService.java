package cn.edu.cqupt.service;

import cn.edu.cqupt.Contract.ERC20Interface;
import cn.edu.cqupt.utils.MnemonicUtils;
import cn.edu.cqupt.utils.SecureRandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.web3j.crypto.WalletUtils.generateWalletFile;

@Service
public class EthereumCoinService {

    @Autowired
    private Web3j web3j;

    /**
     * bip39 Wallet
     * @param password
     * @param path
     * @return
     * @throws CipherException
     * @throws IOException
     */
    public String createBip39Wallet(String password, String path) throws CipherException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        SecureRandom secureRandom = SecureRandomUtils.secureRandom();
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair privateKey = ECKeyPair.create(Hash.sha256(seed));
        String walletFile = generateWalletFile(password, privateKey, new File(path), false);
        Bip39Wallet bip39Wallet = new Bip39Wallet(walletFile, mnemonic);
        return bip39Wallet.toString();
    }

    /**
     * 转移以太币
     * @param receiversAddress
     * @param ethers
     * @param mnemonic
     * @param password
     * @return
     * @throws Exception
     */
    public String transferEthCoin(String receiversAddress, double ethers, String mnemonic, String password) throws Exception {

        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                web3j, credentials, receiversAddress, BigDecimal.valueOf(ethers), Convert.Unit.ETHER).send();
        String currentTime = java.time.LocalDateTime.now().toString();
        String data = "Transfered " + ethers + " ETH from " + credentials.getAddress() +
                " to ->" + receiversAddress + " at " + currentTime +
                "\n\n Gas used : " + transactionReceipt.getGasUsed() +
                "\n Block Hash : " + transactionReceipt.getBlockHash() +
                "\n Transaction Hash : " + transactionReceipt.getTransactionHash() +
                "\n Cumulative Gas Used : " + transactionReceipt.getCumulativeGasUsed() +
                "\n Block Number Raw : " + transactionReceipt.getBlockNumberRaw();

        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        BigInteger wei = ethGetBalance.getBalance();
        return data+"\n"+"Tansaction Completed your balance is " + wei.toString();
    }

    /**
     * 转移以太币
     * @param receiversAddress
     * @param ethers
     * @param keyStoreFile
     * @param password
     * @return
     * @throws Exception
     */
    public String transferEthCoin(String receiversAddress, double ethers, File keyStoreFile, String password) throws Exception {

        Credentials credentials = WalletUtils.loadCredentials(password, keyStoreFile);
        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                web3j, credentials, receiversAddress, BigDecimal.valueOf(ethers), Convert.Unit.ETHER).send();
        String currentTime = java.time.LocalDateTime.now().toString();
        String data = "Transfered " + ethers + " ETH from " + credentials.getAddress() +
                " to ->" + receiversAddress + " at " + currentTime +
                "\n\n Gas used : " + transactionReceipt.getGasUsed() +
                "\n Block Hash : " + transactionReceipt.getBlockHash() +
                "\n Transaction Hash : " + transactionReceipt.getTransactionHash() +
                "\n Cumulative Gas Used : " + transactionReceipt.getCumulativeGasUsed() +
                "\n Block Number Raw : " + transactionReceipt.getBlockNumberRaw();

        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        BigInteger wei = ethGetBalance.getBalance();
        return data+"\n"+"Tansaction Completed your balance is " + wei.toString();
    }

    public String getEthBalance(String address) throws ExecutionException, InterruptedException {
        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();
        BigInteger wei = ethGetBalance.getBalance();
        return "your balance is " + wei.toString();
    }




}
