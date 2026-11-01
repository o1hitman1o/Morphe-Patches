/*
 * Copyright 2026 Morphe.
 * https://github.com/MorpheApp/morphe-patches/issues/1320
 *
 * Adapted from ReVanced's Pause on audio interrupt patch (PR #6464).
 * https://github.com/PatrickMitchellUI/YT-Revanced-patches/blob/946f1c2498ce118f9a092c2a7c6af377d165f698/extensions/youtube/src/main/java/app/revanced/extension/youtube/patches/PauseOnAudioInterruptPatch.java
 *
 * See the included NOTICE file for GPLv3 Section 7 terms that apply to Morphe contributions.
 */

package app.o1hitman1o.extension.audiofocus;

import android.media.AudioManager;

import app.morphe.extension.shared.settings.BooleanSetting;

@SuppressWarnings("unused")
public final class PauseOnAudioInterruptPatch {

    private static final BooleanSetting ENABLED =
            new BooleanSetting("morphe_pause_on_audio_interrupt", false, true);

    public static void initialize() {
        // Class initialization registers the setting with Morphe.
    }

    /**
     * Injection point.
     */
    public static boolean overridePauseWhenDucked(boolean original) {
        return original || ENABLED.get();
    }

    /**
     * Injection point.
     */
    public static int overrideAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
                && ENABLED.get()) {
            return AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
        }
        return focusChange;
    }
}

