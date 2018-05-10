package com.google.common.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Joiner {
    private final String separator;

    public static final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        private MapJoiner(Joiner joiner, String str) {
            this.joiner = joiner;
            this.keyValueSeparator = (String) Preconditions.checkNotNull(str);
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Map<?, ?> map) {
            return appendTo(stringBuilder, map.entrySet());
        }

        public <A extends Appendable> A appendTo(A a, Iterator<? extends Entry<?, ?>> it) throws IOException {
            Preconditions.checkNotNull(a);
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                a.append(this.joiner.toString(entry.getKey()));
                a.append(this.keyValueSeparator);
                a.append(this.joiner.toString(entry.getValue()));
                while (it.hasNext()) {
                    a.append(this.joiner.separator);
                    entry = (Entry) it.next();
                    a.append(this.joiner.toString(entry.getKey()));
                    a.append(this.keyValueSeparator);
                    a.append(this.joiner.toString(entry.getValue()));
                }
            }
            return a;
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Iterable<? extends Entry<?, ?>> iterable) {
            return appendTo(stringBuilder, iterable.iterator());
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Iterator<? extends Entry<?, ?>> it) {
            try {
                appendTo((Appendable) stringBuilder, (Iterator) it);
                return stringBuilder;
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    public static Joiner on(String str) {
        return new Joiner(str);
    }

    public static Joiner on(char c) {
        return new Joiner(String.valueOf(c));
    }

    private Joiner(String str) {
        this.separator = (String) Preconditions.checkNotNull(str);
    }

    private Joiner(Joiner joiner) {
        this.separator = joiner.separator;
    }

    public <A extends Appendable> A appendTo(A a, Iterator<?> it) throws IOException {
        Preconditions.checkNotNull(a);
        if (it.hasNext()) {
            a.append(toString(it.next()));
            while (it.hasNext()) {
                a.append(this.separator);
                a.append(toString(it.next()));
            }
        }
        return a;
    }

    public final StringBuilder appendTo(StringBuilder stringBuilder, Iterator<?> it) {
        try {
            appendTo((Appendable) stringBuilder, (Iterator) it);
            return stringBuilder;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public final String join(Iterable<?> iterable) {
        return join(iterable.iterator());
    }

    public final String join(Iterator<?> it) {
        return appendTo(new StringBuilder(), (Iterator) it).toString();
    }

    public final String join(Object[] objArr) {
        return join(Arrays.asList(objArr));
    }

    public Joiner useForNull(final String str) {
        Preconditions.checkNotNull(str);
        return new Joiner(this) {
            CharSequence toString(Object obj) {
                return obj == null ? str : Joiner.this.toString(obj);
            }

            public Joiner useForNull(String str) {
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }

    public MapJoiner withKeyValueSeparator(String str) {
        return new MapJoiner(str);
    }

    CharSequence toString(Object obj) {
        Preconditions.checkNotNull(obj);
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }
}
