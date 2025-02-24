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

import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Implementation of a Ring Buffer
 * (e.g. Wikipedia: <a href="https://en.wikipedia.org/wiki/Circular_buffer">Circular Buffer</a>).
 * <p>This implementation supports two modes of operations for reading from the buffer:
 *  <ol>
 *      <li>1. Unordered (the default)</li>
 *      <li>2. Ordered</li>
 *  </ol>
 * The difference between Unordered and Ordered modes is that when you use Unordered mode,
 * it starts reading from the start of the buffer, whilst Ordered mode starts reading from the
 * oldest entry in the buffer.
 * Ordered mode is useful for when you want to keep a buffer of the most recent N objects,
 * and you need to read them back from oldest to newest.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 *
 * @param <T> the type of the entries in the buffer.
 */
@NotThreadSafe
public class RingBuffer<T> {

    final static int READ_UNINITIALISED = -1;
    final static int WRITE_UNINITIALISED = -1;

    final T[] entries;
    final int capacity;

    /**
     * When `true` the oldest entry is returned first for {@link #get()} or {@link #copy()}.
     */
    final boolean orderedReads;

    /**
     * The index into entries that was previously read.
     */
    int readIdx;

    /**
     * The index into entries that was previously written.
     */
    int writeIdx;

    /**
     * The number of entries available to be read.
     */
    int available;

    /**
     * Used when {@code orderedReads == true} to determine whether to advance the {@code readIdx}
     * when {@link #put(Object)} is called.
     */
    int putCount;

    /**
     * Any listeners that will be notified of events that change the state of the Ring Buffer.
     */
    @Nullable Listener<@NonNull T>[] listeners = null;



    /**
     * Construct a new Ring Buffer for a specific type.
     *
     * @param type the class of the entries to be stored in the Ring Buffer.
     * @param capacity the capacity of the Ring Buffer.
     */
    public RingBuffer(final Class<T> type, final int capacity) {
        this(type, capacity, false);
    }

    /**
     * Construct a new Ring Buffer for a specific type that optionally supports ordered reads.
     *
     * @param type the class of the entries to be stored in the Ring Buffer.
     * @param capacity the capacity of the Ring Buffer.
     * @param orderedReads true if reads should be ordered, i.e. oldest first, or false otherwise.
     */
    @SuppressWarnings("unchecked")
    public RingBuffer(final Class<T> type, final int capacity, final boolean orderedReads) {
        this.entries = (T[]) Array.newInstance(type, capacity);
        this.capacity = entries.length;
        this.orderedReads = orderedReads;
        this.readIdx = READ_UNINITIALISED;
        this.writeIdx = WRITE_UNINITIALISED;
        this.available = 0;
        this.putCount = 0;
    }

    /**
     * Create a new Ring Buffer for any object.
     *
     * @param capacity the capacity of the Ring Buffer.
     *
     * @return the new RingBuffer instance.
     */
    public static RingBuffer<Object> create(final int capacity) {
       return create(capacity, false);
    }

    /**
     * Create a new Ring Buffer for any object that optionally supports ordered reads.
     *
     * @param capacity the capacity of the Ring Buffer.
     * @param orderedReads true if reads should be ordered, i.e. oldest first, or false otherwise.
     *
     * @return the new RingBuffer instance.
     */
    public static RingBuffer<Object> create(final int capacity, final boolean orderedReads) {
        return new RingBuffer<>(Object.class, capacity, orderedReads);
    }

    /**
     * Determine if the Ring Buffer is empty.
     *
     * @return true if the Ring Buffer is empty, false otherwise.
     */
    public boolean isEmpty() {
        return available == 0;
    }

