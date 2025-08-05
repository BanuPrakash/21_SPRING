package com.adobe.datarestdemo.cfg;

import com.adobe.datarestdemo.entity.Vehicle;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer  {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
//        cors.addMapping("/api/vehicles").allowedOrigins("http://domain2");
        ExposureConfiguration exposureConfiguration =
                config.getExposureConfiguration();
        exposureConfiguration.forDomainType(Vehicle.class)
                .withItemExposure(((metadata, httpMethods) -> {
                        return httpMethods.disable(HttpMethod.DELETE);
                }));
     }
}
