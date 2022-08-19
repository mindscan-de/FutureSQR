@echo off

echo -------------------------------
echo Building the FutureSQR-Frontend
echo -------------------------------

call ng build --base-href=/

echo --------------------------------
echo Deploying the FutureSQR-Frontend
echo --------------------------------

rem call xcopy "./dist/FutureSQR-Frontend" "../FutureSQR-XXX-Backend/src/main/webapp/frontend"  /v /s /e /Y