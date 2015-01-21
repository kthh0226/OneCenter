#!/bin/bash
#protoc.sh
protoc -I=. --java_out=/Users/kthh/github/OneCenter/core/src/main/java ./*.proto
