import java.io.*;
import java.util.Arrays;

import Rijndael.Rijndael_Algorithm;
import org.bouncycastle.util.encoders.Hex;

public class RijndaelBenchmark {
    final static byte[] iv = Hex.decode("2B4D6251655468576D5A713474367739");
    final static byte[] key256 = Hex.decode("4D6351655468576D5A7134743777217A25432A462D4A614E645267556A586E32");
    final static byte[] key128 = Hex.decode("48404D635166546A576E5A7234753778");

    public static void RijndaelCrypt(boolean encrypt, byte[] key, byte[] iv, String inputFile, String outputFile) throws Exception {
        Object roundKeys = Rijndael_Algorithm.makeKey(key); // This creates the round key;

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] feedback = Arrays.copyOf(iv, iv.length);
        byte[] in = new byte[Rijndael_Algorithm.blockSize()];

        int read = 0;
        if (encrypt) {
            while ((read = bis.read(in)) != -1) {
                if (read != Rijndael_Algorithm.blockSize()) throw new Exception("file is not a multiple of BLOCK_SIZE");
                for (int j = 0; j < Rijndael_Algorithm.blockSize(); j++) in[j] = (byte) (in[j] ^ feedback[j]); // CBC feedback
                byte[] out = Rijndael_Algorithm.blockEncrypt(in, 0, roundKeys);
                feedback = Arrays.copyOf(out, out.length);
                bos.write(out);
            }
        } else {
            while ((read = bis.read(in)) != -1) {
                if (read != Rijndael_Algorithm.blockSize()) throw new Exception("file is not a multiple of BLOCK_SIZE");
                feedback = Arrays.copyOf(in, in.length);
                byte[] out = Rijndael_Algorithm.blockDecrypt(in, 0, roundKeys);
                for (int j = 0; j < 16; j++) in[j] = (byte) (out[j] ^ feedback[j]); // CBC feedback
                bos.write(out);
            }
        }

        bis.close();
        fis.close();
        bos.close();
        fos.close();
    }

    public static void main(String[] args) throws Exception {
        RijndaelCrypt(true, key128, iv,"data256.bin","data256.enc");
        RijndaelCrypt(false, key128, iv,"data256.enc","data256.dec");
    }
}
