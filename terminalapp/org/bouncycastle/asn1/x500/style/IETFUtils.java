package org.bouncycastle.asn1.x500.style;

import java.io.IOException;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class IETFUtils {
    public static void appendRDN(StringBuffer stringBuffer, RDN rdn, Hashtable hashtable) {
        if (rdn.isMultiValued()) {
            AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
            Object obj = 1;
            for (int i = 0; i != typesAndValues.length; i++) {
                if (obj != null) {
                    obj = null;
                } else {
                    stringBuffer.append('+');
                }
                appendTypeAndValue(stringBuffer, typesAndValues[i], hashtable);
            }
        } else if (rdn.getFirst() != null) {
            appendTypeAndValue(stringBuffer, rdn.getFirst(), hashtable);
        }
    }

    public static void appendTypeAndValue(StringBuffer stringBuffer, AttributeTypeAndValue attributeTypeAndValue, Hashtable hashtable) {
        String str = (String) hashtable.get(attributeTypeAndValue.getType());
        if (str != null) {
            stringBuffer.append(str);
        } else {
            stringBuffer.append(attributeTypeAndValue.getType().getId());
        }
        stringBuffer.append('=');
        stringBuffer.append(valueToString(attributeTypeAndValue.getValue()));
    }

    public static String valueToString(ASN1Encodable aSN1Encodable) {
        int i;
        StringBuffer stringBuffer = new StringBuffer();
        String str;
        String valueOf;
        if (!(aSN1Encodable instanceof ASN1String) || (aSN1Encodable instanceof DERUniversalString)) {
            try {
                str = "#";
                valueOf = String.valueOf(bytesToString(Hex.encode(aSN1Encodable.toASN1Primitive().getEncoded("DER"))));
                if (valueOf.length() != 0) {
                    valueOf = str.concat(valueOf);
                } else {
                    valueOf = new String(str);
                }
                stringBuffer.append(valueOf);
            } catch (IOException e) {
                throw new IllegalArgumentException("Other value has no encoded form");
            }
        }
        valueOf = ((ASN1String) aSN1Encodable).getString();
        if (valueOf.length() <= 0 || valueOf.charAt(0) != '#') {
            stringBuffer.append(valueOf);
        } else {
            str = "\\";
            valueOf = String.valueOf(valueOf);
            if (valueOf.length() != 0) {
                valueOf = str.concat(valueOf);
            } else {
                valueOf = new String(str);
            }
            stringBuffer.append(valueOf);
        }
        int length = stringBuffer.length();
        if (stringBuffer.length() >= 2 && stringBuffer.charAt(0) == '\\' && stringBuffer.charAt(1) == '#') {
            i = length;
            length = 2;
        } else {
            i = length;
            length = 0;
        }
        while (length != i) {
            if (stringBuffer.charAt(length) == ',' || stringBuffer.charAt(length) == '\"' || stringBuffer.charAt(length) == '\\' || stringBuffer.charAt(length) == '+' || stringBuffer.charAt(length) == '=' || stringBuffer.charAt(length) == '<' || stringBuffer.charAt(length) == '>' || stringBuffer.charAt(length) == ';') {
                stringBuffer.insert(length, "\\");
                length++;
                i++;
            }
            length++;
        }
        if (stringBuffer.length() > 0) {
            length = 0;
            while (stringBuffer.length() > length && stringBuffer.charAt(length) == ' ') {
                stringBuffer.insert(length, "\\");
                length += 2;
            }
        }
        length = stringBuffer.length() - 1;
        while (length >= 0 && stringBuffer.charAt(length) == ' ') {
            stringBuffer.insert(length, '\\');
            length--;
        }
        return stringBuffer.toString();
    }

    private static String bytesToString(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i != cArr.length; i++) {
            cArr[i] = (char) (bArr[i] & 255);
        }
        return new String(cArr);
    }

    public static String canonicalize(String str) {
        String toLowerCase;
        int i;
        int length;
        String toLowerCase2 = Strings.toLowerCase(str);
        if (toLowerCase2.length() > 0 && toLowerCase2.charAt(0) == '#') {
            ASN1Primitive decodeObject = decodeObject(toLowerCase2);
            if (decodeObject instanceof ASN1String) {
                toLowerCase = Strings.toLowerCase(((ASN1String) decodeObject).getString());
                if (toLowerCase.length() > 1) {
                    i = 0;
                    while (i + 1 < toLowerCase.length() && toLowerCase.charAt(i) == '\\' && toLowerCase.charAt(i + 1) == ' ') {
                        i += 2;
                    }
                    length = toLowerCase.length() - 1;
                    while (length - 1 > 0 && toLowerCase.charAt(length - 1) == '\\' && toLowerCase.charAt(length) == ' ') {
                        length -= 2;
                    }
                    if (i > 0 || length < toLowerCase.length() - 1) {
                        toLowerCase = toLowerCase.substring(i, length + 1);
                    }
                }
                return stripInternalSpaces(toLowerCase);
            }
        }
        toLowerCase = toLowerCase2;
        if (toLowerCase.length() > 1) {
            i = 0;
            while (i + 1 < toLowerCase.length()) {
                i += 2;
            }
            length = toLowerCase.length() - 1;
            while (length - 1 > 0) {
                length -= 2;
            }
            toLowerCase = toLowerCase.substring(i, length + 1);
        }
        return stripInternalSpaces(toLowerCase);
    }

    private static ASN1Primitive decodeObject(String str) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decode(str.substring(1)));
        } catch (IOException e) {
            String valueOf = String.valueOf(e);
            throw new IllegalStateException(new StringBuilder(String.valueOf(valueOf).length() + 26).append("unknown encoding in name: ").append(valueOf).toString());
        }
    }

    public static String stripInternalSpaces(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.length() != 0) {
            char charAt = str.charAt(0);
            stringBuffer.append(charAt);
            int i = 1;
            while (i < str.length()) {
                char charAt2 = str.charAt(i);
                if (charAt != ' ' || charAt2 != ' ') {
                    stringBuffer.append(charAt2);
                }
                i++;
                charAt = charAt2;
            }
        }
        return stringBuffer.toString();
    }

    public static boolean rDNAreEqual(RDN rdn, RDN rdn2) {
        if (rdn.isMultiValued()) {
            if (!rdn2.isMultiValued()) {
                return false;
            }
            AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
            AttributeTypeAndValue[] typesAndValues2 = rdn2.getTypesAndValues();
            if (typesAndValues.length != typesAndValues2.length) {
                return false;
            }
            for (int i = 0; i != typesAndValues.length; i++) {
                if (!atvAreEqual(typesAndValues[i], typesAndValues2[i])) {
                    return false;
                }
            }
            return true;
        } else if (rdn2.isMultiValued()) {
            return false;
        } else {
            return atvAreEqual(rdn.getFirst(), rdn2.getFirst());
        }
    }

    private static boolean atvAreEqual(AttributeTypeAndValue attributeTypeAndValue, AttributeTypeAndValue attributeTypeAndValue2) {
        if (attributeTypeAndValue == attributeTypeAndValue2) {
            return true;
        }
        if (attributeTypeAndValue == null) {
            return false;
        }
        if (attributeTypeAndValue2 == null) {
            return false;
        }
        if (!attributeTypeAndValue.getType().equals(attributeTypeAndValue2.getType())) {
            return false;
        }
        if (canonicalize(valueToString(attributeTypeAndValue.getValue())).equals(canonicalize(valueToString(attributeTypeAndValue2.getValue())))) {
            return true;
        }
        return false;
    }
}
