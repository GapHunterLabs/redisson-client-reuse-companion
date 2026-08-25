package dev.gaphunter.redissonclientreusecompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.redissonclientreusecompanion.model.ClientBuildHit

/**
 * Finds `Redisson.create(...)` calls written inside a non-constructor
 * method body -- Redisson's own Getting Started guide states: "
 * RedissonClient is thread-safe, so create a single instance and reuse
 * it as a shared singleton across your application." Building one
 * inside a regular method means a brand new client (and its own
 * connection pool to the Redis/Valkey server) gets created on every
 * call.
 *
 * **v0.1 scope, stated honestly:** only flags the direct
 * `Redisson.create(...)` call -- matches by simple class/method name,
 * so it works whether the real Redisson jar is on the classpath or
 * not. Never flags a call inside a constructor (a legitimate
 * "create once" location).
 */
object JavaClientBuildFinder {

    fun findAll(file: PsiFile): List<ClientBuildHit> {
        val hits = mutableListOf<ClientBuildHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                super.visitMethodCallExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(call: PsiMethodCallExpression): ClientBuildHit? {
        if (call.methodExpression.referenceName != "create") return null
        val qualifier = call.methodExpression.qualifierExpression ?: return null
        if (qualifier.text != "Redisson") return null

        val containingMethod = PsiTreeUtil.getParentOfType(call, PsiMethod::class.java) ?: return null
        if (containingMethod.isConstructor) return null

        return ClientBuildHit(leafOf(call))
    }

    /** Descends to a real leaf PSI element -- LineMarkerInfo must never anchor on a composite node (SDK_GOTCHAS.md SS20). */
    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
