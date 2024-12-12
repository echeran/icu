package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.BreakIterator;

public interface Segmenter {
  Segments segment(CharSequence s);

  @Deprecated
  BreakIterator getNewBreakIterator();

  public enum SegmentationType {
    GRAPHEME_CLUSTER,
    WORD,
    LINE,
    SENTENCE,
  }
}
