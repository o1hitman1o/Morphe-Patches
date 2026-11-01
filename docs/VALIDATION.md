# Validation

Validation used Morphe Patches 2.1.0-dev.5 and Morphe Desktop 1.20.0 with the stock APK below.

- Package: `com.google.android.youtube`
- Version: `21.44.528`, version code `1561310186`, Android 10+
- SHA-256: `55be430ce82dcedcb737ea27d1b78576737b75d11cde04aa89989e5376d3ae6c`

Passed checks:

- GitHub Actions compilation and 36 Java boundary assertions.
- Strict fingerprint validation with official Video quality and its settings dependencies,
  loading the community bundle before the official bundle.
- Normal patch application with all 111 recommended patches, loading the official bundle first.
- Inspection of generated DEX hooks, registers, registration call, and the preference
  in all three layouts. The extension contains only its own three classes.
- Installation through Morphe Expert mode, reported by the maintainer.

Full-set strict validation stops at the official Downloads fingerprint (11 matches).
The focused strict integration and normal full-set application passed. This does not
establish compatibility with other APK versions or device configurations.

The Java checks use Android/BooleanSetting boundary stubs. They verify helper logic
and preference initialization, not Android behavior, the Morphe registry, DEX
verification, or cross-bundle patch application.

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

The output check is pinned to the verified APK's class/register layout. It expects the
Video screen to be provided by the selected official patches.

This add-on needs an official bundle implementing `AddOnManager.registerAddOns()` and
the `morphe_addon_prefs.xml` preference-merge contract. A missing runtime contract
causes patching to fail. It does not load official patch classes across bundle class
loaders or include copies of Morphe settings/extension classes.

The Release workflow builds/releases the bundle and updates generated metadata.
On branches other than `main` or `dev`, it compiles and uploads a test artifact
without releasing. Check combined patch application and device installation before
publishing code changes. Keep `patches-bundle.json`, `patches-list.json`, and
`CHANGELOG.md` under release automation rather than editing their generated contents.
