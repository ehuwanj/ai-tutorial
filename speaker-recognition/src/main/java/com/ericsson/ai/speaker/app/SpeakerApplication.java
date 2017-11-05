package com.ericsson.ai.speaker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@Configuration
@ImportResource({"classpath*:application-context.xml"})
public class SpeakerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpeakerApplication.class, args);
    }
}
