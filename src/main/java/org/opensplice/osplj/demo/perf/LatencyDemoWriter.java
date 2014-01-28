/**
 *                         OpenSplice For Java
 *
 *    This software and documentation are Copyright 2010 to 2013 PrismTech
 *    Limited and its licensees. All rights reserved. See file:
 *
 *                           docs/LICENSE.html
 *
 *    for full copyright notice and license terms.
 */
package org.opensplice.osplj.demo.perf;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.perf.gen.PerfData;
import org.opensplice.osplj.loca.core.LocationData;
import org.opensplice.osplj.loca.core.LocationProvider;

import java.util.concurrent.TimeoutException;

import static java.lang.System.out;
/**
 *
 */
public final class LatencyDemoWriter {
    /**
     *  Argument number
     */
    private static final short ARGN = 3;

    /**
     * The domain used by this test.
     */
    private static final short DOMAIN = 0;
    /**
     * Forbid arbitrary instantiation of a utility class.
     */
    private LatencyDemoWriter() { }

    /**
     * Start the Latency Demo Writer.
     * @param args expected arguments are data size, sample nubmer and write period.
     */
    public static void main(final String[] args) {
        if (args.length < ARGN) {
            out.println("USAGE:\n\tLatencyDemoWriter <size> <count> <period>");
            System.exit(1);
        }

        // Set the property. This could also be set via a property file.
        System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
                "org.opensplice.osplj.core.ServiceEnvironmentImpl");

        final ServiceEnvironment env =
                ServiceEnvironment.createInstance(LatencyDemoWriter.class.getClassLoader());

        final DomainParticipant dp =
                env.getSPI().getParticipantFactory().createParticipant(DOMAIN);


        final Topic<PerfData> topic = dp.createTopic("PerfData", PerfData.class);
        final Publisher pub = dp.createPublisher();


        System.setProperty("provider", "AndroidLocationProvider");
        LocationProvider lp = null;
        org.opensplice.osplj.loca.pub.DataWriter<PerfData> writer =
                new org.opensplice.osplj.loca.pub.DataWriter<PerfData>(pub, topic, pub.getDefaultDataWriterQos(), lp);

        int size = Integer.parseInt(args[0]);
        int count = Integer.parseInt(args[1]);
        int period= Integer.parseInt(args[2]);


        final PerfData sample = new PerfData(0, 0, new byte[size]);
        final LocationData loca = new LocationData(23.32, 42.52);

        for (int i = 0; i < count; i++) {
            sample.timestamp = System.nanoTime();
            try {
                writer.write(sample,loca);
                out.print(".");
                Thread.sleep(period);
            } catch (TimeoutException te) {

                out.print(">>>> TIMED-OUT on WRITE !!!");
            }
            catch (InterruptedException ie) { }
        }

    }
}
