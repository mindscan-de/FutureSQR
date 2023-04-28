package de.mindscan.futuresqr.domain.incubator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.changeset.FSqrFileContentChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;

public class DiffPatcherAlgorithmV1Test {

    @Test
    public void testIsPatchCollision_leftBeforeRight1to5_7to9_IsNotACollision() throws Exception {
        // arrange
        DiffPatcherAlgorithmV1 algorithmV1 = new DiffPatcherAlgorithmV1();
        ScmFileContentChangeSet lscm = new ScmFileContentChangeSet();
        // left diff on right side 1,2,3,4,5
        lscm.diffRightLineCountStart = 1;
        lscm.diffRightLineCountDelta = 5;

        ScmFileContentChangeSet rscm = new ScmFileContentChangeSet();
        // right diff on left side 7,8,9
        rscm.diffLeftLineCountStart = 7;
        rscm.diffLeftLineCountDelta = 3;

        // act
        boolean result = algorithmV1.isPatchCollision( new FSqrFileContentChangeSet( lscm ), new FSqrFileContentChangeSet( rscm ) );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testIsPatchCollision_leftBeforeRightAdjacent1to5_6to8_IsNotACollision() throws Exception {
        // arrange
        DiffPatcherAlgorithmV1 algorithmV1 = new DiffPatcherAlgorithmV1();
        ScmFileContentChangeSet lscm = new ScmFileContentChangeSet();
        // left diff on right side 1,2,3,4,5
        lscm.diffRightLineCountStart = 1;
        lscm.diffRightLineCountDelta = 5;

        ScmFileContentChangeSet rscm = new ScmFileContentChangeSet();
        // right diff on left side 6,7,8
        rscm.diffLeftLineCountStart = 6;
        rscm.diffLeftLineCountDelta = 3;

        // act
        boolean result = algorithmV1.isPatchCollision( new FSqrFileContentChangeSet( lscm ), new FSqrFileContentChangeSet( rscm ) );

        // assert
        assertThat( result, equalTo( false ) );
    }

}
