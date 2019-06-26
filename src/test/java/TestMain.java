import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class TestMain {

    public static void main(String[] args) throws CipherException, IOException {

        Bip39Wallet wallet = WalletUtils.generateBip39Wallet("",new File("C:\\Users\\YUI\\Desktop"));
        System.out.println(wallet.toString());

    }
}
