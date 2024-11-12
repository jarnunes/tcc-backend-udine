@echo off
REM Cria as pastas "java" e "java/lib"
mkdir java
mkdir java\lib

REM Copia o arquivo JAR para a pasta "java/lib"
copy target\lambda-layer.jar java\lib\

REM Compacta a pasta "java" em um arquivo ZIP chamado "layer_content.zip"
powershell Compress-Archive -Path java -DestinationPath layer_content.zip