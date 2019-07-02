package com.junzixiehui.wakaka.infrastructure.common.config.sleuth;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.ErrorParser;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.*;
import org.springframework.cloud.sleuth.instrument.web.client.TraceRestTemplateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author zhoutao
 * @date 2019/4/2
 */
@Configuration
@ConditionalOnProperty(value="spring.sleuth.enabled", matchIfMissing=true)
@AutoConfigureAfter(TraceHttpAutoConfiguration.class)
@AutoConfigureBefore(TraceWebAutoConfiguration.class)
public class SleuthConfig {

    @Value("#{'${spring.sleuth.web.client.skip-uri-prefix}'.split(',')}")
    private List<String> sleuthWebClientSkipUriPrefixList;

    @Value("${spring.sleuth.web.skip-pattern}")
    private String sleuthWebSkipPattern;

    @Bean
    @ConditionalOnProperty(value = "spring.sleuth.web.client.enabled", matchIfMissing = true)
    public TraceRestTemplateInterceptor traceRestTemplateInterceptor(Tracer tracer,
                                                                     HttpSpanInjector spanInjector,
                                                                     HttpTraceKeysInjector httpTraceKeysInjector,
                                                                     ErrorParser errorParser) {
        UrlParttenTraceRestTemplateInterceptor interceptor = new UrlParttenTraceRestTemplateInterceptor(tracer, spanInjector,
                httpTraceKeysInjector, errorParser);
        interceptor.setSkipUriPrefixList(sleuthWebClientSkipUriPrefixList);
        return interceptor;
    }

    @Bean
    @ConditionalOnProperty(value = "spring.sleuth.web.enabled", matchIfMissing = true)
    public TraceFilter traceFilter(BeanFactory beanFactory) {
        return new UrlParttenTraceFilter(beanFactory, Pattern.compile(sleuthWebSkipPattern));
    }
}
