package de.mindscan.futuresqr.domain.repository.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

public class InMemoryCacheProjectConfigurationTableImplTest {

    private static final String PROJECT_A_IDENTIFIER = "project-a";

    @Test
    public void testInMemoryCacheProjectConfigurationTybleImpl() throws Exception {
        // arrange
        // act
        // assert
        InMemoryCacheProjectConfigurationTableImpl configurationCache = new InMemoryCacheProjectConfigurationTableImpl();
    }

    @Test
    public void testIsCached_OnlyCtorRequestUncachedProjectId_returnFalse() throws Exception {
        // arrange
        InMemoryCacheProjectConfigurationTableImpl configurationCache = new InMemoryCacheProjectConfigurationTableImpl();

        // act
        boolean result = configurationCache.isCached( PROJECT_A_IDENTIFIER );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testGetScmConfiguration_OnlyCtorRequestUncachedProjectId_returnNull() throws Exception {
        // arrange
        InMemoryCacheProjectConfigurationTableImpl configurationCache = new InMemoryCacheProjectConfigurationTableImpl();

        // act
        FSqrScmProjectConfiguration result = configurationCache.getScmConfiguration( PROJECT_A_IDENTIFIER );

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testPutProjectConfiguration_SetupProjectRequestSameProject_returnsNotNull() throws Exception {
        // arrange
        InMemoryCacheProjectConfigurationTableImpl configurationCache = new InMemoryCacheProjectConfigurationTableImpl();
        FSqrScmProjectConfiguration expectedScmConfiguration = Mockito.mock( FSqrScmProjectConfiguration.class, "expectedScmConfiguration" );

        // act
        configurationCache.putProjectConfiguration( PROJECT_A_IDENTIFIER, expectedScmConfiguration );

        // assert
        FSqrScmProjectConfiguration result = configurationCache.getScmConfiguration( PROJECT_A_IDENTIFIER );
        assertThat( result, not( nullValue() ) );
    }

    @Test
    public void testPutProjectConfiguration_SetupProjectConfigurationRequestSameProject_returnsSameConfiguration() throws Exception {
        // arrange
        InMemoryCacheProjectConfigurationTableImpl configurationCache = new InMemoryCacheProjectConfigurationTableImpl();
        FSqrScmProjectConfiguration expectedScmConfiguration = Mockito.mock( FSqrScmProjectConfiguration.class, "expectedScmConfiguration" );

        // act
        configurationCache.putProjectConfiguration( PROJECT_A_IDENTIFIER, expectedScmConfiguration );

        // assert
        FSqrScmProjectConfiguration result = configurationCache.getScmConfiguration( PROJECT_A_IDENTIFIER );
        assertThat( result, is( sameInstance( expectedScmConfiguration ) ) );
    }

    @Test
    public void testIsCached_SetupProjectConfiguration_returnsTrue() throws Exception {
        // arrange
        InMemoryCacheProjectConfigurationTableImpl configurationCache = new InMemoryCacheProjectConfigurationTableImpl();
        FSqrScmProjectConfiguration expectedScmConfiguration = Mockito.mock( FSqrScmProjectConfiguration.class, "expectedScmConfiguration" );
        configurationCache.putProjectConfiguration( PROJECT_A_IDENTIFIER, expectedScmConfiguration );

        // act
        boolean result = configurationCache.isCached( PROJECT_A_IDENTIFIER );

        // assert
        assertThat( result, equalTo( true ) );
    }

}
