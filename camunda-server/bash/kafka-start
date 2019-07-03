#!/usr/bin/env bash

export $(grep -v '^#' ./environment.properties | xargs)
pwd=`pwd`
grep='grep-pid'
cd ${zookeeper}/bin/
./zkServer.sh start
echo `${pwd}/${grep} quorum`
sleep 15
cd ${kafka}
rm -r ${kafka_logs}/
cd ${kafka}/bin/
./kafka-server-start.sh -daemon ../config/server.properties
echo `${pwd}/${grep} kafka`
cd ${pwd}
