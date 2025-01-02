package com.ibm.icu.text.segmenter;

public class RuleBasedSegmenterBuilder {

  String rules;

  public RuleBasedSegmenterBuilder() { }

  public RuleBasedSegmenterBuilder setRules(String rules) {
    this.rules = rules;
    return this;
  }

  public RuleBasedSegmenter build() {
    return new RuleBasedSegmenter(this.rules);
  }
}
