# Coercion-Resistant Biometric App
## Threat Model Whitepaper (Concept Level)

## 1) Objective
Protect sensitive data and communications under coercion scenarios. The user can comply convincingly while certain apps/data remain inaccessible.

## 2) Assets
- Sensitive apps (messaging, banking, work, notes)
- Sensitive files (documents, photos, videos)
- Communication logs
- Encryption keys and key metadata

## 3) Adversaries
- Coercive human actor (robber, abuser, law enforcement pressure)
- Has physical access and expects immediate compliance
- May inspect installed apps, storage patterns, notifications, usage stats, battery/network activity, recents

## 4) Threat Scenarios
- Fingerprint A unlock -> **normal mode**
- Fingerprint B unlock -> **duress mode**
- Inspector checks for consistency across app list, notifications, storage usage, and activity history

## 5) Security Goals
1. Sensitive apps/data appear non-existent in duress mode
2. Device behavior is internally consistent (no obvious contradictions)
3. Exposed duress data is real but limited/non-sensitive
4. Sensitive notifications are suppressed
5. Sensitive encryption keys remain inaccessible in duress mode

## 6) Assumptions
- User cannot arbitrarily hide files manually during incident
- Attacker performs practical inspection, not full forensic lab workflow
- Duress mode triggers instantly on alternate biometric

## 7) Limitations / Known Weak Points
- Rooted/compromised OS breaks trust model
- Cloud-synced push notifications may still leak metadata
- Extreme forensics can exceed app-only protections
- Hardware teardown can reveal metadata traces

## 8) Countermeasures
- Pre-encrypt sensitive data before incidents
- Maintain explicit **public** and **secure** zones
- Run consistency checks (storage/usage/network/notifications)
- Optional silent emergency logging hooks (policy-controlled)

## 9) Validation / Testing
- Simulated forced-unlock drills
- Consistency checks across storage, battery, and network patterns
- QA with realistic human inspection behavior
- Independent security review for high-risk deployments

## 10) Implementation Mapping in This Repo
- `BiometricHelper` selects panic vs normal biometric route.
- `DuressPolicy` centralizes what happens in normal/duress transitions.
- `FileHider` is used as the current storage-control primitive.

## 11) Next Engineering Steps
- Replace hardcoded paths with SAF/MediaStore sources
- Add encrypted-at-rest hidden vault format
- Add notification-channel suppression rules by mode
- Add crash-safe journaling + recovery for every file operation
