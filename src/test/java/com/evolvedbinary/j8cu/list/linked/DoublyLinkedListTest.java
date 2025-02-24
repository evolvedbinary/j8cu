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

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DoublyLinkedList}.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class DoublyLinkedListTest {

    @Test
    public void newNode() {
        final DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();
        final String element1 = "element1";
        final DoublyLinkedNode<String> node = linkedList.newNode(element1);
        assertNotNull(node);
        assertEquals(element1, node.data);
        assertNull(node.next);
    }

    @Test
    public void add() {
        final DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();

        // add a first string
        final String str1 = "hello";
        linkedList.add(str1);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertSame(str1, linkedList.head.data);
        assertNull(linkedList.head.previous);
        assertNull(linkedList.head.next);
        assertNotNull(linkedList.last);
        assertSame(linkedList.head, linkedList.last);
        assertSame(str1, linkedList.last.data);

        // add a second string
        final String str2 = "to";
        linkedList.add(str2);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertSame(str1, linkedList.head.data);
        assertNull(linkedList.head.previous);
        assertNotNull(linkedList.head.next);
        assertSame(str2, linkedList.head.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertSame(str2, linkedList.last.data);
        assertNotNull(linkedList.last.previous);
        assertSame(str1, linkedList.last.previous.data);
        assertNull(linkedList.last.next);

        // add a third string
        final String str3 = "you";
        linkedList.add(str3);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertSame(str1, linkedList.head.data);
        assertNull(linkedList.head.previous);
        assertNotNull(linkedList.head.next);
        assertSame(str2, linkedList.head.next.data);
        assertNotNull(linkedList.head.next.previous);
        assertSame(str1, linkedList.head.next.previous.data);
        assertNotNull(linkedList.head.next.next);
        assertSame(str3, linkedList.head.next.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertSame(str3, linkedList.last.data);
        assertNotNull(linkedList.last.previous);
        assertSame(str2, linkedList.last.previous.data);
        assertNull(linkedList.last.next);
    }

    @Test
    public void reverseIterator() {
        // empty list
        DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();
        Iterator<String> reverseIterator = linkedList.reverseIterator();
        assertThrows(IllegalStateException.class, reverseIterator::remove);
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with one element
        final String element1 = "element1";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with two elements the same
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with three elements the same
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with four elements the same
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with two different elements
        final String element2 = "element2";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element2, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with three different elements
        final String element3 = "element3";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element3, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element2, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list with four different elements
        final String element4 = "element4";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element4);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element4, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element3, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element2, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list where the head node has subsequently been removed
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element4);
        linkedList.removeFirst(element1);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element4, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element3, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element2, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list where the last node has subsequently been removed
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element4);
        linkedList.removeFirst(element4);
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element3, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element2, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
        assertThrows(NoSuchElementException.class, reverseIterator::next);

        // list where various nodes have been removed (removeFirst)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element3);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element1);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element4);
        linkedList.add(element1);
        linkedList.add(element3);
        assertEquals(7, linkedList.removeAll(element3));
        assertTrue(linkedList.removeFirst(element1));
        // List should now be: [1,2,1,4,1]
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element4, reverseIterator.next());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element2, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());

        // list where various nodes have been removed (removeOne)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element3);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element1);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element4);
        linkedList.add(element1);
        linkedList.add(element3);
        assertEquals(7, linkedList.removeAll(element3));
        assertTrue(linkedList.removeOne(element1));
        // List should now be: [1,1,2,1,4]
        reverseIterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, reverseIterator::remove);
        assertTrue(reverseIterator.hasNext());
        assertSame(element4, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertSame(element2, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertTrue(reverseIterator.hasNext());
        assertSame(element1, reverseIterator.next());
        assertFalse(reverseIterator.hasNext());
    }

    @Test
    public void removeFirst() {
        // empty list
        DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();
        final String noSuchElement = "no-such-element";
        assertFalse(linkedList.removeFirst(noSuchElement));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with one element
        linkedList = new DoublyLinkedList<>();
        final String element1 = "element1";
        linkedList.add(element1);
        assertFalse(linkedList.removeFirst(noSuchElement));
        assertTrue(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two elements the same, remove just the first of the two
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two elements the same, remove both
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two different elements, remove just the first of the two
        final String element2 = "element2";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove just the second of the two
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove both (first then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two different elements, remove both (second then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three elements the same, remove just the first of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three elements the same, remove all
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove just the first of the three
        final String element3 = "element3";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the second of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the third of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (second then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (third then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (third then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove all (first, second, then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (second, third, then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (third, first, then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (third, second, then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeFirst(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeFirst(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
    }

    @Test
    public void removeOne() {
        // empty list
        DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();
        final String noSuchElement = "no-such-element";
        assertFalse(linkedList.removeOne(noSuchElement));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with one element
        linkedList = new DoublyLinkedList<>();
        final String element1 = "element1";
        linkedList.add(element1);
        assertFalse(linkedList.removeOne(noSuchElement));
        assertTrue(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two elements the same, remove just the first of the two
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two elements the same, remove both
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two different elements, remove just the first of the two
        final String element2 = "element2";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove just the second of the two
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove both (first then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two different elements, remove both (second then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three elements the same, remove just the first of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three elements the same, remove all
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove just the first of the three
        final String element3 = "element3";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the second of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the third of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (second then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (third then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (third then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove all (first, second, then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (second, third, then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (third, first, then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (third, second, then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertTrue(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertFalse(linkedList.removeOne(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertFalse(linkedList.removeOne(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
    }

    @Test
    public void removeAll() {
        // empty list
        DoublyLinkedList<String> linkedList = new DoublyLinkedList<>();
        final String noSuchElement = "no-such-element";
        assertEquals(0, linkedList.removeAll(noSuchElement));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with one element
        linkedList = new DoublyLinkedList<>();
        final String element1 = "element1";
        linkedList.add(element1);
        assertEquals(0, linkedList.removeAll(noSuchElement));
        assertEquals(1, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two elements the same, remove both
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(2, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two different elements, remove just the first of the two
        final String element2 = "element2";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove just the second of the two
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove both (first then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with two different elements, remove both (second then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three elements the same, remove all
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(3, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove just the first of the three
        final String element3 = "element3";
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the second of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the third of the three
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (second then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (third then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (third then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove all (first, second, then third)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (second, third, then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (third, first, then second)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (third, second, then first)
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(1, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element3));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element1));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with variety of elements
        linkedList = new DoublyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element1);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element3);
        linkedList.add(element1);
        assertEquals(5, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element3));
        assertEquals(4, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(1, linkedList.removeAll(element2));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertEquals(0, linkedList.removeAll(element2));
    }
}
