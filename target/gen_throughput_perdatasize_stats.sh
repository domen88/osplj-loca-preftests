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

octave --eval "addpath(\"octave\"); B = [ 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768 ]; S = genstatsmed(B, \"$1/tp_\", \".log\", 10, 30, \"OpenSplice For Java Throughput\", \"data size (bytes)\", \"throughput (kbit/sec)\", 125); print -color -dpng $1.png;S = genstatsautoscalemed(B, \"$1/tp_\", \".log\", 10, 30, \"OpenSplice For Java Throughput\", \"data size (bytes)\", \"throughput (msg/sec)\"); print -color -dpng $1_msg.png;"
