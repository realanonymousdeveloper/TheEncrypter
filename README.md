# The Encrypter

A simple Android app for encrypting and decrypting text messages using AES-256 encryption.

## Features

- Encrypt text messages with optional password protection
- Decrypt encrypted messages back to original text
- Copy encrypted output to clipboard for easy sharing
- Works completely offline
- Clean Material Design UI

## Usage

1. Enter your message in the input field
2. (Optional) Set a custom password - both sender and receiver must use the same password
3. Tap **Encrypt** to generate encrypted text
4. Tap **Copy Output** to copy the result
5. Share the encrypted text via SMS, email, or any messaging app
6. Receiver pastes the encrypted text, enters the same password, and taps **Decrypt**

## Technical Details

- **Encryption**: AES-256-CBC
- **Key Derivation**: PBKDF2WithHmacSHA256 (65536 iterations)
- **Output Format**: Base64 encoded (SMS-friendly)
- **Min SDK**: 23 (Android 6.0)
- **Target SDK**: 36

## Building

Open the project in Android Studio and run on your device or emulator.

```bash
./gradlew assembleDebug
```

## License

MIT License
