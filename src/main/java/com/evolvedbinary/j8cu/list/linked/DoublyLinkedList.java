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

/**
 * Basic Linked List implementation where nodes have `next` and `prev` links.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class DoublyLinkedList<T> extends AbstractLinkedList<T, DoublyLinkedNode<T>> {

    public DoublyLinkedList() {
        super();
    }

    /**
     * @param nodeCacheSize the size of the cache to use for reusing nodes that are removed.
     */
    public DoublyLinkedList(final int nodeCacheSize) {
        super(nodeCacheSize);
    }

    @Override
    public boolean add(@Nullable final T element) {
        final DoublyLinkedNode<T> newNode = createNode(element);
        if (head == null) {
            this.head = newNode;
        } else {
            newNode.previous = this.last;
            this.last.next = newNode;
        }

        this.last = newNode;
        return true;
    }

    @Override
    DoublyLinkedNode<T> newNode(final T element) {
        return new DoublyLinkedNode<>(element);
    }

    /**
     * Return an iterator that progresses over this list in reverse order (i.e. from end to start).
     *
     * @return an iterator that moves in a reverse direction over this list.
     */
    public Iterator<T> reverseIterator() {
        if (last == null) {
            // is empty
            return Collections.emptyIterator();
        }
        return new LinkedListReverseIterator<>(last);
    }

    @Override
    protected long remove(@Nullable final T element, final RemovalMode removalMode) {
        if (RemovalMode.REMOVE_ONE == removalMode && last != null && last.data == element) {
            // optimisation for removing the `last` node when removing any one node
            DoublyLinkedNode<T> node = last;
            if (last == head) {
                // there was only one node
                head = null;
                last = null;
            } else {
                last = last.previous;
                last.next = null;
            }
            discardNode(node);
            return 1;
        }

        long removed = 0;

        @Nullable DoublyLinkedNode<T> node = head;
        @Nullable DoublyLinkedNode<T> prevNode = null;
        while (node != null) {

            if (element == node.data) {
                // matched element

                if (prevNode == null) {
                    // element matched the head node

                    if (last == head) {
                        // list only had one item, so we also need to reset `last`
                        last = null;
                    }

                    // move the current node to the `head` node
                    final DoublyLinkedNode<T> next = node.next;
                    if (next != null) {
                        next.previous = null;
                    }
                    head = next;

                } else {
                    // element matched a non-head node

                    if (last == node) {
                        // we are unlinking the last node, so we need to update the `last` reference
                        last = prevNode;
                    }

                    // unlink this `node`
                    final DoublyLinkedNode<T> next = node.next;
                    if (next != null) {
                        next.previous = prevNode;
                    }
                    prevNode.next  = next;
                }

                removed++;
                discardNode(node);

                if (RemovalMode.REMOVE_ALL != removalMode) {
                    // REMOVE_FIRST or REMOVE_ONE so we just need one result
                    break;
                }

            } else {
                // store the node as the previous node for the next iteration
                prevNode = node;
            }

            // prepare for next iteration...
            node = node.next;
        }

        return removed;
    }
}
