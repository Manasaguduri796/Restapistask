package org.sanketika.springbootproject1.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class KafkaProducerService {

    private NewTopic topic;
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public KafkaProducerService(NewTopic topic, KafkaTemplate<String, Object> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDataset(String datasetId, String method, String initiatedAt, String eventType, String message, String status) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("event_Type", eventType);
        event.put("TimeStamp", Instant.now().toString());

        Map<String, Object> actions = new LinkedHashMap<>();
        actions.put("Dataset_Id", datasetId);
        actions.put("Method", method);
        actions.put("initiated_at", initiatedAt);

        event.put("Action", actions);
        event.put("status", status);
        event.put("message", message);


        try {
            String eventJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
            kafkaTemplate.send(topic.name(), eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Kafka event", e);
        }
    }
    public void getAllDataset(String dataset,String method, String initiatedAt, String eventType, String message, String status) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("event_Type", eventType);
        event.put("TimeStamp", Instant.now().toString());

        Map<String, Object> actions = new LinkedHashMap<>();
        actions.put("Dataset", dataset);
        actions.put("Method", method);
        actions.put("initiated_at", initiatedAt);

        event.put("Action", actions);
        event.put("status", status);
        event.put("message", message);


        try {
            String eventJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
            kafkaTemplate.send(topic.name(), eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Kafka event", e);
        }
    }
    public void sendDatasetByStatus(String datasetId, String method, String initiatedAt, String eventType, String message, String status) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("event_Type", eventType);
        event.put("TimeStamp", Instant.now().toString());

        Map<String, Object> actions = new LinkedHashMap<>();
        actions.put("Dataset_Id", datasetId);
        actions.put("Method", method);
        actions.put("initiated_at", initiatedAt);

        event.put("Action", actions);
        event.put("status", status);
        event.put("message", message);

        try {
            String eventJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
            kafkaTemplate.send(topic.name(), eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Kafka event", e);
        }
    }

}
