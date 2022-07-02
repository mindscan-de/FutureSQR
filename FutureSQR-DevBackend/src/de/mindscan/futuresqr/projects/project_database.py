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

class ProjectDatabase(object):
    '''
    classdocs
    '''


    def __init__(self, params):
        '''
        Constructor
        '''
        self.projectConfigurations = params['allProjects']
        
    def getProjectConfiguration(self, projectid):
        return self.projectConfigurations[projectid]
    
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
    
    def hasProjectLocalPath(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False
        
        if not self.projectConfigurations[projectid]['administration']:
            return False
        
        if not self.projectConfigurations[projectid]['administration']['localPath']:
            return False
        
        return True
    
    def getAllProjectToLocalPathMap(self):
        pass
    
    
    def starProject(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False
        
        # TODO: this only preliminary until user project relation is implemented
        self.projectConfigurations[projectid]['projectIsStarred'] = True
        
        # successfully executed - not state 
        return True
    
    def unstarProject(self, projectid):
        if not self.isProjectIdPresent(projectid):
            return False

        # TODO: this only preliminary until user project relation is implemented
        self.projectConfigurations[projectid]['projectIsStarred'] = False
        # successfully executed
        return True