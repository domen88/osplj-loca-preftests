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
import org.omg.dds.core.policy.History;
import org.omg.dds.core.policy.History.Kind;
import org.omg.dds.core.status.DataAvailableStatus;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.pub.PublisherQos;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.sub.SubscriberQos;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.perf.gen.PerfData;
import org.opensplice.osplj.loca.core.AndroidLocationProvider;
import org.opensplice.osplj.loca.core.LocationData;
import org.opensplice.osplj.loca.core.LocationProvider;
import org.opensplice.osplj.loca.sub.DataReader;
import org.opensplice.osplj.loca.sub.LocusFilter;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import static java.lang.System.out;


public class BestEffortThroughputDemoReader {

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
            out.println("USAGE:\n\tBestEffortThroughputDemoReader <size> <time>");
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

        final Partition pout = pf.Partition().withName(Arrays.asList("In"));
        final SubscriberQos sqos = dp.getDefaultSubscriberQos().withPolicies(pout);
        final Subscriber sub = dp.createSubscriber(sqos);

        final Reliability r = pf.Reliability().withBestEffort();
        
        
        final History h = pf.History().withKind(Kind.KEEP_ALL);

        final DataReaderQos drqos = sub.getDefaultDataReaderQos().withPolicies(r, h);

        LocusFilter<PerfData> filter = new LocusFilter(2.0, 10.0);

        System.setProperty("provider", "AndroidLocationProvider");
        LocationProvider lp = null;
        LocationData l = new LocationData(23.32, 42.52);
        final DataReader<PerfData> dr = new DataReader<PerfData>(sub, topic, drqos, l, filter);

        int size = Integer.parseInt(args[0]);
        int time = Integer.parseInt(args[1]);

        final PerfData sample = new PerfData(0, 0, new byte[size]);
        
        int[] res = new int[time];

        for(int i = 0; i < time; ++i)
        {
            try {
				Thread.currentThread().sleep(1000);
                //List<Sample<PerfData>> it = new ArrayList<Sample<PerfData>>(1000000);
                final Sample.Iterator<PerfData> iterator = dr.take();

                //dr.take(it);
                int sizeof = 0;
                while (iterator.hasNext()){
                    sizeof++;
                    iterator.next();
                }
                res[i] = sizeof;
            }
            catch (Exception e) {e.printStackTrace(); }
        }
        
        for(int i = 0; i < time; ++i)
        {
			System.out.println(size * res[i]);
		}
        
        System.exit(0);
    }
}


