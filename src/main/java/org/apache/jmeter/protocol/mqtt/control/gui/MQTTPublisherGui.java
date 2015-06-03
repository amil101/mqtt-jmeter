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

package org.apache.jmeter.protocol.mqtt.control.gui;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.gui.util.JLabeledRadioI18N;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.mqtt.Utilities.Constants;
import org.apache.jmeter.protocol.mqtt.sampler.PublisherSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledPasswordField;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author Tuan Hiep
 */
public class MQTTPublisherGui extends AbstractSamplerGui implements ChangeListener {

    private static final long serialVersionUID = 240L;

    // QOS button group
    private static final String[] QOS_TYPES_ITEMS = {Constants.MQTT_QOS_VALUE_0, Constants.MQTT_QOS_VALUE_1,
                                                     Constants.MQTT_QOS_VALUE_2};
    private static final String[] MSGTYPES_ITEMS = {Constants.MQTT_CONTENT_TEXT, Constants.MQTT_CONTENT_FILE};

    // Connection Details
    private final JLabeledTextField providerURLField = new JLabeledTextField(JMeterUtils.getResString(Constants.MQTT_PROVIDER_URL));
    private final JLabeledTextField topicName = new JLabeledTextField(JMeterUtils.getResString(Constants.MQTT_TOPIC_NAME));
    private final JLabeledTextField username = new JLabeledTextField(JMeterUtils.getResString(Constants.MQTT_USERNAME));
    private final JLabeledTextField password = new JLabeledPasswordField(JMeterUtils.getResString(Constants.MQTT_PASSWORD));
    private final JLabeledTextField clientId = new JLabeledTextField(JMeterUtils.getResString(Constants.MQTT_CLIENT_ID));
    // Message Details
    private final JLabeledRadioI18N qos = new JLabeledRadioI18N(Constants.MQTT_QOS, QOS_TYPES_ITEMS, QOS_TYPES_ITEMS[0]);

    private final JCheckBox isRetained = new JCheckBox(JMeterUtils.getResString(Constants.MQTT_RETAINED), false);


    private final JLabeledRadioI18N messageContentSource = new JLabeledRadioI18N(Constants.MQTT_CONTENT, MSGTYPES_ITEMS,
            MSGTYPES_ITEMS[0]);
    private final JSyntaxTextArea textMessage = new JSyntaxTextArea(10, 50);
    private final JLabel textArea = new JLabel(JMeterUtils.getResString(Constants.MQTT_CONTENT_TEXT));
    private final JTextScrollPane textPanel = new JTextScrollPane(textMessage);

    public MQTTPublisherGui() {
        init();
    }

