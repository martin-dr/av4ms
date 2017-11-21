package net.droegemueller.av4ms.core.base;

public interface BasePresenter<T> {

    void bind(T view);

    void unbind();

}
