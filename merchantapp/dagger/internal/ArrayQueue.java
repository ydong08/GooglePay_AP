package dagger.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ArrayQueue<E> extends AbstractCollection<E> implements Serializable, Cloneable, Queue<E> {
    private static final long serialVersionUID = 2340985798034038923L;
    private transient Object[] elements = new Object[16];
    private transient int head;
    private transient int tail;

    class QueueIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private QueueIterator() {
            this.cursor = ArrayQueue.this.head;
            this.fence = ArrayQueue.this.tail;
            this.lastRet = -1;
        }

        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        public E next() {
            if (this.cursor == this.fence) {
                throw new NoSuchElementException();
            }
            E e = ArrayQueue.this.elements[this.cursor];
            if (ArrayQueue.this.tail != this.fence || e == null) {
                throw new ConcurrentModificationException();
            }
            this.lastRet = this.cursor;
            this.cursor = (this.cursor + 1) & (ArrayQueue.this.elements.length - 1);
            return e;
        }

        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            if (ArrayQueue.this.delete(this.lastRet)) {
                this.cursor = (this.cursor - 1) & (ArrayQueue.this.elements.length - 1);
                this.fence = ArrayQueue.this.tail;
            }
            this.lastRet = -1;
        }
    }

    private void allocateElements(int i) {
        int i2 = 8;
        if (i >= 8) {
            i2 = (i >>> 1) | i;
            i2 |= i2 >>> 2;
            i2 |= i2 >>> 4;
            i2 |= i2 >>> 8;
            i2 = (i2 | (i2 >>> 16)) + 1;
            if (i2 < 0) {
                i2 >>>= 1;
            }
        }
        this.elements = new Object[i2];
    }

    private void doubleCapacity() {
        int i = this.head;
        int length = this.elements.length;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 < 0) {
            throw new IllegalStateException("Sorry, queue too big");
        }
        Object obj = new Object[i3];
        System.arraycopy(this.elements, i, obj, 0, i2);
        System.arraycopy(this.elements, 0, obj, i2, i);
        this.elements = obj;
        this.head = 0;
        this.tail = length;
    }

    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("e == null");
        }
        this.elements[this.tail] = e;
        int length = (this.tail + 1) & (this.elements.length - 1);
        this.tail = length;
        if (length == this.head) {
            doubleCapacity();
        }
        return true;
    }

    public boolean offer(E e) {
        return add(e);
    }

    public E remove() {
        E poll = poll();
        if (poll != null) {
            return poll;
        }
        throw new NoSuchElementException();
    }

    public E poll() {
        int i = this.head;
        E e = this.elements[i];
        if (e == null) {
            return null;
        }
        this.elements[i] = null;
        this.head = (i + 1) & (this.elements.length - 1);
        return e;
    }

    public E element() {
        E e = this.elements[this.head];
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }

    public E peek() {
        return this.elements[this.head];
    }

    private boolean delete(int i) {
        Object obj = this.elements;
        int length = obj.length - 1;
        int i2 = this.head;
        int i3 = this.tail;
        int i4 = (i - i2) & length;
        int i5 = (i3 - i) & length;
        if (i4 >= ((i3 - i2) & length)) {
            throw new ConcurrentModificationException();
        } else if (i4 < i5) {
            if (i2 <= i) {
                System.arraycopy(obj, i2, obj, i2 + 1, i4);
            } else {
                System.arraycopy(obj, 0, obj, 1, i);
                obj[0] = obj[length];
                System.arraycopy(obj, i2, obj, i2 + 1, length - i2);
            }
            obj[i2] = null;
            this.head = (i2 + 1) & length;
            return false;
        } else {
            if (i < i3) {
                System.arraycopy(obj, i + 1, obj, i, i5);
                this.tail = i3 - 1;
            } else {
                System.arraycopy(obj, i + 1, obj, i, length - i);
                obj[length] = obj[0];
                System.arraycopy(obj, 1, obj, 0, i3);
                this.tail = (i3 - 1) & length;
            }
            return true;
        }
    }

    public int size() {
        return (this.tail - this.head) & (this.elements.length - 1);
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object obj2 = this.elements[i];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                return true;
            }
            i = (i + 1) & length;
        }
    }

    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object obj2 = this.elements[i];
            if (obj2 == null) {
                return false;
            }
            if (obj.equals(obj2)) {
                delete(i);
                return true;
            }
            i = (i + 1) & length;
        }
    }

    public void clear() {
        int i = this.head;
        int i2 = this.tail;
        if (i != i2) {
            this.tail = 0;
            this.head = 0;
            int length = this.elements.length - 1;
            do {
                this.elements[i] = null;
                i = (i + 1) & length;
            } while (i != i2);
        }
    }

    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    public <T> T[] toArray(T[] tArr) {
        Object obj;
        int size = size();
        if (tArr.length < size) {
            obj = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
        } else {
            obj = tArr;
        }
        if (this.head < this.tail) {
            System.arraycopy(this.elements, this.head, obj, 0, size());
        } else if (this.head > this.tail) {
            int length = this.elements.length - this.head;
            System.arraycopy(this.elements, this.head, obj, 0, length);
            System.arraycopy(this.elements, 0, obj, length, this.tail);
        }
        if (obj.length > size) {
            obj[size] = null;
        }
        return obj;
    }

    public ArrayQueue<E> clone() {
        try {
            ArrayQueue<E> arrayQueue = (ArrayQueue) super.clone();
            Object[] objArr = (Object[]) Array.newInstance(this.elements.getClass().getComponentType(), this.elements.length);
            System.arraycopy(this.elements, 0, objArr, 0, this.elements.length);
            arrayQueue.elements = objArr;
            return arrayQueue;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        int length = this.elements.length - 1;
        for (int i = this.head; i != this.tail; i = (i + 1) & length) {
            objectOutputStream.writeObject(this.elements[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i = 0;
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        allocateElements(readInt);
        this.head = 0;
        this.tail = readInt;
        while (i < readInt) {
            this.elements[i] = objectInputStream.readObject();
            i++;
        }
    }
}
