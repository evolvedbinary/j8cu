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

/**
 * Linked List interface.
 *
 * @author <a href="mailto:adam@evolvedbinary.com">Adam Retter</a>
 */
public interface LinkedList<T> extends Iterable<T> {

    /**
     * Determine if the list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Add an element to this list.
     *
     * @param element the element to add into this list.
     *
     * @return true if the element was added to this list, false otherwise.
     *         This is here for subclasses to override, this class always returns true!
     */
    boolean add(@Nullable final T element);

    /**
     * Returns the first element from this list.
     *
     * @return the first element from this list.
     *
     * @throws IllegalStateException if the list is empty.
     */
    @Nullable T head();

    /**
     * Returns the last element from this list.
     *
     * @return the last element from this list.
     *
     * @throws IllegalStateException if the list is empty.
     */
    @Nullable T last();

    /**
     * Returns true if the list contains an element
     * with the same identity as the provided element
     *
     * @return true if the list contains an element with the same identity, false otherwise.
     */
    boolean containsIdentity(@Nullable T element);

    /**
     * Returns true if the list contains an element
     * with the same equality as the provided element
     *
     * @return true if the list contains an element with the same equality, false otherwise.
     */
    boolean containsEquivalent(@Nullable T element);

    /**
     * Remove the first matching element from this list,
     * traversal always happens from the start of the list
     * to the end of the list. If you don't care about
     * traversal order then {@link #removeOne(Object)}
     * may be more efficient.
     *
     * @param element the element to remove from this list.
     *
     * @return true if the element was removed from this list, false otherwise.
     */
    boolean removeFirst(@Nullable T element);

    /**
     * Remove ant one matching element from this list,
     * if the list contains duplicates, any one of them
     * may be removed.
     *
     * @param element the element to remove from this list.
     *
     * @return true if the element was removed from this list, false otherwise.
     */
    boolean removeOne(@Nullable T element);

    /**
     * Remove all matching elements from this List.
     *
     * @param element the element to remove from this list.
     *
     * @return the number of elements removed from this list. Note that we stop counting once we reach {@link Long#MAX_VALUE}.
     */
    long removeAll(@Nullable T element);

    /**
     * Removes all of the elements from this list.
     */
    void clear();
}
