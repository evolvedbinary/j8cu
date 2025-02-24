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
import net.jcip.annotations.NotThreadSafe;

/**
 * Basic Linked List implementation where nodes have a `next` link.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
@NotThreadSafe
public class SinglyLinkedList<T> extends AbstractLinkedList<T, SinglyLinkedNode<T>> {

    public SinglyLinkedList() {
        super();
    }

    public SinglyLinkedList(final int nodeCacheSize) {
        super(nodeCacheSize);
    }

    @Override
    public boolean add(final T element) {
        final SinglyLinkedNode<T> newNode = createNode(element);
        if (head == null) {
            this.head = newNode;
        } else {
            this.last.next = newNode;
        }

        this.last = newNode;
        return true;
    }

    @Override
    SinglyLinkedNode<T> newNode(final T element) {
        return new SinglyLinkedNode<>(element);
    }

    @Override
    protected long remove(@Nullable final T element, final boolean firstOnly) {
        long removed = 0;

        @Nullable SinglyLinkedNode<T> node = head;
        @Nullable SinglyLinkedNode<T> prevNode = null;
        while (node != null) {

            if (element == node.data) {
                // matched element

                if (prevNode == null) {
                    // element matched the head node

                    if (last == head) {
                        // list only had one item, so we also need to reset `last`
                        last = null;
                    }

                    // unlink the `head` node
                    head = node.next;

                } else {
                    // element matched a non-head node

                    if (last == node) {
                        // we are unlinking the last node, so we need to update the `last` reference
                        last = prevNode;
                    }

                    // unlink this `node`
                    prevNode.next = node.next;
                }

                discardNode(node);

                removed++;

                if (firstOnly) {
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
