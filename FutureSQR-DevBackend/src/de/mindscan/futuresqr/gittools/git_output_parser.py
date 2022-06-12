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
# * actually a full changeset contains a list of file changesets, and information about the commit
# * a filechangeset contains a description of the file change (add, remove, modify...), and a list of line changesets 
# * a line changeset contains a description of the lines changed and a list of lines.

def parse_log_full_changeset(log):
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
