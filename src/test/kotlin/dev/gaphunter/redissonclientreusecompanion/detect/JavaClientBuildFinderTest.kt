package dev.gaphunter.redissonclientreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaClientBuildFinderTest : BasePlatformTestCase() {

    fun `test a client built inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "CacheService.java",
            """
            class CacheService {
                void get(String key) {
                    RedissonClient redisson = Redisson.create(config);
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaClientBuildFinder.findAll(file).size)
    }

    fun `test a client built inside a constructor is not flagged`() {
        val file = myFixture.configureByText(
            "CacheService.java",
            """
            class CacheService {
                private final RedissonClient redisson;
                CacheService() {
                    redisson = Redisson.create(config);
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaClientBuildFinder.findAll(file).isEmpty())
    }

    fun `test an unrelated create call is never flagged`() {
        val file = myFixture.configureByText(
            "CacheService.java",
            """
            class CacheService {
                void get(String key) {
                    Order order = Order.create(id);
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaClientBuildFinder.findAll(file).isEmpty())
    }
}
