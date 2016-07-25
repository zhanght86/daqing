#!/bin/bash
#set -x

cd $1
declare -a myarray
for file2 in `ls *.split`; 
    do
        count=0 
#        split_filename=${file2%%.*}
        split_filename=${file2%_midas*}
        for ary in ${myarray[*]};
         do
             if [ $ary = $split_filename ];then
                  let "count+=1"    
             fi
         done
          if [ $count = 0 ];then
     	    myarray=(${myarray[*]} $split_filename);
          fi
    done  
echo ${myarray[*]}
for item in ${myarray[*]}
	do
        cat $item*.split >  $item
        echo "merage $item  $(date +%y%m%d%H%M%S)" >> /var/log/tomcat7/merge.log
	done
#cat *.split >  MergeFileBySplit.$(date +%y%m%d%H%M%S)
mv *.split /leofsdata3/tmp/trash
rm -rf /leofsdata3/tmp/trash/*.split
