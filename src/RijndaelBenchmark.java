import java.io.*;
import java.nio.file.FileSystemException;
import java.util.Arrays;

import Rijndael.Rijndael_Algorithm;
import de.linearbits.subframe.Benchmark;
import de.linearbits.subframe.analyzer.ValueBuffer;
import org.bouncycastle.util.encoders.Hex;

public class RijndaelBenchmark {
    final static byte[] iv = Hex.decode("2B4D6251655468576D5A713474367739");
    final static byte[] key256 = Hex.decode("4D6351655468576D5A7134743777217A25432A462D4A614E645267556A586E32");
    final static byte[] key128 = Hex.decode("48404D635166546A576E5A7234753778");

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments. Please pass a string for the benchmark output file.");
        }
        Benchmark benchmark = new Benchmark(new String[] {"Key Size", "Method", "Data Size" });
        int time = benchmark.addMeasure("Time");
        benchmark.addAnalyzer(time, new ValueBuffer());

        // warmup
        System.out.println("Warming up...");
        RijndaelCrypt(true, key256, iv, "data512.bin", "data512.enc");
        cleanupFile("data512.enc");

        System.out.println("ENCRYPT - Key: 128 bits, Data: 256 MB");
        benchmark.addRun("128", "Encrypt", "256");
        benchmark.startTimer(time);
        RijndaelCrypt(true, key128, iv, "data256.bin", "data256.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("128", "Decrypt", "256");
        System.out.println("DECRYPT - Key: 128 bits, Data: 256 MB");
        benchmark.startTimer(time);
        RijndaelCrypt(false, key128, iv, "data256.enc", "data256.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data256.enc");
        cleanupFile("data256.dec");

        System.out.println("ENCRYPT - Key: 128 bits, Data: 512 MB");
        benchmark.addRun("128", "Encrypt", "512");
        benchmark.startTimer(time);
        RijndaelCrypt(true, key128, iv, "data512.bin", "data512.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("128", "Decrypt", "512");
        System.out.println("DECRYPT - Key: 128 bits, Data: 512 MB");
        benchmark.startTimer(time);
        RijndaelCrypt(false, key128, iv, "data512.enc", "data512.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data512.enc");
        cleanupFile("data512.dec");

        System.out.println("ENCRYPT - Key: 128 bits, Data: 1024 MB");
        benchmark.addRun("128", "Encrypt", "1024");
        benchmark.startTimer(time);
        RijndaelCrypt(true, key128, iv, "data1024.bin", "data1024.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("128", "Decrypt", "1024");
        System.out.println("DECRYPT - Key: 128 bits, Data: 1024 MB");
        benchmark.startTimer(time);
        RijndaelCrypt(false, key128, iv, "data1024.enc", "data1024.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data1024.enc");
        cleanupFile("data1024.dec");

        System.out.println("ENCRYPT - Key: 256 bits, Data: 256 MB");
        benchmark.addRun("256", "Encrypt", "256");
        benchmark.startTimer(time);
        RijndaelCrypt(true, key256, iv, "data256.bin", "data256.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("256", "Decrypt", "256");
        System.out.println("DECRYPT - Key: 256 bits, Data: 256 MB");
        benchmark.startTimer(time);
        RijndaelCrypt(false, key256, iv, "data256.enc", "data256.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data256.enc");
        cleanupFile("data256.dec");

        System.out.println("ENCRYPT - Key: 256 bits, Data: 512 MB");
        benchmark.addRun("256", "Encrypt", "512");
        benchmark.startTimer(time);
        RijndaelCrypt(true, key256, iv, "data512.bin", "data512.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("256", "Decrypt", "512");
        System.out.println("DECRYPT - Key: 256 bits, Data: 512 MB");
        benchmark.startTimer(time);
        RijndaelCrypt(false, key256, iv, "data512.enc", "data512.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data512.enc");
        cleanupFile("data512.dec");

        System.out.println("ENCRYPT - Key: 256 bits, Data: 1024 MB");
        benchmark.addRun("256", "Encrypt", "1024");
        benchmark.startTimer(time);
        RijndaelCrypt(true, key256, iv, "data1024.bin", "data1024.enc");
        benchmark.addStopTimer(time);
        benchmark.addRun("256", "Decrypt", "1024");
        System.out.println("DECRYPT - Key: 256 bits, Data: 1024 MB");
        benchmark.startTimer(time);
        RijndaelCrypt(false, key256, iv, "data1024.enc", "data1024.dec");
        benchmark.addStopTimer(time);
        cleanupFile("data1024.enc");
        cleanupFile("data1024.dec");

        benchmark.getResults().write(new File(args[0]));
    }

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
                byte[] out = Rijndael_Algorithm.blockDecrypt(in, 0, roundKeys);
                for (int j = 0; j < 16; j++) out[j] = (byte) (out[j] ^ feedback[j]); // CBC feedback
                feedback = Arrays.copyOf(in, in.length);
                bos.write(out);
            }
        }

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
