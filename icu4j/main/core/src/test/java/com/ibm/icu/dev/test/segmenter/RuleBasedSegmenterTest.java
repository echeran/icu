// © 2025 and later: Unicode, Inc. and others.
// License & terms of use: https://www.unicode.org/copyright.html

package com.ibm.icu.dev.test.segmenter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.ibm.icu.dev.test.CoreTestFmwk;
import com.ibm.icu.segmenter.RuleBasedSegmenter;
import com.ibm.icu.segmenter.Segmenter;
import com.ibm.icu.segmenter.Segments;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RuleBasedSegmenterTest extends CoreTestFmwk {

  @Test
  public void testRules() {
    Object[][] casesData = {
        {"default",
            "hejsan k:a tack",
            ".*;",
            Arrays.asList("hejsan k:a tack")},
        // TODO: add more cases once RBBI rule syntax is understood
    };

    for (Object[] caseDatum : casesData) {
      String desc = (String) caseDatum[0];
      String source = (String) caseDatum[1];
      String rule = (String) caseDatum[2];
      List<CharSequence> expWords = (List<CharSequence>) caseDatum[3];

      // the following rule substring was taken as a subset from BreakIteratorRules_en_US_TEST.java:

      Segmenter seg = RuleBasedSegmenter.builder()
          .setRules(rule)
          .build();
      Segments segments = seg.segment(source);

      List<CharSequence> actWords = segments.subSequences().collect(Collectors.toList());

      assertThat(desc, actWords, is(expWords));
    }

  }

}
