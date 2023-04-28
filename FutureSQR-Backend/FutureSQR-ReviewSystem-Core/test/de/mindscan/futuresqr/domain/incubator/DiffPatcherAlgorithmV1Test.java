package de.mindscan.futuresqr.domain.incubator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.changeset.FSqrFileContentChangeSet;
import de.mindscan.futuresqr.scmaccess.types.ScmFileContentChangeSet;

public class DiffPatcherAlgorithmV1Test {

    @Test
    public void testIsPatchCollision_leftBeforeRight_IsNotACollision() throws Exception {
        // arrange
        DiffPatcherAlgorithmV1 algorithmV1 = new DiffPatcherAlgorithmV1();
        ScmFileContentChangeSet lscm = new ScmFileContentChangeSet();
        // 1,2,3,4,5
        lscm.diffRightLineCountStart = 1;
        lscm.diffRightLineCountDelta = 5;

        ScmFileContentChangeSet rscm = new ScmFileContentChangeSet();
        // 7,8,9,10,11
        rscm.diffLeftLineCountStart = 7;
        rscm.diffLeftLineCountDelta = 5;

        // act
        boolean result = algorithmV1.isPatchCollision( new FSqrFileContentChangeSet( lscm ), new FSqrFileContentChangeSet( rscm ) );

        // assert
        assertThat( result, equalTo( false ) );
    }

}
