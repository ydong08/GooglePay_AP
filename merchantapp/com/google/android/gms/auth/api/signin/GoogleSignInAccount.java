package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.zzg;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleSignInAccount extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<GoogleSignInAccount> CREATOR = new zza();
    private static Comparator<Scope> fG = new Comparator<Scope>() {
        public /* synthetic */ int compare(Object obj, Object obj2) {
            return zza((Scope) obj, (Scope) obj2);
        }

        public int zza(Scope scope, Scope scope2) {
            return scope.zzase().compareTo(scope2.zzase());
        }
    };
    public static Clock fy = zzg.zzazb();
    private String cs;
    private String eW;
    List<Scope> ek;
    private Uri fA;
    private String fB;
    private long fC;
    private String fD;
    private String fE;
    private String fF;
    private String fz;
    final int versionCode;
    private String zzbfz;

    GoogleSignInAccount(int i, String str, String str2, String str3, String str4, Uri uri, String str5, long j, String str6, List<Scope> list, String str7, String str8) {
        this.versionCode = i;
        this.zzbfz = str;
        this.eW = str2;
        this.fz = str3;
        this.cs = str4;
        this.fA = uri;
        this.fB = str5;
        this.fC = j;
        this.fD = str6;
        this.ek = list;
        this.fE = str7;
        this.fF = str8;
    }

    private JSONObject zzaeh() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (getId() != null) {
                jSONObject.put("id", getId());
            }
            if (getIdToken() != null) {
                jSONObject.put("tokenId", getIdToken());
            }
            if (getEmail() != null) {
                jSONObject.put("email", getEmail());
            }
            if (getDisplayName() != null) {
                jSONObject.put("displayName", getDisplayName());
            }
            if (getGivenName() != null) {
                jSONObject.put("givenName", getGivenName());
            }
            if (getFamilyName() != null) {
                jSONObject.put("familyName", getFamilyName());
            }
            if (getPhotoUrl() != null) {
                jSONObject.put("photoUrl", getPhotoUrl().toString());
            }
            if (getServerAuthCode() != null) {
                jSONObject.put("serverAuthCode", getServerAuthCode());
            }
            jSONObject.put("expirationTime", this.fC);
            jSONObject.put("obfuscatedIdentifier", zzaee());
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.ek, fG);
            for (Scope zzase : this.ek) {
                jSONArray.put(zzase.zzase());
            }
            jSONObject.put("grantedScopes", jSONArray);
            return jSONObject;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object obj) {
        return !(obj instanceof GoogleSignInAccount) ? false : ((GoogleSignInAccount) obj).zzaef().equals(zzaef());
    }

    public String getDisplayName() {
        return this.cs;
    }

    public String getEmail() {
        return this.fz;
    }

    public String getFamilyName() {
        return this.fF;
    }

    public String getGivenName() {
        return this.fE;
    }

    public String getId() {
        return this.zzbfz;
    }

    public String getIdToken() {
        return this.eW;
    }

    public Uri getPhotoUrl() {
        return this.fA;
    }

    public String getServerAuthCode() {
        return this.fB;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }

    public long zzaed() {
        return this.fC;
    }

    public String zzaee() {
        return this.fD;
    }

    public String zzaef() {
        return zzaeh().toString();
    }
}
