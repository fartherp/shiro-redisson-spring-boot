/*
 * Copyright (c) 2019. CK. All rights reserved.
 */

package com.github.fartherp.spring.boot.shiro.autoconfigure;

import com.github.fartherp.shiro.RedisCacheManager;
import com.github.fartherp.shiro.RedisSessionDAO;
import com.github.fartherp.shiro.RedisSessionListener;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author CK
 * @date 2019/1/14
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@ConditionalOnBean(Realm.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@EnableConfigurationProperties(ShiroRedissonProperties.class)
public class ShiroRedissonAutoConfiguration {

    private final ShiroRedissonProperties properties;

    public ShiroRedissonAutoConfiguration(ShiroRedissonProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisCacheManager cacheManager(RedissonClient redisson) {
		ShiroRedissonProperties.ShiroRedissonCache shiroRedissonCache = properties.getCache();
        RedisCacheManager cacheManager = new RedisCacheManager(redisson,
			shiroRedissonCache.getCacheKeyPrefix(), shiroRedissonCache.getPrincipalIdFieldName(),
			shiroRedissonCache.getTtl(), shiroRedissonCache.getCacheLruSize(),
			shiroRedissonCache.getCodecType(), shiroRedissonCache.getCodecKeysType());
        return cacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionDAO sessionDAO(RedisCacheManager cacheManager) {
		ShiroRedissonProperties.ShiroRedissonSession shiroRedissonSession = properties.getSession();
        RedisSessionDAO sessionDAO = new RedisSessionDAO(cacheManager, shiroRedissonSession.getSessionKeyPrefix(),
			shiroRedissonSession.getExpireType(), shiroRedissonSession.isSessionInMemoryEnabled(),
			shiroRedissonSession.getSessionInMemoryTimeout(), shiroRedissonSession.getCodecType(),
			shiroRedissonSession.getSessionLruSize());
        return sessionDAO;
    }

    @Bean
    @ConditionalOnMissingBean(name = "shiroRedisonSessionListener")
    public SessionListener shiroRedisonSessionListener(SessionDAO redisSessionDAO, List<CachingRealm> cachingRealms) {
        return new RedisSessionListener(redisSessionDAO, cachingRealms);
    }

    @Bean
    @ConditionalOnNotWebApplication
    @ConditionalOnMissingBean
    public SessionManager sessionManager(SessionDAO redisSessionDAO, ObjectProvider<SessionListener> sessionListenersProvider) {
        List<SessionListener> sessionListeners = sessionListenersProvider.stream().collect(Collectors.toList());
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setSessionListeners(sessionListeners);
        return sessionManager;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean
    public SessionManager webSessionManager(SessionDAO redisSessionDAO, ObjectProvider<SessionListener> sessionListenersProvider) {
        List<SessionListener> sessionListeners = sessionListenersProvider.stream().collect(Collectors.toList());
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionDAO(redisSessionDAO);
        webSessionManager.setSessionListeners(sessionListeners);
        return webSessionManager;
    }

    @Bean
    @ConditionalOnNotWebApplication
    @ConditionalOnMissingBean
    public SessionsSecurityManager securityManager(CacheManager cacheManager, SessionManager sessionManager, List<Realm> realms) {
        DefaultSecurityManager securityManager = new DefaultSecurityManager(realms);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean
    public SessionsSecurityManager webSecurityManager(CacheManager cacheManager, SessionManager sessionManager, List<Realm> realms) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realms);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }
}
