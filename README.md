# 🔖 Barcode Encoder/Decoder — Code 11 & Code 93

> Final Java programming project for the first year of the Higher Degree in Multiplatform Application Development (DAM).

---

## 📋 Description

A Java implementation of an encoder and decoder for two barcode standards: **Code 11** and **Code 93 Extended**. The project covers the full cycle: encoding a text string into a visual bar representation, and decoding a bar representation back into the original text, including normalization to handle bars of variable widths.

---

## 📦 Project Structure

```
├── Code11.java          # Code 11 implementation
├── Code93.java          # Code 93 Extended implementation
├── Code11Test.java      # Code 11 tests
└── Code93Test.java      # Code 93 Extended tests
```

---

## 🔢 Code 11

### What is it?
Code 11 is a high-density barcode designed primarily for the telecommunications industry. It encodes the digits 0–9 and the `-` symbol.

### Supported characters
| Character | Description |
|---|---|
| `0`–`9` | Numeric digits |
| `-` | Dash |
| `*` | Start/Stop (used internally) |

### How it works

**Encode:**
Each character is converted into a sequence of bars (`█`) and spaces (` `) of two widths (single and double), with a space separator between symbols.

**Decode:**
1. **Normalization:** The minimum and maximum widths of each consecutive bar/space block are analysed and averaged to determine the threshold between single and double width. This allows reading barcodes scanned at any scale.
2. **Block extraction:** The normalized string is split into symbol blocks, separated by triple spaces.
3. **Decoding:** Each block is looked up in the code table to retrieve the corresponding character.

---

## 🔡 Code 93 Extended

### What is it?
Code 93 is a high-density, high-reliability alphanumeric barcode. The **Extended** variant allows encoding all 128 ASCII characters using two-symbol combinations with special prefix symbols (`($)`, `(%)`, `(/)`, `(+)`).

### Supported characters
All ASCII characters from 0 to 127, including:

| ASCII Range | Encoding |
|---|---|
| `0–9`, `A–Z`, `-`, `.`, ` `, `$`, `/`, `+`, `%` | Direct symbols |
| `a–z` | Prefix `(+)` + uppercase letter |
| `NUL` (0) | `(%)U` |
| `SOH–SUB` (1–26) | `($)A` – `($)Z` |
| `ESC–US` (27–31) | `(%)A` – `(%)E` |
| `!`, `"`, `#`, `&`, `'`, `(`, `)`, `*`, `,`, `:` | Prefix `(/)` + letter |
| `;`, `<`, `=`, `>`, `?`, `@`, `[`, `\`, `]`, `^`, `_`, `` ` ``, `{`, `\|`, `}`, `~`, `DEL` | Prefix `(%)` + letter |

### How it works

**Encode:**
1. The Start symbol is prepended.
2. Each character is converted to its 9-unit code (bars and spaces). Special characters use a two-symbol prefix combination.
3. Two **checksums** (C and K) are calculated using weighted sums modulo 47.
4. The Stop symbol and final bar (`█`) are appended.

**Decode:**
1. **Normalization:** Same approach as Code 11 — relative bar widths are adjusted.
2. **Validation:** Length, Start/Stop markers, final bar, and checksums C and K are all verified.
3. **Decoding:** Each 9-unit block is looked up in the table. Prefix blocks (indices `43`–`46`) are combined with the following block to produce the final character.

### Checksums
```
C: weighted sum of all symbols (weights 1–20 cycling right to left) modulo 47
K: weighted sum including C   (weights 1–15 cycling right to left) modulo 47
```

---

## 🧪 Tests

| Category | Code 11 | Code 93 |
|---|---|---|
| Valid encode | ✅ | ✅ |
| Valid decode | ✅ | ✅ |
| Invalid decode (checksum, Start/Stop, final bar) | ✅ | ✅ |
| Normalization (variable-width bars) | ✅ | ✅ |
| Full roundtrip encode→decode | ✅ | ✅ |
| Roundtrip for every ASCII character (0–127) | — | ✅ |

To run the tests:
```bash
javac -cp .:junit.jar Code11.java Code93.java Code11Test.java Code93Test.java
java  -cp .:junit.jar org.junit.runner.JUnitCore Code11Test Code93Test
```

---

## 🛠️ Technologies

- **Java** (no external dependencies)
- **JUnit 4** for testing

---

## 👤 Andreu Martorell Serra

Final Java programming project — 1st year of the Higher Degree in Multiplatform Application Development (DAM).