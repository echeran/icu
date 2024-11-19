package com.ibm.icu.text;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Segments {

  String getSourceString();

  @Deprecated
  Segmenter getSegmenter();

  @Deprecated
  BreakIterator getInstanceBreakIterator();

  default Stream<CharSequence> subSequences() {
    return ranges().map((range) -> getSourceString().subSequence(range.getStart(), range.getLimit()));
  }

  default Stream<Range> ranges() {
    BreakIterator breakIter = getInstanceBreakIterator();
    breakIter.setText(getSourceString());

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    RangeIterable iterable = new RangeIterable(breakIter);
    return StreamSupport.stream(iterable.spliterator(), false);
  };

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

  /**
   * This {@code Iterable} exists to enable the creation of a {@code Spliterator} that in turn
   * enables the creation of a lazy {@code Stream}.
   */
  class RangeIterable implements Iterable<Range> {
    BreakIterator breakIter;

    RangeIterable(BreakIterator breakIter) {
      this.breakIter = breakIter;
    }
    @Override
    public Iterator<Range> iterator() {
      return new RangeIterator(this.breakIter);
    }
  }

  class RangeIterator implements Iterator<Range> {
    BreakIterator breakIter;
    int start;
    int limit;

    RangeIterator(BreakIterator breakIter) {
      this.breakIter = breakIter;
      this.start = breakIter.first();
      this.limit = breakIter.next();
    }

    @Override
    public boolean hasNext() {
      return this.limit != BreakIterator.DONE;
    }

    @Override
    public Range next() {
      Range result = new Range(this.start, this.limit);
      this.start = this.limit;
      this.limit = this.breakIter.next();

      return result;
    }

  }
}
