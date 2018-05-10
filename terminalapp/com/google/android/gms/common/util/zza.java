package com.google.android.gms.common.util;

import android.support.v4.util.ArrayMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

public class zza<E> extends AbstractSet<E> {
    private final ArrayMap<E, E> Nj = new ArrayMap();

    public boolean add(E e) {
        if (this.Nj.containsKey(e)) {
            return false;
        }
        this.Nj.put(e, e);
        return true;
    }

    public boolean addAll(Collection<? extends E> collection) {
        return collection instanceof zza ? zza((zza) collection) : super.addAll(collection);
    }

    public void clear() {
        this.Nj.clear();
    }

    public boolean contains(Object obj) {
        return this.Nj.containsKey(obj);
    }

    public Iterator<E> iterator() {
        return this.Nj.keySet().iterator();
    }

    public boolean remove(Object obj) {
        if (!this.Nj.containsKey(obj)) {
            return false;
        }
        this.Nj.remove(obj);
        return true;
    }

    public int size() {
        return this.Nj.size();
    }

    public boolean zza(zza<? extends E> com_google_android_gms_common_util_zza__extends_E) {
        int size = size();
        this.Nj.putAll(com_google_android_gms_common_util_zza__extends_E.Nj);
        return size() > size;
    }
}
