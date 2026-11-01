# o1hitman1o's Morphe Patches

A community add-on that adds **Pause on audio interrupt** to YouTube patched with Morphe.
When enabled, playback pauses during temporary audio interruptions instead of lowering
volume, using YouTube's existing audio-focus handling.

Maintained by o1hitman1o. This project is not maintained or endorsed by Morphe.

[![Add to Morphe](.github/assets/add-to-morphe.svg)](https://morphe.software/add-source?github=o1hitman1o/Morphe-Patches)

## Install

**Expert mode is required.** This community source adds to the official Morphe patches;
select patches from both sources in the same patching run.

1. Install Morphe Manager and enable **Expert mode** in its settings.
2. Open the **Add to Morphe** button above on your Android device and add
   **o1hitman1o's Morphe Patches**. Alternatively, add this GitHub repository as a
   remote patch source: `https://github.com/o1hitman1o/Morphe-Patches`.
3. Keep the official **Morphe Patches** source enabled alongside this source. Enable
   **experimental app versions** for both sources to select the verified YouTube target.
4. Start with a stock **YouTube 21.44.528** APK (version code **1561310186**), on
   **Android 10 or newer**. Do not use an already patched APK.
5. Select your usual official YouTube patches and this source's **Pause on audio interrupt**
   patch in the same run. Include the official **Video quality** patch to provide the
   Video settings screen and its Settings dependency.
6. Complete patching and install the resulting app.
7. In YouTube, open **Settings → Morphe → Video → Pause on audio interrupt**,
   enable it, and **restart YouTube**. The setting is off by default. Restart again
   after changing the setting.

If selected official patches do not provide the Video screen, the switch appears
at the Morphe settings root.

## Downloads and updates

[Latest release](https://github.com/o1hitman1o/Morphe-Patches/releases/latest)
contains the `.mpp` bundle for manual import. Adding the GitHub source lets Morphe
Manager check for future bundle updates.

Support is limited to the verified **21.44.528** APK, version code **1561310186**,
Android 10 or newer. The target is marked experimental. See
[validation and development notes](docs/VALIDATION.md) for the exact APK hash,
completed checks, and limitations.

## Patches

<!-- PATCHES_START EXPANDED -->
> **[v1.0.2](https://github.com/o1hitman1o/Morphe-Patches/releases/tag/v1.0.2)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;1 patches total
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

The audio-focus hooks were adapted from ReVanced's Pause on audio interrupt patch
(PR #6464).
Original source notices remain in the relevant files.

GPLv3, with the additional terms in [NOTICE](NOTICE). The template's LICENSE and NOTICE
are retained. This project uses its own name and extension namespace.
