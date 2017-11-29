package com.paincker.utils.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * List中的Item非空
 * Created by jzj on 2017/11/22.
 */
public class ItemNonNullList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

    @NotNull
    private final ArrayList<E> mList;

    public ItemNonNullList() {
        mList = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private ItemNonNullList(@NotNull ItemNonNullList<E> other) {
        mList = (ArrayList<E>) other.mList.clone();
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mList.contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return mList.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return mList.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return e != null && mList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return mList.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return mList.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        mList.ensureCapacity(mList.size() + c.size());
        boolean result = false;
        for (E e : c) {
            result |= e != null && mList.add(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        ArrayList<E> list = null;
        for (Object o : c) {
            if (o != null) {
                if (list == null) {
                    list = new ArrayList<>(c.size());
                }
                list.add((E) o);
            }
        }
        return list != null && mList.addAll(index, list);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return mList.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return mList.retainAll(c);
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @NotNull
    @Override
    public E get(int index) {
        return mList.get(index);
    }

    @Nullable
    @Override
    public E set(int index, E element) {
        return element == null ? null : mList.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        if (element != null) {
            mList.add(index, element);
        }
    }

    @NotNull
    @Override
    public E remove(int index) {
        return mList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return mList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mList.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return mList.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return mList.listIterator(index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return mList.subList(fromIndex, toIndex);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ItemNonNullList<E> clone() {
        return new ItemNonNullList<>(this);
    }
}
