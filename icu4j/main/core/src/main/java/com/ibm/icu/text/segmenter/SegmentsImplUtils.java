package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.segmenter.Segments.BoundaryIterable;
import com.ibm.icu.text.segmenter.Segments.IterationDirection;
import com.ibm.icu.text.segmenter.Segments.Segment;
import com.ibm.icu.text.segmenter.Segments.SegmentIterable;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SegmentsImplUtils {

  public static Stream<CharSequence> subSequences(BreakIterator breakIter, CharSequence sourceSequence) {
    return ranges(breakIter, sourceSequence).map(rangeToSequenceFn(sourceSequence));
  }

  public static Stream<Segment> ranges(BreakIterator breakIter, CharSequence sourceSequence) {
    return rangesAfterIndex(breakIter, sourceSequence, -1);
  };

  public static Stream<Segment> rangesAfterIndex(BreakIterator breakIter, CharSequence sourceSequence, int i) {
    breakIter.setText(sourceSequence);

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    SegmentIterable iterable = new SegmentIterable(breakIter, IterationDirection.FORWARDS, i);
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  public static Stream<Segment> rangesBeforeIndex(BreakIterator breakIter, CharSequence sourceSequence, int i) {
     breakIter.setText(sourceSequence);

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    SegmentIterable iterable = new SegmentIterable(breakIter, IterationDirection.BACKWARDS, i);
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  public static Segment rangeAfterIndex(BreakIterator breakIter, CharSequence sourceSequence, int i) {
    breakIter.setText(sourceSequence);

    int start = breakIter.following(i);
    if (start == BreakIterator.DONE) {
      return null;
    }

    int limit = breakIter.next();
    if (limit == BreakIterator.DONE) {
      return null;
    }

    return new Segment(start, limit);
  }

  public static Segment rangeBeforeIndex(BreakIterator breakIter, CharSequence sourceSequence, int i) {
    breakIter.setText(sourceSequence);


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

    return new Segment(limit, start);
  }

  public static Function<Segment, CharSequence> rangeToSequenceFn(CharSequence sourceSequence) {
    return segment -> sourceSequence.subSequence(segment.start, segment.limit);
  }

  public static IntStream boundaries(BreakIterator breakIter, CharSequence sourceSequence) {
    return boundariesAfterIndex(breakIter, sourceSequence, -1);
  }

  public static IntStream boundariesAfterIndex(BreakIterator breakIter, CharSequence sourceSequence, int i) {
    breakIter.setText(sourceSequence);

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    BoundaryIterable iterable = new BoundaryIterable(breakIter, IterationDirection.FORWARDS, i);
    Stream<Integer> boundariesAsIntegers =  StreamSupport.stream(iterable.spliterator(), false);
    return boundariesAsIntegers.mapToInt(Integer::intValue);
  }

  public static IntStream boundariesBeforeIndex(BreakIterator breakIter, CharSequence sourceSequence, int i) {
    breakIter.setText(sourceSequence);

    // create a Stream from a Spliterator of an Iterable so that the Stream can be lazy, not eager
    BoundaryIterable iterable = new BoundaryIterable(breakIter, IterationDirection.BACKWARDS, i);
    Stream<Integer> boundariesAsIntegers =  StreamSupport.stream(iterable.spliterator(), false);
    return boundariesAsIntegers.mapToInt(Integer::intValue);
  }

}
