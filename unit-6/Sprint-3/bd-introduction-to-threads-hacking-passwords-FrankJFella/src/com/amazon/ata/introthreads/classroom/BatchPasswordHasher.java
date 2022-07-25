package com.amazon.ata.introthreads.classroom;

import com.google.common.collect.Maps;

import java.util.*;

/**
 * A class to hash a batch of passwords in a separate thread.
 *
 * Make this class immutable for concurrent processing
 *
 *     1. Make the class final
 *     2. Verify/Make the data members final
 *     3. Check/Make any parameter references in ctor/setters defensive copy
 *     4. Check/Make and getters that return a reference - defensive return
 *
 *     implement the Runnable interface so we can run this process concurrently on Threads
 *     (if used extends Thread, this class could not be also be a subclass)
 *
 *     the Runnable interface requires we define a run() method in our class
 *
 *     the run() is where processing will start when an object of this class is placed on a Thread
 *     (run() method is like the main() for a Java app or handleRequest() for an AWS Lambda Function app)
 */
public final class BatchPasswordHasher implements Runnable {

    private final List<String> passwords;
    private final Map<String, String> passwordToHashes;
    private final String salt;

    // ctor receives a reference to List<> - defensive copy for safety
    public BatchPasswordHasher(List<String> passwords, String salt) {

        // this.passwords = passwords;  // Replaced with defensive copy
        this.passwords = new ArrayList<>(passwords);  // defensice copy of List parameter
        this.salt = salt;
        passwordToHashes = new HashMap<>();
    }

    /**
     *  Hashes all of the passwords, and stores the hashes in the passwordToHashes Map.
     */
    public void hashPasswords() {
        try {
            for (String password : passwords) {
                final String hash = PasswordUtil.hash(password, salt);
                passwordToHashes.put(password, hash);
            }
            System.out.println(String.format("Completed hashing batch of %d passwords.", passwords.size()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns a map where the key is a plain text password and the key is the hashed version of the plaintext password
     * and the class' salt value.
     *
     * @return passwordToHashes - a map of passwords to their hash value.
     */
    public Map<String, String> getPasswordToHashes() {
        return passwordToHashes;
    }

    //  rum() is required by the Runnable interface
    //  run() is where processing will start when an object of this class is placed on a Thread
    //  (run() method is like the main() for a Java app or handleRequest() for an AWS Lambda Function app)
    @Override
    public void run() {
        // call the method to hash the passwords
        this.hashPasswords();  // method in this class to hash passwords this.optional since we are in same class
    }
}
