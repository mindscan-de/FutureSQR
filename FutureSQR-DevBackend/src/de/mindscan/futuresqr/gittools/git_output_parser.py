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
    
    linecounter = 0;
    while linecounter< len(lines):
        line:str = lines[linecounter]
        
        if line.startswith('diff --git '):
            singleFileChangeSet = {}
            
            # found a new file change
            # TODO: exract filenames from  line
            singleFileChangeSet['lazy_diff_line']=lines[linecounter]
            linecounter+=1
            
            if lines[linecounter].startswith('new file mode'):
                linecounter+=1
            
            # parse index line if present
            singleFileChangeSet['lazy_index_line']= "(empty)"
            if lines[linecounter].startswith('index'):
                singleFileChangeSet['lazy_index_line']=lines[linecounter]
                linecounter+=1
                
            # parse ---
            if lines[linecounter].startswith('---'):
                linecounter+=1
            
            # parse +++
            if lines[linecounter].startswith('+++'):
                linecounter+=1
            
            # parse @@ ... @@
            if lines[linecounter].startswith('@@ '):
                singleContentChangeset = {'line_info':lines[linecounter], 'line_diff_data':[]}
                singleFileChangeSet['fileContentChangeSet']=[]
                singleFileChangeSet['fileContentChangeSet'].append(singleContentChangeset)
                linecounter+=1
            
            # TODO: now read until end of lines or until next diff line.
            if len(lines)<=linecounter:
                fileChangeSets.append(singleFileChangeSet)
                break;
            
            # calculate from filenames, whether add, remove, rename or move
            while linecounter<len(lines) and not lines[linecounter].startswith('diff --git')  :
                if lines[linecounter].startswith("@@ "):
                    singleContentChangeset = {'line_info':lines[linecounter], 'line_diff_data':[]}
                    singleFileChangeSet['fileContentChangeSet'].append(singleContentChangeset)                    
                elif lines[linecounter].startswith("\\ No newline at end of file"):
                    pass
                else:
                    singleContentChangeset['line_diff_data'].append(lines[linecounter])
                linecounter+=1
            
            fileChangeSets.append(singleFileChangeSet)
        else:
            linecounter+=1
        
    return fileChangeSets


def parse_log_fileListToArray(log):
    lines = log.split('\n')
    lines_with_rows = [ line.strip().split('\t',2) for line in lines[1:] ]
    file_list = [ row for row in lines_with_rows if len(row)==2 ]
    
    return file_list