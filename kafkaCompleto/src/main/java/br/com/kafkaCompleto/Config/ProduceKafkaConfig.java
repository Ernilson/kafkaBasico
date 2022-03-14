package br.com.kafkaCompleto.Config;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ProduceKafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;
    @Bean
    public ProducerFactory<String, String> producerFactory(){
        var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, String>kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    // Metodos para habilitar a aplicação para criar novos bens
    @Bean
    public KafkaAdmin kafkaAdmin(){
        var configs = new HashMap<String, Object>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
            return new KafkaAdmin(configs);
    }

    // Metodos para criar novos topicos
    @Bean
    public NewTopic topic1(){
        //retorna as partiços e as replicas
        return new NewTopic("topic-1", 10, Short.valueOf("1"));

        //A partir da versão 2.6 do spring kafka é permitido retornar o que esta configurado no broken
        //  return TopicBuilder.name("topic-1").build();
    }

    //A partir da versão 2.7 do spring kafka é permitido cria varios topicos na mesma ben
    /*
    @Bean
    public KafkaAdmin.NewTopics topics(){
        return new KafkaAdmin.NewTopics(
           TopicBuilder.name("topic-1").build(),
           TopicBuilder.name("topic-2").build(),
           TopicBuilder.name("topic-3").build()
        );
    }
*/
}