    /**
     * Get the next entry from the Ring Buffer.
     *
     * @return the entry, or null if the Ring Buffer is empty.
     */
    public @Nullable T get() {
        if (available == 0) {
            // is empty
            return null;
        }

        readIdx = (++readIdx) % capacity;
        final T entry = entries[readIdx];
        available = Math.max(0, available - 1);

        if (available == 0) {
            // if we have removed the last entry from the buffer, we can reset its state to empty
            readIdx = READ_UNINITIALISED;
            writeIdx = WRITE_UNINITIALISED;
            putCount = 0;
        }

        if (listeners != null) {
            // notify any listeners of the event
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].retrieved(entry);
            }
        }

        return entry;
    }

    /**
     * Put an entry into the Ring Buffer.
     *
     * @param entry The entry to place into the Ring Buffer.
     */
    public void put(final T entry) {
        entries[writeIdx = ++writeIdx % capacity] = entry;
        available = Math.min(capacity, available + 1);
        if (orderedReads) {
            if (putCount == capacity) {
                if (readIdx == READ_UNINITIALISED || (readIdx + 1) % capacity == writeIdx) {
                    readIdx = writeIdx;
                }
            } else {
                putCount++;
            }
        }

        if (listeners != null) {
            // notify any listeners of the event
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].stored(entry);
            }
        }
    }

    /**
     * Get a copy of the entries in this buffer.
     *
     * @return a copy of the entries in this buffer, or null if the buffer is empty.
     */
    @SuppressWarnings("unchecked")
    public @Nullable T[] copy() {
        if (available == 0) {
            // is empty
            return null;
        }

        final Class<T> type = (Class<T>) entries.getClass().getComponentType();
        final T[] copy = (T[]) Array.newInstance(type, available);

        int copyReadIdx = readIdx;
        for (int i = 0; i < available; i++) {
            copyReadIdx = (++copyReadIdx) % capacity;
            copy[i] = entries[copyReadIdx];
        }

        return copy;
    }

    /**
     * Empties the Ring Buffer and restores it back to an initial empty state.
     * Note that unlike {@link #reset()} this does null out the entries in the internal buffer.
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            entries[i] = null;
        }
        reset();
    }

    /**
     * Resets the Ring Buffer back to an initial empty state.
     * Note that unlike {@link #clear()} this does not null out the entries in the internal buffer.
     */
    public void reset() {
        readIdx = READ_UNINITIALISED;
        writeIdx = WRITE_UNINITIALISED;
        available = 0;
        putCount = 0;
    }

    /**
     * Add an event listener to this Ring Buffer.
     *
     * @param listener the event listener to add.
     */
    @SuppressWarnings("unchecked")
    public void addListener(final Listener<T> listener) {
        if (listeners == null) {
            listeners = (Listener<T>[]) new Listener[1];
        } else {
            listeners = Arrays.copyOf(listeners, listeners.length + 1);
        }
        listeners[listeners.length - 1] = listener;
    }

    /**
     * Remove an event listener from this Ring Buffer.
     * Note that when looking for the provided listener in the registered listeners,
     * comparison is performed by identity.
     *
     * @param listener the event listener to remove.
     */
    @SuppressWarnings("unchecked")
    public void removeListener(final Listener<T> listener) {
        if (listeners == null) {
            return;
        }

        for (int i = 0; i < listeners.length; i++) {
            if (listener == listeners[i]) {
                if (listeners.length == 1) {
                    // if we are removing the last listener, we can just null out all the listeners
                    listeners = null;
                } else {
                    final Listener<T>[] newListeners = (Listener<T>[]) new Listener[listeners.length - 1];
                    System.arraycopy(listeners, 0, newListeners, 0, i);
                    System.arraycopy(listeners, i + 1, newListeners, i, listeners.length - i - 1);
                    listeners = newListeners;
                }
                break;
            }
        }
    }

    /**
     * An interface that can be used to listen to events on the RingBuffer.
     */
    public interface Listener<T> {

        /**
         * Called when an entry is retrieved from the buffer.
         *
         * @param entry the entry retrieved from the buffer, or null if the Ring Buffer was empty.
         */
        void retrieved(final @Nullable T entry);

        /**
         * Called when an entry is stored into the buffer.
         *
         * @param entry The entry that was stored into the Ring Buffer.
         */
        void stored(final T entry);
    }
}
