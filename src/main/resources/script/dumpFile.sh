#!/bin/bash
#set -x
if [ $# != 4 ];then
    echo "usage:<sourceFilePath> <targetFilePath> <FileName> <slotId> <SERVERIP>"
    exit 1
fi
 
time=`date +%Y%m%d%H%M%S` 
sourceFile=$1
targetFile=$2/$3
targetFileWait=$2/$3_____wait
ssize=0
dsize=0
mkdir -p $2  > /dev/null 2>&1
count=0

if [ ! -f "$sourceFile" ]; then
        dsize=0
        echo $dsize
        exit
fi

if [  -f "$targetFile" ]; then
        dsize=`ls -lk $targetFile |awk '{print $5}'`
        echo $dsize
        exit 
fi
while true
do
        if [ $count -gt 3 ];then
            echo "dumpFile.sh 复制文件$1  失败 $time" >> /var/log/tomcat7/merge.log
            dsize=0
            echo $dsize
            exit 
        fi
        sleep 5
        count=`expr $count + 1`
        cp -f $sourceFile $targetFileWait  > /dev/null 2>&1
        ssize=`ls -lk $1|awk '{print $5}'`
        dsize=`ls -lk ${targetFileWait}|awk '{print $5}'`
        if [ "$ssize"x = "$dsize"x ];then
                mv ${targetFileWait} ${targetFile}
                echo "dumpFile.sh 复制文件$1  成功 $time" >> /var/log/tomcat7/merge.log
                break
        else
                continue
        fi
 
done
(sleep 5;echo GETDISKINFO,$4,;sleep 2) | telnet 127.0.0.1 2021 > /dev/null 2>&1
sleep 20
echo $dsize
