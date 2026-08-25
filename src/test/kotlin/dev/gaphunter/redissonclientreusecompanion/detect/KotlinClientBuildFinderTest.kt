package dev.gaphunter.redissonclientreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinClientBuildFinderTest : BasePlatformTestCase() {

    fun `test a client built inside a regular function is flagged`() {
        val file = myFixture.configureByText(
            "CacheService.kt",
            """
            class CacheService {
                fun get(key: String) {
                    val redisson = Redisson.create(config)
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinClientBuildFinder.findAll(file).size)
    }

    fun `test a client built as a class property is not flagged`() {
        val file = myFixture.configureByText(
            "CacheService.kt",
            """
            class CacheService {
                val redisson = Redisson.create(config)
            }
            """.trimIndent(),
        )
        assertTrue(KotlinClientBuildFinder.findAll(file).isEmpty())
    }

    fun `test an unrelated create call is never flagged`() {
        val file = myFixture.configureByText(
            "CacheService.kt",
            """
            class CacheService {
                fun get(key: String) {
                    val order = Order.create(id)
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinClientBuildFinder.findAll(file).isEmpty())
    }
}
