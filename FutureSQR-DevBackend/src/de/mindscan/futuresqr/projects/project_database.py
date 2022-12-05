'''
Created on 24.06.2022

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
from de.mindscan.futuresqr.assets.hardcoded import _putToTempAssets, _getFromTempAssets


class ProjectDatabase(object):
    '''
    classdocs
    '''


    def __init__(self, params):
        '''
        Constructor
        '''
        self.projectConfigurations = {}
        projectConfigurations = _getFromTempAssets('projectdatabase.json')
        if(len(projectConfigurations)==0):
            self.projectConfigurations = params['allProjects']
        else:
            self.projectConfigurations = projectConfigurations

        self.__persistence = False
        if 'persistenceActive' in params:
            self.__persistence = params['persistenceActive'] 
        
    def getProjectConfiguration(self, projectid):
        return self.projectConfigurations[projectid]
    
    def getAllUserProjects(self, userid:str):
        result = []
        for project in self.projectConfigurations.values():
            converted = {
                'project_id':project['projectID'],
                'project_display_name':project['projectDisplayName'],
                'description':project['projectDescription'],
                'is_starred':project['projectIsStarred'],
                }
            result.append(converted)
            
        return result
    
    def getAllStarredUserProjects(self, userid:str):
        result = []
        for project in self.projectConfigurations.values():
            if not project['projectIsStarred']:
                continue
            
            converted = {
                'project_id':project['projectID'],
                'project_display_name':project['projectDisplayName'],
                'description':project['projectDescription'],
                'is_starred':project['projectIsStarred'],
                }
            result.append(converted)
            
        return result 

    
    def calculateNewReviewIndex(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return None
        
        newReviewId = self.projectConfigurations[projectid]['autoIndex']
        self.projectConfigurations[projectid]['autoIndex']+=1
        
        prefix = self.projectConfigurations[projectid]['reviewPrefix']
        if not prefix.endswith('-'):
            prefix=prefix+"-"
        
        return prefix + str(newReviewId)
    
    def isProjectIdPresent(self, projectid):
        return projectid in self.projectConfigurations 
    
    def getProjectLocalPath(self, projectid):
        if self.isProjectIdPresent(projectid):
            return self.projectConfigurations[projectid]['administration']['localPath']
        return None
    
    def getScmBackend(self, projectid):
        if self.isProjectIdPresent(projectid):
            return self.projectConfigurations[projectid]['administration']['scmBackend']
        return None
    
    def getProjectBranchName(self, projectid):
        if self.isProjectIdPresent(projectid):
            if 'projectBranchName' not in self.projectConfigurations[projectid]:
                return None
            return self.projectConfigurations[projectid]['projectBranchName']
        return None
    
    def hasProjectLocalPath(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False
        if not self.projectConfigurations[projectid]['administration']:
            return False
        if not self.projectConfigurations[projectid]['administration']['localPath']:
            return False
        return True
    
    def hasScmBackend(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False
        if not self.projectConfigurations[projectid]['administration']:
            return False
        if not self.projectConfigurations[projectid]['administration']['scmBackend']:
            return False
        return True
    
    def starProject(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False
        # TODO: this only preliminary until user project relation is implemented
        self.projectConfigurations[projectid]['projectIsStarred'] = True
        # successfully executed - not state 
        self.__persist_projectdatabase()
        return True
    
    def unstarProject(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False
        # TODO: this only preliminary until user project relation is implemented
        self.projectConfigurations[projectid]['projectIsStarred'] = False
        # successfully executed
        self.__persist_projectdatabase()
        return True
    
    def __persist_projectdatabase(self):
        if self.__persistence:
            _putToTempAssets(self.projectConfigurations,'projectdatabase.json')
            
        pass