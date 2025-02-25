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
 * Basic Linked List implementation where nodes have a `next` link
 * where elements are kept in order by means of their comparability.
 * Note that any `null` elements will always come at the end of the list.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
@NotThreadSafe
public class OrderedSinglyLinkedList<T extends Comparable<T>> extends SinglyLinkedList<T> {

    public OrderedSinglyLinkedList() {
        this(0);
    }

    /**
     * @param nodeCacheSize the size of the cache to use for reusing nodes that are removed.
     */
    public OrderedSinglyLinkedList(final int nodeCacheSize) {
        super(nodeCacheSize);
    }

    @Override
    public boolean add(@Nullable final T element) {
        // small optimisation for empty list
        if (head == null) {
            // list is empty, element should become the head
            final SinglyLinkedNode<T> newNode = createNode(element);
            this.head = newNode;
            this.last = head;
            return true;
        }

        // small optimisation for appending to the list, additionally `null` elements always come last!
        if (element == null || (last.data != null && element.compareTo(last.data) >= 0)) {
            // the element should be appended to the list
            final SinglyLinkedNode<T> newNode = createNode(element);
            this.last.next = newNode;
            this.last = newNode;
            return true;
        }

        // find the position to insert the node
        @Nullable SinglyLinkedNode<T> node = head;
        @Nullable SinglyLinkedNode<T> prevNode = null;
        while (node != null) {
            if (node.data == null || element.compareTo(node.data) < 0) {
                // found that insertion position should be before this node

                final SinglyLinkedNode<T> newNode = createNode(element);
                newNode.next = node;

                if (prevNode == null) {
                    // element should be inserted before the head node
                    head = newNode;

                } else {
                    // the element should be inserted before this node
                    prevNode.next = newNode;
                }

                return true;
            }

            // store the node as the previous node for the next iteration
            prevNode = node;

            // prepare for next iteration...
            node = node.next;
        }

        return false;
    }
}
