package fon.mas.novica.spring.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class RequestOriginFilter implements Filter, Ordered {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Environment environment;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isRequestFromGateway(request)) {
            log.debug("Request accepted...");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.debug("Request isn't from gateway");
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Direct access is denied");
        }
    }

    private boolean isRequestFromGateway(HttpServletRequest request){
        String gatewayIp = getGatewayIpAddress();
        String requestIp = request.getRemoteAddr();

        return gatewayIp.equals(requestIp);
    }

    private String getGatewayIpAddress(){
        List<ServiceInstance> instances = discoveryClient.getInstances("Gateway");
        if (instances != null && !instances.isEmpty()){
            try {
                EurekaServiceInstance gateway = (EurekaServiceInstance) instances.get(0);
                return gateway.getInstanceInfo().getIPAddr();
            } catch (ClassCastException ignored){
                return instances.get(0).getHost();
            }
        }
//        throw new RuntimeException("Gateway not found!!!");
        log.warn("GATEWAY NOT FOUND!!! FALLING BACK TO DEFAULT ADDRESS");
        return environment.getProperty("gateway.ip");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

