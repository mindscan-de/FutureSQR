@echo off

echo -----------------------------------
echo Building the Future-SQR-Tryout 0x03
echo -----------------------------------

call ng build --base-href=/

echo ------------------------------------
echo Deploying the Future-SQR-Tryout 0x03
echo ------------------------------------

rem call xcopy "./dist/FutureSQRTry0x03" "../FutureSQR-XXX-Backend/src/main/webapp/frontend"  /v /s /e /Y