// © 2025 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package com.ibm.icu.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.RuleBasedBreakIterator;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Performs segmentation according to the provided rule string. The rule string must follow the
 * same guidelines as for {@link RuleBasedBreakIterator#getInstanceFromCompiledRules(InputStream)}.
 * @draft ICU 78
 */
public class RuleBasedSegmenter implements Segmenter {

  private final BreakIterator prototypeRbbi;

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
    return new RuleBasedSegments(this, s);
  }

  /**
   * @return a builder for constructing {@code RuleBasedSegmenter}
   * @draft ICU 78
   */
  public static Builder builder() {
    return new Builder();
  }

  private RuleBasedSegmenter(BreakIterator breakIter) {
    this.prototypeRbbi = breakIter;
  }

  private BreakIterator getNewBreakIterator() {
    return (BreakIterator) prototypeRbbi.clone();
  }

  /**
   * Builder for {@link RuleBasedSegmenter}
   * @draft ICU 78
   */
  public static class Builder {

    private BreakIterator breakIter = null;

    private Builder() { }

    /**
     * Sets the rule string for segmentation.
     * @param rules rule string.  The rule string must follow the same guidelines as for
     *     {@link RuleBasedBreakIterator#getInstanceFromCompiledRules(InputStream)}.
     * @draft ICU 78
     */
    public Builder setRules(String rules) {
      if (rules == null) {
        throw new IllegalArgumentException("rules cannot be set to null.");
      }
      try {
        breakIter = new RuleBasedBreakIterator(rules);
        return this;
      } catch (RuntimeException rte) {
        throw new IllegalArgumentException("The provided rule string is invalid"
            + " or there was an error in creating the RuleBasedSegmenter.", rte);
      }
    }

    /**
     * Builds the {@code Segmenter}
     * @return the constructed {@code Segmenter} instance
     * @draft ICU 78
     */
    public Segmenter build() {
      if (breakIter == null) {
        throw new IllegalArgumentException("A rule string must be set.");
      } else {
        return new RuleBasedSegmenter(breakIter);
      }
    }
  }

  private class RuleBasedSegments implements Segments {
    private CharSequence source;

    private RuleBasedSegmenter segmenter;

    private BreakIterator breakIter;

    private RuleBasedSegments(RuleBasedSegmenter segmenter, CharSequence source) {
      this.source = source;
      this.segmenter = segmenter;
      this.breakIter = this.segmenter.getNewBreakIterator();

      this.breakIter.setText(source);
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
