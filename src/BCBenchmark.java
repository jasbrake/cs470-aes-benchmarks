import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;

public class BCBenchmark {
    final static byte[] iv = Hex.decode("2B4D6251655468576D5A713474367739");
    final static byte[] key256 = Hex.decode("4D6351655468576D5A7134743777217A25432A462D4A614E645267556A586E32");
    final static byte[] key128 = Hex.decode("48404D635166546A576E5A7234753778");

    public static void main(String[] args) throws InvalidCipherTextException, IOException {
        BCCrypt(true, key128, iv, "bbb.mp4", "bbb_bc_128.bin");
        BCCrypt(false, key128, iv, "bbb_bc_128.bin", "bbb_bc_128.mp4");
        BCCrypt(true, key256, iv, "bbb.mp4", "bbb_bc_256.bin");
        BCCrypt(false, key256, iv, "bbb_bc_256.bin", "bbb_bc_256.mp4");
    }

    /**
     * Encrypt or decrypt the input file to the output file. Uses BouncyCastle library with AES CBC mode and PKCS7
     * padding.
     *
     * @param encrypt whether to encrypt or decrypt
     * @param key the AES key
     * @param iv the AES IV
     * @param inputFile path to the input file
     * @param outputFile path to the output file
     * @throws IOException if the files cannot be found, read from, or written to
     * @throws InvalidCipherTextException if cipher.ProcessBytes or cipher.doFinal fails
     */
    public static void BCCrypt(boolean encrypt, byte[] key, byte[] iv, String inputFile, String outputFile)
            throws IOException, InvalidCipherTextException {
        AESEngine engine = new AESEngine();
        // PaddedBufferedBlockCipher Uses PKCS7 padding by default.
        // We need padding since our input file may not be a multiple of AES Block size (16 bytes).
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
        ParametersWithIV params = new ParametersWithIV(new KeyParameter(key), iv);
        cipher.init(encrypt, params);

        // We use a buffered input and output stream to minimize the number of system calls
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte[] in = new byte[engine.getBlockSize()];
        byte[] out = new byte[engine.getBlockSize()];

        // Read and encrypt or decrypt engine.getBlockSize() number of bytes at a time, writing the result to our
        // output file.
        int encLen = 0;
        int read = 0;
        while ((read = bis.read(in)) != -1) {
            encLen = cipher.processBytes(in, 0, read, out, 0);
            fos.write(out, 0, encLen);
        }

        encLen = cipher.doFinal(out, 0);
        fos.write(out, 0, encLen);

        fis.close();
        bis.close();
        fos.close();
        bos.close();
    }
}
