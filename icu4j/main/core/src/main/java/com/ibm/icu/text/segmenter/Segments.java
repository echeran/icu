package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Segments {

  CharSequence getSourceSequence();

  Stream<CharSequence> subSequences();

  Stream<Segment> ranges();

  Stream<Segment> rangesAfterIndex(int i);

  Stream<Segment> rangesBeforeIndex(int i);

  Segment rangeAfterIndex(int i);

  Segment rangeBeforeIndex(int i);

  Function<Segment, CharSequence> rangeToSequenceFn();

  IntStream boundaries();

  IntStream boundariesAfterIndex(int i);

  IntStream boundariesBeforeIndex(int i);

  //
  // Inner enums/classes in common for other inner classes
  //

  enum IterationDirection {
    FORWARDS,
    BACKWARDS,
  }

  //
  // Inner classes for Range, RangeIterable, and RangeIterator
  //

  class Segment {
    public final int start;
    public final int limit;
    public final int ruleStatus = 0;

    public Segment(int start, int limit) {
      this.start = start;
      this.limit = limit;
    }
  }

  /**
   * This {@code Iterable} exists to enable the creation of a {@code Spliterator} that in turn
   * enables the creation of a lazy {@code Stream}.
   */
  class SegmentIterable implements Iterable<Segment> {
    BreakIterator breakIter;
    IterationDirection direction;
    int startIdx;

    SegmentIterable(BreakIterator breakIter, IterationDirection direction, int startIdx) {
      this.breakIter = breakIter;
      this.direction = direction;
      this.startIdx = startIdx;
    }

    @Override
    public Iterator<Segment> iterator() {
      return new SegmentIterator(this.breakIter, this.direction, this.startIdx);
    }
  }

  class SegmentIterator implements Iterator<Segment> {
    BreakIterator breakIter;
    IterationDirection direction;
    int start;
    int limit;

    SegmentIterator(BreakIterator breakIter, IterationDirection direction, int startIdx) {
      this.breakIter = breakIter;
      this.direction = direction;

      if (direction == IterationDirection.FORWARDS) {
        this.start = breakIter.following(startIdx);
      } else {
        assert direction == IterationDirection.BACKWARDS;
        this.start = breakIter.preceding(startIdx);
      }

      this.limit = getDirectionBasedNextIdx();
    }

    int getDirectionBasedNextIdx() {
      if (direction == IterationDirection.FORWARDS) {
        return breakIter.next();
      } else {
        assert direction == IterationDirection.BACKWARDS;
        return breakIter.previous();
      }
    }

    @Override
    public boolean hasNext() {
      return this.limit != BreakIterator.DONE;
    }

    @Override
    public Segment next() {
      Segment result;
      if (this.limit < this.start) {
        result = new Segment(this.limit, this.start);
      } else {
        result = new Segment(this.start, this.limit);
      }

      this.start = this.limit;
      this.limit = getDirectionBasedNextIdx();

      return result;
    }
  }

  //
  // Inner classes for BoundaryIterable and BoundaryIterator
  //

  class BoundaryIterable implements Iterable<Integer> {
    BreakIterator breakIter;
    IterationDirection direction;
    int startIdx;

    BoundaryIterable(BreakIterator breakIter, IterationDirection direction, int startIdx) {
      this.breakIter = breakIter;
      this.direction = direction;
      this.startIdx = startIdx;
    }

    @Override
    public Iterator<Integer> iterator() {
      return new BoundaryIterator(this.breakIter, this.direction, this.startIdx);
    }
  }

  class BoundaryIterator implements Iterator<Integer> {
    BreakIterator breakIter;
    IterationDirection direction;
    int currIdx;

    BoundaryIterator(BreakIterator breakIter, IterationDirection direction, int startIdx) {
      this.breakIter = breakIter;
      this.direction = direction;

      // TODO(ICU-22987): Remove after fixing preceding(int) to return `DONE` for negative inputs
      if (startIdx < 0 && direction == IterationDirection.BACKWARDS) {
        this.currIdx = BreakIterator.DONE;
        return;
      }

      if (direction == IterationDirection.FORWARDS) {
        this.currIdx = breakIter.following(startIdx);
      } else {
        assert direction == IterationDirection.BACKWARDS;
        this.currIdx = breakIter.preceding(startIdx);
      }
    }

    @Override
    public boolean hasNext() {
      return this.currIdx != BreakIterator.DONE;
    }

    @Override
    public Integer next() {
      int result = this.currIdx;

      if (direction == IterationDirection.FORWARDS) {
        this.currIdx = breakIter.next();
      } else {
        assert direction == IterationDirection.BACKWARDS;
        this.currIdx = breakIter.previous();
      }

      return result;
    }
  }

}
