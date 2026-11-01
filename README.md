# Hitman Audio Pause

A community add-on for YouTube, compatible with official Morphe patches. Maintained by
o1hitman1o; this project is not maintained or endorsed by Morphe.

Adds **Settings → Morphe → Video → Pause on audio interrupt**. The option is off by
default and requires restarting YouTube after a change. When enabled, it requests
pause-on-duck behavior and converts duck notifications into temporary focus loss,
using YouTube's existing pause/resume handling.

## Status

The community bundle has passed:

- GitHub Actions build and 36 Java boundary checks.
- Strict fingerprint validation with official Video quality and its settings dependencies,
  loading the community bundle before the official bundle.
- Normal patch application with all 111 recommended patches, loading the official bundle first.
- Inspection of the generated DEX hooks and registers, registration call, and the preference
  in all three layouts. The extension contains only its own three classes.

Validation used Morphe Patches 2.1.0-dev.5 and Morphe Desktop 1.20.0 on the APK below.
Full-set strict validation stops at the official Downloads fingerprint (11 matches);
that does not affect the successful normal-mode run or the focused strict integration test.
Android runtime/device testing of this community bundle is still pending. It is not a
validated public release yet.

The initial compatibility declaration is restricted to the inspected stock APK:

- Package: `com.google.android.youtube`
- Version: `21.44.528`, version code `1561310186`, Android 10+
- SHA-256: `55be430ce82dcedcb737ea27d1b78576737b75d11cde04aa89989e5376d3ae6c`

Other APK versions/builds have not been verified for this community port.

## Apply with official patches

1. Add official Morphe Patches and this bundle as separate patch sources.
2. Enable Expert mode and experimental app versions for these sources.
3. Select the stock APK listed above, your usual official YouTube patches (which
   bring their Settings dependency), and this bundle's Pause on audio interrupt
   patch in the same patching run.
4. Install, then enable the Video setting and restart YouTube.

The Video screen is supplied by selected official patches such as Video quality.
If it is absent, Morphe's add-on contract places the switch at the settings root.

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

To inspect a combined output APK, install `androguard` and run:

```sh
python3 tests/verify_apk.py combined.apk patches-1.0.0.mpp
```

The output check is pinned to this APK's verified class/register layout. It expects the
Video screen to be provided by the selected official patches.

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
