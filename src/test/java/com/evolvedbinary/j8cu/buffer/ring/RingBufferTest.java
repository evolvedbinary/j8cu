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
package com.evolvedbinary.j8cu.buffer.ring;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static com.evolvedbinary.j8cu.buffer.ring.RingBuffer.READ_UNINITIALISED;
import static com.evolvedbinary.j8cu.buffer.ring.RingBuffer.WRITE_UNINITIALISED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Tests for {@link RingBuffer}.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public class RingBufferTest {

    @Test
    public void create() {
        final int capacity = 5;
        final RingBuffer<Object> ringBuffer = RingBuffer.create(capacity);

        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        for (int i = 0; i < capacity; i++) {
            assertNull(ringBuffer.entries[i]);
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `get` on an empty Ring Buffer does nothing
        assertNull(ringBuffer.get());
        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void createWithOrderedReads() {
        final int capacity = 5;
        final RingBuffer<Object> ringBuffer = RingBuffer.create(capacity, true);

        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        for (int i = 0; i < capacity; i++) {
            assertNull(ringBuffer.entries[i]);
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `get` on an empty Ring Buffer does nothing
        assertNull(ringBuffer.get());
        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructForStringType() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        for (int i = 0; i < capacity; i++) {
            assertNull(ringBuffer.entries[i]);
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `get` on an empty Ring Buffer does nothing
        assertNull(ringBuffer.get());
        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructForStringTypeWithOrderedReads() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        for (int i = 0; i < capacity; i++) {
            assertNull(ringBuffer.entries[i]);
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `get` on an empty Ring Buffer does nothing
        assertNull(ringBuffer.get());
        assertTrue(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndUnderFill() {
        final int capacity = 10;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // under fill by 1/2
        final int quantity = 5;
        final String[] expectedCopy = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertFalse(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructWithOrderedReadsAndUnderFill() {
        final int capacity = 10;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // under fill by 1/2
        final int quantity = 5;
        final String[] expectedCopy = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertFalse(ringBuffer.isEmpty());
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructAndUnderFillAndReadAll() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // under fill by 1/2
        final int quantity = 2;
        final String[] expectedCopy = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: a,b,null,null,null
        final String[] expected = { "a", "b", null, null, null };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);  // readSequence should not have changed by reading null

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndUnderFillAndReadAll() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // under fill by 1/2
        final int quantity = 2;
        final String[] expectedCopy = new String[quantity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: a,b,null,null,null
        final String[] expected = { "a", "b", null, null, null };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndFill() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // fill
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructWithOrderedReadsAndFill() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // fill
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructAndFillAndReadAll() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // fill
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expected: a,b,c,d,e
        final String[] expected = { "a", "b", "c", "d", "e" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndFillAndReadAll() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // fill
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expected: a,b,c,d,e
        final String[] expected = { "a", "b", "c", "d", "e" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFill() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by *2
        final int quantity = 10;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructWithOrderedReadsAndOverFill() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by *2
        final int quantity = 10;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(4, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructAndOverFillAndReadAll() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by *2
        final int quantity = 10;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: f,g,h,i,j
        final String[] expected = { "f", "g", "h", "i", "j" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by *2
        final int quantity = 10;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(4, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: f,g,h,i,j
        final String[] expected = { "f", "g", "h", "i", "j" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFill1() {
        final int capacity = 10;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 1 entry
        final int quantity = 11;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructWithOrderedReadsAndOverFill1() {
        final int capacity = 10;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 1 entry
        final int quantity = 11;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(0, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructAndOverFillAndReadAll1() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 1 entry
        final int quantity = 11;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: k,g,h,i,j
        final String[] expected = { "k", "g", "h", "i", "j" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll1() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 1 entry
        final int quantity = 11;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(0, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: g,h,i,j,k
        final String[] expected = { "g", "h", "i", "j", "k" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFill2() {
        final int capacity = 10;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 2 entries
        final int quantity = 12;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructWithOrderedReadsAndOverFill2() {
        final int capacity = 10;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 2 entries
        final int quantity = 12;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(1, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);
    }

    @Test
    public void constructAndOverFillAndReadAll2() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 2 entries
        final int quantity = 12;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: k,l,h,i,j
        final String[] expected = { "k", "l", "h", "i", "j" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll2() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 2 entries
        final int quantity = 12;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(1, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: h,i,j,k,l
        final String[] expected = { "h", "i", "j", "k", "l" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFillAndReadAll3() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 3 entries
        final int quantity = 13;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(2, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: k,l,m,i,j
        final String[] expected = { "k", "l", "m", "i", "j" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll3() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 3 entries
        final int quantity = 13;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(2, ringBuffer.readIdx);
        assertEquals(2, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: i,j,k,l,m
        final String[] expected = { "i", "j", "k", "l", "m" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFillAndReadAll4() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 4 entries
        final int quantity = 14;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(3, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: k,l,m,n,j
        final String[] expected = { "k", "l", "m", "n", "j" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll4() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 4 entries
        final int quantity = 14;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(3, ringBuffer.readIdx);
        assertEquals(3, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: j,k,l,m,n
        final String[] expected = { "j", "k", "l", "m", "n" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFillAndReadAll5() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 5 entries
        final int quantity = 15;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: k,l,m,n,o
        final String[] expected = { "k", "l", "m", "n", "o" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll5() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 5 entries
        final int quantity = 15;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(4, ringBuffer.readIdx);
        assertEquals(4, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: k,l,m,n,o
        final String[] expected = { "k", "l", "m", "n", "o" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructAndOverFillAndReadAll6() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

        // over fill by 6 entries
        final int quantity = 16;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            expectedCopy[i % capacity] = entry;
        }
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: p,l,m,n,o
        final String[] expected = { "p", "l", "m", "n", "o" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void constructWithOrderedReadsAndOverFillAndReadAll6() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // over fill by 6 entries
        final int quantity = 16;
        final String[] expectedCopy = new String[capacity];
        for (int i = 0; i < quantity; i++) {
            final String entry = "" + (char) ('a' + i);
            ringBuffer.put(entry);
            if (i >= quantity - capacity) {
                expectedCopy[(i - (quantity - capacity)) % capacity] = entry;
            }
        }
        assertEquals(0, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        final String[] copy = ringBuffer.copy();
        assertArrayEquals(expectedCopy, copy);

        // read all - expect: l,m,n,o,p
        final String[] expected = { "l", "m", "n", "o", "p" };
        final String[] actual = new String[capacity];
        for (int i = 0; i < capacity; i++) {
            final String entry = ringBuffer.get();
            actual[i] = entry;
        }
        assertArrayEquals(expected, actual);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // having read all entries, we now expect the next read to produce null
        assertNull(ringBuffer.get());
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // readSequence should not have changed by reading null
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` on an empty Ring Buffer returns null
        assertNull(ringBuffer.copy());
    }

    @Test
    public void putGetPutGetUnordered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. get 1 entry
        final String entry1 = ringBuffer.get();
        assertEquals("a", entry1);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(0, ringBuffer.available);

        // 3. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(1, ringBuffer.available);

        // 4. get 1 more entry
        final String entry2 = ringBuffer.get();
        assertEquals("b", entry2);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(0, ringBuffer.available);
    }

    @Test
    public void putGetPutGetOrdered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(1, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. get 1 entry
        final String entry1 = ringBuffer.get();
        assertEquals("a", entry1);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(0, ringBuffer.available);

        // 3. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(1, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 4. get 1 more entry
        final String entry2 = ringBuffer.get();
        assertEquals("b", entry2);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);
        assertEquals(0, ringBuffer.available);
    }

    @Test
    public void putPutGetGetUnordered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(2, ringBuffer.available);

        // 3. get 1 entry
        final String entry1 = ringBuffer.get();
        assertEquals("a", entry1);

        assertEquals(0, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(1, ringBuffer.available);

        // 4. get 1 more entry
        final String entry2 = ringBuffer.get();
        assertEquals("b", entry2);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);   // should not have changed
        assertEquals(0, ringBuffer.available);
    }

    @Test
    public void putPutGetGetOrdered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(1, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);
        assertEquals(2, ringBuffer.putCount);
        assertEquals(2, ringBuffer.available);

        // 3. get 1 entry
        final String entry1 = ringBuffer.get();
        assertEquals("a", entry1);

        assertEquals(0, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);  // should not have changed
        assertEquals(2, ringBuffer.putCount);  // should not have changed
        assertEquals(1, ringBuffer.available);

        // 4. get 1 more entry
        final String entry2 = ringBuffer.get();
        assertEquals("b", entry2);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(0, ringBuffer.available);
    }

    @Test
    public void putPutGetPutGetGetUnordered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(2, ringBuffer.available);

        // 3. get 1 entry
        final String entry1 = ringBuffer.get();
        assertEquals("a", entry1);

        assertEquals(0, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(1, ringBuffer.available);

        // 4. put 1 more entry
        ringBuffer.put("c");

        assertEquals(0, ringBuffer.readIdx);  // should not have changed
        assertEquals(2, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(2, ringBuffer.available);

        // 5. get 1 more entry
        final String entry2 = ringBuffer.get();
        assertEquals("b", entry2);

        assertEquals(1, ringBuffer.readIdx);
        assertEquals(2, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(1, ringBuffer.available);

        // 6. get 1 more entry
        final String entry3 = ringBuffer.get();
        assertEquals("c", entry3);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(0, ringBuffer.available);
    }

    @Test
    public void putPutGetPutGetGetOrdered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(1, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);
        assertEquals(2, ringBuffer.putCount);
        assertEquals(2, ringBuffer.available);

        // 3. get 1 entry
        final String entry1 = ringBuffer.get();
        assertEquals("a", entry1);

        assertEquals(0, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);  // should not have changed
        assertEquals(2, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 4. put 1 more entry
        ringBuffer.put("c");

        assertEquals(0, ringBuffer.readIdx);  // should not have changed
        assertEquals(2, ringBuffer.writeIdx);
        assertEquals(3, ringBuffer.putCount);
        assertEquals(2, ringBuffer.available);

        // 5. get 1 more entry
        final String entry2 = ringBuffer.get();
        assertEquals("b", entry2);

        assertEquals(1, ringBuffer.readIdx);
        assertEquals(2, ringBuffer.writeIdx);  // should not have changed
        assertEquals(3, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 6. get 1 more entry
        final String entry3 = ringBuffer.get();
        assertEquals("c", entry3);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(0, ringBuffer.available);
    }

    @Test
    public void copyCopyUnordered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. copy the internal buffer
        final String[] copy1 = ringBuffer.copy();
        assertArrayEquals(new String[] { "a" }, copy1);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(0, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(1, ringBuffer.available);  // should not have changed

        // 3. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(2, ringBuffer.available);

        // 4. copy the internal buffer again
        final String[] copy2 = ringBuffer.copy();
        assertArrayEquals(new String[] { "a", "b" }, copy2);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);  // should not have changed
        assertEquals(0, ringBuffer.putCount);  // should not have changed
        assertEquals(2, ringBuffer.available);  // should not have changed

    }

    @Test
    public void copyCopyOrdered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // 1. put 1 entry
        ringBuffer.put("a");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);
        assertEquals(1, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);

        // 2. copy the internal buffer
        final String[] copy1 = ringBuffer.copy();
        assertArrayEquals(new String[] { "a" }, copy1);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(0, ringBuffer.writeIdx);  // should not have changed
        assertEquals(1, ringBuffer.putCount);
        assertEquals(1, ringBuffer.available);   // should not have changed

        // 3. put 1 more entry
        ringBuffer.put("b");

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);
        assertEquals(2, ringBuffer.putCount);
        assertEquals(2, ringBuffer.available);

        // 4. copy the internal buffer again
        final String[] copy2 = ringBuffer.copy();
        assertArrayEquals(new String[] { "a", "b" }, copy2);

        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);  // should not have changed
        assertEquals(1, ringBuffer.writeIdx);  // should not have changed
        assertEquals(2, ringBuffer.putCount);  // should not have changed
        assertEquals(2, ringBuffer.available);  // should not have changed
    }

    @ParameterizedTest
    @MethodSource("randomPutGetPutGet")
    public void putGetPutGetUnordered(final int capacity, final int putCount1, final int getCount1, final int putCount2, final int getCount2) {
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);
        assertFalse(ringBuffer.orderedReads);

        // 1. first set of put operations
        for (int i = 0; i < putCount1; i++) {
            ringBuffer.put("" + (char) ('a' + i));
        }
        final int expectedBeforeReadAvailable1 =  Math.min(capacity, putCount1);
        assertEquals(expectedBeforeReadAvailable1, ringBuffer.available);
        final int expectedWriteIdx1 = (putCount1 - 1) % capacity;
        final int expectedReadIdx1 = READ_UNINITIALISED;
        final int expectedPutCount1 = 0;
        assertEquals(expectedReadIdx1, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx1, ringBuffer.writeIdx);
        assertEquals(expectedPutCount1, ringBuffer.putCount);

        // 2. first set of get operations
        @Nullable final String[] copy1 = ringBuffer.copy();
        assertNotNull(copy1);
        final String[] actual1 = new String[expectedBeforeReadAvailable1];
        for (int i = 0; i < getCount1; i++) {
            final String entry = ringBuffer.get();
            if (i < expectedBeforeReadAvailable1) {
                actual1[i] = entry;
            } else {
                assertNull(entry);
            }
        }
        final int expectedAfterReadAvailable1 = Math.max(0, expectedBeforeReadAvailable1 - getCount1);
        assertEquals(expectedAfterReadAvailable1, ringBuffer.available);
        final int expectedReadIdx2;
        final int expectedWriteIdx2;
        final int expectedPutCount2 = 0;
        final boolean get1EmptiesBuffer = getCount1 >= expectedBeforeReadAvailable1;
        if (get1EmptiesBuffer) {
            expectedReadIdx2 = READ_UNINITIALISED;
            expectedWriteIdx2 = WRITE_UNINITIALISED;
        } else {
            expectedReadIdx2 = expectedBeforeReadAvailable1 - expectedAfterReadAvailable1 - 1;
            expectedWriteIdx2 = expectedWriteIdx1;
        }
        assertEquals(expectedReadIdx2, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx2, ringBuffer.writeIdx);
        assertEquals(expectedPutCount2, ringBuffer.putCount);
        // calculate the first expected result
        final String[] expected1 = new String[expectedBeforeReadAvailable1];
        for (int i = 0; i < putCount1; i++) {
            final int idx = i % expectedBeforeReadAvailable1;
            expected1[idx] = "" + (char) ('a' + i);
        }
        if (getCount1 < expectedBeforeReadAvailable1) {
            // we didn't previously read all the entries and so we need to null the unread ones out
            for (int i = 0; i < expectedBeforeReadAvailable1; i++) {
                if (i >= getCount1) {
                    expected1[i] = null;
                }
            }
        }
        assertArrayEquals(expected1, actual1);
        assertArrayEquals(Arrays.copyOf(expected1, getCount1), Arrays.copyOf(copy1, getCount1));

        // 3. second set of put operations
        for (int i = 0; i < putCount2; i++) {
            ringBuffer.put("" + (char) ('a' + i));
        }
        final int expectedBeforeReadAvailable2 =  Math.min(capacity, expectedAfterReadAvailable1 + putCount2);
        assertEquals(expectedBeforeReadAvailable2, ringBuffer.available);
        final int expectedReadIdx3 = expectedReadIdx2;
        final int expectedWriteIdx3 = (expectedWriteIdx2 + putCount2) % capacity;
        final int expectedPutCount3 = 0;
        assertEquals(expectedReadIdx3, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx3, ringBuffer.writeIdx);
        assertEquals(expectedPutCount3, ringBuffer.putCount);

        // 4. second set of get operations
        @Nullable final String[] copy2 = ringBuffer.copy();
        assertNotNull(copy2);
        final String[] actual2 = new String[expectedBeforeReadAvailable2];
        for (int i = 0; i < getCount2; i++) {
            final String entry = ringBuffer.get();
            if (i < expectedBeforeReadAvailable2) {
                actual2[i] = entry;
            } else {
                assertNull(entry);
            }
        }
        final int expectedAfterReadAvailable2 = Math.max(0, expectedBeforeReadAvailable2 - getCount2);
        assertEquals(expectedAfterReadAvailable2, ringBuffer.available);
        final int expectedReadIdx4;
        final int expectedWriteIdx4;
        final int expectedPutCount4 = 0;
        final boolean get2EmptiesBuffer = getCount2 >= expectedBeforeReadAvailable2;
        if (get2EmptiesBuffer) {
            expectedReadIdx4 = READ_UNINITIALISED;
            expectedWriteIdx4 = WRITE_UNINITIALISED;
        } else {
            expectedReadIdx4 = (expectedReadIdx2 + Math.min(expectedBeforeReadAvailable2, getCount2)) % capacity;
            expectedWriteIdx4 = expectedWriteIdx3;
        }
        assertEquals(expectedReadIdx4, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx4, ringBuffer.writeIdx);
        assertEquals(expectedPutCount4, ringBuffer.putCount);
        // calculate the second expected result
        final String[] expected2Temp = new String[capacity];
        int i = 0;
        for (; i < putCount1; i++) {
            final int idx = i % capacity;
            expected2Temp[idx] = "" + (char) ('a' + i);
        }
        final int continuePutFrom = get1EmptiesBuffer ? (WRITE_UNINITIALISED + 1) : i;
        for (int j = 0; j < putCount2; j++) {
            final int idx = (continuePutFrom + j) % capacity;
            expected2Temp[idx] = "" + (char) ('a' + j);
        }
        final String[] expected2 = new String[expectedBeforeReadAvailable2];
        for (i = 0; i < expectedBeforeReadAvailable2; i++) {
            final int idx = (i + expectedReadIdx3 + 1) % capacity;
            if (i < getCount2) {
                expected2[i] = expected2Temp[idx];
            }
        }
        assertArrayEquals(expected2, actual2);
        assertArrayEquals(Arrays.copyOf(expected2, getCount2), Arrays.copyOf(copy2, getCount2));
    }

    @ParameterizedTest
    @MethodSource("randomPutGetPutGet")
    public void putGetPutGetOrdered(final int capacity, final int putCount1, final int getCount1, final int putCount2, final int getCount2) {
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);
        assertTrue(ringBuffer.orderedReads);

        // 1. first set of put operations
        for (int i = 0; i < putCount1; i++) {
            ringBuffer.put("" + (char) ('a' + i));
        }
        final int expectedBeforeReadAvailable1 =  Math.min(capacity, putCount1);
        assertEquals(expectedBeforeReadAvailable1, ringBuffer.available);
        boolean overwriting = putCount1 > capacity;
        final int expectedWriteIdx1 = (putCount1 - 1) % capacity;
        final int expectedReadIdx1 = overwriting ? expectedWriteIdx1 : READ_UNINITIALISED;
        final int expectedPutCount1 = Math.min(putCount1, capacity);
        assertEquals(expectedReadIdx1, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx1, ringBuffer.writeIdx);
        assertEquals(expectedPutCount1, ringBuffer.putCount);

        // 2. first set of get operations
        @Nullable final String[] copy1 = ringBuffer.copy();
        assertNotNull(copy1);
        final String[] actual1 = new String[expectedBeforeReadAvailable1];
        for (int i = 0; i < getCount1; i++) {
            final String entry = ringBuffer.get();
            if (i < expectedBeforeReadAvailable1) {
                actual1[i] = entry;
            } else {
                assertNull(entry);
            }
        }
        final int expectedAfterReadAvailable1 = Math.max(0, expectedBeforeReadAvailable1 - getCount1);
        assertEquals(expectedAfterReadAvailable1, ringBuffer.available);
        final int expectedReadIdx2;
        final int expectedWriteIdx2;
        final int expectedPutCount2;
        final boolean get1EmptiesBuffer = getCount1 >= expectedBeforeReadAvailable1;
        if (get1EmptiesBuffer) {
            expectedReadIdx2 = READ_UNINITIALISED;
            expectedWriteIdx2 = WRITE_UNINITIALISED;
            expectedPutCount2 = 0;
        } else {
            expectedReadIdx2 = (expectedReadIdx1 + expectedBeforeReadAvailable1 - expectedAfterReadAvailable1) % capacity;
            expectedWriteIdx2 = expectedWriteIdx1;
            expectedPutCount2 = expectedPutCount1;
        }
        assertEquals(expectedReadIdx2, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx2, ringBuffer.writeIdx);
        assertEquals(expectedPutCount2, ringBuffer.putCount);
        // calculate the first expected result
        final String[] expected1Temp = new String[expectedBeforeReadAvailable1];
        for (int i = 0; i < putCount1; i++) {
            final int idx = i % expectedBeforeReadAvailable1;
            expected1Temp[idx] = "" + (char) ('a' + i);
        }
        final String[] expected1 = new String[expectedBeforeReadAvailable1];
        for (int i = 0; i < expectedBeforeReadAvailable1; i++) {
            if (i < getCount1) {
                final int idx = (expectedReadIdx1 + i + 1) % capacity;
                expected1[i] = expected1Temp[idx];
            }
        }
        assertArrayEquals(expected1, actual1);
        assertArrayEquals(Arrays.copyOf(expected1, getCount1), Arrays.copyOf(copy1, getCount1));

        // 3. second set of put operations
        int expectedReadIdx3 = expectedReadIdx2;
        for (int i = 0; i < putCount2; i++) {
            ringBuffer.put("" + (char) ('a' + i));

            // calculate expected read index
            overwriting = expectedPutCount2 + i >= capacity;
            if (overwriting) {
                final int writeIdx = (expectedWriteIdx2 + i + 1) % capacity;
                assertEquals(writeIdx, ringBuffer.writeIdx);
                if (expectedReadIdx3 == READ_UNINITIALISED || (expectedReadIdx3 + 1) % capacity == writeIdx) {
                    expectedReadIdx3 = writeIdx;
                }
            }
            assertEquals(expectedReadIdx3, ringBuffer.readIdx);
        }
        final int expectedBeforeReadAvailable2 =  Math.min(capacity, expectedAfterReadAvailable1 + putCount2);
        assertEquals(expectedBeforeReadAvailable2, ringBuffer.available);
        final int expectedWriteIdx3 = (expectedWriteIdx2 + putCount2) % capacity;
        final int expectedPutCount3;
        if (expectedWriteIdx2 == WRITE_UNINITIALISED) {
            expectedPutCount3 = Math.min(putCount2, capacity);
        } else {
            expectedPutCount3 = Math.min(putCount1 + putCount2, capacity);
        }
        assertEquals(expectedReadIdx3, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx3, ringBuffer.writeIdx);
        assertEquals(expectedPutCount3, ringBuffer.putCount);

        // 4. second set of get operations
        @Nullable final String[] copy2 = ringBuffer.copy();
        assertNotNull(copy2);
        final String[] actual2 = new String[expectedBeforeReadAvailable2];
        for (int i = 0; i < getCount2; i++) {
            final String entry = ringBuffer.get();
            if (i < expectedBeforeReadAvailable2) {
                actual2[i] = entry;
            } else {
                assertNull(entry);
            }
        }
        final int expectedAfterReadAvailable2 = Math.max(0, expectedBeforeReadAvailable2 - getCount2);
        assertEquals(expectedAfterReadAvailable2, ringBuffer.available);
        final int expectedReadIdx4;
        final int expectedWriteIdx4;
        final int expectedPutCount4;
        final boolean get2EmptiesBuffer = getCount2 >= expectedBeforeReadAvailable2;
        if (get2EmptiesBuffer) {
            expectedReadIdx4 = READ_UNINITIALISED;
            expectedWriteIdx4 = WRITE_UNINITIALISED;
            expectedPutCount4 = 0;
        } else {
            expectedReadIdx4 = (expectedReadIdx3 + expectedBeforeReadAvailable2 - expectedAfterReadAvailable2) % capacity;
            expectedWriteIdx4 = expectedWriteIdx3;
            expectedPutCount4 =  expectedPutCount3;
        }
        assertEquals(expectedReadIdx4, ringBuffer.readIdx);
        assertEquals(expectedWriteIdx4, ringBuffer.writeIdx);
        assertEquals(expectedPutCount4, ringBuffer.putCount);
        // calculate the second expected result
        final String[] expected2Temp = new String[capacity];
        int i = 0;
        for (; i < putCount1; i++) {
            final int idx = i % capacity;
            expected2Temp[idx] = "" + (char) ('a' + i);
        }
        final int continuePutFrom = get1EmptiesBuffer ? (WRITE_UNINITIALISED + 1) : i;
        for (int j = 0; j < putCount2; j++) {
            final int idx = (continuePutFrom + j) % capacity;
            expected2Temp[idx] = "" + (char) ('a' + j);
        }
        final String[] expected2 = new String[expectedBeforeReadAvailable2];
        for (i = 0; i < expectedBeforeReadAvailable2; i++) {
            final int idx = (expectedReadIdx3 + i + 1) % capacity;
            if (i < getCount2) {
                expected2[i] = expected2Temp[idx];
            }
        }
        assertArrayEquals(expected2, actual2);
        assertArrayEquals(Arrays.copyOf(expected2, getCount2), Arrays.copyOf(copy2, getCount2));
    }

    @Test
    public void addRemoveListener() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);
        assertNull(ringBuffer.listeners);

        final RingBuffer.Listener<String> listener1 = new CollectingListener<>();
        final RingBuffer.Listener<String> listener2 = new CollectingListener<>();

        // check that remove does nothing when the listeners are empty
        ringBuffer.removeListener(listener1);
        ringBuffer.removeListener(listener2);
        assertNull(ringBuffer.listeners);

        // check that adding listeners keeps the order of listeners
        ringBuffer.addListener(listener1);
        ringBuffer.addListener(listener2);
        assertArrayEquals(new RingBuffer.Listener[] { listener1, listener2 }, ringBuffer.listeners);

        // remove a listener and check that it was removed
        ringBuffer.removeListener(listener1);
        assertArrayEquals(new RingBuffer.Listener[] { listener2 }, ringBuffer.listeners);

        // remove the same listener and check that nothing changed
        ringBuffer.removeListener(listener1);
        assertArrayEquals(new RingBuffer.Listener[] { listener2 }, ringBuffer.listeners);

        // re-add the first listener and check that it was added last
        ringBuffer.addListener(listener1);
        assertArrayEquals(new RingBuffer.Listener[] { listener2, listener1 }, ringBuffer.listeners);

        // remove both listeners and check that there are no listeners
        ringBuffer.removeListener(listener1);
        ringBuffer.removeListener(listener2);
        assertNull(ringBuffer.listeners);
    }

    @Test
    public void addRemoveListenerOrdered() {
        final int capacity = 5;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);
        assertNull(ringBuffer.listeners);

        final RingBuffer.Listener<String> listener1 = new CollectingListener<>();
        final RingBuffer.Listener<String> listener2 = new CollectingListener<>();

        // check that remove does nothing when the listeners are empty
        ringBuffer.removeListener(listener1);
        ringBuffer.removeListener(listener2);
        assertNull(ringBuffer.listeners);

        // check that adding listeners keeps the order of listeners
        ringBuffer.addListener(listener1);
        ringBuffer.addListener(listener2);
        assertArrayEquals(new RingBuffer.Listener[] { listener1, listener2 }, ringBuffer.listeners);

        // remove a listener and check that it was removed
        ringBuffer.removeListener(listener1);
        assertArrayEquals(new RingBuffer.Listener[] { listener2 }, ringBuffer.listeners);

        // remove the same listener and check that nothing changed
        ringBuffer.removeListener(listener1);
        assertArrayEquals(new RingBuffer.Listener[] { listener2 }, ringBuffer.listeners);

        // re-add the first listener and check that it was added last
        ringBuffer.addListener(listener1);
        assertArrayEquals(new RingBuffer.Listener[] { listener2, listener1 }, ringBuffer.listeners);

        // remove both listeners and check that there are no listeners
        ringBuffer.removeListener(listener1);
        ringBuffer.removeListener(listener2);
        assertNull(ringBuffer.listeners);
    }

    @Test
    public void simpleOverfillUnordered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);

        // check buffer is initialised correctly
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // put 4 entries
        ringBuffer.put("a");
        ringBuffer.put("b");
        ringBuffer.put("c");
        ringBuffer.put("d");

        // check new state of buffer
        assertEquals(3, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        String[] copy = ringBuffer.copy();
        assertArrayEquals(new String[] { "d", "b", "c" }, copy);

        // check new state of buffer (should be unchanged after copy)
        assertEquals(3, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // get 3 entries
        String read = ringBuffer.get();
        assertEquals("d", read);
        read = ringBuffer.get();
        assertEquals("b", read);
        read = ringBuffer.get();
        assertEquals("c", read);

        // check new state of buffer
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // put 2 entries
        ringBuffer.put("e");
        ringBuffer.put("f");

        // check new state of buffer
        assertEquals(2, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        copy = ringBuffer.copy();
        assertArrayEquals(new String[] { "e", "f"}, copy);

        // check new state of buffer (should be unchanged after copy)
        assertEquals(2, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // get 2 entries
        read = ringBuffer.get();
        assertEquals("e", read);
        read = ringBuffer.get();
        assertEquals("f", read);

        // check new state of buffer
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        copy = ringBuffer.copy();
        assertNull(copy);

        // check new state of buffer (should be unchanged after copy)
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
    }

    @Test
    public void simpleOverfillOrdered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);

        // check buffer is initialised correctly
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // put 4 entries
        ringBuffer.put("a");
        ringBuffer.put("b");
        ringBuffer.put("c");
        ringBuffer.put("d");

        // check new state of buffer
        assertEquals(3, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(0, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        String[] copy = ringBuffer.copy();
        assertArrayEquals(new String[] { "b", "c", "d" }, copy);

        // check new state of buffer (should be unchanged after copy)
        assertEquals(3, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(0, ringBuffer.readIdx);
        assertEquals(0, ringBuffer.writeIdx);

        // get 3 entries
        String read = ringBuffer.get();
        assertEquals("b", read);
        read = ringBuffer.get();
        assertEquals("c", read);
        read = ringBuffer.get();
        assertEquals("d", read);

        // check new state of buffer
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // put 2 entries
        ringBuffer.put("e");
        ringBuffer.put("f");

        // check new state of buffer
        assertEquals(2, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        copy = ringBuffer.copy();
        assertArrayEquals(new String[] { "e", "f"}, copy);

        // check new state of buffer (should be unchanged after copy)
        assertEquals(2, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(1, ringBuffer.writeIdx);

        // get 2 entries
        read = ringBuffer.get();
        assertEquals("e", read);
        read = ringBuffer.get();
        assertEquals("f", read);

        // check new state of buffer
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);

        // check that `copy` returns the correct entries
        copy = ringBuffer.copy();
        assertNull(copy);

        // check new state of buffer (should be unchanged after copy)
        assertEquals(0, ringBuffer.available);
        assertEquals(capacity, ringBuffer.capacity);
        assertEquals(capacity, ringBuffer.entries.length);
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
    }

    @Test
    public void reset() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);
        assertTrue(ringBuffer.isEmpty());

        ringBuffer.put("a");
        ringBuffer.put("b");
        assertFalse(ringBuffer.isEmpty());

        ringBuffer.reset();
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.available);
        assertEquals(0, ringBuffer.putCount);

        // check entries are not null
        assertArrayEquals(new String[] {"a", "b", null }, ringBuffer.entries);
    }

    @Test
    public void clear() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);
        assertTrue(ringBuffer.isEmpty());

        ringBuffer.put("a");
        ringBuffer.put("b");
        assertFalse(ringBuffer.isEmpty());

        ringBuffer.clear();
        assertEquals(READ_UNINITIALISED, ringBuffer.readIdx);
        assertEquals(WRITE_UNINITIALISED, ringBuffer.writeIdx);
        assertEquals(0, ringBuffer.available);
        assertEquals(0, ringBuffer.putCount);

        // check entries are now null
        assertArrayEquals(new String[3], ringBuffer.entries);
    }

    @Test
    public void listenerEventsOrdered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, true);
        assertNull(ringBuffer.listeners);

        final CollectingListener<String> listener1 = new CollectingListener<>();
        ringBuffer.addListener(listener1);

        // put 4 entries
        ringBuffer.put("a");
        ringBuffer.put("b");
        ringBuffer.put("c");
        ringBuffer.put("d");

        // check the listener received all events
        assertArrayEquals(new String[] { "a", "b", "c", "d" }, listener1.stored);

        // get 3 entries
        ringBuffer.get();
        ringBuffer.get();
        ringBuffer.get();

        // check the listener received all events
        assertArrayEquals(new String[] { "b", "c", "d" }, listener1.retrieved);

        // put 2 entries
        ringBuffer.put("e");
        ringBuffer.put("f");

        // check the listener received all events
        assertArrayEquals(new String[] { "a", "b", "c", "d", "e", "f" }, listener1.stored);

        // get 2 entries
        ringBuffer.get();
        ringBuffer.get();

        // check the listener received all events
        assertArrayEquals(new String[] { "b", "c", "d", "e", "f" }, listener1.retrieved);
    }

    @Test
    public void listenerEventsUnordered() {
        final int capacity = 3;
        final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, false);
        assertNull(ringBuffer.listeners);

        final CollectingListener<String> listener1 = new CollectingListener<>();
        ringBuffer.addListener(listener1);

        // put 4 entries
        ringBuffer.put("a");
        ringBuffer.put("b");
        ringBuffer.put("c");
        ringBuffer.put("d");

        // check the listener received all events
        assertArrayEquals(new String[] { "a", "b", "c", "d" }, listener1.stored);

        // get 3 entries
        ringBuffer.get();
        ringBuffer.get();
        ringBuffer.get();

        // check the listener received all events
        assertArrayEquals(new String[] { "d", "b", "c" }, listener1.retrieved);

        // put 2 entries
        ringBuffer.put("e");
        ringBuffer.put("f");

        // check the listener received all events
        assertArrayEquals(new String[] { "a", "b", "c", "d", "e", "f" }, listener1.stored);

        // get 2 entries
        ringBuffer.get();
        ringBuffer.get();

        // check the listener received all events
        assertArrayEquals(new String[] { "d", "b", "c", "e", "f" }, listener1.retrieved);
    }

    static Stream<Arguments> randomPutGetPutGet() {
        // we will generate 100 sets of arguments of random integers
        final int iterations = 100;
        final Arguments[] argsSets = new Arguments[iterations];
        for (int i = 0; i < iterations; i ++) {
            final int capacity = randomInt(7);
            final int putCount1 = randomInt(24);
            final int getCount1 = randomInt(24);
            final int putCount2 = randomInt(24);
            final int getCount2 = randomInt(24);

            final Arguments args = arguments(capacity, putCount1, getCount1, putCount2, getCount2);
            argsSets[i] = args;
        }

        return Stream.of(argsSets);
    }

    private static int randomInt(final int max) {
        // nextInt is exclusive of the top value, so add 1 to make it inclusive
        return ThreadLocalRandom.current().nextInt(1, max + 1);
    }

    @SuppressWarnings("unchecked")
    private static class CollectingListener<T> implements RingBuffer.Listener<T> {
        T[] retrieved = (T[]) new Object[0];
        T[] stored = (T[]) new Object[0];

        @Override
        public void retrieved(final @Nullable T entry) {
            retrieved = Arrays.copyOf(retrieved, retrieved.length + 1);
            retrieved[retrieved.length - 1] = entry;
        }

        @Override
        public void stored(final T entry) {
            stored = Arrays.copyOf(stored, stored.length + 1);
            stored[stored.length - 1] = entry;
        }
    }
}
