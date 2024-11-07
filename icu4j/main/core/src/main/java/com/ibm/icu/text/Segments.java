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

  default Stream<SegmentRange> ranges() {
    BreakIterator breakIter = getInstanceBreakIterator();
    breakIter.setText(getSourceString());

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    SegmentRangeIterable iterable = new SegmentRangeIterable(breakIter);
    return StreamSupport.stream(iterable.spliterator(), false);
  };

  class SegmentRange {
    int start;
    int limit;

    public SegmentRange(int start, int limit) {
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
  class SegmentRangeIterable implements Iterable<SegmentRange> {
    BreakIterator breakIter;

    SegmentRangeIterable(BreakIterator breakIter) {
      this.breakIter = breakIter;
    }
    @Override
    public Iterator<SegmentRange> iterator() {
      return new SegmentRangeIterator(this.breakIter);
    }
  }

  class SegmentRangeIterator implements Iterator<SegmentRange> {
    BreakIterator breakIter;
    int start;
    int limit;

    SegmentRangeIterator(BreakIterator breakIter) {
      this.breakIter = breakIter;
      this.start = breakIter.first();
      this.limit = breakIter.next();
    }

    @Override
    public boolean hasNext() {
      return this.limit != BreakIterator.DONE;
    }

    @Override
    public SegmentRange next() {
      SegmentRange result = new SegmentRange(this.start, this.limit);
      this.start = this.limit;
      this.limit = this.breakIter.next();

      return result;
    }

  }
}
