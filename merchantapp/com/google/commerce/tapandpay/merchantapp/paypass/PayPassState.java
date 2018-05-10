package com.google.commerce.tapandpay.merchantapp.paypass;

import com.google.common.collect.Sets;
import java.util.Collection;

enum PayPassState {
    IDLE(Sets.newHashSet(PayPassCommandApduIns.SELECT)),
    SELECTED(Sets.newHashSet(PayPassCommandApduIns.SELECT, PayPassCommandApduIns.GPO, PayPassCommandApduIns.READ_RECORD)),
    INITIATED(Sets.newHashSet(PayPassCommandApduIns.SELECT, PayPassCommandApduIns.READ_RECORD, PayPassCommandApduIns.CCC));
    
    private final Collection<PayPassCommandApduIns> mAcceptedCommands;

    private PayPassState(Collection<PayPassCommandApduIns> collection) {
        this.mAcceptedCommands = collection;
    }

    boolean acceptsCommand(PayPassCommandApduIns payPassCommandApduIns) {
        return this.mAcceptedCommands.contains(payPassCommandApduIns);
    }
}
