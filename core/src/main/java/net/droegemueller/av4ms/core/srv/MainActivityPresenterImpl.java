package net.droegemueller.av4ms.core.srv;

import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;
import net.droegemueller.av4ms.core.util.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivityPresenterImpl implements MainActivityPresenter {

    @Inject
    ServerInteractor interactor;

    private MainActivityView view;
    private Disposable subscription = null;
    private SchedulerProvider scheduler;

    @Inject
    public MainActivityPresenterImpl(SchedulerProvider scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void bind(MainActivityView view) {
        this.view = view;
    }


    @Override
    public void updateData() {
        if (null != view) {
            view.showProgress();
        }

        subscription = interactor.loadBasicData()
                // check if result code is OK
//                .map(charactersResponse -> {
//                    if (Constants.CODE_OK == charactersResponse.getCode())
//                        return charactersResponse;
//                    else
//                        throw Exceptions.propagate(new ApiResponseCodeException(charactersResponse.getCode(), charactersResponse.getStatus()));
//                })
//                // check if is there any result
//                .map(charactersResponse -> {
//                    if (charactersResponse.getData().getCount() > 0)
//                        return charactersResponse;
//                    else
//                        throw Exceptions.propagate(new NoSuchCharacterException());
//                })
//                // map CharacterResponse to CharacterModel
//                .map(Mapper::mapCharacterResponseToCharacter)
//                // cache data on database
//                .map(character -> {
//                    try {
//                        databaseHelper.addCharacter(character);
//                    } catch (SQLException e) {
//                        throw Exceptions.propagate(e);
//                    }
//
//                    return character;
//                })
                .observeOn(scheduler.mainThread())
                .subscribe(new Consumer<MesswerteResponse>() {
                               @Override
                               public void accept(MesswerteResponse character) {
                                   if (null != view) {
                                       view.hideProgress();
                                       view.showBasicData(character);

//                                       if (!isConnected)
//                                           view.showOfflineMessage(false);
                                   }
                               }
                           },
                        // handle exceptions
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                if (null != view) {
                                    view.hideProgress();

//                                    if (isConnected) {
//                                        if (throwable instanceof ApiResponseCodeException)
                                            view.showError(throwable);
//                                        else if (throwable instanceof NoSuchCharacterException)
//                                            view.showQueryNoResult();
//                                        else
//                                            view.showRetryMessage(throwable);

//                                    } else {
//                                        view.showOfflineMessage(true);
//                                    }
                                }
                            }
                        });
    }

    @Override
    public void unbind() {
        if (subscription != null && !subscription.isDisposed())
            subscription.dispose();

        interactor.unbind();

        view = null;
    }
}
