package com.google.android.libraries.commerce.hce.applet.smarttap.v2;

import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.common.SmartTap2Values;
import com.google.android.libraries.commerce.hce.ndef.NdefMessages;
import com.google.android.libraries.commerce.hce.ndef.NdefRecords;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class IssuerServiceObjects {
    private IssuerServiceObjects() {
    }

    public static List<NdefRecord> toNdef(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, short s, Iterable<ServiceObject> iterable) {
        ListMultimap create = ArrayListMultimap.create();
        for (ServiceObject serviceObject : iterable) {
            create.put(new ByteArrayWrapper(serviceObject.issuerId()), serviceObject);
        }
        List<NdefRecord> arrayList = new ArrayList();
        for (ByteArrayWrapper byteArrayWrapper : create.keySet()) {
            NdefRecord ndefRecord = null;
            Collection arrayList2 = new ArrayList();
            for (ServiceObject serviceObject2 : create.get(byteArrayWrapper)) {
                NdefRecord issuerNdef;
                Optional serviceObjectNdef = serviceObject2.getServiceObjectNdef(multimap, set, s);
                if (serviceObjectNdef.isPresent()) {
                    arrayList2.add((NdefRecord) serviceObjectNdef.get());
                    if (ndefRecord == null) {
                        issuerNdef = serviceObject2.getIssuerNdef(s);
                        ndefRecord = issuerNdef;
                    }
                }
                issuerNdef = ndefRecord;
                ndefRecord = issuerNdef;
            }
            if (!arrayList2.isEmpty()) {
                arrayList2.add(0, ndefRecord);
                byte[] serviceValueNdefType = SmartTap2Values.getServiceValueNdefType(s);
                arrayList.add(NdefRecords.compose(serviceValueNdefType, NdefMessages.compose((Multimap) multimap, (Set) set, serviceValueNdefType, s, arrayList2).toByteArray(), s));
            }
        }
        return arrayList;
    }
}
