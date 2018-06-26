package org.miklosbarabas.demo.configs;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LogbackAccessConfig {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return factory -> {
            if(factory instanceof TomcatEmbeddedServletContainerFactory){
                TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;

                LogbackValve  logbackValve = new LogbackValve();
                logbackValve.setFilename("logback-access.xml");
                containerFactory.addContextValves(logbackValve);
            }
        };
    }
}
