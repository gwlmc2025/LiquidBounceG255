@echo off
del D:\Users\wodes\Desktop\LiquidBounceG255\build\libs\*.jar /f /q
start /wait cmd.exe /c "gradlew build -x generateGitProperties"
del D:\Users\wodes\Desktop\Games\.minecraft\versions\LiquidBounceG255-1.21.11\mods\liquidbounce-0.36.0.jar /f /q
copy D:\Users\wodes\Desktop\LiquidBounceG255\build\libs\liquidbounce-0.36.0.jar D:\Users\wodes\Desktop\Games\.minecraft\versions\LiquidBounceG255-1.21.11\mods\liquidbounce-0.36.0.jar
powershell -File D:\Users\wodes\Desktop\LiquidBounceG255\start.ps1
pause