package com.amazon.ata.inmemorycaching.classroom.dao.models;

import java.util.Objects;

// Due to our cache key requiring multiple values (userid and groupid)
//     we needed a closs to hold the value

// A cache key must be unique within the cache
//
// a cache key is typically a POJO with ctor, getters, equals(), hashCode and maybe a toString()

// This is an immutable class - it may be used successfully in a concurrent execution environment
//      since we want the class to be immutable - has no setters
public final class GroupMembershipCacheKey {

    private final String userId;  // an individual
    private final String groupId; // a group the individual has joined

    // add final to a method parameter means the method cannot change it
    public GroupMembershipCacheKey(final String userId, final String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        GroupMembershipCacheKey request = (GroupMembershipCacheKey) obj;

        return userId.equals(request.userId) && groupId.equals(request.groupId);
    }
}
