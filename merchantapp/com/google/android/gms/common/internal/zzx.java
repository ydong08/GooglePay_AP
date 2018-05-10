package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.zzd;

public interface zzx extends IInterface {

    public static abstract class zza extends Binder implements zzx {

        static class zza implements zzx {
            private IBinder zzahm;

            zza(IBinder iBinder) {
                this.zzahm = iBinder;
            }

            public IBinder asBinder() {
                return this.zzahm;
            }

            public zzd zza(zzd com_google_android_gms_dynamic_zzd, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.ISignInButtonCreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.zzahm.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzis = com.google.android.gms.dynamic.zzd.zza.zzis(obtain2.readStrongBinder());
                    return zzis;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zza(zzd com_google_android_gms_dynamic_zzd, SignInButtonConfig signInButtonConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.ISignInButtonCreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    if (signInButtonConfig != null) {
                        obtain.writeInt(1);
                        signInButtonConfig.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzahm.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzis = com.google.android.gms.dynamic.zzd.zza.zzis(obtain2.readStrongBinder());
                    return zzis;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzx zzhd(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzx)) ? new zza(iBinder) : (zzx) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IBinder iBinder = null;
            zzd zza;
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
                    zza = zza(com.google.android.gms.dynamic.zzd.zza.zzis(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zza != null ? zza.asBinder() : null);
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
                    zza = zza(com.google.android.gms.dynamic.zzd.zza.zzis(parcel.readStrongBinder()), parcel.readInt() != 0 ? (SignInButtonConfig) SignInButtonConfig.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (zza != null) {
                        iBinder = zza.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.common.internal.ISignInButtonCreator");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    zzd zza(zzd com_google_android_gms_dynamic_zzd, int i, int i2) throws RemoteException;

    zzd zza(zzd com_google_android_gms_dynamic_zzd, SignInButtonConfig signInButtonConfig) throws RemoteException;
}
