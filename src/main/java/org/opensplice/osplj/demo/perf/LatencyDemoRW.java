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
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.Publisher;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.Topic;
import org.opensplice.demo.perf.gen.PerfData;

import java.util.concurrent.TimeoutException;

import static java.lang.System.out;

/**
 * Created with IntelliJ IDEA.
 * User: angelo
 * Date: 2/25/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class LatencyDemoRW {

    /**
     *  Argument number
     */
    private static final short ARGN = 3;

    /**
     * The domain used by this test.
     */
    private static final short DOMAIN = 0;

    public DataReader<PerfData> createReader(DomainParticipant dp, Topic<PerfData> topic, final int count) {
        final Subscriber sub = dp.createSubscriber();
        final DataReaderListener<PerfData> listener =
                new DataReaderListener<PerfData>() {
                    private final long[] latency = new long[count];
                    int i = 0;

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
                            latency[i] = ts - iterator.next().getData().timestamp;
                            out.println(latency[i]);
                            i++;
                        }
                        if (i >= latency.length) {
                            for (int k = 0; k < latency.length; k++) {
                                out.println(latency[k]);
                            }
                            i = 0;
                        }
                    }

                    @Override
                    public void onSubscriptionMatched(SubscriptionMatchedEvent<PerfData> e) {

                    }

                    @Override
                    public void onSampleLost(SampleLostEvent<PerfData> e) {

                    }
                };
        final DataReader<PerfData> reader = sub.createDataReader(topic);
        reader.setListener(listener);
        return reader;
    }

    public DataWriter<PerfData> createWriter(DomainParticipant dp, Topic<PerfData> topic) {
        final Publisher pub = dp.createPublisher();
        return pub.createDataWriter(topic);
    }

    public static void main(String[] args) {
        if (args.length < ARGN) {
            out.println("USAGE:\n\tLatencyDemoWriter <size> <count> <period>");
            System.exit(1);
        }

        LatencyDemoRW demo = new LatencyDemoRW();

        final int size = Integer.parseInt(args[0]);
        final int count = Integer.parseInt(args[1]);
        final int period = Integer.parseInt(args[2]);

        // Set the property. This could also be set via a property file.
        System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
                "org.opensplice.osplj.core.ServiceEnvironmentImpl");

        final ServiceEnvironment env =
                ServiceEnvironment.createInstance(LatencyDemoWriter.class.getClassLoader());

        final DomainParticipant dp =
                env.getSPI().getParticipantFactory().createParticipant(DOMAIN);


        final Topic<PerfData> topic = dp.createTopic("PerfData", PerfData.class);

        final DataReader<PerfData> reader = demo.createReader(dp, topic, count);
        final DataWriter<PerfData> writer = demo.createWriter(dp, topic);

        final PerfData sample = new PerfData(0, 0, new byte[size]);

        final int k = 10*count;
        for (int i = 0; i < k; i++) {
            sample.timestamp = System.nanoTime();
            try {
                writer.write(sample);
                Thread.sleep(period);
            } catch (TimeoutException te) {
                out.print(">>>> TIMED-OUT on WRITE !!!");
            }
            catch (InterruptedException e) { }
        }

    }
}
