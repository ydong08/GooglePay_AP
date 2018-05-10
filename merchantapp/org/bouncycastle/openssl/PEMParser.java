package org.bouncycastle.openssl;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectParser;
import org.bouncycastle.util.io.pem.PemReader;

public class PEMParser extends PemReader {
    private final Map parsers = new HashMap();

    class DSAKeyPairParser implements PEMKeyPairParser {
        private DSAKeyPairParser(PEMParser pEMParser) {
        }

        public PEMKeyPair parse(byte[] bArr) throws IOException {
            try {
                ASN1Sequence instance = ASN1Sequence.getInstance(bArr);
                if (instance.size() != 6) {
                    throw new PEMException("malformed sequence in DSA private key");
                }
                ASN1Integer instance2 = ASN1Integer.getInstance(instance.getObjectAt(1));
                ASN1Integer instance3 = ASN1Integer.getInstance(instance.getObjectAt(2));
                ASN1Integer instance4 = ASN1Integer.getInstance(instance.getObjectAt(3));
                return new PEMKeyPair(new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(instance2.getValue(), instance3.getValue(), instance4.getValue())), ASN1Integer.getInstance(instance.getObjectAt(4))), new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(instance2.getValue(), instance3.getValue(), instance4.getValue())), ASN1Integer.getInstance(instance.getObjectAt(5))));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                Exception exception = e2;
                String str = "problem creating DSA private key: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class ECCurveParamsParser implements PemObjectParser {
        private ECCurveParamsParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                ASN1Primitive fromByteArray = ASN1Primitive.fromByteArray(pemObject.getContent());
                if (fromByteArray instanceof ASN1ObjectIdentifier) {
                    return ASN1Primitive.fromByteArray(pemObject.getContent());
                }
                if (fromByteArray instanceof ASN1Sequence) {
                    return X9ECParameters.getInstance(fromByteArray);
                }
                return null;
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                String str = "exception extracting EC named curve: ";
                String valueOf = String.valueOf(e2.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        }
    }

    class ECDSAKeyPairParser implements PEMKeyPairParser {
        private ECDSAKeyPairParser(PEMParser pEMParser) {
        }

        public PEMKeyPair parse(byte[] bArr) throws IOException {
            try {
                Object instance = ECPrivateKey.getInstance(ASN1Sequence.getInstance(bArr));
                AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, instance.getParameters());
                return new PEMKeyPair(new SubjectPublicKeyInfo(algorithmIdentifier, instance.getPublicKey().getBytes()), new PrivateKeyInfo(algorithmIdentifier, instance));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                Exception exception = e2;
                String str = "problem creating EC private key: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class EncryptedPrivateKeyParser implements PemObjectParser {
        public EncryptedPrivateKeyParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return new PKCS8EncryptedPrivateKeyInfo(EncryptedPrivateKeyInfo.getInstance(pemObject.getContent()));
            } catch (Exception e) {
                Exception exception = e;
                String str = "problem parsing ENCRYPTED PRIVATE KEY: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class KeyPairParser implements PemObjectParser {
        private final PEMKeyPairParser pemKeyPairParser;

        public KeyPairParser(PEMParser pEMParser, PEMKeyPairParser pEMKeyPairParser) {
            this.pemKeyPairParser = pEMKeyPairParser;
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            Object obj = null;
            String str = null;
            for (PemHeader pemHeader : pemObject.getHeaders()) {
                Object obj2;
                String str2;
                if (pemHeader.getName().equals("Proc-Type") && pemHeader.getValue().equals("4,ENCRYPTED")) {
                    String str3 = str;
                    obj2 = 1;
                    str2 = str3;
                } else if (pemHeader.getName().equals("DEK-Info")) {
                    str2 = pemHeader.getValue();
                    obj2 = obj;
                } else {
                    str2 = str;
                    obj2 = obj;
                }
                obj = obj2;
                str = str2;
            }
            byte[] content = pemObject.getContent();
            if (obj == null) {
                return this.pemKeyPairParser.parse(content);
            }
            try {
                StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
                return new PEMEncryptedKeyPair(stringTokenizer.nextToken(), Hex.decode(stringTokenizer.nextToken()), content, this.pemKeyPairParser);
            } catch (Exception e) {
                if (obj != null) {
                    throw new PEMException("exception decoding - please check password and data.", e);
                }
                throw new PEMException(e.getMessage(), e);
            } catch (Exception e2) {
                if (obj != null) {
                    throw new PEMException("exception decoding - please check password and data.", e2);
                }
                throw new PEMException(e2.getMessage(), e2);
            }
        }
    }

    class PKCS10CertificationRequestParser implements PemObjectParser {
        private PKCS10CertificationRequestParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return new PKCS10CertificationRequest(pemObject.getContent());
            } catch (Exception e) {
                Exception exception = e;
                String str = "problem parsing certrequest: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class PKCS7Parser implements PemObjectParser {
        private PKCS7Parser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return ContentInfo.getInstance(new ASN1InputStream(pemObject.getContent()).readObject());
            } catch (Exception e) {
                Exception exception = e;
                String str = "problem parsing PKCS7 object: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class PrivateKeyParser implements PemObjectParser {
        public PrivateKeyParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return PrivateKeyInfo.getInstance(pemObject.getContent());
            } catch (Exception e) {
                Exception exception = e;
                String str = "problem parsing PRIVATE KEY: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class PublicKeyParser implements PemObjectParser {
        public PublicKeyParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            return SubjectPublicKeyInfo.getInstance(pemObject.getContent());
        }
    }

    class RSAKeyPairParser implements PEMKeyPairParser {
        private RSAKeyPairParser(PEMParser pEMParser) {
        }

        public PEMKeyPair parse(byte[] bArr) throws IOException {
            try {
                ASN1Sequence instance = ASN1Sequence.getInstance(bArr);
                if (instance.size() != 9) {
                    throw new PEMException("malformed sequence in RSA private key");
                }
                ASN1Encodable instance2 = RSAPrivateKey.getInstance(instance);
                ASN1Encodable rSAPublicKey = new RSAPublicKey(instance2.getModulus(), instance2.getPublicExponent());
                AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
                return new PEMKeyPair(new SubjectPublicKeyInfo(algorithmIdentifier, rSAPublicKey), new PrivateKeyInfo(algorithmIdentifier, instance2));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                Exception exception = e2;
                String str = "problem creating RSA private key: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class RSAPublicKeyParser implements PemObjectParser {
        public RSAPublicKeyParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), RSAPublicKey.getInstance(pemObject.getContent()));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                Exception exception = e2;
                String str = "problem extracting key: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class X509AttributeCertificateParser implements PemObjectParser {
        private X509AttributeCertificateParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            return new X509AttributeCertificateHolder(pemObject.getContent());
        }
    }

    class X509CRLParser implements PemObjectParser {
        private X509CRLParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return new X509CRLHolder(pemObject.getContent());
            } catch (Exception e) {
                Exception exception = e;
                String str = "problem parsing cert: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    class X509CertificateParser implements PemObjectParser {
        private X509CertificateParser(PEMParser pEMParser) {
        }

        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return new X509CertificateHolder(pemObject.getContent());
            } catch (Exception e) {
                Exception exception = e;
                String str = "problem parsing cert: ";
                String valueOf = String.valueOf(exception.toString());
                throw new PEMException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), exception);
            }
        }
    }

    public PEMParser(Reader reader) {
        super(reader);
        this.parsers.put("CERTIFICATE REQUEST", new PKCS10CertificationRequestParser());
        this.parsers.put("NEW CERTIFICATE REQUEST", new PKCS10CertificationRequestParser());
        this.parsers.put("CERTIFICATE", new X509CertificateParser());
        this.parsers.put("TRUSTED CERTIFICATE", new X509CertificateParser());
        this.parsers.put("X509 CERTIFICATE", new X509CertificateParser());
        this.parsers.put("X509 CRL", new X509CRLParser());
        this.parsers.put("PKCS7", new PKCS7Parser());
        this.parsers.put("ATTRIBUTE CERTIFICATE", new X509AttributeCertificateParser());
        this.parsers.put("EC PARAMETERS", new ECCurveParamsParser());
        this.parsers.put("PUBLIC KEY", new PublicKeyParser(this));
        this.parsers.put("RSA PUBLIC KEY", new RSAPublicKeyParser(this));
        this.parsers.put("RSA PRIVATE KEY", new KeyPairParser(this, new RSAKeyPairParser()));
        this.parsers.put("DSA PRIVATE KEY", new KeyPairParser(this, new DSAKeyPairParser()));
        this.parsers.put("EC PRIVATE KEY", new KeyPairParser(this, new ECDSAKeyPairParser()));
        this.parsers.put("ENCRYPTED PRIVATE KEY", new EncryptedPrivateKeyParser(this));
        this.parsers.put("PRIVATE KEY", new PrivateKeyParser(this));
    }

    public Object readObject() throws IOException {
        PemObject readPemObject = readPemObject();
        if (readPemObject == null) {
            return null;
        }
        String type = readPemObject.getType();
        if (this.parsers.containsKey(type)) {
            return ((PemObjectParser) this.parsers.get(type)).parseObject(readPemObject);
        }
        String str = "unrecognised object: ";
        type = String.valueOf(type);
        throw new IOException(type.length() != 0 ? str.concat(type) : new String(str));
    }
}
