package com.google.android.libraries.commerce.hce.basictlv;

import com.google.android.libraries.commerce.hce.base.NonnullPredicate;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BasicConstructedTlv extends BasicTlv {
    private final Collection<BasicTlv> mChildren = new ArrayList();

    private BasicConstructedTlv(int i) throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException {
        super(i);
        if (!BasicTlv.tagIsConstructed(i)) {
            throw new BasicTlvInvalidTagException(i);
        }
    }

    public static BasicConstructedTlv getDecodedInstance(int i, int i2, byte[] bArr, int i3) throws BasicTlvInvalidLengthException, BasicTlvInvalidTagException, BasicTlvInvalidValueException {
        BasicConstructedTlv basicConstructedTlv = new BasicConstructedTlv(i);
        int i4 = i3 + i2;
        int i5 = i3;
        while (i5 < i4) {
            BasicTlv decodedInstance = BasicTlv.getDecodedInstance(bArr, i5);
            basicConstructedTlv.addChild(decodedInstance);
            i5 += decodedInstance.getSize();
        }
        if (i5 == i4) {
            return basicConstructedTlv;
        }
        throw new BasicTlvInvalidLengthException(i2, i5 - i3);
    }

    public static BasicConstructedTlv getInstance(int i) throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException {
        return new BasicConstructedTlv(i);
    }

    public final int getLength() {
        int i = 0;
        for (BasicTlv size : this.mChildren) {
            i = size.getSize() + i;
        }
        return i;
    }

    public final Collection<BasicTlv> getChildren() {
        return Collections.unmodifiableCollection(this.mChildren);
    }

    public final BasicConstructedTlv addChild(BasicTlv basicTlv) {
        this.mChildren.add(basicTlv);
        return this;
    }

    public final BasicConstructedTlv addAll(Collection<BasicTlv> collection) {
        this.mChildren.addAll(collection);
        return this;
    }

    public BasicTlv getChild(int... iArr) throws NoSuchElementException, BasicTlvInvalidTagException {
        BasicTlv basicTlv = this;
        for (final int i : iArr) {
            basicTlv = (BasicTlv) Iterables.find(basicTlv.asConstructedTlv().getChildren(), new NonnullPredicate<BasicTlv>(this) {
                public boolean applyNonnull(BasicTlv basicTlv) {
                    return basicTlv.getTag() == i;
                }
            });
        }
        return basicTlv;
    }

    protected final int encodeValue(byte[] bArr, int i) throws BasicTlvInvalidTagException, BasicTlvInvalidLengthException {
        for (BasicTlv encode : this.mChildren) {
            i = encode.encode(bArr, i);
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BasicConstructedTlv)) {
            return false;
        }
        return Objects.equals(this.mChildren, ((BasicConstructedTlv) obj).mChildren);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mChildren.hashCode())});
    }

    public final String toString() {
        Joiner on = Joiner.on(",");
        Object[] objArr = new String[3];
        objArr[0] = getTagAsString();
        objArr[1] = Integer.toHexString(getLength());
        String valueOf = String.valueOf(Joiner.on(",").join(this.mChildren));
        objArr[2] = new StringBuilder(String.valueOf(valueOf).length() + 2).append("[").append(valueOf).append("]").toString();
        String valueOf2 = String.valueOf(on.join(objArr).toUpperCase());
        return new StringBuilder(String.valueOf(valueOf2).length() + 2).append("(").append(valueOf2).append(")").toString();
    }
}
