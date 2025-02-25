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

/**
 * Basic Linked List implementation where nodes have `next` and `prev` links,
 * and the list has a bounded size.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
@NotThreadSafe
public class BoundedDoublyLinkedList<T> extends DoublyLinkedList<T> {

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
     * @param maximumSize sets an upper bound on the size of the Linked List.
     */
    public BoundedDoublyLinkedList(final long maximumSize) {
        this(maximumSize, 0);
    }

    /**
     * @param maximumSize sets an upper bound on the size of the Linked List.
     * @param nodeCacheSize the size of the cache to use for reusing nodes that are removed.
     */
    public BoundedDoublyLinkedList(final long maximumSize, final int nodeCacheSize) {
        super(nodeCacheSize);
        if (maximumSize < 1) {
            throw new IllegalArgumentException("maximumSize must be a greater than 0");
        }
        this.maximumSize = maximumSize;
    }

    /**
     * Determine if the linked list is full.
     *
     * @return true if the list is full, false otherwise.
     */
    public boolean isFull() {
        return size == maximumSize;
    }

    /**
     * Get the size of the linked list.
     *
     * @return the size of the linked list.
     */
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
        if (!isFull() && super.add(element)) {
            this.size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFirst(final @Nullable T element) {
        if (super.removeFirst(element)) {
            this.size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeOne(final @Nullable T element) {
        if (super.removeOne(element)) {
            this.size--;
            return true;
        }
        return false;
    }

    @Override
    public long removeAll(final @Nullable T element) {
        final long removed = super.removeAll(element);
        this.size -= removed;
        return removed;
    }

    @Override
    public void clear() {
        super.clear();
        this.size = 0;
    }
}
