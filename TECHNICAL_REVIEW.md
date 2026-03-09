# Biometric Switch — Technical Review & Recommendations

## Current state (quick check)
The project demonstrates a **proof-of-concept** flow:
- Two biometric actions are exposed (`panic` and `normal`).
- A key alias is selected per mode in `BiometricHelper`.
- File operations move files into an app-private hidden directory and restore them later.

This is a useful prototype, but it is not production-safe yet.

## What should be done first (highest priority)

1. **Clarify security model and threat model**
   - Define what the app protects against (casual access, forensic extraction, cloud backup leakage, etc.).
   - Define what “panic mode” must guarantee (e.g., immediate wipe, hidden vault, decoy state).

2. **Fix storage strategy for modern Android**
   - Hardcoded `/sdcard/...` paths are brittle and conflict with scoped storage behavior.
   - Move to `MediaStore` + Storage Access Framework and request least privilege access.

3. **Encrypt hidden content at rest**
   - Current logic copies files and deletes originals, but hidden copies are not encrypted.
   - Introduce per-file encryption using Android Keystore-backed keys.
   - Include IV handling and metadata integrity checks.

4. **Harden biometric authentication flow**
   - Enforce robust prompt policy (allowed authenticators, lockout handling, retry behavior).
   - Separate panic and normal outcomes with explicit state management.

5. **Add reliability guarantees for file operations**
   - Replace direct copy/delete with transactional steps:
     1. copy to temp file,
     2. verify checksum,
     3. fsync,
     4. remove source,
     5. atomically rename.
   - Add recovery logic for interrupted operations.

6. **Create observability and audit-safe logs**
   - Add structured logs with no sensitive filenames/content.
   - Add local operation journal for debugging and rollback.

## Recommended additions (next)

### Product features
- **Decoy vault mode** for panic auth that opens harmless content.
- **Auto-lock timer** that re-hides content after inactivity.
- **Selective restore** (restore one item vs all).
- **Emergency wipe option** with user confirmation safeguards.
- **Backup/restore policy controls** so hidden data is excluded or safely encrypted.

### Security features
- Integrity verification for hidden files (HMAC/signature).
- Root/debug detection policies and user warning surface.
- Optional passphrase fallback for biometric unavailability.
- Key invalidation and re-enrollment handling.

### UX improvements
- Guided onboarding with explicit consent and limitations.
- Clear operation status: queued, running, success, failed.
- Failure recovery screen (“we found partially moved files — repair now”).

### Engineering hygiene
- Convert this repository into a standard Android project layout (`app/src/main/...`).
- Add unit tests for file workflow and crypto helper.
- Add instrumentation tests for biometric prompt flows.
- Add CI checks (ktlint/detekt + unit tests).

## Suggested implementation roadmap

### Milestone 1 (stability + security baseline)
- Scoped storage migration.
- Encrypted hidden store.
- Transactional move engine + crash recovery.
- Minimal telemetry and non-sensitive logs.

### Milestone 2 (usability)
- Onboarding + better state/error UI.
- Selective restore and auto-lock.
- Better biometric fallback and lockout UX.

### Milestone 3 (defense-in-depth)
- Integrity attestation layer.
- Root/debug policy hardening.
- Decoy vault and emergency workflows.

## Definition of done for production readiness
- Threat model approved and testable requirements documented.
- All hidden data encrypted with recoverable metadata.
- File move/restore survives app/process/device interruption.
- End-to-end tests cover panic, normal, failure, and recovery paths.
- Security review completed with no high-severity findings.
