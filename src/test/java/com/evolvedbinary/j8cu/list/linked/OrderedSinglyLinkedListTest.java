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

import org.junit.jupiter.api.Test;

import static com.evolvedbinary.j8cu.list.linked.LinkedListTestUtil.addAll;
import static com.evolvedbinary.j8cu.list.linked.LinkedListTestUtil.toIntegerArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for {@link OrderedSinglyLinkedList}.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class OrderedSinglyLinkedListTest {

    @Test
    public void add() {
        final Integer INT_NEG_34 = -34;
        final Integer INT_0 = 0;
        final Integer INT_1 = 1;
        final Integer INT_9 = 9;
        final Integer INT_22 = 22;
        final Integer INT_100 = 100;
        final Integer INT_999 = 999;

        final Integer[] initialData = new Integer[] { null, INT_9, INT_22, null, INT_NEG_34, INT_0, INT_1, INT_999, INT_100, null, null };
        final Integer[] expected = new Integer[]    { INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null, null, null, null };

        OrderedSinglyLinkedList<Integer> linkedList = new OrderedSinglyLinkedList<>();
        addAll(linkedList, initialData);
        final Integer[] actual = toIntegerArray(linkedList);
        assertArrayEquals(expected, actual, linkedList.toString());

        // append optimisation
        linkedList = new OrderedSinglyLinkedList<>();
        linkedList.add(1);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertEquals(1, linkedList.head.data);
        assertNull(linkedList.head.next);
        assertNotNull(linkedList.last);
        assertSame(linkedList.head, linkedList.last);
        assertEquals(1, linkedList.last.data);
        linkedList.add(3);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertEquals(1, linkedList.head.data);
        assertNotNull(linkedList.head.next);
        assertEquals(3, linkedList.head.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertEquals(3, linkedList.last.data);
        assertNull(linkedList.last.next);
        linkedList.add(2);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertSame(1, linkedList.head.data);
        assertNotNull(linkedList.head.next);
        assertSame(2, linkedList.head.next.data);
        assertNotNull(linkedList.head.next.next);
        assertSame(3, linkedList.head.next.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertSame(3, linkedList.last.data);
        assertNull(linkedList.last.next);

        // append optimisation for null value
        linkedList = new OrderedSinglyLinkedList<>();
        linkedList.add(null);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertNull(linkedList.head.data);
        assertNull(linkedList.head.next);
        assertNotNull(linkedList.last);
        assertSame(linkedList.head, linkedList.last);
        assertNull(linkedList.last.data);
        linkedList.add(null);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertNull(linkedList.head.data);
        assertNotNull(linkedList.head.next);
        assertNull(linkedList.head.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertNull(linkedList.last.data);
        assertNull(linkedList.last.next);

        // prepend
        linkedList = new OrderedSinglyLinkedList<>();
        linkedList.add(2);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertEquals(2, linkedList.head.data);
        assertNull(linkedList.head.next);
        assertNotNull(linkedList.last);
        assertSame(linkedList.head, linkedList.last);
        assertEquals(2, linkedList.last.data);
        linkedList.add(1);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertEquals(1, linkedList.head.data);
        assertNotNull(linkedList.head.next);
        assertEquals(2, linkedList.head.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertEquals(2, linkedList.last.data);
        assertNull(linkedList.last.next);
    }

    @Test
    public void removeFirst() {
        final Integer INT_NEG_34 = -34;
        final Integer INT_0 = 0;
        final Integer INT_1 = 1;
        final Integer INT_9 = 9;
        final Integer INT_22 = 22;
        final Integer INT_100 = 100;
        final Integer INT_999 = 999;

        final Integer[] initialData = new Integer[]{ null, INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null, null };

        final OrderedSinglyLinkedList<Integer> linkedList = new OrderedSinglyLinkedList<>();
        addAll(linkedList, initialData);

        linkedList.removeFirst(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null, null }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_9);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_NEG_34);
        assertArrayEquals(new Integer[]{ INT_0, INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_0);
        assertArrayEquals(new Integer[]{ INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_999);
        assertArrayEquals(new Integer[]{ INT_1, INT_22, INT_100 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_100);
        assertArrayEquals(new Integer[]{ INT_1, INT_22 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_22);
        assertArrayEquals(new Integer[]{ INT_1 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeFirst(INT_1);
        assertArrayEquals(new Integer[0], toIntegerArray(linkedList), linkedList.toString());
    }

    @Test
    public void removeOne() {
        final Integer INT_NEG_34 = -34;
        final Integer INT_0 = 0;
        final Integer INT_1 = 1;
        final Integer INT_9 = 9;
        final Integer INT_22 = 22;
        final Integer INT_100 = 100;
        final Integer INT_999 = 999;

        final Integer[] initialData = new Integer[]{ null, INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null, null };

        final OrderedSinglyLinkedList<Integer> linkedList = new OrderedSinglyLinkedList<>();
        addAll(linkedList, initialData);

        linkedList.removeOne(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null, null }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_9);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_NEG_34);
        assertArrayEquals(new Integer[]{ INT_0, INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_0);
        assertArrayEquals(new Integer[]{ INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_999);
        assertArrayEquals(new Integer[]{ INT_1, INT_22, INT_100 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_100);
        assertArrayEquals(new Integer[]{ INT_1, INT_22 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_22);
        assertArrayEquals(new Integer[]{ INT_1 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeOne(INT_1);
        assertArrayEquals(new Integer[0], toIntegerArray(linkedList), linkedList.toString());
    }

    @Test
    public void removeAll() {
        final Integer INT_NEG_34 = -34;
        final Integer INT_0 = 0;
        final Integer INT_1 = 1;
        final Integer INT_9 = 9;
        final Integer INT_22 = 22;
        final Integer INT_100 = 100;
        final Integer INT_999 = 999;

        final Integer[] initialData = new Integer[]{ null, INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999, null, null };

        final OrderedSinglyLinkedList<Integer> linkedList = new OrderedSinglyLinkedList<>();
        addAll(linkedList, initialData);

        linkedList.removeAll(null);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_9, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_9);
        assertArrayEquals(new Integer[]{ INT_NEG_34, INT_0, INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_NEG_34);
        assertArrayEquals(new Integer[]{ INT_0, INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_0);
        assertArrayEquals(new Integer[]{ INT_1, INT_22, INT_100, INT_999 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_999);
        assertArrayEquals(new Integer[]{ INT_1, INT_22, INT_100 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_100);
        assertArrayEquals(new Integer[]{ INT_1, INT_22 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_22);
        assertArrayEquals(new Integer[]{ INT_1 }, toIntegerArray(linkedList), linkedList.toString());

        linkedList.removeAll(INT_1);
        assertArrayEquals(new Integer[0], toIntegerArray(linkedList), linkedList.toString());
    }
}
