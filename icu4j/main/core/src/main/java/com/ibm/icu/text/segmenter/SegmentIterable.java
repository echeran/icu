package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.segmenter.Segments.IterationDirection;
import java.util.Iterator;

/**
 * This {@code Iterable} exists to enable the creation of a {@code Spliterator} that in turn
 * enables the creation of a lazy {@code Stream}.
 */
class SegmentIterable implements Iterable<Segment> {
  BreakIterator breakIter;
  final IterationDirection direction;
  int startIdx;
  final CharSequence source;

  SegmentIterable(BreakIterator breakIter, IterationDirection direction, int startIdx, CharSequence source) {
    this.breakIter = breakIter;
    this.direction = direction;
    this.startIdx = startIdx;
    this.source = source;
  }

  @Override
  public Iterator<Segment> iterator() {
    return new SegmentIterator(this.breakIter, this.direction, this.startIdx, this.source);
  }
}

