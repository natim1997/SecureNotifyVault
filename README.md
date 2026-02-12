🕵️ SecureNotifyVault (Stealth Spyware PoC)

Proof-of-Concept Android application demonstrating mobile data collection, stealth techniques, encryption, and anti-forensic mechanisms.

📖 Overview

SecureNotifyVault presents itself as a fully functional Calculator application.
Behind the scenes, it operates as a silent intelligence-gathering tool.

The application:

Intercepts incoming notifications (WhatsApp, Telegram, SMS, etc.)

Analyzes content for sensitive keywords

Encrypts and stores collected data locally

Captures photos of unauthorized users attempting to access the vault

This project demonstrates advanced Offensive Mobile Security concepts including:

Stealth persistence

Encrypted local storage

Sensor-triggered defensive behavior

Simulated data exfiltration

🚀 Key Security Features
1️⃣ Stealth & Camouflage 🎭

Disguise – Fully working Calculator interface.

Ghost Mode – Excluded from the Recent Apps list
(android:excludeFromRecents="true").

Code Obfuscation – Release APK compiled with R8/ProGuard to prevent reverse engineering.

2️⃣ Intelligence Gathering 👂

Notification Interception
Uses NotificationListenerService to capture messages from:

WhatsApp

Messenger

Instagram

SMS

Other supported apps

Smart Filtering
De-duplication algorithm removes system noise and repeated notifications.

Keyword Sniffer
Real-time text analysis flags sensitive terms (e.g., password, code, bank) with a red warning badge ⚠️.

3️⃣ Secure Storage 🔒

AES Encryption
All intercepted data is encrypted before being written to disk.

SQLite Local Database
Stored privately and inaccessible to other apps on non-rooted devices.

4️⃣ Intrusion Detection & Surveillance 📸

Honeypot Logic
Calculator operates normally during standard usage.

Intruder Selfie
After 3 incorrect PIN attempts:

Front camera activates silently

No preview

No shutter sound

Photo stored internally via CameraX

Secret Gallery
Intruder photos stored in a hidden internal vault gallery.

5️⃣ Physical Security & Exfiltration 🏃‍♂️

Panic Shake (Kill Switch)
Uses Accelerometer via SensorManager.
A strong shake immediately:

Locks the vault

Crashes the UI to the home screen

Data Exfiltration Simulation
Entire encrypted database can be exported as a CSV file using Android's sharing intent.

🛠️ Tech Stack & Architecture

Language: Java
Minimum SDK: API 24 (Android 7.0)
Target SDK: API 35

Core Components

NotificationListenerService – Background data interception

SQLiteOpenHelper – Database management

CameraX – Background photography

SensorManager – Motion detection

RecyclerView – Data visualization

AES Encryption – Data protection

🔐 How to Access the Vault
Step 1 – Installation & Permissions

On first launch, grant:

Notification Access

Camera Access

Step 2 – Unlocking the Vault

Open the Calculator.

Enter the secret PIN: 1337

Press =

You are now inside the Command & Control (C2) dashboard.

Step 3 – Inside the Vault

Dashboard
View:

Total messages

Suspicious flagged items

Top active app

Logs
Scroll intercepted messages (red = sensitive).

Intruders
Click VIEW INTRUDERS to see captured photos.

Export
Click EXPORT DATA to generate CSV file.

Wipe
Click WIPE DATA to delete all stored evidence.

Step 4 – Testing Security Measures

Panic Test
Shake the phone while inside the vault.
The app locks instantly.

Intruder Test
Enter a wrong code (e.g., 5555) three times.
Check the hidden gallery afterward.

⚠️ Note on Obfuscation (For Grading)

This project follows a Security by Design approach.

Submitted source code remains readable for academic evaluation.

Release APK build enables R8 shrinking and obfuscation.

Demonstrates protection against static analysis and reverse engineering.

⚖️ Disclaimer

This application is a Proof of Concept (PoC) developed strictly for educational and academic purposes in the field of Mobile Security.

It is not intended for malicious use.
The developer assumes no responsibility for misuse of this software.
