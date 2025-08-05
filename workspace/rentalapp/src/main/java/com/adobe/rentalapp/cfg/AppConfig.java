package com.adobe.rentalapp.cfg;

import com.adobe.rentalapp.service.PostService;
import com.adobe.rentalapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
@RequiredArgsConstructor
public class AppConfig {
    private final CacheManager cacheManager;
    // externalize messages and i18N
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        return messageSource;
    }

//    @Scheduled(fixedRate = 2000)
    //https://spring.io/blog/2020/11/10/new-in-spring-5-3-improved-cron-expressions
    @Scheduled(cron = "0 0/30 * * * *")
    public  void clearCache() {
        System.out.println("Cache Cleared!!!");
        cacheManager.getCacheNames().forEach(cache -> {
            cacheManager.getCache(cache).clear();
        });
    }


    @Bean(name="posts-pool")
    public Executor postsPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("POSTS_POOL");
        executor.initialize();
        return executor;

//        return Executors.newFixedThreadPool(10);
    }

    @Bean(name="users-pool")
    public Executor usersPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("USERS_POOL");
        executor.initialize();
        return executor;
//        return Executors.newFixedThreadPool(10);
    }

    // can be used along with RestTemplate / RestClient / WebClient
    public HttpServiceProxyFactory getHttpProxy() {
        RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com");
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory;
    }
    // return implementation of PostService
    @Bean
    PostService postService() {
        return getHttpProxy().createClient(PostService.class);
    }

    // return implementation of UserService
    @Bean
    UserService  userService() {
        return getHttpProxy().createClient(UserService.class);
    }
}
