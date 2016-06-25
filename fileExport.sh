#!/bin/bash
#set -x
if [ $# != 5 ];then
    echo "usage:<username> <host1,host2> <filepath1,filepath2> <exportpath> <passwd>"
    exit 1
fi

cd $4
split_filename
for file2 in `ls *.split`  
    do  
      split_filename=${file2%_midas*}
    done  
#echo $split_filename
#cat *.split >  MergeFileBySplit.$(date +%y%m%d%H%M%S)
cat *.split > $split_filename 
mv *.split /tmp/trash
rm -rf /tmp/trash/*
echo "merage $split_filename  $(date +%y%m%d%H%M%S)" >> /var/log/tomcat7/merge_shell.log
