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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.evolvedbinary.j8cu.list.linked.Bounded.bound;
import static com.evolvedbinary.j8cu.list.linked.LinkedListTestUtil.newList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link BoundedDoublyLinkedList}.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class BoundedDoublyLinkedListTest {

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void create(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        assertThrows(IllegalArgumentException.class, () -> bound(newList(underlyingListClass), 0));
        assertThrows(IllegalArgumentException.class, () -> bound(newList(underlyingListClass), -1));
        assertThrows(IllegalArgumentException.class, () -> bound(newList(underlyingListClass), -2));

        int maximumSize = 1;
        BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), maximumSize);
        assertEquals(maximumSize, linkedList.maximumSize);
        assertEquals(0, linkedList.size);
        assertTrue(linkedList.isEmpty());

        maximumSize = 1024;
        linkedList = bound(newList(underlyingListClass), maximumSize);
        assertEquals(maximumSize, linkedList.maximumSize);
        assertEquals(0, linkedList.size);
        assertTrue(linkedList.isEmpty());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void isFull(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final String element1 = "element1";
        BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), 5);
        assertFalse(linkedList.isFull());
        linkedList.add(element1);
        assertFalse(linkedList.isFull());
        linkedList.add(element1);
        assertFalse(linkedList.isFull());
        linkedList.add(element1);
        assertFalse(linkedList.isFull());
        linkedList.add(element1);
        assertFalse(linkedList.isFull());
        linkedList.add(element1);
        assertTrue(linkedList.isFull());

        final String element2 = "element2";
        final String element3 = "element3";
        final String element4 = "element4";
        final String element5 = "element5";
        linkedList = bound(newList(underlyingListClass), 5);
        assertFalse(linkedList.isFull());
        linkedList.add(element1);
        assertFalse(linkedList.isFull());
        linkedList.add(element2);
        assertFalse(linkedList.isFull());
        linkedList.add(element3);
        assertFalse(linkedList.isFull());
        linkedList.add(element4);
        assertFalse(linkedList.isFull());
        linkedList.add(element5);
        assertTrue(linkedList.isFull());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void add(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final String element1 = "element1";

        int maximumSize = 1;
        BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), maximumSize);
        assertTrue(linkedList.isEmpty());
        assertTrue(linkedList.add(element1));
        assertEquals(1, linkedList.size());
        // try and add more past the maximumSize
        assertFalse(linkedList.add(element1));
        assertEquals(1, linkedList.size());

        maximumSize = 3;
        linkedList = bound(newList(underlyingListClass), maximumSize);
        assertTrue(linkedList.isEmpty());
        assertTrue(linkedList.add(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.add(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.add(element1));
        assertEquals(3, linkedList.size());
        // try and add more past the maximumSize
        assertFalse(linkedList.add(element1));
        assertEquals(3, linkedList.size());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void head(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), 1);
        assertThrows(IllegalStateException.class, linkedList::head);

        final String str1 = "hello";
        linkedList.add(str1);
        assertEquals(str1, linkedList.head());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void last(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), 1);
        assertThrows(IllegalStateException.class, linkedList::last);

        final String str1 = "hello";
        linkedList.add(str1);
        assertEquals(str1, linkedList.last());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void iterator(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        // empty list
        BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), 1);
        Iterator<String> iterator = linkedList.iterator();
        assertThrows(IllegalStateException.class, iterator::remove);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with one element
        final String element1 = "element1";
        linkedList = Bounded.bound(newList(underlyingListClass), 1);
        linkedList.add(element1);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void reverseIterator(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        // empty list
        BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), 1);
        Iterator<String> iterator = linkedList.reverseIterator();
        assertThrows(IllegalStateException.class, iterator::remove);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with one element
        final String element1 = "element1";
        linkedList = Bounded.bound(newList(underlyingListClass), 1);
        linkedList.add(element1);
        iterator = linkedList.reverseIterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void containsIdentity(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final String element1 = "element1";

        // immutable strings
        final BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), 1);
        assertFalse(linkedList.containsIdentity(element1));

        linkedList.add(element1);
        assertTrue(linkedList.containsIdentity(element1));
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void containsEquivalent(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final String element1 = "element1";

        // immutable strings
        final BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), 1);
        assertFalse(linkedList.containsEquivalent(element1));

        linkedList.add(element1);
        assertTrue(linkedList.containsEquivalent(element1));
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void get(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 5;

        BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), maximumSize);

        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(100));

        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("d");
        linkedList.add("e");

        assertEquals("a", linkedList.get(0));
        assertEquals("e", linkedList.get(4));
        assertEquals("b", linkedList.get(1));
        assertEquals("d", linkedList.get(3));
        assertEquals("c", linkedList.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(5));
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void removeFirst(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 5;

        // empty list
        BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), maximumSize);
        assertEquals(0, linkedList.size());
        final String noSuchElement = "no-such-element";
        assertFalse(linkedList.removeFirst(noSuchElement));
        assertEquals(0, linkedList.size());

        // list with one element
        linkedList = bound(newList(underlyingListClass), maximumSize);
        final String element1 = "element1";
        linkedList.add(element1);
        assertEquals(1, linkedList.size());
        assertFalse(linkedList.removeFirst(noSuchElement));
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());

        // list with two elements the same, remove just the first of the two
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());

        // list with two elements the same, remove both
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());

        // list with two different elements, remove just the first of the two
        final String element2 = "element2";
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());

        // list with two different elements, remove just the second of the two
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(1, linkedList.size());

        // list with two different elements, remove both (first then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());

        // list with two different elements, remove both (second then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());

        // list with three elements the same, remove just the first of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(2, linkedList.size());

        // list with three elements the same, remove all
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove just the first of the three
        final String element3 = "element3";
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove just the second of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove just the third of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove two (first then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (second then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (first then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (third then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (third then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove all (first, second, then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element3));
        assertEquals(0, linkedList.size());
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (second, third, then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element3));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (third, first, then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element3));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (third, second, then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeFirst(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeFirst(element2));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeFirst(element3));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeFirst(element1));
        assertEquals(0, linkedList.size());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void removeOne(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 5;

        // empty list
        BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), maximumSize);
        assertEquals(0, linkedList.size());
        final String noSuchElement = "no-such-element";
        assertFalse(linkedList.removeOne(noSuchElement));
        assertEquals(0, linkedList.size());

        // list with one element
        linkedList = bound(newList(underlyingListClass), maximumSize);
        final String element1 = "element1";
        linkedList.add(element1);
        assertEquals(1, linkedList.size());
        assertFalse(linkedList.removeOne(noSuchElement));
        assertTrue(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());

        // list with two elements the same, remove just the first of the two
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());

        // list with two elements the same, remove both
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());

        // list with two different elements, remove just the first of the two
        final String element2 = "element2";
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());

        // list with two different elements, remove just the second of the two
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(1, linkedList.size());

        // list with two different elements, remove both (first then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());

        // list with two different elements, remove both (second then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());

        // list with three elements the same, remove just the first of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(2, linkedList.size());

        // list with three elements the same, remove all
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove just the first of the three
        final String element3 = "element3";
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove just the second of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove just the third of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove two (first then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (second then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (first then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (third then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (third then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove all (first, second, then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element3));
        assertEquals(0, linkedList.size());
        assertTrue(linkedList.isEmpty());

        // list with three different elements, remove all (second, third, then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element3));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (third, first, then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element3));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (third, second, then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertTrue(linkedList.removeOne(element3));
        assertEquals(2, linkedList.size());
        assertTrue(linkedList.removeOne(element2));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertFalse(linkedList.removeOne(element3));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element2));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.removeOne(element1));
        assertEquals(0, linkedList.size());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void removeAll(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 10;

        // empty list
        BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), maximumSize);
        assertEquals(0, linkedList.size());
        final String noSuchElement = "no-such-element";
        assertEquals(0, linkedList.removeAll(noSuchElement));
        assertEquals(0, linkedList.size());

        // list with one element
        linkedList = bound(newList(underlyingListClass), maximumSize);
        final String element1 = "element1";
        linkedList.add(element1);
        assertEquals(1, linkedList.size());
        assertEquals(0, linkedList.removeAll(noSuchElement));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());

        // list with two elements the same, remove both
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(2, linkedList.size());
        assertEquals(2, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());

        // list with two different elements, remove just the first of the two
        final String element2 = "element2";
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(1, linkedList.size());

        // list with two different elements, remove just the second of the two
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(1, linkedList.size());

        // list with two different elements, remove both (first then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());

        // list with two different elements, remove both (second then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());

        // list with three elements the same, remove all
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertEquals(3, linkedList.size());
        assertEquals(3, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove just the first of the three
        final String element3 = "element3";
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove just the second of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove just the third of the three
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(2, linkedList.size());

        // list with three different elements, remove two (first then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (second then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (first then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (third then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove two (third then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(1, linkedList.size());

        // list with three different elements, remove all (first, second, then third)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element3));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (second, third, then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element3));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (third, first, then second)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element3));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());

        // list with three different elements, remove all (third, second, then first)
        linkedList = bound(newList(underlyingListClass), maximumSize);
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(3, linkedList.size());
        assertEquals(1, linkedList.removeAll(element3));
        assertEquals(2, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());
        // try and remove again
        assertEquals(0, linkedList.removeAll(element3));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(0, linkedList.size());

        // list with variety of elements
        linkedList = bound(newList(underlyingListClass), maximumSize);
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
        assertEquals(10, linkedList.size());
        assertEquals(5, linkedList.removeAll(element3));
        assertEquals(5, linkedList.size());
        assertEquals(0, linkedList.removeAll(element3));
        assertEquals(5, linkedList.size());
        assertEquals(4, linkedList.removeAll(element1));
        assertEquals(1, linkedList.size());
        assertEquals(0, linkedList.removeAll(element1));
        assertEquals(1, linkedList.size());
        assertEquals(1, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
        assertEquals(0, linkedList.removeAll(element2));
        assertEquals(0, linkedList.size());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void clear(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 10;
        final BoundedDoublyLinkedList<String> linkedList = bound(newList(underlyingListClass), maximumSize);

        linkedList.add("hello");
        assertEquals(1, linkedList.size());
        linkedList.clear();
        assertEquals(0, linkedList.size());

        linkedList.add("hello");
        linkedList.add("world");
        assertEquals(2, linkedList.size());
        linkedList.clear();
        assertEquals(0, linkedList.size());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void testToString(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 2;
        final BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), maximumSize);
        linkedList.add("1");
        linkedList.add("2");
        assertEquals("[1, 2]", linkedList.toString());
    }

    @ValueSource(classes = {DoublyLinkedList.class, OrderedDoublyLinkedList.class})
    @ParameterizedTest
    public void testToReverseString(final Class<? extends DoublyLinkedList<String>> underlyingListClass) throws InstantiationException, IllegalAccessException {
        final int maximumSize = 2;
        final BoundedDoublyLinkedList<String> linkedList = Bounded.bound(newList(underlyingListClass), maximumSize);
        linkedList.add("1");
        linkedList.add("2");
        assertEquals("[2, 1]", linkedList.toReverseString());
    }
}
