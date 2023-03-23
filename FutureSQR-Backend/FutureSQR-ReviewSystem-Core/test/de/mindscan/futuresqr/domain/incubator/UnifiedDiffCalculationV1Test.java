package de.mindscan.futuresqr.domain.incubator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommands;
import de.mindscan.futuresqr.scmaccess.git.processor.GitOutputProcessors;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

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

    @Test
    public void testSquashDiffs_threeRevisionsInRowSelectFirst_returnsFullChangeSetWithFirstRevisionId() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( "1a306de710d0366ab68f3dc77b538600e1891488" );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( "1a306de710d0366ab68f3dc77b538600e1891488",
                        "1921fa018c148056407eeed200a229349daadb9f" );

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getRevisionId(), equalTo( "1a306de710d0366ab68f3dc77b538600e1891488" ) );
    }

    private List<FSqrRevisionFullChangeSet> retrieveRevisions( String firstRevisionId, String lastRevisionId ) {
        ScmRepository repository = new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" );

        FakeGitCLICommandExecutor gitContentProvider = new FakeGitCLICommandExecutor( false );

        List<ScmFullChangeSet> revisionList = gitContentProvider//
                        .execute( repository, GitCommands.createGetDiffBetweenRevisionsCommand( firstRevisionId, lastRevisionId ) ) //
                        .transform( GitOutputProcessors.toScmFullChangeSetList() );

        List<FSqrRevisionFullChangeSet> result = new ArrayList<>();
        revisionList.stream().forEach( changeSet -> result.add( new FSqrRevisionFullChangeSet( changeSet ) ) );

        return result;
    }

}
