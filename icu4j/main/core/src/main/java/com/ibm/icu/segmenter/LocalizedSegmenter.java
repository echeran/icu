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

  private BreakIterator breakIterPrototype;

  /**
   * Return a {@link Segments} object that encapsulates the segmentation of the input
   * {@code CharSequence}. The {@code Segments} object, in turn, provides the main APIs to support
   * traversal over the resulting segments and boundaries via the Java {@code Stream} abstraction.
   * @param s input {@code CharSequence} on which segmentation is performed. The input must not be
   *     modified while using the resulting {@code Segments} object.
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
    switch (segmentationType) {
      case LINE:
        breakIterPrototype = BreakIterator.getLineInstance(locale);
        break;
      case SENTENCE:
        breakIterPrototype = BreakIterator.getSentenceInstance(locale);
        break;
      case WORD:
        breakIterPrototype = BreakIterator.getWordInstance(locale);
        break;
      case GRAPHEME_CLUSTER:
        breakIterPrototype = BreakIterator.getCharacterInstance(locale);
        break;
    }
  }

  private BreakIterator getNewBreakIterator() {
    return (BreakIterator) breakIterPrototype.clone();
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

    private BreakIterator breakIterPrototype;

    private LocalizedSegments(LocalizedSegmenter segmenter, CharSequence source) {
      this.source = source;

      // We are creating a clone of the Segmenter's prototype BreakIterator field so that this
      // concrete Segments object can avoid sharing state with the other Segments object instances
      // that get spawned from the Segmenter. This allows difference source CharSequences to be used
      // in each Segments object.
      //
      // In turn, the cloned BreakIterator becomes a prototype to be stored in the Segments object,
      // which then gets cloned and used in each of the Segments APIs' implementations. The second
      // level of cloning that happens when the Segments object's local BreakIterator prototype
      // gets cloned allows the iteration state to be separate whenever an Segments API is called.
      // Otherwise, there is a chance that multiple API calls on the same Segments object might
      // mutate the same position/index, if done concurrently.
      breakIterPrototype = (BreakIterator) segmenter.getNewBreakIterator();
      // It's okay to perform .setText on the object that we want to clone later because we should
      // then not have to call .setText on the clones.
      breakIterPrototype.setText(source);
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
      return SegmentsImplUtils.segmentAt((BreakIterator) breakIterPrototype.clone(), source, i);
    }

    /**
     * Returns a {@code Stream} of all {@code Segment}s in the source sequence. Start with the first
     * and iterate forwards until the end of the sequence.
     * @return a {@code Stream} of all {@code Segments} in the source sequence.
     * @draft ICU 78
     */
    @Override
    public Stream<Segment> segments() {
      return SegmentsImplUtils.segments((BreakIterator) breakIterPrototype.clone(), source);
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
      return SegmentsImplUtils.isBoundary((BreakIterator) breakIterPrototype.clone(), source, i);
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
      return SegmentsImplUtils.segmentsFrom((BreakIterator) breakIterPrototype.clone(), source, i);
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
      return SegmentsImplUtils.segmentsBefore((BreakIterator) breakIterPrototype.clone(), source, i);
    }

    /**
     * Returns all segmentation boundaries, starting from the beginning and moving forwards.
     * @return An {@code IntStream} of all segmentation boundaries, starting at the first
     * boundary with index 0, and moving forwards in the input sequence.
     * @draft ICU 78
     */
    @Override
    public IntStream boundaries() {
      return SegmentsImplUtils.boundaries((BreakIterator) breakIterPrototype.clone(), source);
    }

    /**
     * Returns all segmentation boundaries after the provided index.  Iteration moves forwards.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return An {@code IntStream} of all boundaries {@code b} such that {@code b > i}
     * @draft ICU 78
     */
    @Override
    public IntStream boundariesAfter(int i) {
      return SegmentsImplUtils.boundariesAfter((BreakIterator) breakIterPrototype.clone(), source, i);
    }

    /**
     * Returns all segmentation boundaries on or before the provided index. Iteration moves backwards.
     * @param i index in the input {@code CharSequence} to the {@code Segmenter}
     * @return An {@code IntStream} of all boundaries {@code b} such that {@code b ≤ i}
     * @draft ICU 78
     */
    @Override
    public IntStream boundariesBackFrom(int i) {
      return SegmentsImplUtils.boundariesBackFrom((BreakIterator) breakIterPrototype.clone(), source, i);
    }
  }
}
