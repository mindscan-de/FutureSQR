package de.mindscan.futuresqr.domain.repository.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.mindscan.futuresqr.domain.model.FSqrScmProjectConfiguration;

public class FSqrScmProjectConfigurationRepositoryImplTest {

    @Test
    public void testGetAllProjectConfigurations_Ctor_projectConfigurationsEmpty() throws Exception {
        // arrange
        FSqrScmProjectConfigurationRepositoryImpl repository = new FSqrScmProjectConfigurationRepositoryImpl();

        // act
        Collection<FSqrScmProjectConfiguration> result = repository.getAllProjectConfigurations();

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testAddScmProjectConfiguration_CtorAddProjectConfiguration_projectConfigurationNotEmpty() throws Exception {
        // arrange
        FSqrScmProjectConfigurationRepositoryImpl repository = new FSqrScmProjectConfigurationRepositoryImpl();
        FSqrScmProjectConfiguration projectConfiguration = new FSqrScmProjectConfiguration( "test", "Test DisplayName", "11111-1111-11111", 1 );

        // act
        repository.addScmProjectConfiguration( projectConfiguration );

        // assert
        Collection<FSqrScmProjectConfiguration> result = repository.getAllProjectConfigurations();
        assertThat( result, not( empty() ) );

    }

    @Test
    public void testHasProjectConfiguration_CtorTestForUnknownProjectConfiguration_expectHasNoSuchProject() throws Exception {
        // arrange
        String unknownProjectIdentifier = "unknown-project";
        FSqrScmProjectConfigurationRepositoryImpl repository = new FSqrScmProjectConfigurationRepositoryImpl();

        // act
        boolean result = repository.hasProjectConfiguration( unknownProjectIdentifier );

        // assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testHasProjectConfiguration_CtorAddKnownProjectId_expectThatHasSuchProject() throws Exception {
        // arrange
        String knownProjectIdentifier = "known-project";

        FSqrScmProjectConfigurationRepositoryImpl repository = new FSqrScmProjectConfigurationRepositoryImpl();
        FSqrScmProjectConfiguration projectConfiguration = new FSqrScmProjectConfiguration( knownProjectIdentifier, "Test DisplayName", "11111-1111-11111", 1 );
        repository.addScmProjectConfiguration( projectConfiguration );

        // act
        boolean result = repository.hasProjectConfiguration( knownProjectIdentifier );

        // assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testGetProjectConfigurationString_CtorAddKnownProjectIdGetKnownProjectId_expectSameInstance() throws Exception {
        // arrange
        String knownProjectIdentifier = "known-project";

        FSqrScmProjectConfigurationRepositoryImpl repository = new FSqrScmProjectConfigurationRepositoryImpl();
        FSqrScmProjectConfiguration expectedProjectConfiguration = new FSqrScmProjectConfiguration( knownProjectIdentifier, "Test DisplayName",
                        "11111-1111-11111", 1 );
        repository.addScmProjectConfiguration( expectedProjectConfiguration );

        // act
        FSqrScmProjectConfiguration result = repository.getProjectConfiguration( knownProjectIdentifier );

        // assert
        assertThat( result, is( sameInstance( expectedProjectConfiguration ) ) );
    }

}
