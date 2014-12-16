#!/bin/bash
#protoc.sh
protoc -I=. --java_out=/Users/bear/github/OneCenterRepository/onecenter-server/src ./*.proto
protoc -I=. --java_out=/Users/bear/github/OneCenterRepository/onecenter-phone/src ./*.proto
