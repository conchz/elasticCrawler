package org.spider.rx;

import rx.Observable;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2015-10-12.
 *
 * @author dolphineor
 */
public class ObservableList<T> {

    protected final List<T> list;

    protected final PublishSubject<T> onAdd;


    public ObservableList() {
        this.list = new ArrayList<>();
        this.onAdd = PublishSubject.create();
    }

    public void add(T value) {
        list.add(value);
        onAdd.onNext(value);
    }

    public List<T> getList() {
        return list;
    }

    public Observable<T> getObservable() {
        return onAdd;
    }
}
