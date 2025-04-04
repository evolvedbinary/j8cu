/*
 * Copyright © 2025, Evolved Binary Ltd. <tech@evolvedbinary.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.evolvedbinary.j8cu.list.linked;

import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;

/**
 * Basic Linked List implementation where nodes have a `next` link,
 * and the list has a bounded size.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
@NotThreadSafe
public class BoundedSinglyLinkedList<T> extends SinglyLinkedList<T> implements BoundedLinkedList<T> {

    /**
     * The underlying list on which we are imposing a size bound.
     */
    final SinglyLinkedList<T> underlyingList;

    /**
     * An upper bound on the size of the Linked List.
     */
    final long maximumSize;

    /**
     * Optimisation to make it fast to get the size.
     * Only used if an upper bound has been set on the list.
     */
    long size;

    /**
     * @param underlyingList the list to place a size limit on.
     * @param maximumSize sets an upper bound on the size of the Linked List.
     */
    BoundedSinglyLinkedList(final SinglyLinkedList<T> underlyingList, final long maximumSize) {
        this.underlyingList = underlyingList;
        if (maximumSize < 1) {
            throw new IllegalArgumentException("maximumSize must be a greater than 0");
        }
        this.maximumSize = maximumSize;
    }

    @Override
    public boolean isFull() {
        return size == maximumSize;
    }

    @Override
    public long size() {
        return size;
    }

    /**
     * Add an element to this list.
     *
     * @param element the element to add into this list.
     *
     * @return true if the element was added to this list, or false if adding the entry would exceed the maximum size
     *              of this list.
     */
    @Override
    public boolean add(@Nullable final T element) {
        if (!isFull() && underlyingList.add(element)) {
            this.size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return underlyingList.isEmpty();
    }

    @Override
    public @Nullable T head() {
        return underlyingList.head();
    }

    @Override
    public @Nullable T last() {
        return underlyingList.last();
    }

    @Override
    public Iterator<T> iterator() {
        return underlyingList.iterator();
    }

    @Override
    public boolean containsIdentity(@Nullable T element) {
        return underlyingList.containsIdentity(element);
    }

    @Override
    public boolean containsEquivalent(@Nullable T element) {
        return underlyingList.containsEquivalent(element);
    }

    @Override
    public @Nullable T get(final long index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }

        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("Requested index was: " + index + ", but indexable range is 0 to: " + (size - 1));
        }

        // optimisation for head
        if (index == 0) {
            return underlyingList.head.data;
        }

        // optimisation for last
        if (index == size - 1) {
            return underlyingList.last.data;
        }

        // iterate forward
        SinglyLinkedNode<T> node = underlyingList.head;
        for (int i = 1; i <= index; i ++) {
            node = node.next;
        }
        return node.data;
    }

    @Override
    protected long remove(@Nullable final T element, final RemovalMode removalMode) {
        final long removed = underlyingList.remove(element, removalMode);
        this.size -= removed;
        return removed;
    }

    @Override
    public void clear() {
        underlyingList.clear();
        this.size = 0;
    }

    @Override
    public String toString() {
        return underlyingList.toString();
    }
}
