package android.support.v4.text;

public abstract class TextDirectionHeuristicCompat {
    private final TextDirectionAlgorithm mAlgorithm;

    protected abstract boolean defaultIsRtl();

    public TextDirectionHeuristicCompat(TextDirectionAlgorithm textDirectionAlgorithm) {
        this.mAlgorithm = textDirectionAlgorithm;
    }

    public boolean isRtl(CharSequence charSequence, int i, int i2) {
        if (charSequence == null || i < 0 || i2 < 0 || charSequence.length() - i2 < i) {
            throw new IllegalArgumentException();
        } else if (this.mAlgorithm == null) {
            return defaultIsRtl();
        } else {
            return doCheck(charSequence, i, i2);
        }
    }

    private boolean doCheck(CharSequence charSequence, int i, int i2) {
        switch (this.mAlgorithm.checkRtl(charSequence, i, i2)) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                return defaultIsRtl();
        }
    }
}
