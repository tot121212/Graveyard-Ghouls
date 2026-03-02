package com.totsnuk.graveyardghouls.pojo;

public interface Action<E extends Enum<E>> {
    E getElement();

    Record getPayload();
}
