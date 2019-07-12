/*
 * Copyright (c) 2019. CK. All rights reserved.
 */

package com.github.fartherp.spring.boot.shiro.autoconfigure;

import com.github.fartherp.shiro.CodecType;
import com.github.fartherp.shiro.ExpireType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by IntelliJ IDEA.
 *
 * @author CK
 * @date 2019/1/14
 */
@ConfigurationProperties(prefix = "shiro.redisson")
public class ShiroRedissonProperties {

    private final ShiroRedissonCache cache = new ShiroRedissonCache();

    private final ShiroRedissonSession session = new ShiroRedissonSession();

    public ShiroRedissonCache getCache() {
        return cache;
    }

    public ShiroRedissonSession getSession() {
        return session;
    }

    static class ShiroRedissonCache {
        private String cacheKeyPrefix;
        private long ttl;
        private String principalIdFieldName;
        private int cacheLruSize;

        public String getCacheKeyPrefix() {
            return cacheKeyPrefix;
        }

        public void setCacheKeyPrefix(String cacheKeyPrefix) {
            this.cacheKeyPrefix = cacheKeyPrefix;
        }

        public long getTtl() {
            return ttl;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }

        public String getPrincipalIdFieldName() {
            return principalIdFieldName;
        }

        public void setPrincipalIdFieldName(String principalIdFieldName) {
            this.principalIdFieldName = principalIdFieldName;
        }

		public int getCacheLruSize() {
			return cacheLruSize;
		}

		public void setCacheLruSize(int cacheLruSize) {
			this.cacheLruSize = cacheLruSize;
		}
	}

    static class ShiroRedissonSession {
        private String sessionKeyPrefix;
        private ExpireType expireType;
        private boolean sessionInMemoryEnabled;
        private long sessionInMemoryTimeout;
        private CodecType codecType;
        private int sessionLruSize;

        public String getSessionKeyPrefix() {
            return sessionKeyPrefix;
        }

        public void setSessionKeyPrefix(String sessionKeyPrefix) {
            this.sessionKeyPrefix = sessionKeyPrefix;
        }

        public ExpireType getExpireType() {
            return expireType;
        }

        public void setExpireType(ExpireType expireType) {
            this.expireType = expireType;
        }

        public boolean isSessionInMemoryEnabled() {
            return sessionInMemoryEnabled;
        }

        public void setSessionInMemoryEnabled(boolean sessionInMemoryEnabled) {
            this.sessionInMemoryEnabled = sessionInMemoryEnabled;
        }

        public long getSessionInMemoryTimeout() {
            return sessionInMemoryTimeout;
        }

        public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
            this.sessionInMemoryTimeout = sessionInMemoryTimeout;
        }

        public CodecType getCodecType() {
            return codecType;
        }

        public void setCodecType(CodecType codecType) {
            this.codecType = codecType;
        }

		public int getSessionLruSize() {
			return sessionLruSize;
		}

		public void setSessionLruSize(int sessionLruSize) {
			this.sessionLruSize = sessionLruSize;
		}
	}
}
