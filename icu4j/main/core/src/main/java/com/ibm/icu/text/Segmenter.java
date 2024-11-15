package com.ibm.icu.text;

public interface Segmenter {
  Segments segment(String s);

  @Deprecated
  BreakIterator getNewBreakIterator();

  public enum SegmentationType {
    GRAPHEME_CLUSTER,
    WORD,
    LINE,
    SENTENCE,
  }
}
