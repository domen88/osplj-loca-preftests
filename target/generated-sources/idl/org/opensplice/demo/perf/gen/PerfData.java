package org.opensplice.demo.perf.gen;


/**
* org/opensplice/demo/perf/gen/PerfData.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/domenicoscotece/jmobile/tests/perftests/src/main/idl/performance.idl
* marted� 21 gennaio 2014 16.34.26 CET
*/

/**
* Updated by idl2j
* from /Users/domenicoscotece/jmobile/tests/perftests/src/main/idl/performance.idl
* martedì 21 gennaio 2014 16.34.27 CET
*/

import org.opensplice.osplj.dcps.keys.KeyList;

@KeyList(
    topicType = "PerfData",
    keys = {"id"}
)
public final class PerfData implements org.omg.CORBA.portable.IDLEntity
{
  public int id = (int)0;
  public long timestamp = (long)0;
  public byte data[] = null;

  public PerfData ()
  {
  } // ctor

  public PerfData (int _id, long _timestamp, byte[] _data)
  {
    id = _id;
    timestamp = _timestamp;
    data = _data;
  } // ctor

} // class PerfData