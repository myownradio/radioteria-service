package com.radioteria.business.services;

public interface AccessControlled<O> {
    boolean isAccessibleFor(O target);
}
