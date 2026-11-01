/*
 * Copyright 2026 o1hitman1o.
 *
 * Adapted from ReVanced's Pause on audio interrupt patch (PR #6464).
 * See LICENSE and NOTICE for the terms and original attribution.
 */

package app.o1hitman1o.patches.audiofocus

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.util.getReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.util.MethodUtil

private const val EXTENSION_CLASS =
    "Lapp/o1hitman1o/extension/audiofocus/PauseOnAudioInterruptPatch;"
private const val PREFERENCE_CLASS =
    "app.o1hitman1o.extension.audiofocus.AudioInterruptPreference"
private const val ADD_ON_PREFERENCES = "morphe_addon_prefs.xml"
private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"

private val audioInterruptPreferencesPatch = resourcePatch {
    execute {
        val declaration = this[ADD_ON_PREFERENCES]
        if (!declaration.exists()) {
            declaration.writeText("<morphe-add-on-preferences/>")
        }
        document(ADD_ON_PREFERENCES).use { document ->
            val root = document.documentElement
            if (root.tagName != "morphe-add-on-preferences") {
                throw PatchException("Invalid Morphe add-on preference declaration")
            }
            val screen = document.createElement("screen").apply {
                setAttribute("key", "morphe_settings_screen_12_video_sort_by_key")
            }
            val preference = document.createElement(PREFERENCE_CLASS).apply {
                setAttributeNS(ANDROID_NAMESPACE, "android:key", "morphe_pause_on_audio_interrupt")
                setAttributeNS(ANDROID_NAMESPACE, "android:title", "Pause on audio interrupt")
                setAttributeNS(ANDROID_NAMESPACE, "android:summary",
                    "Pause playback instead of lowering volume when other audio plays. Restart after changing.")
                setAttributeNS(ANDROID_NAMESPACE, "android:defaultValue", "false")
            }
            screen.appendChild(preference)
            root.appendChild(screen)
        }
    }
}

@Suppress("unused")
val pauseOnAudioInterruptPatch = bytecodePatch(
    name = "Pause on audio interrupt",
    description = "Adds a Video setting to pause playback instead of lowering volume during audio interruptions.",
) {
    compatibleWith(
        Compatibility(
            name = "YouTube",
            packageName = "com.google.android.youtube",
            apkFileType = ApkFileType.APK_REQUIRED,
            targets = listOf(
                AppTarget("21.44.528", 1561310186, isExperimental = true, minSdk = 29)
            )
        )
    )
    dependsOn(audioInterruptPreferencesPatch)
    extendWith("extensions/audio-pause.mpe")

    execute {
        val builderMethod = AudioFocusRequestBuilderFingerprint.method
        val constructorReference = builderMethod.implementation!!.instructions
            .mapNotNull { it.getReference<MethodReference>() }
            .single { it.definingClass == builderMethod.returnType && it.name == "<init>" }
        val constructor = mutableClassDefBy(constructorReference.definingClass).methods.single {
            MethodUtil.methodSignaturesMatch(it, constructorReference)
        }
        val parameterTypes = constructor.parameterTypes.map { it.toString() }
        if (parameterTypes.count { it == "Z" } != 1) {
            throw PatchException("Expected one pauseOnDuck parameter in audio focus request constructor")
        }
        val pauseOnDuckRegister = 1 + parameterTypes.takeWhile { it != "Z" }.sumOf {
            if (it == "J" || it == "D") 2 else 1
        }
        constructor.addInstructions(
            0,
            """
                invoke-static/range { p$pauseOnDuckRegister .. p$pauseOnDuckRegister }, $EXTENSION_CLASS->overridePauseWhenDucked(Z)Z
                move-result p$pauseOnDuckRegister
            """
        )
        AudioFocusChangeListenerFingerprint.method.addInstructions(
            0,
            """
                invoke-static/range { p1 .. p1 }, $EXTENSION_CLASS->overrideAudioFocusChange(I)I
                move-result p1
            """
        )
    }

    finalize {
        // Cross-bundle patch dependencies are unavailable; resolve the runtime contract last.
        Fingerprint(
            definingClass = "Lapp/morphe/extension/youtube/addon/AddOnManager;",
            name = "registerAddOns",
            isStatic = true,
            returnType = "V",
            parameters = listOf()
        ).method.addInstruction(0, "invoke-static {}, $EXTENSION_CLASS->initialize()V")
    }
}
