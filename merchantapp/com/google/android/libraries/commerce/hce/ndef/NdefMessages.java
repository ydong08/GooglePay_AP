package com.google.android.libraries.commerce.hce.ndef;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import com.google.android.libraries.commerce.hce.primitives.ByteArrayWrapper;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class NdefMessages {
    private NdefMessages() {
    }

    public static NdefMessage compose(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, Collection<NdefRecord> collection) {
        return new NdefMessage(updateRecords((Multimap) multimap, (Set) set, bArr, s, (Collection) collection));
    }

    public static NdefMessage compose(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, NdefRecord... ndefRecordArr) {
        return new NdefMessage(updateRecords((Multimap) multimap, (Set) set, bArr, s, ndefRecordArr));
    }

    public static Optional<NdefMessage> fromOptionals(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, Optional<NdefRecord>... optionalArr) {
        Collection arrayList = new ArrayList();
        for (Optional optional : optionalArr) {
            if (optional.isPresent()) {
                NdefRecord ndefRecord = (NdefRecord) optional.get();
                if (ndefRecord != null) {
                    arrayList.add(ndefRecord);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(new NdefMessage(updateRecords((Multimap) multimap, (Set) set, bArr, s, arrayList)));
    }

    private static NdefRecord[] updateRecords(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, Collection<NdefRecord> collection) {
        if (collection.size() == 0 || !modificationsPresent(multimap, set, bArr)) {
            return (NdefRecord[]) collection.toArray(new NdefRecord[collection.size()]);
        }
        ByteArrayWrapper byteArrayWrapper = new ByteArrayWrapper(bArr);
        if (multimap != null && multimap.containsKey(byteArrayWrapper)) {
            collection.addAll(multimap.get(byteArrayWrapper));
        }
        if (set != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                if (set.contains(new ByteArrayWrapper(NdefRecords.getType((NdefRecord) it.next(), s)))) {
                    it.remove();
                }
            }
        }
        return (NdefRecord[]) collection.toArray(new NdefRecord[collection.size()]);
    }

    private static NdefRecord[] updateRecords(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr, short s, NdefRecord... ndefRecordArr) {
        return (ndefRecordArr.length == 0 || !modificationsPresent(multimap, set, bArr)) ? ndefRecordArr : updateRecords((Multimap) multimap, (Set) set, bArr, s, Lists.newArrayList((Object[]) ndefRecordArr));
    }

    private static boolean modificationsPresent(Multimap<ByteArrayWrapper, NdefRecord> multimap, Set<ByteArrayWrapper> set, byte[] bArr) {
        if (multimap != null && !multimap.isEmpty() && bArr != null) {
            return true;
        }
        if (set == null || set.isEmpty()) {
            return false;
        }
        return true;
    }
}
