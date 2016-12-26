package com.radioteria.business.services;

public interface HasOwner<O> {
    boolean isOwnedBy(O owner);
}
