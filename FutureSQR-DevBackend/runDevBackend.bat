pushd
cd %dp0%
set PYTHONPATH=%dp0%/src:%PYTHONPATH%
echo %PYTHONPATH% 
cd src/de/mindscan/futuresqr/devhttpserver
uvicorn futuresqr_dev:app --reload --root-path
popd