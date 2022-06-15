'''
Created on 08.06.2022

MIT License

Copyright (c) 2022 Maxim Gansert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

@autor: Maxim Gansert
'''

import subprocess
from de.mindscan.futuresqr.gittools.git_output_parser import parse_log_by_rs_us, parse_log_full_changeset, parse_log_fileListToArray

GIT_FIELDS = ['shortrev','revisionid','authorname','authorid','date','reldate','message']
GIT_FORMAT_PARAMS = ['%h','%H','%an','%ae','%ad','%ar','%s']

# ATTENTION: NEVER EVER PUT THIS CODE INTO PRODUCTION, THIS CODE IS DANGEROUS

GIT_EXECUTABLE_PATH = 'C:\\Program Files\\Git\\cmd\\git.exe'

def __execute_git_command( git_parameters ):
    git_command = [ GIT_EXECUTABLE_PATH ]
    git_command.extend(git_parameters)
    
    output = subprocess.run(git_command, stdout=subprocess.PIPE)
    return output.stdout.decode()

def __execute_git_command_on_local_repo( local_git_repo, git_parameters):
    full_git_parameters =  [ '-C', local_git_repo ]
    full_git_parameters.extend(git_parameters)
    
    return __execute_git_command(full_git_parameters)
    

def calculateRecentRevisionsForLocalGitRepo(local_repo_path:str):
    '''
    This method provides a list of all versions for a given local git repository.
    
    :param local_repo_path: path to the local git repository.
    
    This method uses the ascii values 0x1e and 0x1f. These are reserved
    for record separation(RS) (0x1e) and for unit separation(US) (0x1f).
    
    @return: dictionary with revisions from newest to oldest.
    '''
    
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    git_parameters = [
        'log',  
        '--pretty=format:%x1f'+formatdetails+'%x1e'
    ]
    
    log = __execute_git_command_on_local_repo(local_repo_path, git_parameters)

    revisions = parse_log_by_rs_us(log, GIT_FIELDS)
    
    recentRevisions = {
        'revisions': revisions
    }
    return recentRevisions


def calculateDiffForSingleRevision(local_git_repo_path:str, revisionid:str):
    
    git_parameters = [
        'log',  
        '-u',
        '-1',
        revisionid
        ]
    
    log = __execute_git_command_on_local_repo(local_git_repo_path, git_parameters)
    
    fileChangeSets = parse_log_full_changeset(log)
    
    diffData = {
            'fileChangeSet': fileChangeSets
        }
    return diffData


def calculateFileListForSigleRevision(local_git_repo_path:str, revisionid:str):
    pretty_format=['%H','%cn','%cr']
    formatdetails = '%x1f'.join(pretty_format)

    git_parameters = [
        'log',
        '--find-renames',
        '--name-status',
        '--pretty=format:%x1f'+formatdetails+'%x1e',
        '-1',
        revisionid
        ]
    
    log = __execute_git_command_on_local_repo(local_git_repo_path, git_parameters)
    
    fileToActionMap = parse_log_fileListToArray(log)
    # TODO: filter all lines starting with 0x1f
    # TODO: good engough: filter first line
    # TODO: parse the result from filepath (key) to action (value) dictionary
    
    fileDetails = {
        'fileActionMap': fileToActionMap
        }
    
    return fileDetails