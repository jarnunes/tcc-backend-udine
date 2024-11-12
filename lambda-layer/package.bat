@echo off
rem Cria as pastas "java" e "java/lib"
mkdir java
mkdir java\lib

rem remove os arquivos .jar
del java\lib\lambda-layer.jar
del layer_content.zip

rem Copia o arquivo JAR para a pasta "java/lib"
copy target\lambda-layer.jar java\lib\

rem Compacta a pasta "java" em um arquivo ZIP chamado "layer_content.zip"
powershell Compress-Archive -Path java -DestinationPath layer_content.zip