    private void init() {
        // Setting default values
        providerURLField.setText(JMeterUtils.getResString(Constants.MQTT_PROVIDER_URL_DEFAULT_VALUE));
        username.setText(JMeterUtils.getResString(Constants.MQTT_USERNAME_DEFAULT_VALUE));
        password.setText(JMeterUtils.getResString(Constants.MQTT_PASSWORD_DEFAULT_VALUE));


        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        JPanel mainPanel = new VerticalPanel();
        add(mainPanel, BorderLayout.CENTER);
        //-----------------------------------URL/CLIENT_ID---------------------------------------//
        JPanel DPanel = new JPanel();
        DPanel.setLayout(new BoxLayout(DPanel, BoxLayout.X_AXIS));
        DPanel.add(providerURLField);
        DPanel.add(clientId);
        JPanel ControlPanel = new VerticalPanel();
        ControlPanel.add(DPanel);
        ControlPanel.add(createDestinationPane());
        ControlPanel.add(createAuthPane());
        ControlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),
                JMeterUtils.getResString(Constants.MQTT_CONNECTION_LEGEND)));
        mainPanel.add(ControlPanel);
        //---------------------------------------Message Format----------------------------------//
        JPanel StampPanel = new VerticalPanel();
        StampPanel.add(isRetained);
        qos.setLayout(new BoxLayout(qos, BoxLayout.X_AXIS));
        StampPanel.add(this.qos);
        StampPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), JMeterUtils.getResString(Constants.MQTT_MESSAGE_OPTIONS_LEGEND)));
        mainPanel.add(StampPanel);
        //--------------------------------------Message Type-------------------------------------//
        JPanel ContentPanel = new VerticalPanel();
        messageContentSource.setLayout(new BoxLayout(messageContentSource, BoxLayout.X_AXIS));
        ContentPanel.add(messageContentSource);
        //-------------------------------------Content Panel -----------------------------------//

        JPanel messageContentPanel = new JPanel(new BorderLayout());
        messageContentPanel.add(this.textArea, BorderLayout.NORTH);
        messageContentPanel.add(this.textPanel, BorderLayout.CENTER);
        ContentPanel.add(messageContentPanel);
        ContentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),
                JMeterUtils.getResString(Constants.MQTT_CONTENT_LEGEND)));
        mainPanel.add(ContentPanel);

        messageContentSource.addChangeListener(this);
    }

    /**
     * @return JPanel Panel with checkbox to choose  user and password
     */
    private Component createAuthPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalStrut(10));
        panel.add(username);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(password);
        return panel;
    }

    /**
     * @return JPanel that contains destination infos
     */
    private Component createDestinationPane() {
        JPanel panel = new VerticalPanel();
        this.topicName.setLayout((new BoxLayout(topicName, BoxLayout.X_AXIS)));
        panel.add(topicName);
        JPanel TPanel = new JPanel();
        TPanel.setLayout(new BoxLayout(TPanel, BoxLayout.X_AXIS));
        TPanel.add(Box.createHorizontalStrut(100));
        panel.add(TPanel);
        return panel;
    }

    /**
     * To Clear the GUI
     */
    @Override
    public void clearGui() {
        super.clearGui();
        providerURLField.setText(StringUtils.EMPTY);
        clientId.setText(StringUtils.EMPTY);
        topicName.setText(StringUtils.EMPTY);
        username.setText(StringUtils.EMPTY);
        password.setText(StringUtils.EMPTY);
        messageContentSource.setText(Constants.MQTT_CONTENT_TEXT);
        textArea.setText(StringUtils.EMPTY);
        textMessage.setInitialText(StringUtils.EMPTY);
    }

    private void setupSamplerProperties(PublisherSampler sampler) {
        this.configureTestElement(sampler);
        sampler.setProperty(Constants.MQTT_PROVIDER_URL, providerURLField.getText());
        sampler.setProperty(Constants.MQTT_CLIENT_ID, clientId.getText());
        sampler.setProperty(Constants.MQTT_TOPIC_NAME, topicName.getText());
        sampler.setProperty(Constants.MQTT_USERNAME, username.getText());
        sampler.setProperty(Constants.MQTT_PASSWORD, password.getText());
        sampler.setProperty(Constants.MQTT_QOS, qos.getText());
        sampler.setProperty(Constants.MQTT_RETAINED, isRetained.isSelected());
        sampler.setProperty(Constants.MQTT_CONTENT_TEXT, textMessage.getText());
    }

    /**
     * the implementation loads the URL and the soap action for the request.
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        PublisherSampler sampler = (PublisherSampler) el;
        providerURLField.setText(sampler.getPropertyAsString(Constants.MQTT_PROVIDER_URL, Constants.MQTT_PROVIDER_URL_DEFAULT_VALUE));
        topicName.setText(sampler.getPropertyAsString(Constants.MQTT_TOPIC_NAME));
        clientId.setText(sampler.getPropertyAsString(Constants.MQTT_CLIENT_ID));
        username.setText(sampler.getPropertyAsString(Constants.MQTT_USERNAME));
        password.setText(sampler.getPropertyAsString(Constants.MQTT_PASSWORD));
        textMessage.setInitialText(sampler.getPropertyAsString(Constants.MQTT_CONTENT_TEXT));
        textMessage.setCaretPosition(0);
        messageContentSource.setText(sampler.getPropertyAsString(Constants.MQTT_CONTENT_TEXT));
    }

    @Override
    public TestElement createTestElement() {
        PublisherSampler sampler = new PublisherSampler();
        setupSamplerProperties(sampler);
        return sampler;
    }

    @Override
    public String getLabelResource() {
        return Constants.MQTT_PUBLISHER_NAME;
    }

    @Override
    public void modifyTestElement(TestElement s) {
        PublisherSampler sampler = (PublisherSampler) s;
        setupSamplerProperties(sampler);
    }

    /**
     * When we change some parameter by clicking on the GUI
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == messageContentSource) {
            updateChoice(messageContentSource.getText());
        }
    }

    /**
     * To Update the choice of message to send
     *
     * @param command
     */
    private void updateChoice(String command) {
        if (Constants.MQTT_CONTENT_TEXT.equals(command)) {
            this.textArea.setVisible(true);
            this.textPanel.setVisible(true);
        } else if (Constants.MQTT_CONTENT_FILE.equals(command)) {
            this.textArea.setVisible(false);
            this.textPanel.setVisible(false);
        }

        validate();
    }

}
 