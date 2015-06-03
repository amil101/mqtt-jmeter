/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License. 

 Copyright 2014 University Joseph Fourier, LIG Laboratory, ERODS Team

 */

package org.apache.jmeter.protocol.mqtt.sampler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.protocol.mqtt.Utilities.Constants;
import org.apache.jmeter.protocol.mqtt.client.MqttPublisher;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.IOException;
import java.util.Date;

public class PublisherSampler extends AbstractSampler implements ThreadListener, TestStateListener {

    private static final Logger log = LoggingManager.getLoggerForClass();
    public transient MqttPublisher producer = null;

    @Override
    public void threadStarted() {
        if (log.isDebugEnabled()) {
            log.debug("Thread started " + new Date());
            log.debug("MQTT PublishSampler: ["
                      + Thread.currentThread().getName() + "], hashCode=["
                      + hashCode() + "]");
        }

        if (producer == null) {
            try {
                producer = new MqttPublisher();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        String url = getPropertyAsString(Constants.MQTT_PROVIDER_URL, Constants.MQTT_PROVIDER_URL_DEFAULT_VALUE);
        String clientID = getPropertyAsString(Constants.MQTT_CLIENT_ID);
        String topicName = getPropertyAsString(Constants.MQTT_TOPIC_NAME);
        String username = getPropertyAsString(Constants.MQTT_USERNAME, Constants.MQTT_USERNAME_DEFAULT_VALUE);
        String password = getPropertyAsString(Constants.MQTT_PASSWORD, Constants.MQTT_PASSWORD_DEFAULT_VALUE);
        int qos = getPropertyAsInt(Constants.MQTT_QOS);
        boolean retain = getPropertyAsBoolean(Constants.MQTT_RETAINED);
        String messageContent = getPropertyAsString(Constants.MQTT_CONTENT_TEXT);
        this.producer.setupTest(url, clientID, topicName, username, password, qos, retain, messageContent);
    }

    @Override
    public void threadFinished() {
        log.debug("Thread ended " + new Date());

        if (producer != null) {

            try {
                producer.close();

            } catch (IOException e) {
                e.printStackTrace();
                log.warn(e.getLocalizedMessage(), e);
            }

        }

    }

    // -------------------------Sample------------------------------------//

    @Override
    public SampleResult sample(Entry entry) {
        return this.producer.runTest(new JavaSamplerContext(new Arguments()));
    }

    @Override
    public void testEnded() {
        log.debug("Thread ended " + new Date());


        if (producer != null) {

            try {
                producer.close();
                System.out.println("close at:" + new Date());

            } catch (IOException e) {
                e.printStackTrace();
                log.warn(e.getLocalizedMessage(), e);
            }

        }


    }

    @Override
    public void testEnded(String arg0) {
        testEnded();

    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String arg0) {
        testStarted();

    }
}
