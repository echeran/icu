package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.RuleBasedBreakIterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RuleBasedSegmenter implements Segmenter {

  private String rules;

  @Override
  public Segments segment(CharSequence s) {
    return new RuleBasedSegments(s, this);
  }

  RuleBasedSegmenter(String rules) {
    this.rules = rules;
  }

  /**
   * @internal
   * @deprecated This API is ICU internal only.
   */
  @Override
  @Deprecated
  public RuleBasedBreakIterator getNewBreakIterator() {
    return new RuleBasedBreakIterator(this.rules);
  }

  public static class RuleBasedSegments implements Segments {
    private CharSequence source;

    private RuleBasedSegmenter segmenter;

    private BreakIterator breakIter;

    RuleBasedSegments(CharSequence source, RuleBasedSegmenter segmenter) {
      this.source = source;
      this.segmenter = segmenter;
      this.breakIter = this.segmenter.getNewBreakIterator();
    }

    @Override
    public Stream<CharSequence> subSequences() {
      return SegmentsImplUtils.subSequences(this.breakIter, this.source);
    }

    @Override
    public Stream<Segment> ranges() {
      return SegmentsImplUtils.ranges(this.breakIter, this.source);
    }

    @Override
    public boolean isBoundary(int i) {
      return SegmentsImplUtils.isBoundary(this.breakIter, this.source, i);
    }

    @Override
    public Stream<Segment> rangesAfterIndex(int i) {
      return SegmentsImplUtils.rangesAfterIndex(this.breakIter, this.source, i);
    }

    @Override
    public Stream<Segment> rangesBeforeIndex(int i) {
      return SegmentsImplUtils.rangesBeforeIndex(this.breakIter, this.source, i);
    }

    @Override
    public Segment rangeAfterIndex(int i) {
      return SegmentsImplUtils.rangeAfterIndex(this.breakIter, this.source, i);
    }

    @Override
    public Segment rangeBeforeIndex(int i) {
      return SegmentsImplUtils.rangeBeforeIndex(this.breakIter, this.source, i);
    }

    @Override
    public Function<Segment, CharSequence> rangeToSequenceFn() {
      return SegmentsImplUtils.rangeToSequenceFn(this.source);
    }

    @Override
    public IntStream boundaries() {
      return SegmentsImplUtils.boundaries(this.breakIter, this.source);
    }

    @Override
    public IntStream boundariesAfter(int i) {
      return SegmentsImplUtils.boundariesAfter(this.breakIter, this.source, i);
    }

    @Override
    public IntStream boundariesBackFrom(int i) {
      return SegmentsImplUtils.boundariesBackFrom(this.breakIter, this.source, i);
    }
  }
}
