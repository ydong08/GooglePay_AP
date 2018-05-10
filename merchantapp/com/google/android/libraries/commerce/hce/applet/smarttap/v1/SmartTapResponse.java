package com.google.android.libraries.commerce.hce.applet.smarttap.v1;

import com.google.android.libraries.commerce.hce.applet.HceApplet.AppletProcessCommandResponse;
import com.google.android.libraries.commerce.hce.applet.smarttap.v1.SmartTapCallback.RedeemableEntity;
import com.google.android.libraries.commerce.hce.iso7816.ResponseApdu;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class SmartTapResponse implements AppletProcessCommandResponse {
    private final int instruction;
    private final Optional<Long> merchantIdOptional;
    private final ResponseApdu responseApdu;
    private final Set<? extends RedeemableEntity> returnedCards;

    public SmartTapResponse(int i, ResponseApdu responseApdu, Long l, Set<? extends RedeemableEntity> set) {
        this.instruction = i;
        this.responseApdu = responseApdu;
        this.merchantIdOptional = Optional.fromNullable(l);
        if (set == null) {
            set = ImmutableSet.of();
        }
        this.returnedCards = set;
    }

    public ResponseApdu getResponseApdu() {
        return this.responseApdu;
    }
}
