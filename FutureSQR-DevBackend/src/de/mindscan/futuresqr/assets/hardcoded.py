'''
Created on 09.06.2022

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

import json
import hashlib
import uuid

TEMP_ASSET_FOLDER = '../../../../../tempassets/'

FUTURESQR_NAMESPACE_OID = uuid.uuid3(uuid.NAMESPACE_OID,'FutureSQR')
SYSCONFIG_NAMESPACE_OID = uuid.uuid3(FUTURESQR_NAMESPACE_OID, 'SystemInstance')
USERNAMES_NAMESPACE_OID = uuid.uuid3(FUTURESQR_NAMESPACE_OID, 'SystemUsers')

'''
Purpose of this module:

This module will provide either hard coded assets or hard coded data, which
should be retrieved through a database or some other kind of complex calculation.
Sometimes we just don't need to solve this particular problem until we know
we want to go that route. Also this is the development backend, which should
provide just enough reasonable data to develop the ui / frontend. We may
need to solve this problem later - but that means we can still go fast, and 
do not need to solve everything by now.
'''

def _getFromTempAssets( filename:str ):
    '''
    Provide an asset stored as a json file in the temp asset folder.
    
    :param filename:
    '''
    # TODO test existence...
    
    with open(TEMP_ASSET_FOLDER+str(filename),'r') as inputfile:
        return json.load(inputfile)
    print("something went wrong with : " + str(filename) )
    return {}

def _putToTempAssets( data , filename:str ):
    with open(TEMP_ASSET_FOLDER+str(filename),'w') as outputfile:
        return json.dump(data, outputfile, indent=2, sort_keys=True)
    print("something went wrong with "+str(filename) )
    pass

def getSystemConfigurationMap(instanceName:str):
    instanceUuid = uuid.uuid3(SYSCONFIG_NAMESPACE_OID, instanceName)
    filename = 'systemInstance.'+str(instanceUuid)+".json"

    print("Loading config instance : " + str(filename))
    
    return _getFromTempAssets(filename);
