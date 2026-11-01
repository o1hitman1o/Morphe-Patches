#!/usr/bin/env python3
"""Check production Java helpers with boundary stubs; not an Android integration test."""
from pathlib import Path
import shutil
import subprocess
import tempfile

repo = Path(__file__).resolve().parents[1]
source = repo / 'extensions/extension/src/main/java/app/o1hitman1o/extension/audiofocus'
with tempfile.TemporaryDirectory() as temporary:
    root = Path(temporary)
    stubs = {
        'android/media/AudioManager.java': '''package android.media;
public final class AudioManager {
 public static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
 public static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
}''',
        'android/content/Context.java': 'package android.content; public class Context {}',
        'android/util/AttributeSet.java': 'package android.util; public interface AttributeSet {}',
        'android/preference/SwitchPreference.java': '''package android.preference;
public class SwitchPreference {
 public SwitchPreference(android.content.Context context, android.util.AttributeSet attributes) {}
}''',
        'app/morphe/extension/shared/settings/BooleanSetting.java': '''package app.morphe.extension.shared.settings;
public final class BooleanSetting {
 public static boolean enabled;
 public static int registrations;
 public BooleanSetting(String key, Boolean defaultValue, boolean rebootApp) {
  if (!key.equals("morphe_pause_on_audio_interrupt") || defaultValue || !rebootApp)
   throw new AssertionError("Wrong key, default, or restart flag");
  registrations++;
 }
 public Boolean get() { return enabled; }
}''',
        'RuntimeCheck.java': '''import app.o1hitman1o.extension.audiofocus.AudioInterruptPreference;
import app.o1hitman1o.extension.audiofocus.PauseOnAudioInterruptPatch;
import app.morphe.extension.shared.settings.BooleanSetting;
public final class RuntimeCheck {
 public static void main(String[] args) throws Exception {
  if (args[0].equals("preference-first"))
   new AudioInterruptPreference(new android.content.Context(), null);
  else
   PauseOnAudioInterruptPatch.overridePauseWhenDucked(false);
  if (BooleanSetting.registrations != 1) throw new AssertionError("Setting not initialized once");
  int checks = 0;
  for (boolean enabled : new boolean[] {false, true}) {
   BooleanSetting.enabled = enabled;
   for (boolean original : new boolean[] {false, true}) {
    if (PauseOnAudioInterruptPatch.overridePauseWhenDucked(original) != (original || enabled))
     throw new AssertionError("pauseOnDuck behavior");
    checks++;
   }
   for (int event : new int[] {-3, -2, -1, 0, 1, 2, 3}) {
    int expected = enabled && event == -3 ? -2 : event;
    if (PauseOnAudioInterruptPatch.overrideAudioFocusChange(event) != expected)
     throw new AssertionError("Focus event changed incorrectly: " + event);
    checks++;
   }
  }
  PauseOnAudioInterruptPatch.initialize();
  new AudioInterruptPreference(new android.content.Context(), null);
  if (BooleanSetting.registrations != 1) throw new AssertionError("Duplicate registration");
  System.out.println("PASS: " + args[0] + ": " + checks + " behavior checks; single initialization");
 }
}'''
    }
    for path, contents in stubs.items():
        file = root / path
        file.parent.mkdir(parents=True, exist_ok=True)
        file.write_text(contents)
    for file in source.glob('*.java'):
        shutil.copy(file, root / file.name)
    compiler = [shutil.which('javac')] if shutil.which('javac') else [
        'java', '-m', 'jdk.compiler/com.sun.tools.javac.Main'
    ]
    subprocess.run(compiler + ['-d', str(root), *map(str, root.rglob('*.java'))], check=True)
    for order in ['preference-first', 'hook-first']:
        subprocess.run(['java', '-cp', str(root), 'RuntimeCheck', order], check=True)
