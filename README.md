# Hitman Audio Pause

A community add-on for YouTube, compatible with official Morphe patches. Maintained by
o1hitman1o; this project is not maintained or endorsed by Morphe.

Adds **Settings → Morphe → Video → Pause on audio interrupt**. The option is off by
default and requires restarting YouTube after a change. When enabled, it requests
pause-on-duck behavior and converts duck notifications into temporary focus loss,
using YouTube's existing pause/resume handling.

## Status

This is the initial community-bundle port. The original fork implementation was
compiled, applied to the stock APK, inspected, and tested on a device. The new
cross-bundle settings and registration integration still require a complete build,
combined patch application, and device testing. No community release is validated yet.

The initial compatibility declaration is restricted to the inspected stock APK:

- Package: `com.google.android.youtube`
- Version: `21.44.528`, version code `1561310186`, Android 10+
- SHA-256: `55be430ce82dcedcb737ea27d1b78576737b75d11cde04aa89989e5376d3ae6c`

Other APK versions/builds have not been verified for this community port.

## Apply with official patches

1. Add official Morphe Patches and this bundle as separate patch sources.
2. Enable Expert mode and experimental app versions for these sources.
3. Select the stock APK listed above, official Morphe Settings and your usual official
   patches, and this bundle's Pause on audio interrupt patch in the same patching run.
4. Install, then enable the Video setting and restart YouTube.

Use the official Morphe source, not the earlier full-bundle fork: that fork already
contains the feature. Do not apply both implementations or patch a previously patched APK.

This add-on needs an official bundle implementing `AddOnManager.registerAddOns()` and
the `morphe_addon_prefs.xml` preference-merge contract. A missing runtime contract
causes patching to fail. It does not load official patch classes across bundle class
loaders or include copies of Morphe settings/extension classes.

After a validated release is published, the repository can be added through
https://morphe.software/add-source?github=o1hitman1o/youtube-audio-pause

## Development

Based on the official Morphe patches template. Keep development changes on `dev`.
Configure GitHub Packages credentials as described in the template documentation,
then run:

```sh
./gradlew :patches:buildAndroid --no-daemon
python3 tests/verify_runtime.py
```

The Java checks use Android/BooleanSetting boundary stubs. They verify helper logic
and preference initialization, not Android behavior, the Morphe registry, DEX
verification, or cross-bundle patch application.

The template's Release workflow builds/releases the bundle and updates generated
metadata. On a branch other than `main` or `dev`, it compiles and uploads a test
artifact without releasing. Do not publish a release until combined application and
device tests pass. Keep `patches-bundle.json`, `patches-list.json`, and `CHANGELOG.md`
under the template's release automation rather than editing their generated contents.

## Attribution and license

The audio-focus hooks were adapted from the implementation in
[o1hitman1o/morphe-patches](https://github.com/o1hitman1o/morphe-patches/tree/510a931869e4603f278091ea81292999c3f36d35),
originally adapted from ReVanced's Pause on audio interrupt patch (PR #6464).
Original source notices remain in the relevant files.

GPLv3, with the additional terms in [NOTICE](NOTICE). The template's LICENSE and NOTICE
are retained. This project uses its own name and extension namespace.
