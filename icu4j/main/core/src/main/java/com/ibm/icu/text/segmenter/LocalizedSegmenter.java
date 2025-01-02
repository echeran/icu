package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LocalizedSegmenter implements Segmenter {

  private ULocale locale;

  private SegmentationType segmentationType;

  @Override
  public Segments segment(CharSequence s) {
    return new LocalizedSegments(s, this);
  }

  LocalizedSegmenter(ULocale locale, SegmentationType segmentationType) {
    this.locale = locale;
    this.segmentationType = segmentationType;
  }

  /**
   * @internal
   * @deprecated This API is ICU internal only.
   */
  @Override
  @Deprecated
  public BreakIterator getNewBreakIterator() {
    BreakIterator breakIter;
    switch (this.segmentationType) {
      case LINE:
        breakIter = BreakIterator.getLineInstance(this.locale);
        break;
      case SENTENCE:
        breakIter = BreakIterator.getSentenceInstance(this.locale);
        break;
      case WORD:
        breakIter = BreakIterator.getWordInstance(this.locale);
        break;
      case GRAPHEME_CLUSTER:
      default:
        breakIter = BreakIterator.getCharacterInstance(this.locale);
        break;
    }
    return breakIter;
  }

  public enum SegmentationType {
    GRAPHEME_CLUSTER,
    WORD,
    LINE,
    SENTENCE,
  }

  public class LocalizedSegments implements Segments {

    private CharSequence source;

    private LocalizedSegmenter segmenter;

    private BreakIterator breakIter;

    private LocalizedSegments(CharSequence source, LocalizedSegmenter segmenter) {
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
    public IntStream boundariesAfterIndex(int i) {
      return SegmentsImplUtils.boundariesAfterIndex(this.breakIter, this.source, i);
    }

    @Override
    public IntStream boundariesBeforeIndex(int i) {
      return SegmentsImplUtils.boundariesBeforeIndex(this.breakIter, this.source, i);
    }
  }

}
