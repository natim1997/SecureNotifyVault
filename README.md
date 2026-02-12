# SecureNotifyVault

SecureNotifyVault is an Android Proof-of-Concept application developed for academic evaluation in Mobile Security.  
The project demonstrates stealth techniques, encrypted persistence, background monitoring, and intrusion detection mechanisms.

---

## Table of Contents

1. [Overview](#overview)
2. [Core Features](#core-features)
3. [Architecture](#architecture)
4. [Usage Guide](#usage-guide)
5. [Build Configuration](#build-configuration)
6. [Academic Context](#academic-context)
7. [Disclaimer](#disclaimer)

---

## Overview

SecureNotifyVault presents a functional Calculator interface while internally demonstrating controlled mobile security techniques.

The application:

- Monitors incoming notifications
- Performs keyword-based analysis
- Encrypts and stores collected data locally
- Detects unauthorized access attempts
- Simulates controlled data export

This project was implemented strictly for academic purposes.

---

## Core Features

### Stealth & Camouflage

- Functional Calculator interface (cover layer)
- Hidden dashboard accessible via secret PIN
- Excluded from Recent Apps (`android:excludeFromRecents="true"`)
- Release build obfuscated using R8

---

### Notification Monitoring

- Implemented using `NotificationListenerService`
- De-duplication filtering for system noise
- Keyword-based flagging of sensitive content

---

### Encrypted Local Storage

- AES encryption before database persistence
- SQLite database (`SQLiteOpenHelper`)
- Stored in private application directory

---

### Intrusion Detection

- Three incorrect PIN attempts trigger:
  - Silent front camera capture (CameraX)
  - Local image storage
- Internal gallery for reviewing captured attempts

---

### Sensor-Based Lock Mechanism

- Accelerometer monitoring via `SensorManager`
- Rapid shake triggers:
  - Immediate vault lock
  - UI termination

---

### Data Export Simulation

- Database export to CSV format
- Android sharing intent integration
- Demonstrates controlled exfiltration scenario

---

## Architecture

**Language:** Java  
**Minimum SDK:** API 24  
**Target SDK:** API 35  

### Core Components

- `NotificationListenerService`
- `SQLiteOpenHelper`
- `CameraX`
- `SensorManager`
- `RecyclerView`
- AES Encryption Module

### System Layers

- UI Layer (Calculator + Dashboard)
- Background Service Layer
- Encryption & Storage Layer
- Sensor Monitoring Layer

---

## Usage Guide

### 1. Installation & Permissions

Upon first launch, grant:

- Notification Access
- Camera Access

Without these permissions, core functionality will not operate.

---

### 2. Unlocking the Hidden Dashboard

1. Open the Calculator interface
2. Enter: `1337`
3. Press `=`

If authentication succeeds, the internal dashboard will open.

---

### 3. Dashboard Capabilities

- View intercepted notifications
- Identify flagged sensitive messages
- View captured intrusion attempts
- Export encrypted database
- Wipe all stored data

---

### 4. Testing Security Mechanisms

**Panic Test:**  
Shake the device rapidly while inside the dashboard.

**Intrusion Test:**  
Enter an incorrect PIN three times from the Calculator screen.

---

## Build Configuration

The Release build enables:

- R8 code shrinking
- Name obfuscation
- Removal of unused resources

This demonstrates mitigation against static analysis and reverse engineering.

The submitted source version remains readable for academic grading.

---

## Academic Context

This project was developed as part of a Mobile Security course assignment.

It demonstrates applied
