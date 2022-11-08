#!/bin/bash
export PYTHONPATH=$(pwd)/src:%PYTHONPATH%
cd src/de/mindscan/futuresqr/devhttpserver
python3 -m uvicorn futuresqr_dev:app --reload
cd ../../../../..