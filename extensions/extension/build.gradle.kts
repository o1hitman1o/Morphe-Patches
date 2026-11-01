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

// This extension is Java-only; Morphe supplies the shared runtime classes.
configurations.configureEach {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
}
