package de.mindscan.futuresqr.domain.databases;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
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
        FSqrScmProjectConfigurationRepositoryImpl repository = new FSqrScmProjectConfigurationRepositoryImpl();
        String unknownProjectName = "unknown-project";

        // act
        boolean result = repository.hasProjectConfiguration( unknownProjectName );

        assertThat( result, equalTo( false ) );
    }

}
