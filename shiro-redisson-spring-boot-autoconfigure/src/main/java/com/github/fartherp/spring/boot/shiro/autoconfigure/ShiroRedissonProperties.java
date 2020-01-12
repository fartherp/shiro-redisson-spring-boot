/**
 *    Copyright (c) 2020 CK.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.fartherp.spring.boot.shiro.autoconfigure;

import com.github.fartherp.shiro.CodecType;
import com.github.fartherp.shiro.ExpireType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Shiro Redisson Properties.
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
        private int cacheLruSize;
        private CodecType codecType;
        private CodecType codecKeysType;

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

		public int getCacheLruSize() {
			return cacheLruSize;
		}

		public void setCacheLruSize(int cacheLruSize) {
			this.cacheLruSize = cacheLruSize;
		}

		public CodecType getCodecType() {
			return codecType;
		}

		public void setCodecType(CodecType codecType) {
			this.codecType = codecType;
		}

		public CodecType getCodecKeysType() {
			return codecKeysType;
		}

		public void setCodecKeysType(CodecType codecKeysType) {
			this.codecKeysType = codecKeysType;
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
