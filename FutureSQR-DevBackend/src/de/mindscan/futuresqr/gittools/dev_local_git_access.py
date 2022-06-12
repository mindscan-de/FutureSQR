'''
Created on 08.06.2022

@author: JohnDoe
'''
import subprocess

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
    for record separation (0x1e) and for unit separation (0x1f).
    
    @return: dictionary with revisions from newest to oldest.
    '''
    
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    GIT_PARAMETERS = [
        'log',  
        '--pretty=format:%x1f'+formatdetails+'%x1e'
    ]
    
    log = __execute_git_command_on_local_repo(local_repo_path, GIT_PARAMETERS)

    log = log.strip('\n\x1e').split('\x1e')
    log = [ row.strip().split('\x1f') for row in log ]
    log = [ dict(zip(GIT_FIELDS,row)) for row in log ]
    
    recentRevisions = {
        'revisions': log
    }
    return recentRevisions


def __parse_file_changes_from_log(log):
    result = []
    
    lines = log.split('\n')
    
    linecounter = 0;
    while linecounter< len(lines):
        line:str = lines[linecounter]
        
        if line.startswith('diff --git '):
            entry = {}
            
            # found a new file change
            # TODO: exract filenames from  line
            entry['lazy_diff_line']=lines[linecounter]
            linecounter+=1
            
            # parse index
            entry['lazy_index_line']=lines[linecounter]
            linecounter+=1
            
            # parse ---
            linecounter+=1
            
            # parse +++
            linecounter+=1
            
            # parse @@ ... @@
            entry['lazy_lineinfo_line']=lines[linecounter]
            linecounter+=1
            
            # TODO: now read until end of lines or until next diff line.
            if len(lines)<=linecounter:
                result.append(entry)
                break;
            
            # calculate from filenames, whether add, remove, rename or move
            entry['lazy_file_diff'] = []
            while linecounter<len(lines) and not lines[linecounter].startswith('diff --git') :
                entry['lazy_file_diff'].append(lines[linecounter])
                linecounter+=1
            
            result.append(entry)
        else:
            linecounter+=1
        
    return result


def calculateDiffForSingleRevision(local_git_repo_path:str, revisionid:str):
    
    GIT_PARAMETERS = [
        'log',  
        '-u',
        '-1',
        revisionid
        ]
    
    log = __execute_git_command_on_local_repo(local_git_repo_path, GIT_PARAMETERS)
    
    fileChanges = __parse_file_changes_from_log(log)
    
    diffData = {
            'changes': fileChanges
        }
    return diffData