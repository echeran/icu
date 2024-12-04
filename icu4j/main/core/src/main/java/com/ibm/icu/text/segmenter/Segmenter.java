package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;

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
