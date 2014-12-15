#!/bin/bash
#protoc.sh
protoc -I=. --java_out=/Users/bear/github/onecenter-core/src/main/java ./*.proto
