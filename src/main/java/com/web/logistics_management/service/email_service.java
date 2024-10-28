package com.web.logistics_management.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.databind.JsonNode;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class email_service {


    @Value("${aws.access.id}")
    private String AWS_ACCESS_ID;

    @Value("${aws.access.key}")
    private String AWS_ACCESS_KEY;

    @Value("${aws.access.region}")
    private String AWS_ACCESS_REGION;

    public void sendEmail(String email, String head, String main) {
        try {
            // AWS 인증 정보 설정
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(AWS_ACCESS_ID, AWS_ACCESS_KEY);

            // AWS SES 클라이언트 생성
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(AWS_ACCESS_REGION)
                    .build();

            // 이메일 메시지 생성
            Destination destination = new Destination().withToAddresses("mnl005@naver.com");
            Content subject = new Content().withData(head);
            Content textBody = new Content().withData(main);
            Body body = new Body().withText(textBody);
            Message message = new Message().withSubject(subject).withBody(body);

            // 이메일 전송 요청 생성
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(email)
                    .withDestination(destination)
                    .withMessage(message);

            // 이메일 전송
            client.sendEmail(request);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }
}
