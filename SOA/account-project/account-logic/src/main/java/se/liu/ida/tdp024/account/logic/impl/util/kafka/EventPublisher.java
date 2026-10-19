package se.liu.ida.tdp024.account.logic.impl.util.kafka;

public interface EventPublisher {
    void publish(String topic, String message);
}
