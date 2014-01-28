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
import org.opensplice.osplj.loca.core.LocationData;
import org.opensplice.osplj.loca.core.LocationProvider;
import org.opensplice.osplj.loca.sub.LocusFilter;

import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import static java.lang.System.out;


public class RoundTripDemoReader {


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


        // Set the property. This could also be set via a property file.
        System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
                "org.opensplice.osplj.core.ServiceEnvironmentImpl");

        final ServiceEnvironment env =
                ServiceEnvironment.createInstance(LatencyDemoWriter.class.getClassLoader());

        final PolicyFactory pf = env.getSPI().getPolicyFactory();

        final DomainParticipant dp =
                env.getSPI().getParticipantFactory().createParticipant(DOMAIN);


        System.setProperty("provider", "AndroidLocationProvider");
        final LocationProvider lp = null;
        final LocationData l = new LocationData(23.32, 42.52);

        final Topic<PerfData> topic = dp.createTopic("PerfData", PerfData.class);

        final Partition pin = pf.Partition().withName(Arrays.asList("Out"));
        final PublisherQos pqos = dp.getDefaultPublisherQos().withPolicies(pin);
        final Publisher pub = dp.createPublisher(pqos);

        final Partition pout = pf.Partition().withName(Arrays.asList("In"));
        final SubscriberQos sqos = dp.getDefaultSubscriberQos().withPolicies(pout);
        final Subscriber sub = dp.createSubscriber(sqos);

        final Reliability r = pf.Reliability().withReliable();
        final DataWriterQos dwqos = pub.getDefaultDataWriterQos().withPolicies(r);
        final org.opensplice.osplj.loca.pub.DataWriter<PerfData> dw =
                new org.opensplice.osplj.loca.pub.DataWriter<PerfData>(pub, topic, dwqos, lp);
        //final DataWriter<PerfData> dw = pub.createDataWriter(topic, dwqos);

        final DataReaderQos drqos = sub.getDefaultDataReaderQos().withPolicies(r);
        LocusFilter<PerfData> filter = new LocusFilter(2.0, 10.0);
        final org.opensplice.osplj.loca.sub.DataReader<PerfData> dr =
                new org.opensplice.osplj.loca.sub.DataReader<PerfData>(sub, topic, drqos, l, filter);
        //final DataReader<PerfData> dr = sub.createDataReader(topic, drqos);

        final WaitSet ws = env.getSPI().newWaitSet();

        //ws.attachCondition(dr.createReadCondition(sub.createDataState().withAnyViewState()));
        Vector<Class<? extends Status>> sl = new Vector<Class<? extends Status>>();
        sl.add(DataAvailableStatus.class);
        StatusCondition<?> sc = dr.getStatusCondition();
        sc.setEnabledStatuses(sl);
        ws.attachCondition(sc);

        int size = Integer.parseInt(args[0]);

        final PerfData sample = new PerfData(0, 0, new byte[size]);

        while (true) {
            try {
                ws.waitForConditions();
                Sample.Iterator<PerfData> it = dr.take();
                dw.write(it.next().getData(),l);
            }
            catch (TimeoutException te) { }
        }
    }
}


