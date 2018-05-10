package com.google.common.math;

final class MathPreconditions {
    static int checkPositive(String str, int i) {
        if (i > 0) {
            return i;
        }
        throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 26).append(str).append(" (").append(i).append(") must be > 0").toString());
    }

    static void checkRoundingUnnecessary(boolean z) {
        if (!z) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    private MathPreconditions() {
    }
}
