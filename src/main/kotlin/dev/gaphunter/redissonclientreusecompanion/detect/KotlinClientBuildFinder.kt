package dev.gaphunter.redissonclientreusecompanion.detect

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.redissonclientreusecompanion.model.ClientBuildHit
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/** Kotlin counterpart of [JavaClientBuildFinder]. */
object KotlinClientBuildFinder {

    fun findAll(file: PsiFile): List<ClientBuildHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<ClientBuildHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
                super.visitDotQualifiedExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(expression: KtDotQualifiedExpression): ClientBuildHit? {
        val call = expression.selectorExpression as? KtCallExpression ?: return null
        if (call.calleeExpression?.text != "create") return null
        if (expression.receiverExpression.text != "Redisson") return null

        if (PsiTreeUtil.getParentOfType(expression, KtConstructor::class.java) != null) return null
        if (PsiTreeUtil.getParentOfType(expression, KtNamedFunction::class.java) == null) return null

        return ClientBuildHit(leafOf(expression))
    }

    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
