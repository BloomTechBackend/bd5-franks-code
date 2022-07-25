package com.amazon.ata.introthreads.classroom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class to pre-compute hashes for all common passwords to speed up cracking the hacked database.
 *
 * Passwords are downloaded from https://github.com/danielmiessler/SecLists/tree/master/Passwords/Common-Credentials
 */
public class PasswordHasher {
    // should create the file in your workspace directory
    private static final String PASSWORDS_AND_HASHES_FILE = "./passwordsAndHashesOutput.csv";

    // a "salt" is a value used to encrypt/hash a value to make it harder to decrypt
    //          typically unique to a value and a large string (128, 256, 512, 1024 chars)
    private static final String DISCOVERED_SALT = "salt";  // "salt" is a VERY POOR salt value

    /**
     * Generates hashes for all of the given passwords.
     *
     * @param passwords List of passwords to hash
     * @return map of password to hash
     * @throws InterruptedException
     */
    public static Map<String, String> generateAllHashes(List<String> passwords) throws InterruptedException {
        // Added final to the Map to make it immutable for concurrency
        final Map<String, String> passwordToHashes = Maps.newConcurrentMap();
        // Prior to adding concurrency (Threads) this Map was populated by one call to the BatchPasswordHasher.
        // With concurrency each BatchPasswordHasher will have a separate Map of values we need to merge into this one

        // Split the list of passwords passed a parameter into separate Lists for each thread
        // We plan on using 4 Threads so will create 4 password sub lists, one per thread
        //                                         .partition(List      , #-items-per-sub-list
        List<List<String>> passwordSublists = Lists.partition(passwords , passwords.size() / 100);

        // Since the BatchPasswordHasher object in the Thread will be destroyed when the Thread is done
        //       and it contains the hashed passwords we need to merge into the result Map
        //       we will save the BatchPasswords so we can access them after the Thread completes
        List<BatchPasswordHasher> theHashers = new ArrayList<>();

        // Since we need to wait for all Threads to complete before we can start merging the results
        //       from the BatchPasswordHashers
        //       so we will save the Threads and ask Java to wait for all of them to complete
        List<Thread> theThreads = new ArrayList<>();

        // Loop through the list of password sub lists, giving one to each for the BatchPasswordHashers on a thread
        for(int i=0; i < passwordSublists.size(); i++) {
            // Instantiate a BatchPasswordHasher and assign it the current password sublist
            BatchPasswordHasher batchHasher = new BatchPasswordHasher(passwordSublists.get(i), DISCOVERED_SALT);
            theHashers.add(batchHasher);               // Save the BatchPasswordHasher sowe can access once the Thread completes

            // batchHasher.hashPasswords(); // replaced by starting a thread with the current BatchPaswordHasher()
            Thread aThread = new Thread(batchHasher);  // Create a Thread with the current BatchPasswordHasher()
            theThreads.add(aThread);                   // Remember the new Thread so we can Java to wait for it to complete
            aThread.start();                           // Start the new Thread - make Thread RUNNABLE
                                                       //       once the Thread is started processing resumes
                                                       //       Java does not wait for the Thread to complete
        }  // end of for-loop

        // Call the waitForThreadsToComplete() method to suspend our processing
        //      until all the Threads we started (stored in a List) have completed
        //
        waitForThreadsToComplete(theThreads);

        // Now that all Threads have completed running their BatchPasswordHashers
        // Merge the results from each BatchPasswordHasher into the result Map

        for(BatchPasswordHasher aHasher : theHashers) {
            // copy all the entries from current BatchPasswordHasher Map to the result Map
            passwordToHashes.putAll(aHasher.getPasswordToHashes());
        }
        // passwordToHashes.putAll(batchHasher.getPasswordToHashes());  // replaced by teh for-loop to merge results

        return passwordToHashes;
    }

    /**
     * Makes the thread calling this method wait until passed in threads are done executing before proceeding.
     *
     * @param threads to wait on
     * @throws InterruptedException
     *
     * the .join() method will wait until a Thread is complete before resume
     *
     */
    public static void waitForThreadsToComplete(List<Thread> threads) throws InterruptedException {
        // Go through the List of threads we started and wait for them all to complete
        for (Thread thread : threads) {
            thread.join(); // wait for the current Thread to complete before resuming processing
        }
    }

    /**
     * Writes pairs of password and its hash to a file.
     */
    static void writePasswordsAndHashes(Map<String, String> passwordToHashes) {
        File file = new File(PASSWORDS_AND_HASHES_FILE);
        try (
            BufferedWriter writer = Files.newBufferedWriter(file.toPath());
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)
        ) {
            for (Map.Entry<String, String> passwordToHash : passwordToHashes.entrySet()) {
                final String password = passwordToHash.getKey();
                final String hash = passwordToHash.getValue();

                csvPrinter.printRecord(password, hash);
            }
            System.out.println("Wrote output of batch hashing to " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
