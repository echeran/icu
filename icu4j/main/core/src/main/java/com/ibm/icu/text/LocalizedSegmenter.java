package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;
import java.util.stream.Stream;

public class LocalizedSegmenter implements Segmenter {

  private ULocale locale;

  private SegmentationType segmentationType;

  @Override
  public Segments segment(String s) {
    return new LocalizedSegments(s, this);
  }

  public ULocale getLocale() {
    return this.locale;
  }

  public SegmentationType getSegmentationType() {
    return this.segmentationType;
  }

  public enum SegmentationType {
    CHARACTER,
    WORD,
    LINE,
    SENTENCE,
    // TITLE,
    // COUNT
  }

  public static Builder builder() {
    return new Builder();
  }

  LocalizedSegmenter(ULocale locale, SegmentationType segmentationType) {
    this.locale = locale;
    this.segmentationType = segmentationType;
  }

  BreakIterator getBreakIterator() {
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
      case CHARACTER:
      default:
        breakIter = BreakIterator.getCharacterInstance(this.locale);
        break;
    }
    return breakIter;
  }

  public static class Builder {

    private ULocale locale = ULocale.ROOT;

    private SegmentationType segmentationType = SegmentationType.CHARACTER;

    Builder() { }

    public Builder setLocale(ULocale locale) {
      this.locale = locale;
      return this;
    }

    public Builder setSegmentationType(SegmentationType segmentationType) {
      this.segmentationType = segmentationType;
      return this;
    }

    public LocalizedSegmenter build() {
      return new LocalizedSegmenter(this.locale, this.segmentationType);
    }

  }

  public static class LocalizedSegments implements Segments {

    private String source;

    private LocalizedSegmenter segmenter;

    private LocalizedSegments(String source, LocalizedSegmenter segmenter) {
      this.source = source;
      this.segmenter = segmenter;
    }

    @Override
    public String getSourceString() {
      return source;
    }

    @Override
    public Stream<SegmentRange> ranges() {
      BreakIterator breakIter = this.segmenter.getBreakIterator();
      breakIter.setText(this.source);

      int start = breakIter.first();
      int limit = breakIter.next();
      if (limit == BreakIterator.DONE) {
        return Stream.empty();
      } else {
        Stream.Builder<SegmentRange> streamBuilder = Stream.builder();
        while (limit != BreakIterator.DONE) {
          SegmentRange range = new SegmentRange(start, limit);
          streamBuilder.add(range);
          start = limit;
          limit = breakIter.next();
        }
        return streamBuilder.build();
      }
    }
  }

}
