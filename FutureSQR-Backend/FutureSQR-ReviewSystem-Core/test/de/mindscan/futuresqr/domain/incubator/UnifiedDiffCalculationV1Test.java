package de.mindscan.futuresqr.domain.incubator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.changeset.FSqrFileChangeSet;
import de.mindscan.futuresqr.domain.model.changeset.FSqrRevisionFullChangeSet;
import de.mindscan.futuresqr.scmaccess.git.command.GitCommands;
import de.mindscan.futuresqr.scmaccess.git.processor.GitOutputProcessors;
import de.mindscan.futuresqr.scmaccess.types.ScmFullChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmRepository;

public class UnifiedDiffCalculationV1Test {

    // UnifiedDiffCalculationV1.java
    private static final String FIRST_OF_THREE_1A306DE7 = "1a306de710d0366ab68f3dc77b538600e1891488";
    private static final String SECOND_OF_THREE_DFB2A463 = "dfb2a46357b1f5cd02407a8ee9dc88ecb24eace8";
    private static final String LAST_OF_THREE_1921FA01 = "1921fa018c148056407eeed200a229349daadb9f";

    // UnifiedDiffCalculationV1.java + FSqrRevisionFullChangeSet.java
    private static final String FIRST_OF_FIVE_1A306DE7 = "1a306de710d0366ab68f3dc77b538600e1891488";
    private static final String SECOND_OF_FIVE_DFB2A463 = "dfb2a46357b1f5cd02407a8ee9dc88ecb24eace8";
    private static final String THIRD_OF_FIVE_1921FA01 = "1921fa018c148056407eeed200a229349daadb9f";
    private static final String FOURTH_OF_FIVE_30370E41 = "30370e41bf66e94f5fb5c1f3b6787f11cdf8b606";
    private static final String LAST_OF_FIVE_EF0B753D = "ef0b753dba814ab41dd11a760c41aaa19d250389";

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
    public void testSquashDiffs_emptyIntermediaRevisions_returnsEmptyFileChangeSetInFullChangeSet() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( FIRST_OF_THREE_1A306DE7 );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = new ArrayList<>();

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getFileChangeSet(), empty() );
    }

    @Test
    public void testSquashDiffs_selectedRevisionHasNullValue_returnsEmptyFileChangeSetInFullChangeSet() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( null );
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
        selectedRevisions.add( FIRST_OF_THREE_1A306DE7 );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( FIRST_OF_THREE_1A306DE7, LAST_OF_THREE_1921FA01 );

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getRevisionId(), equalTo( FIRST_OF_THREE_1A306DE7 ) );
    }

    @Test
    public void testSquashDiffs_threeRevisionsInRowSelectLast_returnsFullChangeSetWithLastRevisionId() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( LAST_OF_THREE_1921FA01 );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( FIRST_OF_THREE_1A306DE7, LAST_OF_THREE_1921FA01 );

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getRevisionId(), equalTo( LAST_OF_THREE_1921FA01 ) );
    }

    @Test
    public void testSquashDiffs_threeRevisionsInRowSelectFirstTwo_returnsFullChangeSetWithSecondRevisionId() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( FIRST_OF_THREE_1A306DE7 );
        selectedRevisions.add( SECOND_OF_THREE_DFB2A463 );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( FIRST_OF_THREE_1A306DE7, LAST_OF_THREE_1921FA01 );

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getRevisionId(), equalTo( SECOND_OF_THREE_DFB2A463 ) );
    }

    @Test
    public void testSquashDiffs_threeRevisionsInRowSelectFirstTwo_expectFullChangeSetHasOneFileChangeSet() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( FIRST_OF_THREE_1A306DE7 );
        selectedRevisions.add( SECOND_OF_THREE_DFB2A463 );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( FIRST_OF_THREE_1A306DE7, LAST_OF_THREE_1921FA01 );

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );

        // assert
        assertThat( squashedDiffs.getFileChangeSet(), hasSize( 1 ) );
    }

    @Test
    public void testSquashDiffs_threeRevisionsInRowSelectFirstTwo_expectFullChangeSetHasThreeContentChangesetsInOnlyFileChangeset() throws Exception {
        // arrange
        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
        List<String> selectedRevisions = new ArrayList<>();
        // first revision.
        selectedRevisions.add( FIRST_OF_THREE_1A306DE7 );
        selectedRevisions.add( SECOND_OF_THREE_DFB2A463 );
        Collection<String> filterRevisions = null;
        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( FIRST_OF_THREE_1A306DE7, LAST_OF_THREE_1921FA01 );

        // act
        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );
        FSqrFileChangeSet fileChangeSet = squashedDiffs.getFileChangeSet().get( 0 );

        // assert
        assertThat( fileChangeSet.getFileContentChangeSet(), hasSize( 3 ) );
    }

// TODO: do some approval tests, for the output list for the file change sets and their content changesets.    
//    @Test
//    public void testSquashDiffs_threeRevisionsInRowSelectFirstTwo_expectFullChangeSetHasThreeContentChangesetsInOnlyFileChangeset() throws Exception {
//        // arrange
//        UnifiedDiffCalculationV1 diffCalculation = new UnifiedDiffCalculationV1();
//        List<String> selectedRevisions = new ArrayList<>();
//        // first revision.
//        selectedRevisions.add( FIRST_OF_THREE_1A306DE7 );
//        selectedRevisions.add( SECOND_OF_THREE_DFB2A463 );
//        Collection<String> filterRevisions = null;
//        List<FSqrRevisionFullChangeSet> intermediateRevisions = retrieveRevisions( FIRST_OF_THREE_1A306DE7, LAST_OF_THREE_1921FA01 );
//
//        // act
//        FSqrRevisionFullChangeSet squashedDiffs = diffCalculation.squashDiffs( intermediateRevisions, filterRevisions, selectedRevisions );
//        FSqrFileChangeSet fileChangeSet = squashedDiffs.getFileChangeSet().get( 0 );
//
//        // contentchangeset #1  -95,8 / +95,9
//        // contentchangeset #2  -200,6 / +201,10
//        // contentchangeset #3  -211,8 / +216,9  <<-- this one will fail because we currently don't correct for the truth, without truth correction: 211,9
//        
//        // assert
//        assertThat( fileChangeSet.getFileContentChangeSet(), hasSize( 3 ) );
//    }

    private List<FSqrRevisionFullChangeSet> retrieveRevisions( String firstRevisionId, String lastRevisionId ) {
        ScmRepository repository = new ScmRepository( "D:\\Temp\\future-square-cache\\FutureSQR" );

        FakeGitCLICommandExecutor gitContentProvider = new FakeGitCLICommandExecutor( true );

        List<ScmFullChangeSet> revisionList = gitContentProvider//
                        .execute( repository, GitCommands.createGetDiffBetweenRevisionsCommand( firstRevisionId, lastRevisionId ) ) //
                        .transform( GitOutputProcessors.toScmFullChangeSetList() );

        List<FSqrRevisionFullChangeSet> result = new ArrayList<>();
        revisionList.stream().forEach( changeSet -> result.add( new FSqrRevisionFullChangeSet( changeSet ) ) );

        return result;
    }

}
