package org.bouncycastle.math.field;

public interface PolynomialExtensionField extends FiniteField {
    Polynomial getMinimalPolynomial();
}
