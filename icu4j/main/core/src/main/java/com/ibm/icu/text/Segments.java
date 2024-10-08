package com.ibm.icu.text;

import java.util.stream.Stream;

public interface Segments {

  String getSourceString();

  default Stream<CharSequence> subSequences() {
    return ranges().map((range) -> getSourceString().subSequence(range.getStart(), range.getLimit()));
  }

  Stream<SegmentRange> ranges();

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

    public int getLimit(){
      return limit;
    }
  }

}
