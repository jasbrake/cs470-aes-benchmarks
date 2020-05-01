import de.linearbits.subframe.Benchmark;
import de.linearbits.subframe.analyzer.ValueBuffer;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.nio.file.FileSystemException;

public class BCBenchmark {
    final static byte[] iv = Hex.decode("2B4D6251655468576D5A713474367739");
    final static byte[] key256 = Hex.decode("4D6351655468576D5A7134743777217A25432A462D4A614E645267556A586E32");
    final static byte[] key128 = Hex.decode("48404D635166546A576E5A7234753778");

    public static void main(String[] args) throws InvalidCipherTextException, IOException {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments. Please pass a string for the benchmark output file.");
        }
        Benchmark benchmark = new Benchmark(new String[] {"Key Size", "Method", "Data Size" });
        int time = benchmark.addMeasure("Time");
        benchmark.addAnalyzer(time, new ValueBuffer());

        // warmup
        System.out.println("Warming up...");
        BCCrypt(true, key256, iv, "data512.bin", "data512.enc");
        cleanupFile("data512.enc");

        System.out.println("ENCRYPT - Key: 128 bits, Data: 256 MB");
        benchmark.addRun("128", "Encrypt", "256");
        benchmark.startTimer(time);
        BCCrypt(true, key128, iv, "data256.bin", "data256.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("128", "Decrypt", "256");
        System.out.println("DECRYPT - Key: 128 bits, Data: 256 MB");
        benchmark.startTimer(time);
        BCCrypt(false, key128, iv, "data256.enc", "data256.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data256.enc");
        cleanupFile("data256.dec");

        System.out.println("ENCRYPT - Key: 128 bits, Data: 512 MB");
        benchmark.addRun("128", "Encrypt", "512");
        benchmark.startTimer(time);
        BCCrypt(true, key128, iv, "data512.bin", "data512.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("128", "Decrypt", "512");
        System.out.println("DECRYPT - Key: 128 bits, Data: 512 MB");
        benchmark.startTimer(time);
        BCCrypt(false, key128, iv, "data512.enc", "data512.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data512.enc");
        cleanupFile("data512.dec");

        System.out.println("ENCRYPT - Key: 128 bits, Data: 1024 MB");
        benchmark.addRun("128", "Encrypt", "1024");
        benchmark.startTimer(time);
        BCCrypt(true, key128, iv, "data1024.bin", "data1024.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("128", "Decrypt", "1024");
        System.out.println("DECRYPT - Key: 128 bits, Data: 1024 MB");
        benchmark.startTimer(time);
        BCCrypt(false, key128, iv, "data1024.enc", "data1024.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data1024.enc");
        cleanupFile("data1024.dec");

        System.out.println("ENCRYPT - Key: 256 bits, Data: 256 MB");
        benchmark.addRun("256", "Encrypt", "256");
        benchmark.startTimer(time);
        BCCrypt(true, key256, iv, "data256.bin", "data256.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("256", "Decrypt", "256");
        System.out.println("DECRYPT - Key: 256 bits, Data: 256 MB");
        benchmark.startTimer(time);
        BCCrypt(false, key256, iv, "data256.enc", "data256.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data256.enc");
        cleanupFile("data256.dec");

        System.out.println("ENCRYPT - Key: 256 bits, Data: 512 MB");
        benchmark.addRun("256", "Encrypt", "512");
        benchmark.startTimer(time);
        BCCrypt(true, key256, iv, "data512.bin", "data512.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("256", "Decrypt", "512");
        System.out.println("DECRYPT - Key: 256 bits, Data: 512 MB");
        benchmark.startTimer(time);
        BCCrypt(false, key256, iv, "data512.enc", "data512.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data512.enc");
        cleanupFile("data512.dec");

        System.out.println("ENCRYPT - Key: 256 bits, Data: 1024 MB");
        benchmark.addRun("256", "Encrypt", "1024");
        benchmark.startTimer(time);
        BCCrypt(true, key256, iv, "data1024.bin", "data1024.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("256", "Decrypt", "1024");
        System.out.println("DECRYPT - Key: 256 bits, Data: 1024 MB");
        benchmark.startTimer(time);
        BCCrypt(false, key256, iv, "data1024.enc", "data1024.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data1024.enc");
        cleanupFile("data1024.dec");

        benchmark.getResults().write(new File(args[0]));
    }

    /**
     * Encrypt or decrypt the input file to the output file. Uses BouncyCastle library with AES CBC mode and no padding.
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
        BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(engine));
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
            bos.write(out, 0, encLen);
        }

        encLen = cipher.doFinal(out, 0);
        bos.write(out, 0, encLen);

        bis.close();
        fis.close();
        bos.close();
        fos.close();
    }

    private static void cleanupFile(String path) throws FileSystemException {
        File f = new File(path);
        if (!f.delete())
            throw new FileSystemException("could not delete " + f.getAbsolutePath());
    }
}
