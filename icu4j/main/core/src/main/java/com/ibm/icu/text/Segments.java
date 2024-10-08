package com.ibm.icu.text;

import java.util.stream.Stream;

public class Segments {

  private String sourceString;

  private Segmenter segmenter;

  public Segments(String sourceString, Segmenter segmenter) {
    this.sourceString = sourceString;
    this.segmenter = segmenter;
  }

  public Stream<CharSequence> subSequences() {
    return ranges().map((range) -> sourceString.subSequence(range.getStart(), range.getLimit()));
  }

  public Stream<SegmentRange> ranges() {
    return null;
  }

  public static class SegmentRange {
    int start;
    int limit;

    public SegmentRange(int start, int limit) {
      this.start = start;
      this.limit = limit;
    }

    public int getStart() {
      return start;
    }

    public int getLimit(){
      return limit;
    }
  }

}
