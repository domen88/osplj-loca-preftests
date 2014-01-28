package org.opensplice.demo.perf.gen;


/**
* org/opensplice/demo/perf/gen/LocationAwareHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/domenicoscotece/jmobile/tests/perftests/src/main/idl/performance.idl
* marted� 21 gennaio 2014 16.34.26 CET
*/

abstract public class LocationAwareHelper
{
  private static String  _id = "IDL:org/opensplice/demo/perf/gen/LocationAware:1.0";

  public static void insert (org.omg.CORBA.Any a, org.opensplice.demo.perf.gen.LocationAware that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static org.opensplice.demo.perf.gen.LocationAware extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_double);
          _members0[0] = new org.omg.CORBA.StructMember (
            "longitude",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_double);
          _members0[1] = new org.omg.CORBA.StructMember (
            "latitude",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (org.opensplice.demo.perf.gen.LocationAwareHelper.id (), "LocationAware", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static org.opensplice.demo.perf.gen.LocationAware read (org.omg.CORBA.portable.InputStream istream)
  {
    org.opensplice.demo.perf.gen.LocationAware value = new org.opensplice.demo.perf.gen.LocationAware ();
    value.longitude = istream.read_double ();
    value.latitude = istream.read_double ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, org.opensplice.demo.perf.gen.LocationAware value)
  {
    ostream.write_double (value.longitude);
    ostream.write_double (value.latitude);
  }

}
