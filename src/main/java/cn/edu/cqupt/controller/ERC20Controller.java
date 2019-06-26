package cn.edu.cqupt.controller;

import cn.edu.cqupt.Contract.ERC20Interface;
import cn.edu.cqupt.service.ERC20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/ethereum/token")
public class ERC20Controller {

    @Autowired
    private ERC20Service erc20Service;

    /**
     * echo
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }


    /**
     * 转移
     * transfer
     * @param tokenAddress
     * @param receiversAddress
     * @param token
     * @param keyStoreFilePath
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transfer(String tokenAddress, String receiversAddress, long token, String keyStoreFilePath, String password) throws Exception {

        return erc20Service.transferToken(tokenAddress, receiversAddress, token, new File(keyStoreFilePath), password);
    }

    /**
     * 授权
     * @param tokenAddress
     * @param receiversAddress
     * @param token
     * @param keyStoreFilePath
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public String approve(String tokenAddress, String receiversAddress, long token, String keyStoreFilePath, String password) throws Exception {

        return erc20Service.approveToken(tokenAddress, receiversAddress, token, new File(keyStoreFilePath), password);
    }


    /**
     * 第三方转移
     * @param tokenAddress
     * @param from
     * @param to
     * @param token
     * @param keyStoreFilePath
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/transferFrom", method = RequestMethod.POST)
    public String transferFrom(String tokenAddress,String from, String to, long token, String keyStoreFilePath, String password) throws Exception {

        return erc20Service.TransferFrom(tokenAddress, from, to, token, new File(keyStoreFilePath), password);
    }


    /**
     * 获取余额
     * @param tokenAddress
     * @param address
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getBalance/{tokenAddress}/{address}", method = RequestMethod.GET)
    public String getBalance(@PathVariable("tokenAddress") String tokenAddress, @PathVariable("address") String address) throws Exception {

        return erc20Service.getBalance(tokenAddress, address);
    }

    /**
     * 获取token发行总量
     * @param tokenAddress
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTotalSupply/{tokenAddress}", method = RequestMethod.GET)
    public String getTotalSupply(@PathVariable("tokenAddress") String tokenAddress) throws Exception {

        return erc20Service.getTotalSupply(tokenAddress);
    }

    @RequestMapping(value = "/listen", method = RequestMethod.GET)
    public String listen(@PathVariable("tokenAddress") String tokenAddress) throws Exception {

        return erc20Service.listenErc20Transaction(tokenAddress);
    }




















}
