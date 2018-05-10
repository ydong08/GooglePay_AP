package org.bouncycastle.asn1.x500;

public interface X500NameStyle {
    boolean areEqual(X500Name x500Name, X500Name x500Name2);

    int calculateHashCode(X500Name x500Name);

    String toString(X500Name x500Name);
}
