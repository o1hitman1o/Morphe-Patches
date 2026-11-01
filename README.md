# Hitman Audio Pause

A community add-on that adds **Pause on audio interrupt** to YouTube patched with Morphe.
When enabled, playback pauses during temporary audio interruptions instead of lowering
volume, using YouTube's existing audio-focus handling.

Maintained by o1hitman1o. This project is not maintained or endorsed by Morphe.

[![Add to Morphe](.github/assets/add-to-morphe.svg)](https://morphe.software/add-source?github=o1hitman1o/youtube-audio-pause)

## Install

1. Open the **Add to Morphe** link on your Android device and add this patch source.
   You can also add `o1hitman1o/youtube-audio-pause` as a GitHub source in Morphe Manager.
2. Keep the official **Morphe Patches** source enabled. Enable **Expert mode** and
   experimental app versions for both sources.
3. Select a stock **YouTube 21.44.528** APK. In the same patching run, select your usual
   official patches and this source's **Pause on audio interrupt** patch. Include
   **Video quality** to provide the Video settings screen.
4. Install the patched app. Open **Settings → Morphe → Video → Pause on audio interrupt**,
   enable it, and restart YouTube. The setting is off by default.

Use the official Morphe source alongside this add-on. The earlier full-bundle fork
already includes this feature; applying both implementations is unsupported. Start
with a stock APK rather than an already patched app.

If selected official patches do not provide the Video screen, the switch appears
at the Morphe settings root.

## Downloads and updates

[Latest release](https://github.com/o1hitman1o/youtube-audio-pause/releases/latest)
contains the `.mpp` bundle for manual import. Adding the GitHub source lets Morphe
Manager check for future bundle updates.

Support is limited to the verified **21.44.528** APK, version code **1561310186**,
Android 10 or newer. The target is marked experimental. See
[validation and development notes](docs/VALIDATION.md) for the exact APK hash,
completed checks, and limitations.

## Patches

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0](https://github.com/o1hitman1o/youtube-audio-pause/releases/tag/v1.0.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;1 patches total
<details open>
<summary>📦 YouTube&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**Package name:** `com.google.android.youtube`

**🎯 Supported versions:**

| 🧪&nbsp;21.44.528 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Pause on audio interrupt](#pause-on-audio-interrupt) | Adds a Video setting to pause playback instead of lowering volume during audio interruptions. |  |

</details>

<!-- PATCHES_END -->

## Attribution and license

The audio-focus hooks were adapted from the implementation in
[o1hitman1o/morphe-patches](https://github.com/o1hitman1o/morphe-patches/tree/510a931869e4603f278091ea81292999c3f36d35),
originally adapted from ReVanced's Pause on audio interrupt patch (PR #6464).
Original source notices remain in the relevant files.

GPLv3, with the additional terms in [NOTICE](NOTICE). The template's LICENSE and NOTICE
are retained. This project uses its own name and extension namespace.
