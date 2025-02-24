# Java 8 Collection Utilities

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/evolvedbinary/j8cu/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/evolvedbinary/j8cu/tree/main)
[![Coverage Status](https://coveralls.io/repos/github/evolvedbinary/j8cu/badge.svg?branch=main)](https://coveralls.io/github/evolvedbinary/j8xu?branch=main)
[![Java 8](https://img.shields.io/badge/java-8-blue.svg)](https://adoptopenjdk.net/)
[![License](https://img.shields.io/badge/license-BSD%203-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.evolvedbinary.j8cu/j8cu/badge.svg)](https://search.maven.org/search?q=g:com.evolvedbinary.j8xu)

Some extra Collection classes targetting Java 8.

The main things here at the moment are:

1. A [RingBuffer](src/main/java/com/evolvedbinary/j8cu/buffer/ring/RingBuffer.java) implementation.
2. A variety of [Linked List](src/main/java/com/evolvedbinary/j8cu/list/linked/) implementations.

## Maven Dependency
You can add the library to your project as a dependency with the following Maven coordinates:
```xml
<dependency>
    <groupId>com.evolvedbinary.j8cu</groupId>
    <artifactId>j8cu</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Ring Buffer
Implementation of a [RingBuffer](src/main/java/com/evolvedbinary/j8cu/buffer/ring/RingBuffer.java) (e.g. https://en.wikipedia.org/wiki/Circular_buffer) in Java 8.
This implementation supports two modes of operations for reading from the buffer:
    
    1. Unordered (the default)
    2. Ordered

The difference between Unordered and Ordered modes is that when you use Unordered mode,
it starts reading from the start of the buffer, whilst Ordered mode starts reading from the 
oldest entry in the buffer. Ordered mode has FIFO (First In, First Out) like semantics.

Ordered mode is useful for when you want to keep a buffer of the most recent N objects,
and you need to read them back from oldest to newest.

The RingBuffer also provides several additional features:
* Listeners may be registered to receive events when the state of the RingBuffer changes.
* All entries may be copied out of the RingBuffer.
* The RingBuffer can be `clear`ed which removes references to all entries and resets its state, or it can just be `reset` whereby any entry references are maintained but could be overwritten in future on subsequence calls to `put`.

### Unordered Ring Buffer Example

```java
import com.evolvedbinary.j8cu.buffer.ring.RingBuffer;

final int capacity = 3;
final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity);

ringBuffer.put("a");
ringBuffer.put("b");
ringBuffer.put("c");
ringBuffer.put("d");
ringBuffer.put("e");
```

Then subsequent calls to `get` entries from the RingBuffer would produce:
```
ringBuffer.get() => "d"
ringBuffer.get() => "e"
ringBuffer.get() => "c"
ringBuffer.get() => null
```

### Ordered Ring Buffer Example

```java
import com.evolvedbinary.j8cu.buffer.ring.RingBuffer;

final int capacity = 3;
final boolean orderedReads = true;
final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, orderedReads);

ringBuffer.put("a");
ringBuffer.put("b");
ringBuffer.put("c");
ringBuffer.put("d");
ringBuffer.put("e");
```

Then subsequent calls to `get` entries from the RingBuffer would produce:
```
ringBuffer.get() => "c"
ringBuffer.get() => "d"
ringBuffer.get() => "e"
ringBuffer.get() => null
```

### Listening to RingBuffer Events

It is also possible to add and remove listeners to a RingBuffer. One reason this can be useful would be if you would
like to get and/or be notified the next *n* entries within some sort of window (e.g. time frame or event space). For example:

```java
import com.evolvedbinary.j8cu.buffer.ring.RingBuffer;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


final int capacity = 7;
final boolean orderedReads = true;
final RingBuffer<String> ringBuffer = new RingBuffer<>(String.class, capacity, orderedReads);

// capture the entries added to the RingBuffer in the next 2 seconds
final int windowSeconds = 2;
final TimeWindowListener timeWindowListener = new TimeWindowListener(windowSeconds);
ringBuffer.addListener(timeWindowListener);

// put entries for the next 2 * 1.5 seconds
final long end = System.currentTimeMillis() + (windowSeconds * 1_500);
int i = 0;
while (System.currentTimeMillis() < end) {
    final int increment = i++ % 24;
    ringBuffer.put("" + (char)('a' + increment));
    Thread.sleep(250); // slow down this is just a demo!
}

// print out what was captured by the listener
final List<String> captured = timeWindowListener.getCaptured();
System.out.println("Added: " + captured.size());
System.out.println("Captured: " + captured);


/**
 * Simple example listener implementation that captures all `put` entries for a window measured in seconds
 */
private static class TimeWindowListener implements RingBuffer.Listener<String> {
    private final int window;  // seconds
    private final long start;
    private final List<String> captured = new ArrayList<>();

    public TimeWindowListener(final int window) {
        this.window = window;
        this.start = System.currentTimeMillis();
    }

    @Override
    public void retrieved(final @Nullable String entry) {
    }

    @Override
    public void stored(final String entry) {
        final long diff = System.currentTimeMillis() - start;
        if (diff < ((long) window) * 1_000) {
            captured.add(entry);
        }
    }

    public List<String> getCaptured() {
        return new ArrayList<>(captured);
    }
}
```

