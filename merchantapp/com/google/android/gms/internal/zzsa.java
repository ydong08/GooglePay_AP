package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.zzab;
import java.lang.ref.WeakReference;

public class zzsa<R extends Result> extends TransformedResult<R> implements ResultCallback<R> {
    private final Object DS;
    private final WeakReference<GoogleApiClient> DU;
    private ResultTransform<? super R, ? extends Result> GD;
    private zzsa<? extends Result> GE;
    private volatile ResultCallbacks<? super R> GF;
    private PendingResult<R> GG;
    private Status GH;
    private final zza GI;
    private boolean GJ;

    final class zza extends Handler {
        final /* synthetic */ zzsa GL;

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    PendingResult pendingResult = (PendingResult) message.obj;
                    synchronized (this.GL.DS) {
                        if (pendingResult == null) {
                            this.GL.GE.zzak(new Status(13, "Transform returned null"));
                        } else if (pendingResult instanceof zzrt) {
                            this.GL.GE.zzak(((zzrt) pendingResult).getStatus());
                        } else {
                            this.GL.GE.zza(pendingResult);
                        }
                    }
                    return;
                case 1:
                    RuntimeException runtimeException = (RuntimeException) message.obj;
                    String str = "TransformedResultImpl";
                    String str2 = "Runtime exception on the transformation worker thread: ";
                    String valueOf = String.valueOf(runtimeException.getMessage());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                    throw runtimeException;
                default:
                    Log.e("TransformedResultImpl", "TransformationResultHandler received unknown message type: " + message.what);
                    return;
            }
        }
    }

    private void zzak(Status status) {
        synchronized (this.DS) {
            this.GH = status;
            zzal(this.GH);
        }
    }

    private void zzal(Status status) {
        synchronized (this.DS) {
            if (this.GD != null) {
                Object onFailure = this.GD.onFailure(status);
                zzab.zzb(onFailure, (Object) "onFailure must not return null");
                this.GE.zzak(onFailure);
            } else if (zzauu()) {
                ResultCallbacks resultCallbacks = this.GF;
            }
        }
    }

    private void zzaus() {
        if (this.GD != null || this.GF != null) {
            GoogleApiClient googleApiClient = (GoogleApiClient) this.DU.get();
            if (!(this.GJ || this.GD == null || googleApiClient == null)) {
                googleApiClient.zza(this);
                this.GJ = true;
            }
            if (this.GH != null) {
                zzal(this.GH);
            } else if (this.GG != null) {
                this.GG.setResultCallback(this);
            }
        }
    }

    private boolean zzauu() {
        return (this.GF == null || ((GoogleApiClient) this.DU.get()) == null) ? false : true;
    }

    private void zze(Result result) {
        if (!(result instanceof Releasable)) {
        }
    }

    public void onResult(final R r) {
        synchronized (this.DS) {
            if (!r.getStatus().isSuccess()) {
                zzak(r.getStatus());
                zze((Result) r);
            } else if (this.GD != null) {
                zzrs.zzatv().submit(new Runnable(this) {
                    final /* synthetic */ zzsa GL;

                    public void run() {
                        GoogleApiClient googleApiClient;
                        try {
                            zzqm.DR.set(Boolean.valueOf(true));
                            this.GL.GI.sendMessage(this.GL.GI.obtainMessage(0, this.GL.GD.onSuccess(r)));
                            zzqm.DR.set(Boolean.valueOf(false));
                            this.GL.zze(r);
                            googleApiClient = (GoogleApiClient) this.GL.DU.get();
                            if (googleApiClient != null) {
                                googleApiClient.zzb(this.GL);
                            }
                        } catch (RuntimeException e) {
                            this.GL.GI.sendMessage(this.GL.GI.obtainMessage(1, e));
                            zzqm.DR.set(Boolean.valueOf(false));
                            this.GL.zze(r);
                            googleApiClient = (GoogleApiClient) this.GL.DU.get();
                            if (googleApiClient != null) {
                                googleApiClient.zzb(this.GL);
                            }
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            zzqm.DR.set(Boolean.valueOf(false));
                            this.GL.zze(r);
                            googleApiClient = (GoogleApiClient) this.GL.DU.get();
                            if (googleApiClient != null) {
                                googleApiClient.zzb(this.GL);
                            }
                        }
                    }
                });
            } else if (zzauu()) {
                ResultCallbacks resultCallbacks = this.GF;
            }
        }
    }

    public void zza(PendingResult<?> pendingResult) {
        synchronized (this.DS) {
            this.GG = pendingResult;
            zzaus();
        }
    }
}
