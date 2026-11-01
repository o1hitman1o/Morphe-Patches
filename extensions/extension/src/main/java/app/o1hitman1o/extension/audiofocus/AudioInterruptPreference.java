/*
 * Copyright 2026 o1hitman1o.
 * See LICENSE and NOTICE for the terms.
 */

package app.o1hitman1o.extension.audiofocus;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

@SuppressWarnings("deprecation")
public final class AudioInterruptPreference extends SwitchPreference {
    public AudioInterruptPreference(Context context, AttributeSet attributes) {
        super(context, attributes);
        // Settings can be opened before the first player hook loads add-ons.
        PauseOnAudioInterruptPatch.initialize();
    }
}
