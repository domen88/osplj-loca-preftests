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

octave --eval "addpath(\"octave\"); B = [ 1 2 4 8 16 32 64 128 256 512 1024 ]; S = genstatsmedmincompared(B, \"$1/lat_\", \"$2/lat_\", \".log\", 20000, 100000, \"OpenSplice Latency\", \"data size (bytes)\", \"latency (usec)\", \"OpenSplice For Java\", \"OpenSplice For Java with Location\"); print -color -dpng $1_compared.png;"
