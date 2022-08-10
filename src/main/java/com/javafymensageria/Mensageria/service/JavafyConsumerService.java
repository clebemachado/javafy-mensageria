package com.javafymensageria.Mensageria.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javafymensageria.Mensageria.DTO.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JavafyConsumerService {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(
            topics = "${kafka.topico}",
            groupId = "group1",
            containerFactory = "listenerContainerFactory",
            clientIdPrefix = "topico-javafy")
    public void consumirMensagem(@Payload String mensagem) throws JsonProcessingException {

        EmailDTO emailDTO = objectMapper.readValue(mensagem, EmailDTO.class);
        String novaMsg = mensagem;
        System.out.println(mensagem);
        emailService.sendEmail(emailDTO);

    }

}
