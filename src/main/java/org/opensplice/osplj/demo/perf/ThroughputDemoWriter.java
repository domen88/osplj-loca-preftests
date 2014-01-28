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
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.sub.*;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.perf.gen.PerfData;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import static java.lang.System.in;
import static java.lang.System.out;

public class ThroughputDemoWriter {

    /**
     *  Argument number
     */
    private static final short ARGN = 2;

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
            out.println("USAGE:\n\tThroughtputDemoWriter <size> <time>");
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

        final Reliability r = pf.Reliability().withReliable();
        final DataWriterQos dwqos = pub.getDefaultDataWriterQos().withPolicies(r);
        final DataWriter<PerfData> dw = pub.createDataWriter(topic, dwqos);

        int size = Integer.parseInt(args[0]);
        int time = Integer.parseInt(args[1]);

        final PerfData sample = new PerfData(0, 0, new byte[size]);
        final long[] rtt = new long[time];

        long orig = System.nanoTime();
        int seconds = 0;
        long current = orig;
        
        while (true) {
            try {
                dw.write(sample);
                current = System.nanoTime();
                if(current > orig + ((long)time * 1000000000))
                {
					break;
				}
                if(current > orig + ((long)seconds+1) * 1000000000)
                {
					seconds = (int)((current - orig) / (long)1000000000);
				}
			    rtt[seconds] ++;
                
            } catch (TimeoutException te) {
                current = System.nanoTime();
                if(current > orig + ((long)time * 1000000000))
                {
					break;
				}
                if(current > orig + ((long)seconds+1) * 1000000000)
                {
					seconds = (int)((current - orig) / (long)1000000000);
				}
            }
        }

        for(long mps : rtt)
        {
			System.out.println(mps * size);
		}

        System.exit(0);
    }

}

