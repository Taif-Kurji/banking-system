package se.liu.ida.tdp024.account.logic.test.util;

import java.util.ArrayList;
import java.util.List;

import se.liu.ida.tdp024.account.logic.impl.util.kafka.EventPublisher;

public class FakeEventPublisher implements EventPublisher {

    public List<String> messages = new ArrayList<>();

    @Override
    public void publish(String topic, String message) {
        messages.add(topic + ": " + message);
    }
}
