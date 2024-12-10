package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Segments {

  String getSourceString();

  @Deprecated
  Segmenter getSegmenter();

  @Deprecated
  BreakIterator getInstanceBreakIterator();

  default Stream<CharSequence> subSequences() {
    return ranges().map(rangeToSequenceFn());
  }

  default Stream<Range> ranges() {
    return rangesAfterIndex(-1);
  };

  default Stream<Range> rangesAfterIndex(int i) {
    BreakIterator breakIter = getInstanceBreakIterator();
    breakIter.setText(getSourceString());

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    RangeIterable iterable = new RangeIterable(breakIter, IterationDirection.FORWARDS, i);
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  default Stream<Range> rangesBeforeIndex(int i) {
    BreakIterator breakIter = getInstanceBreakIterator();
    breakIter.setText(getSourceString());

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    RangeIterable iterable = new RangeIterable(breakIter, IterationDirection.BACKWARDS, i);
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  default Range rangeAfterIndex(int i) {
    BreakIterator breakIter = getInstanceBreakIterator();
    breakIter.setText(getSourceString());

    int start = breakIter.following(i);
    if (start == BreakIterator.DONE) {
      return null;
    }

    int limit = breakIter.next();

    return new Range(start, limit);
  }

  default Range rangeBeforeIndex(int i) {
    BreakIterator breakIter = getInstanceBreakIterator();
    breakIter.setText(getSourceString());


    // TODO(ICU-22987): Remove after fixing preceding(int) to return `DONE` for negative inputs
    if (i < 0) {
      // return the same thing as we would if preceding() returned DONE
      return null;
    }

    int start = breakIter.preceding(i);
    int limit = breakIter.previous();

    if (start == BreakIterator.DONE || limit == BreakIterator.DONE) {
      return null;
    }

    assert limit <= start;

    return new Range(limit, start);
  }

  default Function<Range, CharSequence> rangeToSequenceFn() {
    return range -> getSourceString().subSequence(range.getStart(), range.getLimit());
  }

  class Range {
    int start;
    int limit;

    public Range(int start, int limit) {
      this.start = start;
      this.limit = limit;
    }

    public int getStart() {
      return start;
    }

    public int getLimit() {
      return limit;
    }
  }

  enum IterationDirection {
    FORWARDS,
    BACKWARDS,
  }

  /**
   * This {@code Iterable} exists to enable the creation of a {@code Spliterator} that in turn
   * enables the creation of a lazy {@code Stream}.
   */
  class RangeIterable implements Iterable<Range> {
    BreakIterator breakIter;
    IterationDirection direction;
    int startIdx;

    RangeIterable(BreakIterator breakIter, IterationDirection direction, int startIdx) {
      this.breakIter = breakIter;
      this.direction = direction;
      this.startIdx = startIdx;
    }
    @Override
    public Iterator<Range> iterator() {
      return new RangeIterator(this.breakIter, this.direction, this.startIdx);
    }
  }

  class RangeIterator implements Iterator<Range> {
    BreakIterator breakIter;
    IterationDirection direction;
    int startIdx; // remove this if not needed
    int start;
    int limit;

    RangeIterator(BreakIterator breakIter, IterationDirection direction, int startIdx) {
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
    public Range next() {
      Range result;
      if (this.limit < this.start) {
        result = new Range(this.limit, this.start);
      } else {
        result = new Range(this.start, this.limit);
      }

      this.start = this.limit;
      this.limit = getDirectionBasedNextIdx();

      return result;
    }

  }
}
