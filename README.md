# YouTube Downloader

An Android application for downloading YouTube videos and audio using yt-dlp.

## Features

- Download YouTube videos in MP4 format
- Download YouTube audio as MP3
- Built with Jetpack Compose and Material 3
- Uses yt-dlp via the youtubedl-android library for reliable downloads

## Requirements

- Android 8.0 (API 26) or higher
- Internet permission for downloading content
- Storage permission for saving downloads

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Download Engine**: yt-dlp via [youtubedl-android](https://github.com/junkfood02/youtubedl-android)
- **FFmpeg**: For audio/video processing
- **Min SDK**: 26
- **Target SDK**: 36
- **Compile SDK**: 37

## Dependencies

Key dependencies from `gradle/libs.versions.toml`:
- `androidx.compose.material3` - Material 3 components
- `androidx.activity.compose` - Compose integration
- `io.github.junkfood02.youtubedl-android:library:0.18.1` - yt-dlp wrapper
- `io.github.junkfood02.youtubedl-android:ffmpeg:0.18.1` - FFmpeg binary

## How It Works

1. User enters a YouTube URL
2. App initializes yt-dlp and FFmpeg on first launch
3. On "Download Video": downloads best MP4 quality
4. On "Download Audio": extracts audio and converts to MP3
5. Files saved to the device's Downloads folder

## Building

```bash
./gradlew assembleDebug
```

## Credits & Acknowledgments

This project is made possible by the following open-source projects:

- **[yt-dlp](https://github.com/yt-dlp/yt-dlp)** - The core download engine (Unlicense)
- **[youtubedl-android](https://github.com/junkfood02/youtubedl-android)** by **junkfood02** - Android wrapper for yt-dlp including FFmpeg (MIT License)
- **[Seal](https://github.com/JunkFood02/Seal)** by **junkfood02** - Reference Android video/audio downloader app that inspired this project (GPL-3.0)
- **[FFmpeg](https://ffmpeg.org/)** - Audio/video processing library (LGPL/GPL)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Third-party licenses:
- **yt-dlp**: Unlicense (public domain)
- **FFmpeg**: LGPL v2.1 / GPL v2
- **youtubedl-android**: MIT License