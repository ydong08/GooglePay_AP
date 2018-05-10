package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;

public interface zzs extends IInterface {

    public static abstract class zza extends Binder implements zzs {
        public zza() {
            attachInterface(this, "com.google.android.gms.common.internal.ICertData");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.common.internal.ICertData");
                    zzd zzarf = zzarf();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zzarf != null ? zzarf.asBinder() : null);
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.common.internal.ICertData");
                    int zzarg = zzarg();
                    parcel2.writeNoException();
                    parcel2.writeInt(zzarg);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.common.internal.ICertData");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    zzd zzarf() throws RemoteException;

    int zzarg() throws RemoteException;
}
