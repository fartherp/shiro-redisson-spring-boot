/*
 * Copyright (c) 2019. CK. All rights reserved.
 */

package com.github.fartherp.spring.boot.shiro.autoconfigure;

import com.github.fartherp.shiro.RedisCacheManager;
import com.github.fartherp.shiro.RedisSessionDAO;
import com.github.fartherp.shiro.RedisSessionListener;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.junit.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 *  @author: cuiyuqiang
 *  @email: cuiyuqiang@ddjf.com.cn
 *  @date: 2019/6/20 11:13
 *  @project: risk-control-parent
 * </pre>
 */
public class WebShiroRedissonAutoConfigurationTest extends ShiroRedissonAutoConfigurationTest {
	private static final MockServletWebServerFactory webServerFactory = new MockServletWebServerFactory();

	private WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(RedissonAutoConfiguration.class, ShiroRedissonAutoConfiguration.class))
		.withUserConfiguration(TestConfiguration.class, Config.class)
		.withPropertyValues("spring.redis.redisson.config:classpath:redisson.yaml");

	@Test
	public void testDefaultCacheManager() {
		this.contextRunner.run((context) -> {
			assertThat(context.getBean(RedisCacheManager.class)).isNotNull();
		});
	}

	@Test
	public void testOverrideCacheManager() {
		this.contextRunner.withPropertyValues("shiro.redisson.cache.cache-key-prefix:a",
			"shiro.redisson.cache.ttl:1", "shiro.redisson.cache.principal-id-field-name:testId")
			.run((context) -> {
				RedisCacheManager redisCacheManager = context.getBean(RedisCacheManager.class);
				assertThat(redisCacheManager.getKeyPrefix()).isEqualTo("a");
				assertThat(redisCacheManager.getTtl()).isEqualTo(1);
				assertThat(redisCacheManager.getPrincipalIdFieldName()).isEqualTo("testId");
			});
	}

	@Test
	public void testDefaultSessionDAO() {
		this.contextRunner.run((context) -> {
			assertThat(context.getBean(SessionDAO.class)).isNotNull();
		});
	}

	@Test
	public void testOverrideSessionDAO() {
		this.contextRunner.withPropertyValues("shiro.redisson.session.session-key-prefix:a",
			"shiro.redisson.session.expire-type:no_expire", "shiro.redisson.session.session-in-memory-enabled:false",
			"shiro.redisson.session.session-in-memory-timeout:1", "shiro.redisson.session.codec-type:long_codec")
			.run((context) -> {
				RedisSessionDAO redisSessionDAO = context.getBean(RedisSessionDAO.class);
				assertThat(redisSessionDAO.getSessionKeyPrefix()).isEqualTo("a");
				assertThat(redisSessionDAO.getExpire()).isEqualTo(-1);
				assertThat(redisSessionDAO.isSessionInMemoryEnabled()).isEqualTo(false);
				assertThat(redisSessionDAO.getSessionInMemoryTimeout()).isEqualTo(1);
			});
	}

	@Test
	public void testDefaultShiroRedisonSessionListener() {
		this.contextRunner.run((context) -> {
			assertThat(context.getBean("shiroRedisonSessionListener", SessionListener.class)).isNotNull();
			assertThat(context.getBean(RedisSessionListener.class)).isNotNull();
		});
	}

	@Test
	public void testWebSessionManager() {
		this.contextRunner.run((context) -> {
			assertThat(context.getBean(DefaultSessionManager.class)).isNotNull();
			DefaultWebSessionManager defaultSessionManager = context.getBean(DefaultWebSessionManager.class);
			assertThat(defaultSessionManager.getSessionDAO()).isEqualTo(context.getBean(SessionDAO.class));
			assertThat(defaultSessionManager.getSessionListeners().size()).isEqualTo(1);
			assertThat(((List<SessionListener>) defaultSessionManager.getSessionListeners()).get(0)).isEqualTo(context.getBean(RedisSessionListener.class));
		});
	}

	@Test
	public void testWebSecurityManager() {
		this.contextRunner.run((context) -> {
			assertThat(context.getBean(DefaultSecurityManager.class)).isNotNull();
			DefaultWebSecurityManager defaultSessionManager = context.getBean(DefaultWebSecurityManager.class);
			assertThat(defaultSessionManager.getSessionManager()).isEqualTo(context.getBean(DefaultWebSessionManager.class));
			assertThat(defaultSessionManager.getCacheManager()).isEqualTo(context.getBean(RedisCacheManager.class));
		});
	}

	@Configuration
	public static class Config {
		@Bean
		public ServletWebServerFactory webServerFactory() {
			return webServerFactory;
		}

		@Bean
		public WebServerFactoryCustomizerBeanPostProcessor ServletWebServerCustomizerBeanPostProcessor() {
			return new WebServerFactoryCustomizerBeanPostProcessor();
		}
	}
}
