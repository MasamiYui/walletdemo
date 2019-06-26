package cn.edu.cqupt.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.crypto.Hash;

public class MnemonicUtils {
    private static final int SEED_ITERATIONS = 2048;
    private static final int SEED_KEY_SIZE = 512;
    private static List<String> WORD_LIST = null;

    public MnemonicUtils() {
    }

    public static String generateMnemonic(byte[] initialEntropy) {
        validateEntropy(initialEntropy);
        List<String> words = getWords();
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        byte checksum = calculateChecksum(initialEntropy);
        boolean[] bits = convertToBits(initialEntropy, checksum);
        int iterations = (ent + checksumLength) / 11;
        StringBuilder mnemonicBuilder = new StringBuilder();

        for(int i = 0; i < iterations; ++i) {
            int index = toInt(nextElevenBits(bits, i));
            mnemonicBuilder.append((String)words.get(index));
            boolean notLastIteration = i < iterations - 1;
            if (notLastIteration) {
                mnemonicBuilder.append(" ");
            }
        }

        return mnemonicBuilder.toString();
    }

    public static byte[] generateEntropy(String mnemonic) {
        BitSet bits = new BitSet();
        int size = mnemonicToBits(mnemonic, bits);
        if (size == 0) {
            throw new IllegalArgumentException("Empty mnemonic");
        } else {
            int ent = 32 * size / 33;
            if (ent % 8 != 0) {
                throw new IllegalArgumentException("Wrong mnemonic size");
            } else {
                byte[] entropy = new byte[ent / 8];

                for(int i = 0; i < entropy.length; ++i) {
                    entropy[i] = readByte(bits, i);
                }

                validateEntropy(entropy);
                byte expectedChecksum = calculateChecksum(entropy);
                byte actualChecksum = readByte(bits, entropy.length);
                if (expectedChecksum != actualChecksum) {
                    throw new IllegalArgumentException("Wrong checksum");
                } else {
                    return entropy;
                }
            }
        }
    }

    public static List<String> getWords() {
        if (WORD_LIST == null) {
            WORD_LIST = Collections.unmodifiableList(populateWordList());
        }

        return WORD_LIST;
    }

    public static byte[] generateSeed(String mnemonic, String passphrase) {
        if (isMnemonicEmpty(mnemonic)) {
            throw new IllegalArgumentException("Mnemonic is required to generate a seed");
        } else {
            passphrase = passphrase == null ? "" : passphrase;
            String salt = String.format("mnemonic%s", passphrase);
            PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
            gen.init(mnemonic.getBytes(StandardCharsets.UTF_8), salt.getBytes(StandardCharsets.UTF_8), 2048);
            return ((KeyParameter)gen.generateDerivedParameters(512)).getKey();
        }
    }

    public static boolean validateMnemonic(String mnemonic) {
        try {
            generateEntropy(mnemonic);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    private static boolean isMnemonicEmpty(String mnemonic) {
        return mnemonic == null || mnemonic.trim().isEmpty();
    }

    private static boolean[] nextElevenBits(boolean[] bits, int i) {
        int from = i * 11;
        int to = from + 11;
        return Arrays.copyOfRange(bits, from, to);
    }

    private static void validateEntropy(byte[] entropy) {
        if (entropy == null) {
            throw new IllegalArgumentException("Entropy is required");
        } else {
            int ent = entropy.length * 8;
            if (ent < 128 || ent > 256 || ent % 32 != 0) {
                throw new IllegalArgumentException("The allowed size of ENT is 128-256 bits of multiples of 32");
            }
        }
    }

    private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        int totalLength = ent + checksumLength;
        boolean[] bits = new boolean[totalLength];

        int i;
        for(i = 0; i < initialEntropy.length; ++i) {
            for(int j = 0; j < 8; ++j) {
                byte b = initialEntropy[i];
                bits[8 * i + j] = toBit(b, j);
            }
        }

        for(i = 0; i < checksumLength; ++i) {
            bits[ent + i] = toBit(checksum, i);
        }

        return bits;
    }

    private static boolean toBit(byte value, int index) {
        return (value >>> 7 - index & 1) > 0;
    }

    private static int toInt(boolean[] bits) {
        int value = 0;

        for(int i = 0; i < bits.length; ++i) {
            boolean isSet = bits[i];
            if (isSet) {
                value += 1 << bits.length - i - 1;
            }
        }

        return value;
    }

    private static int mnemonicToBits(String mnemonic, BitSet bits) {
        int bit = 0;
        List<String> vocabulary = getWords();
        StringTokenizer tokenizer = new StringTokenizer(mnemonic, " ");

        while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            int index = vocabulary.indexOf(word);
            if (index < 0) {
                throw new IllegalArgumentException(String.format("Mnemonic word '%s' should be in the word list", word));
            }

            for(int k = 0; k < 11; ++k) {
                bits.set(bit++, isBitSet(index, 10 - k));
            }
        }

        return bit;
    }

    private static byte readByte(BitSet bits, int startByte) {
        byte res = 0;

        for(int k = 0; k < 8; ++k) {
            if (bits.get(startByte * 8 + k)) {
                res = (byte)(res | 1 << 7 - k);
            }
        }

        return res;
    }

    private static boolean isBitSet(int n, int k) {
        return (n >> k & 1) == 1;
    }

    public static byte calculateChecksum(byte[] initialEntropy) {
        int ent = initialEntropy.length * 8;
        byte mask = (byte)(255 << 8 - ent / 32);
        byte[] bytes = Hash.sha256(initialEntropy);
        return (byte)(bytes[0] & mask);
    }

    private static List<String> populateWordList() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("en-mnemonic-word-list.txt");

        try {
            return readAllLines(inputStream);
        } catch (Exception var2) {
            throw new IllegalStateException(var2);
        }
    }

    private static List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList data = new ArrayList();

        String line;
        while((line = br.readLine()) != null) {
            data.add(line);
        }

        return data;
    }
}

