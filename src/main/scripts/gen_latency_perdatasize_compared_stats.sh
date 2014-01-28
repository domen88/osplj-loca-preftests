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

octave --eval "addpath(\"octave\"); B = [ 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768 ]; S = genstatsmedmincompared(B, \"$1/lat_\", \"$2/lat_\", \".log\", 20000, 100000, \"OpenSplice Latency\", \"data size (bytes)\", \"latency (usec)\", \"OpenSplice For Java V1.1.0\", \"OpenSplice Enterprise V6.3.1 (java,sp)\"); print -color -dpng $1_compared.png; C = [ 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 ]; S = genstatsmedmincompared(C, \"$1/lat_\", \"$2/lat_\", \".log\", 20000, 100000, \"OpenSplice Latency (small data)\", \"data size (bytes)\", \"latency (usec)\", \"OpenSplice For Java\", \"OpenSplice Enterprise (java,sp)\"); print -color -dpng $1_compared_small.png;"
