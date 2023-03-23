package de.mindscan.futuresqr.domain.incubator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;

public class UnifiedDiffCalculationV1Test {

    @Test
    public void testSquashDiffs_emptySelections_returnsEmptyFileChangeSetInFullChangeSet() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = new ArrayList<>();

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getFileChangeSet(), empty() );
    }

}
