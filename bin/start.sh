#!/bin/bash

nohup java -jar target/ziran-api-server.jar -port=8607 -package=com.github.ziran_ink.ziran_api_server -jksPath=src/main/java/api.ziran.ink.jks -jksPassword=xuzewei2019 -jksKeyPassword=W74jUKtA > server.nohup.out 2>&1 &
echo $! > bin/server.pid
