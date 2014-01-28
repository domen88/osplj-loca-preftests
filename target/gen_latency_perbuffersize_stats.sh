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

octave --eval "addpath(\"octave\"); B = [ 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768 ]; S = genstatsnomax(B, \"$1/lat_\", \".log\", 20000, 100000, \"OpenSplice For Java Latency (data size = 128 bytes)\", \"buffer size (bytes)\", \"latency (usec)\"); print -color -dpng $1.png; S = genstatsmedmin99(B, \"$1/lat_\", \".log\", 20000, 100000, \"OpenSplice For Java Latency (data size = 128 bytes)\", \"buffer size (bytes)\", \"latency (usec)\"); print -color -dpng $1_99.png;"
