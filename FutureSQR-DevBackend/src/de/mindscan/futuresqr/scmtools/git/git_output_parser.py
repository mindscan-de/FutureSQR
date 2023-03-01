'''
Created on 12.06.2022

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
from IPython.core.splitinput import LineInfo

import re

def parse_log_by_rs_us(log, fieldnames):
    records = log.strip('\n\x1e').split('\x1e')
    records = [ row.strip().split('\x1f') for row in records ]
    records = [ dict(zip(fieldnames,row)) for row in records ]
    return records

# TODO:
# * the last entry cotains one superfluous emtpy diff element.... (would require to parse the number of lines and process the the particluar diff.
 
# * actually a full changeset contains a list of file changesets, and information about the commit
# * a filechangeset contains a description of the file change (add, remove, modify...), and a list of line changesets 
# * a line changeset contains a description of the lines changed and a list of lines.


def parse_log_full_changeset(log):
    fileChangeSets = []
    
    lines = log.split('\n')
    
    binary_file_mode = False
    linecounter = 0;
    while linecounter< len(lines):
        line:str = lines[linecounter]
        
        if line.startswith('diff --git '):
            singleFileChangeSet = {}
            
            # debug output
            # print(lines[linecounter:linecounter+5])
            
            # found a new file change
            fromPath, toPath = fileChangeSetGitDiffSplitter(lines[linecounter])
            singleFileChangeSet['fromPath'] = fromPath
            singleFileChangeSet['toPath'] = toPath
            linecounter+=1
            
            if lines[linecounter].startswith('new file mode'):
                linecounter+=1
            
            if lines[linecounter].startswith('deleted file mode'):
                singleFileChangeSet['lazy_deleted_file_line'] = lines[linecounter]
                linecounter+=1
                
            # simularity index / rename_from / rename_to
            if lines[linecounter].startswith('similarity index'):
                singleFileChangeSet['similarity_info_line'] = lines[linecounter]
                linecounter+=1
            if lines[linecounter].startswith('rename from'):
                singleFileChangeSet['renamed_from'] = lines[linecounter]
                linecounter+=1
            if lines[linecounter].startswith('rename to'):
                singleFileChangeSet['renamed_to'] = lines[linecounter]
                linecounter+=1
            
            # parse index line if present
            singleFileChangeSet['lazy_index_line']= "(empty)"
            if lines[linecounter].startswith('index'):
                singleFileChangeSet['lazy_index_line']=lines[linecounter]
                linecounter+=1
                
            if lines[linecounter].startswith('Binary files'):
                singleFileChangeSet['binary_file_info_line'] = lines[linecounter]
                binary_file_mode = True
                linecounter+=1
                
                
            # parse ---
            if lines[linecounter].startswith('---'):
                linecounter+=1
            
            # parse +++
            if lines[linecounter].startswith('+++'):
                linecounter+=1
            
            # parse @@ ... @@
            if lines[linecounter].startswith('@@ '):
                leftLineStart, leftLineCount, rightLineStart, rightLineCount = contentChangeSetLineInfoSplitter(lines[linecounter])
                
                singleContentChangeset = {
                    'line_diff_data':[],
                    'diffLeftLineCountStart':leftLineStart,
                    'diffLeftLineCountDelta':leftLineCount,
                    'diffRightLineCountStart':rightLineStart,
                    'diffRightLineCountDelta':rightLineCount                    
                    }
                singleFileChangeSet['fileContentChangeSet']=[]
                singleFileChangeSet['fileContentChangeSet'].append(singleContentChangeset)
                linecounter+=1
            
            # TODO: now read until end of lines or until next diff line.
            if len(lines)<=linecounter:
                fileChangeSets.append(singleFileChangeSet)
                break;
            
            if not binary_file_mode:
                if(lines[linecounter]==''):
                    singleContentChangeset={'line_diff_data':[]}
                                
                # calculate from filenames, whether add, remove, rename or move
                while linecounter<len(lines) and not lines[linecounter].startswith('diff --git') :
                    # debug output
                    # print(lines[linecounter])                
                    if lines[linecounter].startswith("@@ "):
                        singleContentChangeset = {'line_info':lines[linecounter], 'line_diff_data':[]}
                        singleFileChangeSet['fileContentChangeSet'].append(singleContentChangeset)                    
                    elif lines[linecounter].startswith("\\ No newline at end of file"):
                        pass
                    else:
                        singleContentChangeset['line_diff_data'].append(lines[linecounter])
                    linecounter+=1
            
            fileChangeSets.append(singleFileChangeSet)
            binary_file_mode = False;
        else:
            linecounter+=1
        
    return fileChangeSets

def contentChangeSetLineInfoSplitter(contentLineInfo:str):
    lineInfoSplitted = contentLineInfo.split("@@", 3)
    lineData = re.split(",|\+|\-", lineInfoSplitted[1].strip()) 
    
    ls = int(lineData[1])
    lc = int(lineData[2])
    rs = int(lineData[3])
    rc = int(lineData[4] or '0')
        
    return ls,lc,rs,rc

def fileChangeSetGitDiffSplitter(diffGitLineInfo:str):
    filenamesCombined = diffGitLineInfo.strip().split(" a/", 2)
    filenames = filenamesCombined[1].strip().split(" b/", 2)
    return filenames[0], filenames[1]



def parse_log_fileListToArray(log):
    lines = log.split('\n')
    lines_with_rows = [ handle_renames(line.strip().split('\t',3)) for line in lines ]
    
    file_list = [ row for row in lines_with_rows if len(row)==2 ]
    
    return file_list

def handle_renames(row):
    if(len(row) == 2):
        return row;
    if(len(row) == 3):
        return [row[0], row[2]];
    return row