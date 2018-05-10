package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public final class Tables {
    private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>() {
        public Map<Object, Object> apply(Map<Object, Object> map) {
            return Collections.unmodifiableMap(map);
        }
    };

    static abstract class AbstractCell<R, C, V> implements Cell<R, C, V> {
        AbstractCell() {
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            if (Objects.equal(getRowKey(), cell.getRowKey()) && Objects.equal(getColumnKey(), cell.getColumnKey()) && Objects.equal(getValue(), cell.getValue())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(getRowKey(), getColumnKey(), getValue());
        }

        public String toString() {
            String valueOf = String.valueOf(getRowKey());
            String valueOf2 = String.valueOf(getColumnKey());
            String valueOf3 = String.valueOf(getValue());
            return new StringBuilder(((String.valueOf(valueOf).length() + 4) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length()).append("(").append(valueOf).append(",").append(valueOf2).append(")=").append(valueOf3).toString();
        }
    }

    static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final C columnKey;
        private final R rowKey;
        private final V value;

        ImmutableCell(R r, C c, V v) {
            this.rowKey = r;
            this.columnKey = c;
            this.value = v;
        }

        public R getRowKey() {
            return this.rowKey;
        }

        public C getColumnKey() {
            return this.columnKey;
        }

        public V getValue() {
            return this.value;
        }
    }

    private Tables() {
    }

    public static <R, C, V> Cell<R, C, V> immutableCell(R r, C c, V v) {
        return new ImmutableCell(r, c, v);
    }

    static boolean equalsImpl(Table<?, ?, ?> table, Object obj) {
        if (obj == table) {
            return true;
        }
        if (!(obj instanceof Table)) {
            return false;
        }
        return table.cellSet().equals(((Table) obj).cellSet());
    }
}
