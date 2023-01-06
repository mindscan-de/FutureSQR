@echo off

cd %~dp0

echo -------------------------------
echo Building the FutureSQR-Frontend
echo -------------------------------

call ng build --base-href=/FutureSQR/

echo --------------------------------
echo Deploying the FutureSQR-Frontend
echo --------------------------------

call xcopy "dist/FutureSQR-Frontend" "../FutureSQR-Backend/FutureSQR-TomcatDevBackend/src/main/webapp"  /v /s /e /Y
call xcopy "dist/FutureSQR-Frontend/assets" "../FutureSQR-Backend/FutureSQR-TomcatDevBackend/src/main/webapp/assets"  /v /s /e /Y