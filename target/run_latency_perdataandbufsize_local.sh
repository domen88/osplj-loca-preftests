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

OUT_DIR=latency_perdataandbuffersize_${HOSTNAME}_local_`date +%Y_%m_%d-%H-%M`
mkdir -p $OUT_DIR
echo "Write logs in $OUT_DIR"


#for size in 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768
for size in 1 2 4 8 16 32 64 128 256 512 1024
do
  bufsize=`expr $size \* 2`
  if [ "$bufsize" -lt 512 ]; then bufsize=512; fi
  echo
  echo ------ Run with data size = $size and buffer size = $bufsize ------
  ./rttrd -vmopts "-Dddsi.network.receiver.udp.buffer.size=${bufsize}" -bg $size &
  sleep 2
  ./rttwr -vmopts "-Dddsi.network.receiver.udp.buffer.size=${bufsize}" $size 100000 100 > $OUT_DIR/lat_${size}.log
  kill -9 `cat pid`
done
  
