// Demo data for Redisson Client Reuse Companion -- used with
// `./gradlew runIde` to capture the real Marketplace screenshot. Open
// this file, the warning should appear on the Redisson.create() line
// inside getUnsafely.

class CacheService {

    private final RedissonClient redisson;

    CacheService(Config config) {
        // Built once, in the constructor -- NOT flagged.
        this.redisson = Redisson.create(config);
    }

    String getUnsafely(Config config, String key) {
        // A new client built here on every call -- FLAGGED. Each
        // instance spins up its own connection pool to Redis.
        RedissonClient redisson = Redisson.create(config);
        return redisson.getBucket(key).get().toString();
    }

    String getSafely(String key) {
        // Reuses the instance built once in the constructor -- NOT
        // flagged.
        return redisson.getBucket(key).get().toString();
    }
}
