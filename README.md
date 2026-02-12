#  SecureNotifyVault (Stealth Spyware PoC)

**A High-Level Proof-of-Concept for Android Offensive Security, demonstrating advanced data collection, encryption, and stealth mechanisms.**

---

##  Project Overview
SecureNotifyVault is a sophisticated spyware tool disguised as a fully functional **Calculator**. It utilizes Android's `NotificationListenerService` to intercept sensitive data from third-party applications (WhatsApp, Telegram, SMS) in the background.

The project demonstrates a complete **Cyber Kill Chain** simulation: from **Infiltration** (Calculator disguise), through **Collection** (Data sniffing), **Analysis** (Keyword detection), to **Exfiltration** (CSV Export).

---

##  Advanced Technical Implementation
This project goes beyond simple app development by implementing core security and malware concepts:

### 1. Steganography & Social Engineering 
* **Concept:** The app employs a Trojan Horse strategy. To the casual observer (or victim), it functions exactly like a standard calculator.
* **Implementation:** The UI handles mathematical operations perfectly. Access to the malicious "Vault" is only triggered by a specific cryptographic hash/pin (`1337`) combined with the `=` operator.
* **Ghost Mode:** The activity defines `android:excludeFromRecents="true"` in the manifest, ensuring the app vanishes from the "Recent Apps" stack immediately after minimizing, hindering forensic analysis.

### 2. Data Encryption at Rest (AES) 
* **Concept:** Storing stolen data in plain text is a security vulnerability. This project implements cryptographic protection.
* **Implementation:** Every intercepted notification is encrypted using **AES (Advanced Encryption Standard)** before being committed to the SQLite database.
* **Key Management:** The decryption key is embedded within the secure logic, ensuring that even if a forensic investigator dumps the database file (`.db`), the content remains unreadable gibberish without the app context.

### 3. Background Persistence & Privilege Escalation 
* **Concept:** Malware requires persistent access to data streams without user interaction.
* **Implementation:** The app utilizes a `NotificationListenerService`. This system-level service runs in the background, independent of the UI, and listens for `onNotificationPosted` events. It employs a smart de-duplication algorithm to filter system noise and capture only unique, high-value messages.

### 4. Real-Time Pattern Matching (Analysis) 
* **Concept:** Automated data triage.
* **Implementation:** The app doesn't just store data; it analyzes it. An internal algorithm scans incoming text against a dictionary of sensitive keywords (e.g., "OTP", "Password", "Bank"). Matches are flagged with a high-priority visual indicator (⚠️) in the dashboard, simulating targeted intelligence gathering.

### 5. Sensor-Based Anti-Forensics (The "Kill Switch") 
* **Concept:** Emergency defense mechanisms to prevent analysis when physical access is compromised.
* **Implementation:** The `VaultActivity` implements `SensorEventListener` to monitor the device's **Accelerometer**. A specific G-force threshold (simulating a panic shake) triggers an immediate lockdown, terminating the session and returning to the innocent calculator screen.

### 6. Intrusion Detection System (Honeypot) 
* **Concept:** Monitoring unauthorized access attempts.
* **Implementation:** Using the **CameraX API**, the app silently captures photos via the front-facing camera when incorrect PINs are entered. The capture happens without a preview surface or shutter sound, storing the evidence in a hidden internal gallery.

### 7. Code Obfuscation (Anti-Reversing) 
* **Concept:** Protecting the malware's logic from security researchers.
* **Implementation:** The release build is compiled with **R8/ProGuard** enabled. This shrinks the bytecode and renames classes/methods to random characters (e.g., `a.b()`), making static analysis and reverse engineering significantly harder.

---

##  Features Summary

| Feature | Description |
| :--- | :--- |
| **Stealth Mode** | Disguised as a calculator; hidden from recent apps. |
| **Data Sniffer** | Captures notifications from WhatsApp, Telegram, etc. |
| **Secure Storage** | AES Encrypted SQLite database. |
| **Intruder Selfie** | Silent photo capture on failed login attempts. |
| **Panic Switch** | Shake device to immediately lock the vault. |
| **Data Export** | Exfiltrate stolen data to CSV format. |
| **Keyword Alert** | Highlights sensitive messages (OTP, Bank, Passwords). |

---

##  Critical Setup (Permissions)
**IMPORTANT:** Due to the invasive nature of this tool, you must manually grant privileges:

1.  **Notification Access:**
    * Go to Android Settings -> Search "Notification Access".
    * Toggle **ON** for SecureNotifyVault.
    * *Why?* This grants the app the ability to read notifications from other apps.
2.  **Camera Access:**
    * Allow when prompted.
    * *Why?* Required for the Intruder Selfie feature.

---

##  Usage Guide (How to Demo)

1.  **Open the App:** It looks like a calculator.
2.  **Enter Safe Mode:** Type `1337` and press `=`.
3.  **View Intel:** Browse intercepted messages in the dashboard.
4.  **Simulate Attack:** Send a WhatsApp message with the word "password" to see the red alert.
5.  **Test Panic Mode:** Shake the phone vigorously to verify the app locks itself.
6.  **View Intruders:** Enter a wrong code (e.g., `5555`) 3 times, then log in and check the Gallery.
7.  **Exfiltrate:** Click "EXPORT DATA" to share the stolen logs via CSV.

---

##  Disclaimer
This application is a **Proof of Concept (PoC)** developed strictly for educational purposes as a Final Project in Mobile Security. It demonstrates how malicious actors operate and emphasizes the importance of mobile permissions and security awareness.
