package com.ibm.icu.text;

import java.util.stream.Stream;

public class RuleBasedSegmenter implements Segmenter {

  private String rules;


  @Override
  public Segments segment(String s) {
    return new RuleBasedSegments(s, this);
  }

  public String getRules() {
    return this.rules;
  }

  public static Builder builder() {
    return new Builder();
  }

  RuleBasedSegmenter(String rules) {
    this.rules = rules;
  }

  RuleBasedBreakIterator getBreakIterator() {
    return new RuleBasedBreakIterator(this.rules);
  }

  public static class Builder {

    String rules;

    Builder() { }

    public Builder setRules(String rules) {
      this.rules = rules;
      return this;
    }

    public RuleBasedSegmenter build() {
      return new RuleBasedSegmenter(this.rules);
    }
  }

  public static class RuleBasedSegments implements Segments {
    private String source;

    private RuleBasedSegmenter segmenter;

    @Override
    public Stream<SegmentRange> ranges() {
      RuleBasedBreakIterator breakIter = this.segmenter.getBreakIterator();
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

    RuleBasedSegments(String source, RuleBasedSegmenter segmenter) {
      this.source = source;
      this.segmenter = segmenter;
    }

    @Override
    public String getSourceString() {
      return this.source;
    }
  }
}
