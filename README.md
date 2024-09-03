# Encryption Machine

## Overview

Encryption Machine is a Java-based cipher program designed to encrypt and decrypt messages using a progressive substitution algorithm. This project implements custom encryption techniques to ensure secure communication, leveraging key data structures like `ArrayList` and `HashMap` to manage permutations and enhance the encryption process.

## Features

- **Encryption**: Encrypts plaintext messages using a custom substitution algorithm, transforming them into secure ciphertext.
- **Decryption**: Decrypts ciphertext back into readable plaintext, allowing for secure message transmission and retrieval.
- **Customizable**: The encryption process can be adapted or expanded with additional permutations to increase security.

## Implementation Details

### Algorithm

The core of the Encryption Machine is a progressive substitution algorithm. This algorithm works by iteratively substituting characters in the plaintext with other characters, determined by a series of predefined rules and permutations. The same process is reversed during decryption to restore the original message.

### Data Structures

- **ArrayList**: Used to manage and iterate over lists of characters, enabling dynamic and flexible handling of data during encryption and decryption.
- **HashMap**: Utilized to store the mappings between original characters and their substituted counterparts. This allows for efficient lookups and ensures that each character is consistently encrypted and decrypted.
