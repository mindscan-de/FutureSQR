pushd %~dp0
cd %~dp0
set PYTHONPATH=%~dp0/src;%PYTHONPATH%
cd src/de/mindscan/futuresqr/devhttpserver
uvicorn futuresqr_dev:app --reload
popd