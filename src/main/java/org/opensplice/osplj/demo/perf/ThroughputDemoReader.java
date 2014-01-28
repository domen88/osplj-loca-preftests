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
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import static java.lang.System.out;


public class ThroughputDemoReader {


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



        final Topic<PerfData> topic = dp.createTopic("PerfData", PerfData.class);

        final Partition pout = pf.Partition().withName(Arrays.asList("In"));
        final SubscriberQos sqos = dp.getDefaultSubscriberQos().withPolicies(pout);
        final Subscriber sub = dp.createSubscriber(sqos);

        final Reliability r = pf.Reliability().withReliable();
        
        //final History h = pf.History().withDepth(1000000);
        final History h = pf.History().withDepth(1);

        final DataReaderQos drqos = sub.getDefaultDataReaderQos().withPolicies(r, h);
        final DataReader<PerfData> dr = sub.createDataReader(topic, drqos);

        final WaitSet ws = env.getSPI().newWaitSet();

        //ws.attachCondition(dr.createReadCondition(sub.createDataState().withAnyViewState()));
        //Vector<Class<? extends Status>> sl = new Vector<Class<? extends Status>>();
        //sl.add(DataAvailableStatus.class);
        //StatusCondition<?> sc = dr.getStatusCondition();
        //sc.setEnabledStatuses(sl);
        //ws.attachCondition(sc);

        int size = Integer.parseInt(args[0]);

        final PerfData sample = new PerfData(0, 0, new byte[size]);

        while (true) {
            try {
				Thread.currentThread().sleep(1000);
                List<Sample<PerfData>> it = new ArrayList<Sample<PerfData>>(1000000);
                dr.take(it);
                //System.out.println(it.size() + " messages/sec : " + (it.size() * size) + " byte/sec");
            }
            catch (Exception e) {e.printStackTrace(); }
        }
    }
}


