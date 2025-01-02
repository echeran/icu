package com.ibm.icu.text.segmenter;

import com.ibm.icu.text.segmenter.LocalizedSegmenter.SegmentationType;
import com.ibm.icu.util.ULocale;

public class LocalizedSegmenterBuilder {

  private ULocale locale = ULocale.ROOT;

  private SegmentationType segmentationType = SegmentationType.GRAPHEME_CLUSTER;

  public LocalizedSegmenterBuilder() { }

  public LocalizedSegmenterBuilder setLocale(ULocale locale) {
    this.locale = locale;
    return this;
  }

  public LocalizedSegmenterBuilder setSegmentationType(SegmentationType segmentationType) {
    this.segmentationType = segmentationType;
    return this;
  }

  public LocalizedSegmenter build() {
    return new LocalizedSegmenter(this.locale, this.segmentationType);
  }
}
