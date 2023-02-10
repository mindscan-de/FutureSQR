@echo off

cd %~dp0

echo -------------------------------
echo Building the FutureSQR-Frontend
echo -------------------------------

call ng build --base-href=/FutureSQR/

echo --------------------------------
echo Deploying the FutureSQR-Frontend
echo --------------------------------

call xcopy "dist/FutureSQR-Frontend" "../FutureSQR-Backend/FutureSQR-SpringDevBackend/src/main/resources/static"  /v /s /e /Y
call xcopy "dist/FutureSQR-Frontend/assets" "../FutureSQR-Backend/FutureSQR-SpringDevBackend/src/main/resources/static/assets"  /v /s /e /Y
