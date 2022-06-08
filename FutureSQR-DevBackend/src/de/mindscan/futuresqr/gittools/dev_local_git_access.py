'''
Created on 08.06.2022

@author: JohnDoe
'''
import subprocess

GIT_FIELDS = ['shortrev','revisionid','authorname','authorid','date','message']
GIT_FORMAT_PARAMS = ['%h','%H','%an','%ae','%ar','%s']


def calculateRecentRevisionsForLocalGitRepo(local_git_repo_path:str):
    formatdetails = '%x1f'.join(GIT_FORMAT_PARAMS)
    
    GIT_LOG_COMMAND = [
        'C:\\Program Files\\Git\\cmd\\git.exe', 
        '-C',  local_git_repo_path, 
        'log',  
        '--pretty=format:%x1f'+formatdetails+'%x1e'
    ]
    
    output = subprocess.run(GIT_LOG_COMMAND, stdout=subprocess.PIPE)
    log = output.stdout.decode()    

    log = log.strip('\n\x1e').split('\x1e')
    log = [ row.strip().split('\x1f') for row in log ]
    log = [ dict(zip(GIT_FIELDS,row)) for row in log ]
    
    recentRevisions = {
        'revisions': log
    }
    return recentRevisions