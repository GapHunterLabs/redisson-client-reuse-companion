package dev.gaphunter.redissonclientreusecompanion.model

import com.intellij.psi.PsiElement

/** One `Redisson.create(...)` call found inside a non-constructor method body. */
data class ClientBuildHit(val callElement: PsiElement)
