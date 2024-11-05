package com.ibm.icu.text;

public interface Segmenter {
  Segments segment(String s);

  @Deprecated
  BreakIterator getNewBreakIterator();

  public enum SegmentationType {
    CHARACTER,
    WORD,
    LINE,
    SENTENCE,
    // TITLE,
    // COUNT
  }
}
