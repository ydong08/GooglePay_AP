package com.google.android.libraries.commerce.hce.applet.smarttap.ose;

import com.google.android.libraries.commerce.hce.basictlv.BasicConstructedTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicPrimitiveTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.iso7816.Aid;
import com.google.common.base.Optional;
import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class AidInfo implements Comparable<AidInfo> {
    public abstract Aid aid();

    public abstract int priority();

    public abstract Optional<SmartTap2ProprietaryData> proprietaryDataOptional();

    public static AidInfo create(Aid aid, int i, Optional<SmartTap2ProprietaryData> optional) {
        return new AutoValue_AidInfo(aid, i, optional);
    }

    public int compareTo(AidInfo aidInfo) {
        return Integer.compare(priority(), aidInfo.priority());
    }

    static Optional<AidInfo> parse(BasicTlv basicTlv) throws BasicTlvException {
        Aid aid = null;
        Optional absent = Optional.absent();
        BasicConstructedTlv asConstructedTlv = basicTlv.asConstructedTlv();
        int i = 0;
        for (BasicTlv basicTlv2 : asConstructedTlv.getChildren()) {
            int i2;
            Aid valueOf;
            Optional optional;
            switch (basicTlv2.getTag()) {
                case 79:
                    byte[] value = basicTlv2.asPrimitiveTlv().getValue();
                    if (value != null && value.length != 0) {
                        Optional optional2;
                        if (!Arrays.equals(Aid.SMART_TAP_AID_V2_0.array(), value)) {
                            if (!Arrays.equals(Aid.SMART_TAP_AID_V1_3.array(), value)) {
                                optional2 = absent;
                                i2 = i;
                                valueOf = Aid.valueOf(value);
                                optional = optional2;
                                break;
                            }
                            optional2 = absent;
                            i2 = i;
                            valueOf = Aid.SMART_TAP_AID_V1_3;
                            optional = optional2;
                            break;
                        }
                        optional2 = absent;
                        i2 = i;
                        valueOf = Aid.SMART_TAP_AID_V2_0;
                        optional = optional2;
                        break;
                    }
                    return Optional.absent();
                case 115:
                    optional = Optional.of(SmartTap2ProprietaryData.parse(basicTlv2));
                    i2 = i;
                    valueOf = aid;
                    break;
                case 135:
                    valueOf = aid;
                    int asInt = getAsInt(asConstructedTlv.getChild(135).asPrimitiveTlv().getValue());
                    optional = absent;
                    i2 = asInt;
                    break;
                default:
                    optional = absent;
                    i2 = i;
                    valueOf = aid;
                    break;
            }
            aid = valueOf;
            i = i2;
            absent = optional;
        }
        if (aid == null) {
            return Optional.absent();
        }
        return Optional.of(create(aid, i, absent));
    }

    private static int getAsInt(byte[] bArr) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        for (int i = 0; i < 4 - bArr.length; i++) {
            allocate.put((byte) 0);
        }
        allocate.put(bArr).rewind();
        return allocate.getInt();
    }

    BasicTlv toTlv() throws BasicTlvException {
        BasicTlv instance = BasicPrimitiveTlv.getInstance(79, aid().array());
        BasicTlv addChild = BasicConstructedTlv.getInstance(97).addChild(instance).addChild(BasicPrimitiveTlv.getInstance(135, (byte) priority()));
        if (proprietaryDataOptional().isPresent()) {
            addChild.addChild(((SmartTap2ProprietaryData) proprietaryDataOptional().get()).toTlv());
        }
        return addChild;
    }
}
