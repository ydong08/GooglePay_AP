package com.google.common.base;

import java.util.Arrays;

public final class MoreObjects {

    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;

        static final class ValueHolder {
            String name;
            ValueHolder next;
            Object value;

            private ValueHolder() {
            }
        }

        private ToStringHelper(String str) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = (String) Preconditions.checkNotNull(str);
        }

        public ToStringHelper add(String str, Object obj) {
            return addHolder(str, obj);
        }

        public ToStringHelper add(String str, int i) {
            return addHolder(str, String.valueOf(i));
        }

        public String toString() {
            boolean z = this.omitNullValues;
            StringBuilder append = new StringBuilder(32).append(this.className).append('{');
            String str = "";
            for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                Object obj = valueHolder.value;
                if (!z || obj != null) {
                    append.append(str);
                    str = ", ";
                    if (valueHolder.name != null) {
                        append.append(valueHolder.name).append('=');
                    }
                    if (obj == null || !obj.getClass().isArray()) {
                        append.append(obj);
                    } else {
                        CharSequence deepToString = Arrays.deepToString(new Object[]{obj});
                        append.append(deepToString, 1, deepToString.length() - 1);
                    }
                }
            }
            return append.append('}').toString();
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            return valueHolder;
        }

        private ToStringHelper addHolder(String str, Object obj) {
            ValueHolder addHolder = addHolder();
            addHolder.value = obj;
            addHolder.name = (String) Preconditions.checkNotNull(str);
            return this;
        }
    }

    public static <T> T firstNonNull(T t, T t2) {
        return t != null ? t : Preconditions.checkNotNull(t2);
    }

    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(obj.getClass().getSimpleName());
    }

    private MoreObjects() {
    }
}
