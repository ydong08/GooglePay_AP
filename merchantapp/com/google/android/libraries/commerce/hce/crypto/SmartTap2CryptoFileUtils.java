package com.google.android.libraries.commerce.hce.crypto;

import android.content.Context;
import android.net.Uri;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.util.Scanner;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

public class SmartTap2CryptoFileUtils {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();

    private SmartTap2CryptoFileUtils() {
    }

    public static ECPublicKey readPublicKey(Context context, Uri uri, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ECPublicKey readPublicKey = readPublicKey(inputStream, smartTap2ECKeyManager);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
            return readPublicKey;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    public static ECPublicKey readPublicKey(InputStream inputStream, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        byte[] toByteArray = ByteStreams.toByteArray(inputStream);
        ECPublicKey readPublicPemKey = readPublicPemKey(new ByteArrayInputStream(toByteArray), smartTap2ECKeyManager);
        if (readPublicPemKey == null) {
            readPublicPemKey = readPublicBinKey(new ByteArrayInputStream(toByteArray), smartTap2ECKeyManager);
        }
        if (readPublicPemKey == null) {
            return readPublicTextKey(new ByteArrayInputStream(toByteArray), smartTap2ECKeyManager);
        }
        return readPublicPemKey;
    }

    public static ECPublicKey readPublicTextKey(InputStream inputStream, SmartTap2ECKeyManager smartTap2ECKeyManager) throws ValuablesCryptoException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.next());
        }
        return smartTap2ECKeyManager.decodeCompressedPublicKey(Hex.decode(stringBuilder.toString()));
    }

    public static ECPublicKey readPublicBinKey(InputStream inputStream, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        return smartTap2ECKeyManager.decodeCompressedPublicKey(ByteStreams.toByteArray(inputStream));
    }

    public static ECPublicKey readPublicPemKey(InputStream inputStream, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        return (ECPublicKey) readPemKeys(inputStream, smartTap2ECKeyManager).getPublic();
    }

    public static KeyPair readPemKeys(InputStream inputStream, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        Throwable th;
        PEMParser pEMParser = null;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        try {
            PEMParser pEMParser2 = new PEMParser(inputStreamReader);
            try {
                PrivateKey privateKeyFromInfo;
                PublicKey publicKey = null;
                for (Object readObject = pEMParser2.readObject(); readObject != null; readObject = pEMParser2.readObject()) {
                    if (readObject instanceof SubjectPublicKeyInfo) {
                        publicKey = publicKeyFromInfo((SubjectPublicKeyInfo) readObject, smartTap2ECKeyManager);
                    } else if (readObject instanceof PrivateKeyInfo) {
                        privateKeyFromInfo = privateKeyFromInfo((PrivateKeyInfo) readObject, smartTap2ECKeyManager);
                    } else if (readObject instanceof PEMKeyPair) {
                        privateKeyFromInfo = privateKeyFromInfo(((PEMKeyPair) readObject).getPrivateKeyInfo(), smartTap2ECKeyManager);
                        publicKey = publicKeyFromInfo(((PEMKeyPair) readObject).getPublicKeyInfo(), smartTap2ECKeyManager);
                    }
                }
                KeyPair keyPair = new KeyPair(publicKey, privateKeyFromInfo);
                if (pEMParser2 != null) {
                    pEMParser2.close();
                }
                inputStreamReader.close();
                return keyPair;
            } catch (Throwable th2) {
                th = th2;
                pEMParser = pEMParser2;
                if (pEMParser != null) {
                    pEMParser.close();
                }
                inputStreamReader.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (pEMParser != null) {
                pEMParser.close();
            }
            inputStreamReader.close();
            throw th;
        }
    }

    private static PublicKey publicKeyFromInfo(SubjectPublicKeyInfo subjectPublicKeyInfo, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        return smartTap2ECKeyManager.decodeX509EncodedPublicKey(subjectPublicKeyInfo.getEncoded());
    }

    private static PrivateKey privateKeyFromInfo(PrivateKeyInfo privateKeyInfo, SmartTap2ECKeyManager smartTap2ECKeyManager) throws IOException, ValuablesCryptoException {
        return smartTap2ECKeyManager.decodePKCS8EncodedPrivateKey(privateKeyInfo.getEncoded());
    }
}
