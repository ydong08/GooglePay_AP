package com.google.android.libraries.commerce.hce.terminal.payment;

import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv.TagLengthBuffer;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidLengthException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvInvalidTagException;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class ProcessingOptions {
    private static final byte[] COUNTRY_CODE_US = new byte[]{(byte) 3, (byte) 72};
    private static final byte[] EMPTY_PDOL_RESPONSE = new byte[]{(byte) -125, (byte) 0};
    private static final Integer FCI_PROPRIETARY_TEMPLATE = Integer.valueOf(165);
    private static final Integer FCI_TEMPLATE = Integer.valueOf(111);
    private static final byte[] GET_PROCESSING_OPTIONS_PREFIX = new byte[]{Byte.MIN_VALUE, (byte) -88, (byte) 0, (byte) 0};
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    private static final Integer PDOL_REQUEST_TAG = Integer.valueOf(40760);
    private static final Map<Integer, byte[]> PDOL_RESPONSES = ImmutableMap.of(Integer.valueOf(40733), RISK_MANAGEMENT_DATA, Integer.valueOf(40730), COUNTRY_CODE_US, Integer.valueOf(40757), TYPE_DATA);
    private static final byte[] RISK_MANAGEMENT_DATA = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    private static final byte[] TERMINAL_COUNTRY_CODE = COUNTRY_CODE_US;
    private static final byte[] TYPE_DATA = new byte[]{(byte) 0};

    static class PdolRequest {
        private int expectedSize;
        private int tag;

        public PdolRequest(int i, int i2) {
            this.tag = i;
            this.expectedSize = i2;
        }

        public int getTag() {
            return this.tag;
        }

        public int getExpectedSize() {
            return this.expectedSize;
        }
    }

    private ProcessingOptions() {
    }

    public static byte[] getGetProcessingOptionsCommand(Multimap<Integer, BasicTlv> multimap) throws BasicTlvException {
        r1 = new byte[4][];
        r1[1] = new byte[]{(byte) getPayload(multimap).length};
        r1[2] = r0;
        r1[3] = new byte[]{(byte) 0};
        return Bytes.concat(r1);
    }

    private static byte[] getPayload(Multimap<Integer, BasicTlv> multimap) throws BasicTlvException {
        Collection collection = multimap.get(FCI_TEMPLATE);
        if (collection.size() != 1) {
            LOG.w("Expected one top level FCI template tag %s, found %s.", FCI_TEMPLATE, Integer.valueOf(collection.size()));
            return EMPTY_PDOL_RESPONSE;
        }
        BasicTlv basicTlv = null;
        for (BasicTlv basicTlv2 : ((BasicTlv) collection.iterator().next()).asConstructedTlv().getChildren()) {
            BasicTlv basicTlv22;
            if (basicTlv22.getTag() == FCI_PROPRIETARY_TEMPLATE.intValue()) {
                for (BasicTlv basicTlv222 : basicTlv222.asConstructedTlv().getChildren()) {
                    if (basicTlv222.getTag() == PDOL_REQUEST_TAG.intValue()) {
                        if (basicTlv == null) {
                            basicTlv = basicTlv222;
                        } else {
                            LOG.w("Extra PDOL request found and will be ignored: %s", basicTlv222);
                        }
                    }
                    basicTlv222 = basicTlv;
                    basicTlv = basicTlv222;
                }
            }
        }
        if (basicTlv != null) {
            return getPdolResponses(getPdolRequests(basicTlv));
        }
        LOG.w("No PDOL request found.", new Object[0]);
        return EMPTY_PDOL_RESPONSE;
    }

    private static byte[] getPdolResponses(Collection<PdolRequest> collection) {
        byte[] bArr;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (PdolRequest pdolRequest : collection) {
            int tag = pdolRequest.getTag();
            int expectedSize = pdolRequest.getExpectedSize();
            if (PDOL_RESPONSES.containsKey(Integer.valueOf(tag))) {
                bArr = (byte[]) PDOL_RESPONSES.get(Integer.valueOf(tag));
                if (bArr.length != expectedSize) {
                    LOG.w("Terminal PDOL response for tag %s has length %s but length %s was expected. Returning all zeros. Original response: %s", Integer.valueOf(tag), Integer.valueOf(bArr.length), Integer.valueOf(expectedSize), bArr);
                    bArr = new byte[expectedSize];
                    Arrays.fill(bArr, (byte) 0);
                }
            } else {
                LOG.w("Terminal has no PDOL response for tag %s. Returning all zeros.", Integer.valueOf(tag));
                bArr = new byte[expectedSize];
                Arrays.fill(bArr, (byte) 0);
            }
            byteArrayOutputStream.write(bArr, 0, bArr.length);
        }
        bArr = byteArrayOutputStream.toByteArray();
        r1 = new byte[3][];
        r1[0] = new byte[]{(byte) -125};
        r1[1] = new byte[]{(byte) bArr.length};
        r1[2] = bArr;
        return Bytes.concat(r1);
    }

    private static Collection<PdolRequest> getPdolRequests(BasicTlv basicTlv) throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException {
        Collection arrayList = new ArrayList();
        TagLengthBuffer wrap = TagLengthBuffer.wrap(basicTlv.getValue(), 0);
        while (wrap.remaining() > 0) {
            arrayList.add(new PdolRequest(wrap.getTag(), wrap.getLength()));
        }
        return arrayList;
    }
}
