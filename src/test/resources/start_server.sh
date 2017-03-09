#!/bin/bash
HOST=$1
USERNAME=$2
PASSWORD=$3

/usr/local/bin/sshpass -p ${PASSWORD} ssh ${USERNAME}@${HOST} "nohup /usr/bin/iperf -s -D > /dev/null 2>&1"
