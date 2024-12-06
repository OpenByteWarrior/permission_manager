package com.permission_management.domain.models;

import java.util.Set;

public interface ResourceContainer<T> {
    Set<T> getResources();
    void setResources(Set<T> resources);
}
