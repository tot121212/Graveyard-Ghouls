package com.totsnuk.graveyardghouls.pojo;

public interface Action {
    Enum<?> getElement();

    Record getPayload();
}
