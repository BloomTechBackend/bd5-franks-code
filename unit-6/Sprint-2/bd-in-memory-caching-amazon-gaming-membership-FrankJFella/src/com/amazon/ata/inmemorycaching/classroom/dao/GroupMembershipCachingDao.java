package com.amazon.ata.inmemorycaching.classroom.dao;

import com.amazon.ata.inmemorycaching.classroom.dao.models.GroupMembershipCacheKey;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

// This class will manage calls to the data store for membership validation
//      using the Google Guava cache manager.

// It will insert calls to the cache manager to locate the data requested
//         instead of calling the original DAO
// if the cache manager does not find what it looking for in its cache
//    IT will call the original DAO to get the data
// The original DAO is identified to cache manager as a DelegateDAO
//
// We need to mimic the behavior (methods) of the original DAO methods
//    so application code remains the same whether we use caching or not
//
// In this example we the DelegateDao we are mimicking is:
//         public boolean isUserInGroup(final String userId, final String groupId)
//
// We need to a method is this class with the same method signature as the
//    method we are mimicking.
//
// The cache manager requires a key to identify the data it should search for in the cache
// In this example the cache key will contain the userid and groupid requested
//
// Since multiple values are required for the cache key,
//       we need to create a class to hold them

public class GroupMembershipCachingDao {

    // Define a reference to a LoadingCache object for the cache
    //        a LoadingCache object has a key and a value
    //        the key is the cache-key
    //        the value of what is stored in the cache (data to be saved)

    //                         caching-key-class   , data-class
    private LoadingCache<GroupMembershipCacheKey, Boolean> theCache;

    // in the ctor we instantiate the cache, set it's attributes and assign to the reference
    //
    // delegateDao is reference to the Dao to be called bythe cache manager
    //         when a value is not found in the cache
    //         the delegateDao is our original GroupMembershipDao
    // It will be dependency inject when the ctor is called
    @Inject
    public GroupMembershipCachingDao(final GroupMembershipDao delegateDao) {
       this.theCache = CacheBuilder.newBuilder()
               .maximumSize(20000)                  // max number of entries to hold in the cache
               .expireAfterWrite(3, TimeUnit.HOURS) // evict entries 3 hours after 1st written
               .build(CacheLoader.from(delegateDao::isUserInGroup));  // Go build the cache with the delegateDao method
               // the delegateDao class must have a method called isUserInGroup() that receives a cache-key-object
               // delegateDao::isUserInGroup - delegateDAO is an object of the original DAO class
               //                              :: - go to the class and look for... (scope resolution)
               //                              isUserInGroup - method in the class that receives a casch-key
    }  // end of ctor

    // create a method for application to call to use the cached version of the data access
    // it should have the same name, parameters and return type as the original Dao method
    // so the application doesn't have to change

    public boolean isUserInGroup(final String userId, String groupId)  {
        // Call the cache manager with the cache-key containing the values provided
        //      and return what it returns to us
        //
        // .getUnchecked() invokes the cache-manger with the key given
        //                 and returns the value from the cache manager
        //
        // .get() does the same thing as .getUnchecked(), but may throw a checked exception
        //             which requires either throws on the method signature
        //             you put in a try/catch block and catch the ExecutionExecution

        return theCache.getUnchecked(new GroupMembershipCacheKey(userId, groupId));
    }
}
