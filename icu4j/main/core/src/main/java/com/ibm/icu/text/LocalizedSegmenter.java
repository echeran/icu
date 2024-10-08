package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;

public class LocalizedSegmenter implements Segmenter {

  private ULocale locale;

  private SegmentationType segmentationType;

  public enum SegmentationType {
    CHARACTER,
    WORD,
    LINE,
    SENTENCE,
    // TITLE,
    // COUNT
  }

  public Builder builder() {
    return new Builder();
  }

  LocalizedSegmenter(ULocale locale, SegmentationType segmentationType) {
    this.locale = locale;
    this.segmentationType = segmentationType;
  }

  public static class Builder {

    private ULocale locale = ULocale.ROOT;

    private SegmentationType segmentationType = SegmentationType.CHARACTER;

    public Builder() {
    }

    public Builder setLocale(ULocale locale) {
      this.locale = locale;
      return this;
    }

    public Builder setSegmentationType(SegmentationType segmentationType) {
      this.segmentationType = segmentationType;
      return this;
    }

    public LocalizedSegmenter build() {
      return new LocalizedSegmenter(locale, segmentationType);
    }

  }

}
