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

OUT_DIR=throughput_besteffort_perdatasize_${HOSTNAME}_local_`date +%Y_%m_%d-%H-%M`
mkdir -p $OUT_DIR
echo "Write logs in $OUT_DIR"

for size in 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768
do
  echo
  echo ------ Run with data size = $size ------
  ./betpwr -bg $size &
  sleep 2
  ./betprd $size 30 > $OUT_DIR/tp_${size}.log
  kill -9 `cat pid`
  sleep 5
done
  
