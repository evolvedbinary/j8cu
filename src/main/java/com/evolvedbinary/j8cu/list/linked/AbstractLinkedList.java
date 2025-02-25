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

import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * Base class for a Linked List.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public abstract class AbstractLinkedList<T, N extends AbstractNode<T, N>> implements LinkedList<T> {

    @Nullable N head = null;

    /**
     * Points at the last item in the Linked List; an optimisation to allow faster appends!
     */
    @Nullable N last = null;

    /**
     * Simple cache that holds previously
     * removed nodes so that they may be reused
     * when adding new nodes.
     */
    @Nullable final AbstractNode<T, N>[] reusableNodesCache;
    int reusableNodes = 0;

    protected AbstractLinkedList() {
        // no cache
        this(0);
    }

    /**
     * @param nodeCacheSize the size of the cache to use for reusing nodes that are removed.
     */
    @SuppressWarnings("unchecked")
    public AbstractLinkedList(final int nodeCacheSize) {
        if (nodeCacheSize <= 0) {
            // no cache
            this.reusableNodesCache = null;
        } else {
            this.reusableNodesCache = (N[]) new AbstractNode[nodeCacheSize];
        }
    }

    /**
     * Create a node for this list.
     * May create a new node, or reuse a previously removed one from the cache.
     *
     * @param element the element to store in the node.
     *
     * @return the node for the list.
     */
    @SuppressWarnings("unchecked")
    protected N createNode(final T element) {
        if (reusableNodesCache != null && reusableNodes > 0) {
            // get node from cache
            final N cachedNode = (N) reusableNodesCache[--reusableNodes];
            reusableNodesCache[reusableNodes] = null;
            cachedNode.data = element;
            return cachedNode;
        }

        return newNode(element);
    }

    /**
     * Create a new node object for this list.
     *
     * @param element the element to store in the node.
     *
     * @return the new node.
     */
    abstract N newNode(final T element);

    /**
     * Discard a node that is no longer used in this list.
     * May add the node to a cache for later reuse.
     *
     * @param node the node that is no longer used in this cache.
     */
    protected void discardNode(final N node) {
        node.next = null;
        node.data = null;
        if (reusableNodesCache != null && reusableNodes < reusableNodesCache.length) {
            reusableNodesCache[reusableNodes++] = node;
        }
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public @Nullable T head() {
        if (head == null) {
            throw new IllegalStateException("The list is empty");
        }
        return head.data;
    }

    @Override
    public @Nullable T last() {
        if (last == null) {
            throw new IllegalStateException("The list is empty");
        }
        return last.data;
    }

    @Override
    public Iterator<T> iterator() {
        if (head == null) {
            // is empty
            return Collections.emptyIterator();
        }
        return new LinkedListIterator<>(head);
    }

    @Override
    public boolean containsIdentity(@Nullable T element) {
        for (final T listElement : this) {
            if (listElement == element) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsEquivalent(@Nullable T element) {
        for (final T listElement : this) {
            if (Objects.equals(listElement, element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeFirst(@Nullable final T element) {
        return remove(element, RemovalMode.REMOVE_FIRST) == 1;
    }

    @Override
    public boolean removeOne(@Nullable final T element) {
        return remove(element, RemovalMode.REMOVE_ONE) == 1;
    }

    @Override
    public long removeAll(@Nullable final T element) {
        return remove(element, RemovalMode.REMOVE_ALL);
    }

    protected enum RemovalMode {
        /**
         * Remove the first matching element.
         */
        REMOVE_FIRST,

        /**
         * Remove any one matching element.
         */
        REMOVE_ONE,

        /**
         * Remove all matching elements
         */
        REMOVE_ALL
    }

    protected abstract long remove(@Nullable final T element, final RemovalMode removalMode);

    @Override
    public void clear() {
        this.head = null;
        this.last = null;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        final Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append(']');
        return builder.toString();
    }
}
