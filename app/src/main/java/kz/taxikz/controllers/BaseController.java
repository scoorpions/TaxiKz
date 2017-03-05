package kz.taxikz.controllers;

public abstract class BaseController {
    protected abstract void onInject();

    public BaseController() {
        onInject();
    }
}
