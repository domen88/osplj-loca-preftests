package org.opensplice.demo.perf.gen;

/**
* org/opensplice/demo/perf/gen/PerfDataHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/domenicoscotece/jmobile/tests/perftests/src/main/idl/performance.idl
* marted� 21 gennaio 2014 16.34.26 CET
*/

public final class PerfDataHolder implements org.omg.CORBA.portable.Streamable
{
  public org.opensplice.demo.perf.gen.PerfData value = null;

  public PerfDataHolder ()
  {
  }

  public PerfDataHolder (org.opensplice.demo.perf.gen.PerfData initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = org.opensplice.demo.perf.gen.PerfDataHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    org.opensplice.demo.perf.gen.PerfDataHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return org.opensplice.demo.perf.gen.PerfDataHelper.type ();
  }

}