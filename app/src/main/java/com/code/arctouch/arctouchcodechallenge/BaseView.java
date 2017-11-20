package com.code.arctouch.arctouchcodechallenge;

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
