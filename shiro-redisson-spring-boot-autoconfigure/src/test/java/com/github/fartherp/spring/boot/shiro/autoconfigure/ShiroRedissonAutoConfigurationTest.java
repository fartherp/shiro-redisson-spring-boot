/*
 * Copyright (c) 2019. CK. All rights reserved.
 */

package com.github.fartherp.spring.boot.shiro.autoconfigure;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author CK
 * @date 2019/6/20
 */
public abstract class ShiroRedissonAutoConfigurationTest {

	@Configuration
	protected static class TestConfiguration {
		@Bean
		public HashedCredentialsMatcher hashedCredentialsMatcher() {
			HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
			hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
			hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
			return hashedCredentialsMatcher;
		}

		@Bean
		public MyShiroRealm myShiroRealm(CredentialsMatcher hashedCredentialsMatcher) {
			MyShiroRealm myShiroRealm = new MyShiroRealm();
			myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
			return myShiroRealm;
		}

		@Bean
		public RedisProperties redisProperties() {
			return new RedisProperties();
		}
	}

	public static class MyShiroRealm extends AuthorizingRealm {

		@Override
		protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
			return null;
		}

		@Override
		protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
			return null;
		}
	}
}
