package cn.edu.cqupt.controller;

import cn.edu.cqupt.service.EthereumCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.CipherException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/ethereum/ETH")
public class EthereumCoinController {

    @Autowired
    private EthereumCoinService ecs;

    @RequestMapping(value = "/createBip39Wallet", method = RequestMethod.POST)
    public String createBip39Wallet(String password, String path) throws CipherException, IOException {

        return ecs.createBip39Wallet(password, path);
    }

    @RequestMapping(value = "/getBalance/{address}", method = RequestMethod.GET)
    public String getBalance(@PathVariable("address") String address) throws CipherException, IOException, ExecutionException, InterruptedException {

        return ecs.getEthBalance(address);
    }


    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transfer(String receiversAddress, double ethers, String keyStoreFilePath, String password) throws Exception {

        return ecs.transferEthCoin(receiversAddress, ethers, new File(keyStoreFilePath), password);
    }









}
