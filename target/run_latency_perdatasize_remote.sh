#!/bin/sh
#
#                         OpenSplice For Java
#
#    This software and documentation are Copyright 2010 to 2013 PrismTech
#    Limited and its licensees. All rights reserved. See file:
#
#                           docs/LICENSE.html
#
#    for full copyright notice and license terms.
#

if [ $# -lt 1 ]
then
  echo "Usage : run_latency_perdatasize_remote.sh <remote_host>:<remote_path>"
  exit
fi

export REMOTE_HOST=`echo $1 | cut -d':' -f1`
export REMOTE_DIR=`echo $1 | cut -d':' -f2`
# check if REMOTE_HOST is accessible
ping -c 1 $REMOTE_HOST 2>&1 > /dev/null
if [ $? != 0 ]; then
    echo "ERROR: cannot ping $REMOTE_HOST"
    exit 1
fi


OUT_DIR=latency_perdatasize_${HOSTNAME}-${REMOTE_HOST}_`date +%Y_%m_%d-%H-%M`
mkdir -p $OUT_DIR
echo "Write logs in $OUT_DIR"

bufsize=65536
CATPID='`cat pid`'

for size in 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768
do
  echo
  echo ------ Run with data size = $size ------
  ssh $REMOTE_HOST "cd $REMOTE_DIR;./rttrd -vmopts \"-Dddsi.network.interfaces=eth0 -Dddsi.network.receiver.udp.buffer.size=${bufsize}\" -bg $size > /dev/null 2>/dev/null < /dev/null"
  sleep 2
  ./rttwr -vmopts "-Dddsi.network.interfaces=dev21726 -Dddsi.network.receiver.udp.buffer.size=${bufsize}" $size 100000 100 > $OUT_DIR/lat_${size}.log
  ssh $REMOTE_HOST "cd $REMOTE_DIR;kill -9 $CATPID"
done
  
