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

TEMP_ASSET_FOLDER = '../../../../../tempassets/'

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

def getAllProjectToLocalPathMap():
    return {
    'furiousiron-frontend':"D:\\Temp\\future-square-cache\\FuriousIron-Frontend",
    'futuresqr':"D:\\Temp\\future-square-cache\\FutureSQR",
    'furiousiron-hfb':"D:\\Temp\\future-square-cache\\FuriousIron-HFB"
    }
    