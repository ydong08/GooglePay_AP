package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.base.Optional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class Ose {
    private static final byte[] APPLICATION_LABEL = "AndroidPay".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] APPLICATION_VERSION = Hex.decode("0001");
    private static final FormattingLogger logger = FormattingLoggers.newContextLogger();

    public static abstract class OseResponse {
        public abstract List<AidInfo> aidInfos();

        public abstract Optional<ByteArrayWrapper> mobileDeviceNonce();

        public abstract Optional<TransactionalDetails> transactionalDetails();

        static OseResponse create(Optional<TransactionalDetails> optional, Optional<ByteArrayWrapper> optional2, List<AidInfo> list) {
            return new AutoValue_Ose_OseResponse(optional, optional2, list);
        }
    }

    private Ose() {
    }

    public static OseResponse parse(byte[] bArr) throws BasicTlvException {
        Collection treeSet = new TreeSet();
        Optional absent = Optional.absent();
        Optional absent2 = Optional.absent();
        Optional optional = absent;
        absent = absent2;
        for (BasicTlv basicTlv : BasicTlv.getDecodedInstances(ResponseApdu.fromResponse(bArr).getResponseData())) {
            if (basicTlv.getTag() == 111) {
                for (BasicTlv basicTlv2 : basicTlv2.asConstructedTlv().getChildren()) {
                    switch (basicTlv2.getTag()) {
                        case 165:
                            try {
                                for (BasicTlv basicTlv22 : basicTlv22.asConstructedTlv().getChild(48908).asConstructedTlv().getChildren()) {
                                    if (basicTlv22.getTag() == 97) {
                                        absent2 = AidInfo.parse(basicTlv22);
                                        if (absent2.isPresent()) {
                                            treeSet.add((AidInfo) absent2.get());
                                        } else {
                                            logger.w("Failed to parse AID", new Object[0]);
                                        }
                                    }
                                }
                                break;
                            } catch (Throwable e) {
                                throw new BasicTlvException(String.format("TLV %s does not contain child %s inside child %s in OSE response", new Object[]{Integer.valueOf(r5.getTag()), Integer.valueOf(48908), Integer.valueOf(165)}), e);
                            }
                        case 193:
                            absent = Optional.of(TransactionalDetails.fromBytes(basicTlv22.asPrimitiveTlv().getValue()));
                            break;
                        case 194:
                            optional = Optional.of(new ByteArrayWrapper(basicTlv22.asPrimitiveTlv().getValue()));
                            break;
                        default:
                            break;
                    }
                }
                continue;
            }
            optional = optional;
            absent = absent;
        }
        if (!treeSet.isEmpty()) {
            return OseResponse.create(absent, optional, new ArrayList(treeSet));
        }
        throw new BasicTlvException(String.format("No AIDs found in OSE response", new Object[0]));
    }
}
