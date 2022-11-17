#!/bin/bash
current_path=`pwd`
export PYTHONPATH="$(pwd)/src:$PYTHONPATH"
cd src/de/mindscan/futuresqr/devhttpserver
python3 -m uvicorn futuresqr_dev:app --reload
cd $current_path