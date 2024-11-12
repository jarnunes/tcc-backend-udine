@echo off
REM Cria as pastas "java" e "java/lib"
mkdir java
mkdir java\lib

rem remove os arquivos .jar
del java\lib\configuration.jar
del layer_content.zip

REM Copia o arquivo JAR para a pasta "java/lib"
copy target\configuration.jar java\lib\

REM Compacta a pasta "java" em um arquivo ZIP chamado "layer_content.zip"
powershell Compress-Archive -Path java -DestinationPath layer_content.zip