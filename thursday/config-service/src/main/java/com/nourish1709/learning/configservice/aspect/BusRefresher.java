package com.nourish1709.learning.configservice.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bus.event.Destination;
import org.springframework.cloud.bus.event.RefreshRemoteApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BusRefresher {

    private final ApplicationEventPublisher publisher;
    private final Destination.Factory destinationFactory;

    @Value("${spring.cloud.bus.id:application}")
    private String busId;

    @After("@annotation(com.nourish1709.learning.configservice.aspect.RefreshSettings) && args(application, ..)")
    public void invokeRefreshEvent(String application) {
//        var destination = getDestination(application);

        log.info("Broadcasting a refresh event through the bus: {}. Destination: {}", busId, application);
        publisher.publishEvent(new RefreshRemoteApplicationEvent(this, this.busId, destinationFactory.getDestination(application)));
    }
//
//    private String getDestination(String application) {
//        return isProfileSpecific(application) ?
//                substringBeforeLast(application, "-") : application;
//    }
//
//    private boolean isProfileSpecific(String application) {
//        return countMatches(substringAfter(application, "treeservices-"), "-") > 0;
//    }
}
