#!/usr/bin/env python3
"""Inspect a combined output for the pinned 21.44.528 APK; requires androguard.

Usage: python3 tests/verify_apk.py combined.apk community.mpp
This checks output structure, not Android's runtime verifier or device behavior.
"""
from pathlib import Path
import sys
from zipfile import ZipFile

from loguru import logger
logger.remove()
from androguard.core.axml import AXMLPrinter
from androguard.core.dex import DEX

apk, bundle = map(Path, sys.argv[1:])
namespace = 'http://schemas.android.com/apk/res/android'
key = '{' + namespace + '}key'
own_prefix = 'Lapp/o1hitman1o/extension/audiofocus/'
with ZipFile(bundle) as archive:
    extension = DEX(archive.read('extensions/audio-pause.mpe'))
    classes = [c.get_name() for c in extension.get_classes()]
    assert all(c.startswith(own_prefix) for c in classes), 'Unrelated runtime classes bundled'
    assert own_prefix + 'AudioInterruptPreference;' in classes
    assert own_prefix + 'PauseOnAudioInterruptPatch;' in classes
    print(f'PASS: extension contains only its own {len(classes)} classes')

wanted = {
    'Lcze;', 'Lared;', 'Lapp/morphe/extension/youtube/addon/AddOnManager;',
    own_prefix + 'AudioInterruptPreference;', own_prefix + 'PauseOnAudioInterruptPatch;'
}
found = {}
with ZipFile(apk) as archive:
    assert 'morphe_addon_prefs.xml' not in archive.namelist()
    for path in ['res/xml/morphe_prefs.xml', 'res/xml/morphe_prefs_icons.xml',
                 'res/xml/morphe_prefs_icons_bold.xml']:
        tree = AXMLPrinter(archive.read(path)).get_xml_obj()
        preferences = tree.xpath('//*[@android:key="morphe_pause_on_audio_interrupt"]',
                                 namespaces={'android': namespace})
        assert len(preferences) == 1, (path, 'Missing or duplicate preference')
        preference = preferences[0]
        assert preference.tag == 'app.o1hitman1o.extension.audiofocus.AudioInterruptPreference'
        assert preference.get('{' + namespace + '}defaultValue') == 'false'
        assert preference.getparent().get(key) == 'morphe_settings_screen_12_video_sort_by_key', (
            path, 'Video screen absent or preference placed in the wrong screen')
        print('PASS: one disabled-by-default switch in Video:', path)
    for entry in sorted((e for e in archive.infolist() if e.filename.endswith('.dex')),
                        key=lambda e: e.file_size):
        dex = DEX(archive.read(entry))
        for cls in dex.get_classes():
            if cls.get_name() in wanted:
                assert cls.get_name() not in found, 'Duplicate class definition'
                found[cls.get_name()] = cls
        if wanted <= found.keys():
            break
assert wanted <= found.keys(), wanted - found.keys()
for owner, name, hook, register in [
    ('Lcze;', '<init>', 'overridePauseWhenDucked', 6),
    ('Lared;', 'onAudioFocusChange', 'overrideAudioFocusChange', 17),
    ('Lapp/morphe/extension/youtube/addon/AddOnManager;', 'registerAddOns', 'initialize', None)
]:
    method = next(m for m in found[owner].get_methods() if m.get_name() == name)
    instructions = list(method.get_instructions())
    assert own_prefix in instructions[0].get_output()
    assert hook in instructions[0].get_output()
    if register is not None:
        assert instructions[0].get_name() == 'invoke-static/range'
        assert instructions[0].get_operands()[0][1] == register
        assert instructions[1].get_name() == 'move-result'
        assert instructions[1].get_operands()[0][1] == register
    print('PASS: generated hook and register:', owner, name)
constructor = next(m for m in found[own_prefix + 'AudioInterruptPreference;'].get_methods()
                   if m.get_name() == '<init>')
assert any('initialize()V' in i.get_output() for i in constructor.get_instructions())
print('PASS: preference initializes its setting before Morphe synchronizes the UI')
