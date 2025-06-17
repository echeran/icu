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
 * same guidelines as for {@link RuleBasedBreakIterator#RuleBasedBreakIterator(String)}.
 * @draft ICU 78
 */
public class RuleBasedSegmenter implements Segmenter {

  private final BreakIterator breakIterPrototype;

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
    breakIterPrototype = breakIter;
  }

  private BreakIterator getNewBreakIterator() {
    return (BreakIterator) breakIterPrototype.clone();
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

    private BreakIterator breakIterPrototype;

    private RuleBasedSegments(RuleBasedSegmenter segmenter, CharSequence source) {
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
      breakIterPrototype = segmenter.getNewBreakIterator();
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
