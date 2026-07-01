$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$javaHome = if ($env:JAVA_HOME) { $env:JAVA_HOME } else { "C:\Users\User\Downloads\oracleJdk-25" }
$javac = Join-Path $javaHome "bin\javac.exe"
$fx = "C:\Users\User\Downloads\javafx-sdk-23.0.1\lib\*"
$sqlite = "C:\Users\User\Downloads\sqlite-jdbc-3.47.1.0.jar"
$cp = "$fx;$sqlite"
$srcRoot = Join-Path $projectRoot "src"
$bin = Join-Path $projectRoot "bin"
$resources = Join-Path $projectRoot "src\main\resources"

New-Item -ItemType Directory -Force -Path $bin | Out-Null
$sourceFiles = Get-ChildItem -Path $srcRoot -Recurse -Filter *.java | ForEach-Object { $_.FullName }

& $javac -encoding UTF-8 -cp $cp -d $bin -sourcepath $srcRoot @sourceFiles
Copy-Item -Path (Join-Path $resources "*") -Destination $bin -Recurse -Force

Write-Host "Build complete."
