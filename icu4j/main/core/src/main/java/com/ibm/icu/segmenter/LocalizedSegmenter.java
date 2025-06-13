// © 2025 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package com.ibm.icu.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Performs segmentation according to the rules defined for the locale.
 */
public class LocalizedSegmenter implements Segmenter {

  private ULocale locale;

  private SegmentationType segmentationType;

  /**
   * Return a {@link Segments} object that encapsulates the segmentation of the input
   * {@code CharSequence}. The {@code Segments} object, in turn, provides the main APIs to support
   * traversal over the resulting segments and boundaries via the Java {@code Stream} abstraction.
   * @param s input {@code CharSequence} on which segmentation is performed
   * @return A {@code Segments} object with APIs to access the results of segmentation, including
   *     APIs that return {@code Stream}s of the segments and boundaries.
   * @draft ICU 78
   */
  @Override
  public Segments segment(CharSequence s) {
    return new LocalizedSegments(this, s);
  }

  /**
   * @return a builder for constructing {@code LocalizedSegmenter}
   * @draft ICU 78
   */
  public static Builder builder() {
    return new Builder();
  }

  private LocalizedSegmenter(ULocale locale, SegmentationType segmentationType) {
    this.locale = locale;
    this.segmentationType = segmentationType;
  }

  private BreakIterator getNewBreakIterator() {
    BreakIterator breakIter;
    switch (segmentationType) {
      case LINE:
        breakIter = BreakIterator.getLineInstance(locale);
        break;
      case SENTENCE:
        breakIter = BreakIterator.getSentenceInstance(locale);
        break;
      case WORD:
        breakIter = BreakIterator.getWordInstance(locale);
        break;
      case GRAPHEME_CLUSTER:
      default:
        breakIter = BreakIterator.getCharacterInstance(locale);
        break;
    }
    return breakIter;
  }

  /**
   * The type of segmentation to be performed. See the ICU User Guide page
   * <a
   * href="https://unicode-org.github.io/icu/userguide/boundaryanalysis/#four-types-of-breakiterator">Boundary Analysis</a>
   * for further details.
   */
  public enum SegmentationType {
    GRAPHEME_CLUSTER,
    WORD,
    LINE,
    SENTENCE,
  }

  /**
   * Builder for {@link LocalizedSegmenter}
   * @draft ICU 78
   */
  public static class Builder {

    private ULocale locale = ULocale.ROOT;

    private SegmentationType segmentationType = null;

    private Builder() { }

    /**
     * Set the locale for which segmentation rules will be loaded
     * @param locale an ICU locale object
     * @draft ICU 78
     */
    public Builder setLocale(ULocale locale) {
      if (locale == null) {
        throw new IllegalArgumentException("locale cannot be set to null.");
      }
      this.locale = locale;
      return this;
    }

    /**
     * Set the locale for which segmentation rules will be loaded
     * @param locale a Java locale object
     * @draft ICU 78
     */
    public Builder setLocale(Locale locale) {
      if (locale == null) {
        throw new IllegalArgumentException("locale cannot be set to null.");
      }
      this.locale = ULocale.forLocale(locale);
      return this;
    }

    /**
     * Set the segmentation type to be performed.
     * @param segmentationType
     * @draft ICU 78
     */
    public Builder setSegmentationType(SegmentationType segmentationType) {
      if (segmentationType == null) {
        throw new IllegalArgumentException("segmentationType cannot be set to null.");
      }
      this.segmentationType = segmentationType;
      return this;
    }

    /**
     * Builds the {@code Segmenter}
     * @return the constructed {@code Segmenter} instance
     * @draft ICU 78
     */
    public Segmenter build() {
      if (segmentationType == null) {
        throw new IllegalArgumentException("segmentationType is null and must be set to a specific value.");
      }
      return new LocalizedSegmenter(locale, segmentationType);
    }

  }

  private class LocalizedSegments implements Segments {

    private CharSequence source;

    private LocalizedSegmenter segmenter;

    private BreakIterator breakIter;

    private LocalizedSegments(LocalizedSegmenter segmenter, CharSequence source) {
      this.source = source;
      this.segmenter = segmenter;
      breakIter = this.segmenter.getNewBreakIterator();

      breakIter.setText(source);
    }

    /**
     * Returns the segment that contains index {@code i}. Containment is inclusive of the start index
     * and exclusive of the limit index.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return A segment that either starts at or contains index {@code i}
     * @draft ICU 78
     */
    @Override
    public Segment segmentAt(int i) {
      return SegmentsImplUtils.segmentAt(breakIter, source, i);
    }

    /**
     * Returns a {@code Stream} of all {@code Segment}s in the source sequence. Start with the first
     * and iterate forwards until the end of the sequence.
     * @return a {@code Stream} of all {@code Segments} in the source sequence.
     * @draft ICU 78
     */
    @Override
    public Stream<Segment> segments() {
      return SegmentsImplUtils.segments(breakIter, source);
    }

    /**
     * Returns whether offset {@code i} is a segmentation boundary. Throws an exception when
     * {@code i} is not a valid index position for the source sequence.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return Returns whether offset {@code i} is a segmentation boundary.
     * @draft ICU 78
     */
    @Override
    public boolean isBoundary(int i) {
      return SegmentsImplUtils.isBoundary(breakIter, source, i);
    }

    /**
     * Returns a {@code Stream} of all {@code Segment}s in the source sequence where all segment limits
     * {@code  l} satisfy {@code i < l}.  Iteration moves forwards.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return a {@code Stream} of all {@code Segment}s at or after {@code i}
     * @draft ICU 78
     */
    @Override
    public Stream<Segment> segmentsFrom(int i) {
      return SegmentsImplUtils.segmentsFrom(breakIter, source, i);
    }

    /**
     * Returns a {@code Stream} of all {@code Segment}s in the source sequence where all segment
     * limits {@code  l} satisfy {@code l ≤ i}. Iteration moves backwards.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return a {@code Stream} of all {@code Segment}s before {@code i}
     * @draft ICU 78
     */
    @Override
    public Stream<Segment> segmentsBefore(int i) {
      return SegmentsImplUtils.segmentsBefore(breakIter, source, i);
    }

    /**
     * Returns all segmentation boundaries, starting from the beginning and moving forwards.
     * @return An {@code IntStream} of all segmentation boundaries, starting at the first
     * boundary with index 0, and moving forwards in the input sequence.
     * @draft ICU 78
     */
    @Override
    public IntStream boundaries() {
      return SegmentsImplUtils.boundaries(breakIter, source);
    }

    /**
     * Returns all segmentation boundaries after the provided index.  Iteration moves forwards.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return An {@code IntStream} of all boundaries {@code b} such that {@code b > i}
     * @draft ICU 78
     */
    @Override
    public IntStream boundariesAfter(int i) {
      return SegmentsImplUtils.boundariesAfter(breakIter, source, i);
    }

    /**
     * Returns all segmentation boundaries on or before the provided index. Iteration moves backwards.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return An {@code IntStream} of all boundaries {@code b} such that {@code b ≤ i}
     * @draft ICU 78
     */
    @Override
    public IntStream boundariesBackFrom(int i) {
      return SegmentsImplUtils.boundariesBackFrom(breakIter, source, i);
    }
  }
}
