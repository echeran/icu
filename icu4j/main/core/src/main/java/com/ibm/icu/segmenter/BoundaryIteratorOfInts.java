// © 2025 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package com.ibm.icu.segmenter;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.segmenter.Segments.IterationDirection;

/**
 * An iterator of segmentation boundaries that can operate in either the forwards or reverse
 * direction.
 *
 * <p>When constructed to operate in the forwards direction, the iterator will return all boundaries
 * that are strictly after the input index value provided to the constructor. However, when
 * constructed to operate in the backwards direction, if the input index is already a segmentation
 * boundary, then it will be included as the first value that the iterator returns as it iterates
 * backwards.
 */
class BoundaryIteratorOfInts {
  private BreakIterator breakIter;
  private IterationDirection direction;
  private int currIdx;

  BoundaryIteratorOfInts(BreakIterator breakIter, CharSequence sourceSequence, IterationDirection direction, int startIdx) {
    this.breakIter = breakIter;
    this.direction = direction;

    if (direction == IterationDirection.FORWARDS) {
      currIdx = breakIter.following(startIdx);
    } else {
      assert direction == IterationDirection.BACKWARDS;

      // When iterating backwards over boundaries, adjust the start index forwards by 1 to
      // counteract the behavior from BreakIterator.preceding(), which we use to initialize the
      // BreakIterator state, that always moves backwards by at least 1. We want to support an
      // API that includes the input index when it is itself a boundary, unlike the behavior of
      // BreakIterator.preceding().
      //
      // Note: we have to set the initial index indirectly because there is no way to statelessly
      // query whether an index is on a boundary. Instead, BreakIterator.isBoundary() will mutate
      // state when the input is not on a boundary, before it returns the value indicating a
      // boundary.
      int sourceLength = sourceSequence.length();
      boolean isOnBoundary =
          0 <= startIdx
          && startIdx <= sourceLength
          && breakIter.isBoundary(startIdx);
      int backFromIdx = isOnBoundary ? startIdx + 1 : startIdx;

      this.currIdx = breakIter.preceding(backFromIdx);
    }
  }

  public boolean hasNext() {
    return currIdx != BreakIterator.DONE;
  }

  public Integer next() {
    int result = currIdx;

    if (direction == IterationDirection.FORWARDS) {
      currIdx = breakIter.next();
    } else {
      assert direction == IterationDirection.BACKWARDS;
      currIdx = breakIter.previous();
    }

    return result;
  }
}
