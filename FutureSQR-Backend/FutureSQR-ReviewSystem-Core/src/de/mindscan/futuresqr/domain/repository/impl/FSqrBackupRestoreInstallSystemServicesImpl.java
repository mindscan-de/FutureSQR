/**
 * 
 * MIT License
 *
 * Copyright (c) 2023 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.futuresqr.domain.repository.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.mindscan.futuresqr.domain.application.FSqrApplicationServices;
import de.mindscan.futuresqr.domain.connection.FSqrDatabaseConnection;
import de.mindscan.futuresqr.domain.databases.type.FSqrSqliteDatabaseImpl;
import de.mindscan.futuresqr.domain.repository.FSqrBackupRestoreInstallSystemServices;
import de.mindscan.futuresqr.domain.repository.FSqrDatabaseBackedRepository;

/**
 * 
 * For now speaking i currently have no idea, how i'm going to implement this. The code 
 * will speak to me, when it is time...
 * 
 * Backup Restore is not only important for Backup and Restore a working instance but also
 * for development. E.g. keeping my own reviews/and review data (dogfooding), but also to
 * provide a mechanism to migrate to new databases and to migrate to new persistence data 
 * model layouts.
 * 
 * Also this feature is important to import 3rd party databases / backups to migrate them 
 * to this review tool. Therefore we have multiple reasons to actually have this feature
 * being present very "early" in the development process. (I mean before other features).
 * 
 * We will need different readers, e.g. for different formats and for different backup 
 * versions and such.
 * 
 * + Readers (for 3rd party backups, and own backups and file/futuresqr versions)
 * + Writers (for current format)
 * + Importers for different database systems? (- or just use the repo?)
 * + Exporters for different database systems? (- or just use the repo?)
 * 
 */
public class FSqrBackupRestoreInstallSystemServicesImpl implements FSqrBackupRestoreInstallSystemServices {

    private FSqrApplicationServices services;

    // TODO: Backup policy
    // The backup policy contains the information whether a table / which table is elegible for 
    // export to file / backup to file. Because some of the information can be retrieved after
    // reinstalling the instance using a scm crawler, so we can consider these information as
    // re retrievable. All tables with user information and user generated content must be carefully
    // backed up / exported to a file.
    // aside from this a standard database backup is possible.
    // anyways we may not only want to back up the database content, but also configuration files,
    // certificates and authentication tokens and passwords.
    // The backup policy tells what should be backed up or exported
    // basically we have an export, an import and backup and restore policy to ensure overall application 
    // and data consistency. 
    private Map<String, Boolean> backupPolicy = new HashMap<>();

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setApplicationServices( FSqrApplicationServices services ) {
        this.services = services;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void reinitDatabase() {
        FSqrDatabaseConnection dbConnection = services.getDatabaseConnection();

        FSqrSqliteDatabaseImpl database = new FSqrSqliteDatabaseImpl();
        database.dropTables( dbConnection );
        database.createTables( dbConnection );

        // now reinit the database with hardcoded data
        // TODO: this should be reworked, there should not by any herdcoded data, database tables should be 
        //       loaded or initialized frombackups.
        Collection<FSqrDatabaseBackedRepository> allDbBackedRepos = services.getDatabaseBackedRepositories();
        for (FSqrDatabaseBackedRepository dbBackedRepo : allDbBackedRepos) {
            dbBackedRepo.reinitDatabaseTables();
        }
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void exportSystemInstance( Path exportPath ) {

        // TODO: export system configuration ... if we have one
        // TODO: go through all repositories and export full Objects (instead of relational tables?)
        //       allows to restructure the database while new/next import.  -> upgrade path
        // TODO: alternate simple behavior just save a copy of the relational tables.

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void importSystemInstance( Path importPath ) {

        // according to the format of the backup we need to select a reader and then call the reader to provide the data
        // an then invoke an importer which distributes the loaded/routed data to the repositories.

        // TODO: use the repositories to import the objects into the database.

    }

}
