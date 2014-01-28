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
import org.omg.dds.core.event.*;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.perf.gen.PerfData;
import org.opensplice.osplj.loca.core.LocationData;
import org.opensplice.osplj.loca.core.LocationProvider;
import org.opensplice.osplj.loca.sub.LocusFilter;

import java.io.IOException;

import static java.lang.System.out;

/**
 * Latency Demo Reader class.
 */
public final class LatencyDemoReader {
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
    private LatencyDemoReader() { }

    /**
     * Start the Latency Demo Writer.
     * @param args expected arguments are data size, sample nubmer and write period.
     */
    public static void main(final String[] args) {
        if (args.length < ARGN) {
            out.println("USAGE:\n\tLatencyDemoWriter <size> <count> ");
            System.exit(1);
        }

        int size = Integer.parseInt(args[0]);
        final int count = Integer.parseInt(args[1]);

        // Set the property. This could also be set via a property file.
        System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
                "org.opensplice.osplj.core.ServiceEnvironmentImpl");

        final ServiceEnvironment env =
                ServiceEnvironment.createInstance(LatencyDemoReader.class.getClassLoader());

        final DomainParticipant dp =
                env.getSPI().getParticipantFactory().createParticipant(DOMAIN);


        final Topic<PerfData> topic = dp.createTopic("PerfData", PerfData.class);
        final Subscriber sub = dp.createSubscriber();



        final PerfData sample = new PerfData(0, 0, new byte[size]);


        final DataReaderListener<PerfData> listener =
                new DataReaderListener<PerfData>() {
                    private final long[] latency = new long[count];
                    @Override
                    public void onRequestedDeadlineMissed(RequestedDeadlineMissedEvent<PerfData> e) {
                    }

                    @Override
                    public void onRequestedIncompatibleQos(RequestedIncompatibleQosEvent<PerfData> e) {

                    }

                    @Override
                    public void onSampleRejected(SampleRejectedEvent<PerfData> e) {

                    }

                    @Override
                    public void onLivelinessChanged(LivelinessChangedEvent<PerfData> e) {

                    }

                    @Override
                    public void onDataAvailable(DataAvailableEvent<PerfData> e) {
                        Sample.Iterator<PerfData> iterator = e.getSource().take();
                        long ts = System.nanoTime();
                        while (iterator.hasNext()) {
                            out.println(ts - iterator.next().getData().timestamp);
                        }
                    }

                    @Override
                    public void onSubscriptionMatched(SubscriptionMatchedEvent<PerfData> e) {

                    }

                    @Override
                    public void onSampleLost(SampleLostEvent<PerfData> e) {

                    }
                };

        LocusFilter<PerfData> filter = new LocusFilter(2.0, 10.0);

        System.setProperty("provider", "AndroidLocationProvider");
        LocationProvider lp = null;
        LocationData l = new LocationData(23.32, 42.52);
        final org.opensplice.osplj.loca.sub.DataReader<PerfData> reader =
                new org.opensplice.osplj.loca.sub.DataReader<PerfData>(sub, topic, sub.getDefaultDataReaderQos(), l, filter);
        //final DataReader<PerfData> reader = sub.createDataReader(topic);
        reader.setListener(listener);


    }
}

