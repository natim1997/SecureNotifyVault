מעולה. הבנתי את הסגנון שאתה רוצה — נקי, מופרד עם קווים, Table of Contents אמיתי, כותרות ברורות, בולטים מסודרים.

הנה גרסה כתובה בדיוק בפורמט כזה:

SecureNotifyVault

SecureNotifyVault is an Android Proof-of-Concept application developed for academic evaluation in Mobile Security.
The project demonstrates stealth behavior, encrypted persistence, background monitoring, and intrusion detection mechanisms.

Table of Contents

Overview

Features

Architecture

Usage

Build Configuration

Academic Context

Disclaimer

Overview

SecureNotifyVault presents a functional Calculator interface while internally demonstrating controlled offensive security techniques.

The application:

Monitors incoming notifications

Performs keyword-based content analysis

Encrypts collected data locally

Detects unauthorized access attempts

Simulates controlled data export

This project was implemented strictly for academic and research purposes.

Features
Stealth Behavior

Calculator-based disguise interface

Hidden dashboard accessible via secret PIN

Excluded from Recent Apps (excludeFromRecents)

Release build obfuscated using R8

Notification Monitoring

Implemented via NotificationListenerService

Filters duplicate/system notifications

Flags predefined sensitive keywords

Encrypted Local Storage

AES encryption before persistence

SQLite database (SQLiteOpenHelper)

Stored in private app directory

Intrusion Detection

Three incorrect PIN attempts trigger:

Silent front-camera capture (CameraX)

Local image storage

Internal gallery for captured images

Sensor-Based Lock Mechanism

Accelerometer monitoring (SensorManager)

Rapid shake triggers:

Immediate lock

UI termination

Data Export Simulation

Database export to CSV

Android sharing intent used to simulate data exfiltration

Architecture

Language: Java
Minimum SDK: API 24
Target SDK: API 35

Core Components:

NotificationListenerService

SQLiteOpenHelper

CameraX

SensorManager

RecyclerView

AES Encryption module

The system is structured into:

UI Layer (Calculator + Dashboard)

Background Service Layer

Encryption & Storage Layer

Sensor Monitoring Layer

Usage
Initial Setup

Grant the following permissions on first launch:

Notification Access

Camera Access

Without these permissions, core functionality will not operate.

Accessing the Dashboard

Open the Calculator interface

Enter 1337

Press =

Successful authentication opens the internal dashboard.

Dashboard Capabilities

View intercepted messages

Identify flagged sensitive content

View captured intrusion attempts

Export database as CSV

Wipe stored data

Build Configuration

The Release build enables:

R8 code shrinking

Name obfuscation

Unused resource removal

This configuration demonstrates protection against static analysis and reverse engineering.

The submitted source code remains readable for academic review.

Academic Context

This project was developed as part of a Mobile Security course assignment.
It demonstrates applied offensive security concepts within a controlled environment.

Disclaimer

This repository contains a Proof-of-Concept implementation for academic purposes only.
The author does not support or encourage misuse of the demonstrated techniques.
