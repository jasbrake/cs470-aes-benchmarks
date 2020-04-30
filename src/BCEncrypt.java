import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;

import static org.bouncycastle.util.Arrays.areEqual;

public class BCEncrypt {
    public static void main(String[] args) throws InvalidCipherTextException, IOException {
        AESEngine engine = new AESEngine();
        // Uses PKCS7 padding by default
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));

//        byte[] iv = Hex.decode("5F060D3716B345C253F6749ABAC10917");
        byte[] iv = new byte[engine.getBlockSize()];
        byte[] out = new byte[engine.getBlockSize()];
        byte[] input = new byte[engine.getBlockSize()];

        KeyParameter key = new KeyParameter(Hex.decode("603DEB1015CA71BE2B73AEF0857D77811F352C073B6108D72D9810A30914DFF4"));
        ParametersWithIV params = new ParametersWithIV(key, iv);
        cipher.init(true, params);

        FileInputStream fis = new FileInputStream("bbb.mp4");
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream("bbb_bc.bin");
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        int encLen = 0;
        int read = 0;
        while ((read = bis.read(input)) != -1) {
            encLen = cipher.processBytes(input, 0, read, out, 0);
            fos.write(out, 0, encLen);
        }

        encLen = cipher.doFinal(out, 0);
        fos.write(out, 0, encLen);

//        cipher.init(false, params);
//
//        int len2 = cipher.processBytes(out, 0, out.length, out, 0);
//
//        len2 += cipher.doFinal(out, len2);
//
//        if (!areEqual(data, out))
//        {
//            System.out.println("failed reversal - " + "expected " + new String(Hex.encode(data)) + " got " + new String(Hex.encode(out)));
//        }

        fis.close();
        bis.close();
        fos.close();
        bos.close();
    }
}
