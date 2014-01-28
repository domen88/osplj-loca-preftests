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
import org.omg.dds.core.StatusCondition;
import org.omg.dds.core.WaitSet;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.PolicyFactory;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.status.DataAvailableStatus;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.sub.*;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.perf.gen.PerfData;
import org.opensplice.osplj.loca.core.AndroidLocationProvider;
import org.opensplice.osplj.loca.core.LocationData;
import org.opensplice.osplj.loca.core.LocationProvider;
import org.opensplice.osplj.loca.pub.DataWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import static java.lang.System.in;
import static java.lang.System.out;

public class BestEffortThroughputDemoWriter {

    /**
     *  Argument number
     */
    private static final short ARGN = 1;

    /**
     * The domain used by this test.
     */
    private static final short DOMAIN = 0;
    /**
     * Forbid arbitrary instantiation of a utility class.
     */

    /**
     * Start the Latency Demo Writer.
     * @param args expected arguments are data size, sample nubmer and write period.
     */


    public static void main(final String[] args) {
        if (args.length < ARGN) {
            out.println("USAGE:\n\tBestEffortThroughputDemoWriter <size> ");
            System.exit(1);
        }

        // Set the property. This could also be set via a property file.
        System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
                "org.opensplice.osplj.core.ServiceEnvironmentImpl");

        final ServiceEnvironment env =
                ServiceEnvironment.createInstance(LatencyDemoWriter.class.getClassLoader());

        final PolicyFactory pf = env.getSPI().getPolicyFactory();

        final DomainParticipant dp =
                env.getSPI().getParticipantFactory().createParticipant(DOMAIN);



        final Topic<PerfData> topic = dp.createTopic("PerfData", PerfData.class);

        final Partition pin = pf.Partition().withName(Arrays.asList("In"));
        final PublisherQos pqos = dp.getDefaultPublisherQos().withPolicies(pin);
        final Publisher pub = dp.createPublisher(pqos);

        final Reliability r = pf.Reliability().withBestEffort();
        final DataWriterQos dwqos = pub.getDefaultDataWriterQos().withPolicies(r);

        System.setProperty("provider", "AndroidLocationProvider");
        LocationProvider lp = null;
        DataWriter<PerfData> dataWriter = new DataWriter<PerfData>(pub, topic, dwqos, lp);

        int size = Integer.parseInt(args[0]);

        final PerfData sample = new PerfData(0, 0, new byte[size]);
        final LocationData loca = new LocationData(23.32, 42.52);

        long orig = System.nanoTime();
        int seconds = 0;
        long current = orig;
        
        while (true) {
            try {
                dataWriter.write(sample, loca);
                
            } catch (TimeoutException te) {
            }
        }
    }

}

