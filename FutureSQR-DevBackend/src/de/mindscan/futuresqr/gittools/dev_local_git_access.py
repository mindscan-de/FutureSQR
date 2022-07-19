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

GIT_FIELDS = ['shortrev','revisionid','authorname','authorid','date','shortdate','reldate','parents','parentsshort','message']
GIT_FORMAT_PARAMS = ['%h','%H','%an','%ae','%ad','%as','%ar','%P','%p','%s']

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
    

def calculateRecentRevisionsForLocalGitRepo(local_repo_path:str, limit:int = -1):
    '''
    This method provides a list of all revisions for a given local git repository.
    
    :param local_repo_path: path to the local git repository.
    :param limit: define a maximum number of revisions. if greater 0 then maximum of limit revisions is retrieved.  
    
    This method uses the ascii values 0x1e and 0x1f. These are reserved
    for record separation(RS) (0x1e) and for unit separation(US) (0x1f).
    
    if a limit is defined, then #calculateNRecentRevisionsForLocalGitRepo is invoked.
    
    @return: dictionary with revisions from newest to oldest.
    '''
    if limit > 0:
        return calculateNRecentRevisionsForLocalGitRepo(local_repo_path, limit)
    
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


def calculateNRecentRevisionsForLocalGitRepo(local_repo_path:str, max_revisions:int):
    '''
    This method provides a list of a limited number of revisions for a given local git repository.
    
    :param local_repo_path: path to the local git repository.
    :param max_revisions: define a maximum number of revisions, at most the maximum number of revisiosns is retrieved.  
    
    This method uses the ascii values 0x1e and 0x1f. These are reserved
    for record separation(RS) (0x1e) and for unit separation(US) (0x1f).
    
    if a limit is defined, then #calculateNRecentRevisionsForLocalGitRepo is invoked.
    
    @return: dictionary with revisions from newest to oldest.
    '''
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    git_parameters = [
        'log',
        '--pretty=format:%x1f'+formatdetails+'%x1e',
        'HEAD~'+str(max_revisions)+'..'
    ]
    
    log = __execute_git_command_on_local_repo(local_repo_path, git_parameters)

    revisions = parse_log_by_rs_us(log, GIT_FIELDS)
    
    recentRevisions = {
        'revisions': revisions
    }
    return recentRevisions


def calculateRecentRevisionsFromRevisionToHeadForLocalGitRepo(local_repo_path:str, from_commit_hash:str):
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    git_parameters = [
        'log',
        '--pretty=format:%x1f'+formatdetails+'%x1e',
        str(from_commit_hash)+'^..HEAD'
        # str(from_commit_hash)+'^..'
    ]
    
    log = __execute_git_command_on_local_repo(local_repo_path, git_parameters)

    revisions = parse_log_by_rs_us(log, GIT_FIELDS)
    
    recentRevisions = {
        'revisions': revisions
    }
    return recentRevisions
    

def caluclateSimpleRevisionInformation(local_git_repo_path:str, revisionid:str):
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    git_parameters = [
        'log',
        #'-u',
        '--pretty=format:%x1f'+formatdetails+'%x1e',
        '-1',
        revisionid
        ]

    log = __execute_git_command_on_local_repo(local_git_repo_path, git_parameters)
    revision = parse_log_by_rs_us(log, GIT_FIELDS)

    return revision


def calculateSimpleRevisionInformationForRevisionList(local_git_repo_path:str, revisionlist:list):
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    git_parameters = [
        'log',
        '--pretty=format:%x1f'+formatdetails+'%x1e',
    ]
    # TODO: this is not how git works... we start with smallest and the latest. 
    # startrev^...endrevisision
    git_parameters.extend(revisionlist)
    git_parameters.append('--')

    log = __execute_git_command_on_local_repo(local_git_repo_path, git_parameters)
    revisions = parse_log_by_rs_us(log, GIT_FIELDS)
    revisions = [ revision for revision in revisions if revision['revisionid'] in revisionlist] 

    return revisions


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


def calculateFileListForSingleRevision(local_git_repo_path:str, revisionid:str):
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


# Todo, this needs a diff between oldest and newest revision and then for each the filelist, which needs 
#       to be parsed individually and then filtered for the desired revisions and then combined to a new 
#       complete filelist
def calculateFileListForListOfRevisions(local_git_repo_path:str, revisionid_list:list):
    if len(revisionid_list) == 1:
        return calculateFileListForSingleRevision(local_git_repo_path, revisionid_list[0])
    
    pretty_format=['%H','%cn','%cr']
    formatdetails = '%x1f'.join(pretty_format)

    git_parameters = [
        'log',
        '--find-renames',
        '--name-status',
        '--pretty=format:%x1f'+formatdetails+'%x1e'
        ]
    
    print("which version does belong")
    print(revisionid_list)
    

    git_parameters.append(revisionid_list[0]+"^..."+revisionid_list[-1])
    git_parameters.append('--')
    
    log = __execute_git_command_on_local_repo(local_git_repo_path, git_parameters)
    print(log)


    # build a map of hash functions
    
    # TODO this is just wrong.... now.    
    fileToActionMap = parse_log_fileListToArray(log)
    # TODO: filter all lines starting with 0x1f
    # TODO: good engough: filter first line
    # TODO: parse the result from filepath (key) to action (value) dictionary
    
    fileDetails = {
        'fileActionMap': fileToActionMap
        }
    
    return fileDetails
    
    
def updateProjectCache(local_git_repo_path:str, branch_name:str):
    git_parameters = [
        'pull',
        'origin',
        branch_name # main/master
        ]
    
    __execute_git_command_on_local_repo(local_git_repo_path, git_parameters)
    
    pass