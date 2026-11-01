extension {
    name = "extensions/audio-pause.mpe"
}

android {
    namespace = "app.o1hitman1o.extension.audiofocus"
}

dependencies {
    // Supplied at runtime by official Morphe patches, never bundled in this extension.
    compileOnly("app.morphe:morphe-extensions-library:2.1.0")
}
