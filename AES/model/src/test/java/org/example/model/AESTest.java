package org.example.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class AESTest {

    @Test
    public void testEncryptionDecryption() throws AES.AESException, AES.AESKeyException {
        AES aes = new AES();
        byte[] key = new byte[16];  // Klucz 128-bitowy (0x00...)
        byte[] plaintext = "Test123456789012".getBytes();  // Dokładnie 16 bajtów

        aes.setKey(key);
        byte[] encrypted = aes.encrypt(plaintext);
        byte[] decrypted = aes.decrypt(encrypted);

        assertNotNull(encrypted, "Szyfrowanie zwróciło null!");
        assertNotNull(decrypted, "Deszyfrowanie zwróciło null!");
        assertArrayEquals(plaintext, decrypted, "Odszyfrowany tekst nie zgadza się z oryginałem!");
    }

    @Test
    public void testEncryptionDecryptionKey() throws AES.AESException, AES.AESKeyException {
        AES aes = new AES();
        byte[] key = new byte[16];
        byte[] message = "cos".getBytes();
        aes.setKey(key);
        System.out.println(new String(message, StandardCharsets.UTF_8));
        System.out.println(aes.getKluczGlowny());
        byte[] encrypted = aes.encode(message, key);
        String tekst = new String(encrypted, StandardCharsets.UTF_8);
        System.out.println(tekst);
        byte[] decrypted = aes.decode(encrypted, key);
        String decryptedText = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println(decryptedText);
    }

   /* @Test
    public void testfMul() {
        AES aes = new AES();

        // Przykłady poprawnych wartości mnożenia w GF(2^8)
        assertEquals((byte) 0x57, aes.fMul((byte) 0x57, (byte) 0x01));  // a * 1 = a
        assertEquals((byte) 0xAE, aes.fMul((byte) 0x57, (byte) 0x02));  // 0x57 * 0x02 = 0xAE
        assertEquals((byte) 0x07, aes.fMul((byte) 0x57, (byte) 0x03));  // 0x57 * 0x03 = 0x07
    }
*/
}