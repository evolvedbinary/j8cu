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
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link SinglyLinkedList}.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class SinglyLinkedListTest {

    @Test
    public void newNode() {
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        final String element1 = "element1";
        final SinglyLinkedNode<String> node = linkedList.newNode(element1);
        assertNotNull(node);
        assertEquals(element1, node.data);
        assertNull(node.next);
    }

    @Test
    public void create() {
        SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        linkedList = new SinglyLinkedList<>(-1);
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        linkedList = new SinglyLinkedList<>(0);
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        final int nodeCacheSize = 10;
        linkedList = new SinglyLinkedList<>(10);
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
        assertNotNull(linkedList.reusableNodesCache);
        assertEquals(nodeCacheSize, linkedList.reusableNodesCache.length);
        assertEquals(0, linkedList.reusableNodes);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void reuseNodes() {
        final int nodeCacheSize = 2;
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>(nodeCacheSize);

        final SinglyLinkedNode<String>[] expectedArray = (SinglyLinkedNode<String>[]) new SinglyLinkedNode[nodeCacheSize];

        final String element1 = "element1";
        linkedList.add(element1);
        assertEquals(0, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        final String element2 = "element2";
        linkedList.add(element2);
        assertEquals(0, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        final String element3 = "element3";
        linkedList.add(element3);
        assertEquals(0, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        expectedArray[0] = new SinglyLinkedNode<>(null);
        linkedList.removeFirst(element2);
        assertEquals(1, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        expectedArray[1] = new SinglyLinkedNode<>(null);
        linkedList.removeFirst(element1);
        assertEquals(2, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        linkedList.removeFirst(element3);
        assertEquals(2, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        expectedArray[1] = null;
        final String element4 = "element4";
        linkedList.add(element4);
        assertEquals(1, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        expectedArray[0] = null;
        final String element5 = "element5";
        linkedList.add(element5);
        assertEquals(0, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        final String element6 = "element6";
        linkedList.add(element6);
        assertEquals(0, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);

        expectedArray[0] = new SinglyLinkedNode<>(null);
        linkedList.removeFirst(element6);
        assertEquals(1, linkedList.reusableNodes);
        assertArrayEquals(expectedArray, linkedList.reusableNodesCache);
    }

    @Test
    public void add() {
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();

        // add a first string
        final String str1 = "hello";
        linkedList.add(str1);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertSame(str1, linkedList.head.data);
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
        assertNotNull(linkedList.head.next);
        assertSame(str2, linkedList.head.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertSame(str2, linkedList.last.data);
        assertNull(linkedList.last.next);

        // add a third string
        final String str3 = "you";
        linkedList.add(str3);
        assertFalse(linkedList.isEmpty());
        assertNotNull(linkedList.head);
        assertSame(str1, linkedList.head.data);
        assertNotNull(linkedList.head.next);
        assertSame(str2, linkedList.head.next.data);
        assertNotNull(linkedList.head.next.next);
        assertSame(str3, linkedList.head.next.next.data);
        assertNotNull(linkedList.last);
        assertNotSame(linkedList.head, linkedList.last);
        assertSame(str3, linkedList.last.data);
        assertNull(linkedList.last.next);
    }

    @Test
    public void head() {
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        assertThrows(IllegalStateException.class, linkedList::head);

        final String str1 = "hello";
        linkedList.add(str1);
        assertEquals(str1, linkedList.head());

        final String str2 = "world";
        linkedList.add(str2);
        assertEquals(str1, linkedList.head());
    }

    @Test
    public void last() {
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        assertThrows(IllegalStateException.class, linkedList::last);

        final String str1 = "hello";
        linkedList.add(str1);
        assertEquals(str1, linkedList.last());

        final String str2 = "world";
        linkedList.add(str2);
        assertEquals(str2, linkedList.last());
    }

    @Test
    public void iterator() {
        // empty list
        SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        Iterator<String> iterator = linkedList.iterator();
        assertThrows(IllegalStateException.class, iterator::remove);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with one element
        final String element1 = "element1";
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with two elements the same
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with three elements the same
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with four elements the same
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with two different elements
        final String element2 = "element2";
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element2, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with three different elements
        final String element3 = "element3";
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element2, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element3, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list with four different elements
        final String element4 = "element4";
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element4);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element2, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element3, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element4, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list where the head node has subsequently been removed
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element4);
        linkedList.removeFirst(element1);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element2, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element3, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element4, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list where the last node has subsequently been removed
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        linkedList.add(element4);
        linkedList.removeFirst(element4);
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element2, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element3, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        // list where various nodes have been removed
        linkedList = new SinglyLinkedList<>();
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
        iterator = linkedList.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element2, iterator.next());
        assertSame(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element4, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(element1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void containsIdentity() {
        final String element1 = "element1";
        final String element2 = "element2";
        final String element3 = "element3";

        // immutable strings
        final SinglyLinkedList<String> linkedList1 = new SinglyLinkedList<>();
        assertFalse(linkedList1.containsIdentity(element1));
        assertFalse(linkedList1.containsIdentity(element2));
        assertFalse(linkedList1.containsIdentity(element3));

        linkedList1.add(element1);
        assertTrue(linkedList1.containsIdentity(element1));

        linkedList1.add(element2);
        assertTrue(linkedList1.containsIdentity(element1));
        assertTrue(linkedList1.containsIdentity(element2));

        linkedList1.add(element3);
        assertTrue(linkedList1.containsIdentity(element1));
        assertTrue(linkedList1.containsIdentity(element2));
        assertTrue(linkedList1.containsIdentity(element3));

        linkedList1.removeOne(element1);
        assertFalse(linkedList1.containsIdentity(element1));
        assertTrue(linkedList1.containsIdentity(element2));
        assertTrue(linkedList1.containsIdentity(element3));

        linkedList1.removeOne(element2);
        assertFalse(linkedList1.containsIdentity(element1));
        assertFalse(linkedList1.containsIdentity(element2));
        assertTrue(linkedList1.containsIdentity(element3));

        linkedList1.removeOne(element3);
        assertFalse(linkedList1.containsIdentity(element1));
        assertFalse(linkedList1.containsIdentity(element2));
        assertFalse(linkedList1.containsIdentity(element3));

        // wrapped strings
        final Wrapper<String> wrappedElement1 = new Wrapper<>(element1);
        final Wrapper<String> wrappedElement2 = new Wrapper<>(element2);
        final Wrapper<String> wrappedElement3 = new Wrapper<>(element3);
        final SinglyLinkedList<Wrapper<String>> linkedList2 = new SinglyLinkedList<>();
        assertFalse(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(wrappedElement2));
        assertFalse(linkedList2.containsIdentity(wrappedElement3));

        linkedList2.add(wrappedElement1);
        assertTrue(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element1)));

        linkedList2.add(wrappedElement2);
        assertTrue(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element1)));
        assertTrue(linkedList2.containsIdentity(wrappedElement2));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element2)));

        linkedList2.add(wrappedElement3);
        assertTrue(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element1)));
        assertTrue(linkedList2.containsIdentity(wrappedElement2));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element2)));
        assertTrue(linkedList2.containsIdentity(wrappedElement3));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element3)));

        linkedList2.removeOne(wrappedElement1);
        assertFalse(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element1)));
        assertTrue(linkedList2.containsIdentity(wrappedElement2));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element2)));
        assertTrue(linkedList2.containsIdentity(wrappedElement3));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element3)));

        linkedList2.removeOne(wrappedElement2);
        assertFalse(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element1)));
        assertFalse(linkedList2.containsIdentity(wrappedElement2));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element2)));
        assertTrue(linkedList2.containsIdentity(wrappedElement3));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element3)));

        linkedList2.removeOne(wrappedElement3);
        assertFalse(linkedList2.containsIdentity(wrappedElement1));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element1)));
        assertFalse(linkedList2.containsIdentity(wrappedElement2));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element2)));
        assertFalse(linkedList2.containsIdentity(wrappedElement3));
        assertFalse(linkedList2.containsIdentity(new Wrapper<>(element3)));
    }

    @Test
    public void containsEquivalent() {
        final String element1 = "element1";
        final String element2 = "element2";
        final String element3 = "element3";

        // immutable strings
        final SinglyLinkedList<String> linkedList1 = new SinglyLinkedList<>();
        assertFalse(linkedList1.containsEquivalent(element1));
        assertFalse(linkedList1.containsEquivalent(element2));
        assertFalse(linkedList1.containsEquivalent(element3));

        linkedList1.add(element1);
        assertTrue(linkedList1.containsEquivalent(element1));

        linkedList1.add(element2);
        assertTrue(linkedList1.containsEquivalent(element1));
        assertTrue(linkedList1.containsEquivalent(element2));

        linkedList1.add(element3);
        assertTrue(linkedList1.containsEquivalent(element1));
        assertTrue(linkedList1.containsEquivalent(element2));
        assertTrue(linkedList1.containsEquivalent(element3));

        linkedList1.removeOne(element1);
        assertFalse(linkedList1.containsEquivalent(element1));
        assertTrue(linkedList1.containsEquivalent(element2));
        assertTrue(linkedList1.containsEquivalent(element3));

        linkedList1.removeOne(element2);
        assertFalse(linkedList1.containsEquivalent(element1));
        assertFalse(linkedList1.containsEquivalent(element2));
        assertTrue(linkedList1.containsEquivalent(element3));

        linkedList1.removeOne(element3);
        assertFalse(linkedList1.containsEquivalent(element1));
        assertFalse(linkedList1.containsEquivalent(element2));
        assertFalse(linkedList1.containsEquivalent(element3));

        // wrapped strings
        final Wrapper<String> wrappedElement1 = new Wrapper<>(element1);
        final Wrapper<String> wrappedElement2 = new Wrapper<>(element2);
        final Wrapper<String> wrappedElement3 = new Wrapper<>(element3);
        final SinglyLinkedList<Wrapper<String>> linkedList2 = new SinglyLinkedList<>();
        assertFalse(linkedList2.containsEquivalent(wrappedElement1));
        assertFalse(linkedList2.containsEquivalent(wrappedElement2));
        assertFalse(linkedList2.containsEquivalent(wrappedElement3));

        linkedList2.add(wrappedElement1);
        assertTrue(linkedList2.containsEquivalent(wrappedElement1));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element1)));

        linkedList2.add(wrappedElement2);
        assertTrue(linkedList2.containsEquivalent(wrappedElement1));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element1)));
        assertTrue(linkedList2.containsEquivalent(wrappedElement2));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element2)));

        linkedList2.add(wrappedElement3);
        assertTrue(linkedList2.containsEquivalent(wrappedElement1));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element1)));
        assertTrue(linkedList2.containsEquivalent(wrappedElement2));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element2)));
        assertTrue(linkedList2.containsEquivalent(wrappedElement3));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element3)));

        linkedList2.removeOne(wrappedElement1);
        assertFalse(linkedList2.containsEquivalent(wrappedElement1));
        assertFalse(linkedList2.containsEquivalent(new Wrapper<>(element1)));
        assertTrue(linkedList2.containsEquivalent(wrappedElement2));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element2)));
        assertTrue(linkedList2.containsEquivalent(wrappedElement3));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element3)));

        linkedList2.removeOne(wrappedElement2);
        assertFalse(linkedList2.containsEquivalent(wrappedElement1));
        assertFalse(linkedList2.containsEquivalent(new Wrapper<>(element1)));
        assertFalse(linkedList2.containsEquivalent(wrappedElement2));
        assertFalse(linkedList2.containsEquivalent(new Wrapper<>(element2)));
        assertTrue(linkedList2.containsEquivalent(wrappedElement3));
        assertTrue(linkedList2.containsEquivalent(new Wrapper<>(element3)));

        linkedList2.removeOne(wrappedElement3);
        assertFalse(linkedList2.containsEquivalent(wrappedElement1));
        assertFalse(linkedList2.containsEquivalent(new Wrapper<>(element1)));
        assertFalse(linkedList2.containsEquivalent(wrappedElement2));
        assertFalse(linkedList2.containsEquivalent(new Wrapper<>(element2)));
        assertFalse(linkedList2.containsEquivalent(wrappedElement3));
        assertFalse(linkedList2.containsEquivalent(new Wrapper<>(element3)));
    }

    @Test
    public void removeFirst() {
        // empty list
        SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        final String noSuchElement = "no-such-element";
        assertFalse(linkedList.removeFirst(noSuchElement));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with one element
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two elements the same, remove both
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove just the second of the two
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove both (first then second)
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three elements the same, remove all
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the second of the three
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the third of the three
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeFirst(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then second)
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        final String noSuchElement = "no-such-element";
        assertFalse(linkedList.removeOne(noSuchElement));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with one element
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two elements the same, remove both
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove just the second of the two
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove both (first then second)
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element1);
        linkedList.add(element1);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three elements the same, remove all
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the second of the three
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the third of the three
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertTrue(linkedList.removeOne(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then second)
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        final String noSuchElement = "no-such-element";
        assertEquals(0, linkedList.removeAll(noSuchElement));
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        // list with one element
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove just the second of the two
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with two different elements, remove both (first then second)
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element1));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the second of the three
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element2));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove just the third of the three
        linkedList = new SinglyLinkedList<>();
        linkedList.add(element1);
        linkedList.add(element2);
        linkedList.add(element3);
        assertEquals(1, linkedList.removeAll(element3));
        assertNotNull(linkedList.head);
        assertNotNull(linkedList.last);
        assertFalse(linkedList.isEmpty());

        // list with three different elements, remove two (first then second)
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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
        linkedList = new SinglyLinkedList<>();
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

    @Test
    public void clear() {
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();

        linkedList.add("hello");
        linkedList.clear();
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());

        linkedList.add("hello");
        linkedList.add("world");
        linkedList.clear();
        assertNull(linkedList.head);
        assertNull(linkedList.last);
        assertTrue(linkedList.isEmpty());
    }

    @Test
    public void testToString() {
        final SinglyLinkedList<String> linkedList = new SinglyLinkedList<>();
        linkedList.add("1");
        linkedList.add("2");
        linkedList.add("1");
        linkedList.add("b");
        linkedList.add("3");
        linkedList.add("a");
        assertEquals("[1, 2, 1, b, 3, a]", linkedList.toString());
    }

    @Test
    public void testEquals() {
        final String element1 = "element1";
        final String element2 = "element2";
        final SinglyLinkedNode<String> node1 = new SinglyLinkedNode<>(element1);
        final SinglyLinkedNode<String> node2 = new SinglyLinkedNode<>(element2);

        assertFalse(node1.equals(null));
        assertFalse(node1.equals(new Object()));
        assertTrue(node1.equals(node1));
        assertFalse(node1.equals(node2));
        node2.data = element1;
        assertTrue(node1.equals(node2));

        node1.next = node2;
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        final String element1 = "element1";
        final String element2 = "element2";
        final SinglyLinkedNode<String> node1 = new SinglyLinkedNode<>(element1);
        final SinglyLinkedNode<String> node2 = new SinglyLinkedNode<>(element2);

        assertNotEquals(0, node1.hashCode());
        assertNotEquals(node1.hashCode(), node2.hashCode());

        node2.data = element1;
        assertEquals(node1.hashCode(), node2.hashCode());

        node1.data = null;
        assertEquals(0, node1.hashCode());
        node2.data = null;
        assertEquals(0, node2.hashCode());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    private static class Wrapper<T> {
        @Nullable final T value;

        public Wrapper(@Nullable final T value) {
            this.value = value;
        }

        @Override
        public final boolean equals(final Object other) {
            if (!(other instanceof Wrapper)) {
                return false;
            }
            final Wrapper<?> otherWrapper = (Wrapper<?>) other;
            return Objects.equals(value, otherWrapper.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }
}
