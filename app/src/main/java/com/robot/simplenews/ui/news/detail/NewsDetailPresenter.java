package com.robot.simplenews.ui.news.detail;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.entity.NewsDetailEntity;
import com.robot.simplenews.util.RxUtil;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 */
public class NewsDetailPresenter implements NewsDetailContract.Presenter {
    private RxAppCompatActivity mRxActivity;
    private Context mContext;
    private NewsDetailContract.View mNewsDetailView;

    public NewsDetailPresenter(RxAppCompatActivity rxActivity) {
        this.mRxActivity = rxActivity;
        this.mContext = mRxActivity;
    }

    @Override
    public void attachView(@NonNull NewsDetailContract.View view) {
        mNewsDetailView = view;
    }

    @Override
    public void detachView() {
        mNewsDetailView = null;
    }

    @Override
    public void loadNewsDetail(String docId) {
        mNewsDetailView.showProgress();
        NewsApi.getInstance().getNewsDetail(docId).observeOn(AndroidSchedulers.mainThread())
                .compose((mRxActivity).<NewsDetailEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<NewsDetailEntity>() {
                    @Override
                    public void call(NewsDetailEntity newsDetailEntity) {
                        if (newsDetailEntity != null) {
                            mNewsDetailView.showNewsDetialContent(newsDetailEntity.getBody());
                        }
                        mNewsDetailView.hideProgress();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mNewsDetailView.hideProgress();
                    }
                });
    }
}
