package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import android.annotation.TargetApi;
import com.google.android.libraries.commerce.hce.applet.smarttap.SmartTapException;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.MerchantInfo;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCommand.Response;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlv;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvException;
import com.google.android.libraries.commerce.hce.basictlv.BasicTlvUtil;
import com.google.android.libraries.commerce.hce.iso7816.Iso7816StatusWord;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.android.libraries.commerce.hce.util.Bcd;
import com.google.android.libraries.commerce.hce.util.Hex;
import com.google.android.libraries.logging.text.FormattingLogger;
import com.google.android.libraries.logging.text.FormattingLoggers;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

@TargetApi(19)
public class GetSmartTapDataCommand implements SmartTapCommand {
    private static final FormattingLogger LOG = FormattingLoggers.newContextLogger();
    static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyyMMddkkmmss");
    private MerchantInfo merchantInfo;
    private boolean moreData;
    private Set<RedeemableEntity> moreRedeemableEntities;
    private final SmartTapCallback smartTapCallback;

    @Inject
    public GetSmartTapDataCommand(SmartTapCallback smartTapCallback) {
        this.smartTapCallback = smartTapCallback;
    }

    public Response process(byte[] bArr) throws SmartTapException {
        Throwable e;
        try {
            this.merchantInfo = parseMerchantInfo(bArr);
            return createGetSmartTapDataResponse(ImmutableSet.copyOf(this.smartTapCallback.getRedemptionInfos(this.merchantInfo)));
        } catch (BasicTlvException e2) {
            e = e2;
            throw new SmartTapException(String.format("TLV parsing failed for APDU [%s]", new Object[]{BaseEncoding.base16().encode(bArr)}), e);
        } catch (IllegalArgumentException e3) {
            e = e3;
            throw new SmartTapException(String.format("TLV parsing failed for APDU [%s]", new Object[]{BaseEncoding.base16().encode(bArr)}), e);
        }
    }

    private MerchantInfo parseMerchantInfo(byte[] bArr) throws BasicTlvException, SmartTapException {
        Date date = null;
        List<BasicTlv> decodedInstances = BasicTlv.getDecodedInstances(bArr, 5, bArr.length - 1);
        List arrayList = new ArrayList();
        Map enumMap = new EnumMap(MerchantCapability.class);
        String str = null;
        long j = 0;
        for (BasicTlv basicTlv : decodedInstances) {
            switch (basicTlv.getTag()) {
                case 57105:
                    try {
                        date = TIME_FORMAT.parse(Long.toString(Bcd.decode(basicTlv.getValue())));
                        break;
                    } catch (Throwable e) {
                        throw new SmartTapException("Time parsing failed", e);
                    }
                case 57106:
                    break;
                case 57137:
                    j = Bcd.decode(basicTlv.getValue());
                    break;
                case 57138:
                    str = new String(basicTlv.getValue(), StandardCharsets.US_ASCII);
                    break;
                case 57139:
                    enumMap = MerchantCapability.parse(basicTlv.getValue());
                    break;
                case 57153:
                    arrayList.add(Long.valueOf(Bcd.decode(basicTlv.getValue())));
                    break;
                default:
                    break;
            }
        }
        if (j == 0) {
            throw new IllegalArgumentException("No merchant id specified");
        }
        LOG.d("Received merchant info: %s", MerchantInfo.create(j, str, arrayList, date, enumMap));
        return MerchantInfo.create(j, str, arrayList, date, enumMap);
    }

    private GetSmartTapDataResponse createGetSmartTapDataResponse(Set<RedeemableEntity> set) {
        int i;
        byte[] consumerId = this.smartTapCallback.getConsumerId(this.merchantInfo.merchantId());
        LOG.d("Building response for customer: %s, redeemables: %s", Hex.encode(consumerId), set);
        List arrayList = new ArrayList();
        Set hashSet = new HashSet();
        if (this.moreData) {
            i = 0;
        } else {
            BasicTlv tlv;
            if (consumerId.length <= 16) {
                tlv = BasicTlvUtil.tlv(57121, consumerId);
            } else {
                LOG.w("Given ConsumerId length > 16, trim to 16", new Object[0]);
                tlv = BasicTlvUtil.tlv(57121, Arrays.copyOfRange(consumerId, consumerId.length - 16, consumerId.length - 1));
            }
            arrayList.add(tlv);
            i = tlv.getSize();
        }
        this.moreData = false;
        this.moreRedeemableEntities = new HashSet();
        int i2 = i;
        for (RedeemableEntity redeemableEntity : set) {
            if (this.moreData) {
                this.moreRedeemableEntities.add(redeemableEntity);
            } else {
                List<BasicTlv> arrayList2 = new ArrayList();
                switch (redeemableEntity.type()) {
                    case LOYALTY:
                        arrayList2.add(BasicTlvUtil.tlv(57153, Bcd.encode(redeemableEntity.programId().longValue())));
                        arrayList2.add(BasicTlvUtil.tlv(57155, redeemableEntity.value()));
                        break;
                    case OFFER:
                        if (redeemableEntity.programId() != null) {
                            arrayList2.add(BasicTlvUtil.tlv(57169, Bcd.encode(redeemableEntity.programId().longValue())));
                        }
                        arrayList2.add(BasicTlvUtil.tlv(57171, redeemableEntity.value()));
                        break;
                    default:
                        continue;
                }
                int i3 = i2;
                for (BasicTlv size : arrayList2) {
                    i3 += size.getSize();
                }
                if (i3 > 255) {
                    this.moreData = true;
                    this.moreRedeemableEntities.add(redeemableEntity);
                } else {
                    arrayList.addAll(arrayList2);
                    hashSet.add(redeemableEntity);
                }
                i2 = i3;
            }
        }
        return new GetSmartTapDataResponse(ResponseApdu.fromDataAndStatusWord(BasicTlvUtil.tlvToByteArray((BasicTlv[]) arrayList.toArray(new BasicTlv[0])), this.moreData ? Iso7816StatusWord.WARNING_MORE_DATA : Iso7816StatusWord.NO_ERROR), hashSet, this.merchantInfo.merchantId());
    }

    public Response getMoreData() {
        if (this.merchantInfo == null || !this.moreData) {
            return new Response(Iso7816StatusWord.CONDITIONS_NOT_SATISFIED);
        }
        return createGetSmartTapDataResponse(this.moreRedeemableEntities);
    }
}
