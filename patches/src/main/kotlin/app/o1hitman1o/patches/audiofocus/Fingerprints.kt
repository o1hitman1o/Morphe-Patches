/*
 * Copyright 2026 Morphe.
 * https://github.com/MorpheApp/morphe-patches/issues/1320
 *
 * Adapted from ReVanced's Pause on audio interrupt patch (PR #6464).
 * https://github.com/PatrickMitchellUI/YT-Revanced-patches/tree/946f1c2498ce118f9a092c2a7c6af377d165f698/patches/src/main/kotlin/app/revanced/patches/youtube/misc/audiofocus
 *
 * See the included NOTICE file for GPLv3 Section 7 terms that apply to Morphe contributions.
 */

package app.o1hitman1o.patches.audiofocus

import app.morphe.patcher.Fingerprint

/**
 * AudioFocusRequestCompat.Builder.build().
 */
internal object AudioFocusRequestBuilderFingerprint : Fingerprint(
    isStatic = false,
    returnType = "L",
    parameters = listOf(),
    strings = listOf(
        "Can't build an AudioFocusRequestCompat instance without a listener"
    )
)

internal object AudioFocusChangeListenerFingerprint : Fingerprint(
    name = "onAudioFocusChange",
    isStatic = false,
    returnType = "V",
    parameters = listOf("I"),
    strings = listOf(
        "AudioFocus DUCK",
        "AudioFocus loss; Will lower volume"
    )
)

