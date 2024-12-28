package io.github.fherbreteau.functional;

import com.authzed.api.v1.LookupResourcesRequest;
import com.authzed.api.v1.LookupSubjectsRequest;
import com.authzed.api.v1.RelationshipUpdate;

public final class Utils {
    private Utils() { }

    public static String getRelation(RelationshipUpdate update) {
        return update.getRelationship().getRelation();
    }

    public static String getSubjectType(RelationshipUpdate update) {
        return update.getRelationship().getSubject().getObject().getObjectType();
    }

    public static String getSubjectId(RelationshipUpdate update) {
        return update.getRelationship().getSubject().getObject().getObjectId();
    }

    public static String getResourceType(RelationshipUpdate update) {
        return update.getRelationship().getResource().getObjectType();
    }

    public static String getResourceId(RelationshipUpdate update) {
        return update.getRelationship().getResource().getObjectId();
    }

    public static String getResourceType(LookupSubjectsRequest request) {
        return request.getResource().getObjectType();
    }

    public static String getResourceId(LookupSubjectsRequest request) {
        return request.getResource().getObjectId();
    }
}
