package com.industrialmaster.farmnet.presenters;

import android.app.Activity;
import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Comment;
import com.industrialmaster.farmnet.models.response.UserDetailsResponse;
import com.industrialmaster.farmnet.network.DisposableManager;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.CommentView;
import com.industrialmaster.farmnet.views.View;
import com.industrialmaster.farmnet.views.activities.MainActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class CommentPresenterImpl extends BasePresenter implements CommentPresenter {

    private CommentView commentView;

    private DatabaseReference commentRef;


    private String userId = readSharedPreferences(FarmnetConstants.USER_ID, "");
    private String accessToken = "Bearer " + readSharedPreferences(FarmnetConstants.TOKEN_PREFS_KEY, FarmnetConstants.CheckUserLogin.LOGOUT_USER);

    public CommentPresenterImpl(Activity activityContext, View view) {
        super(activityContext);
        if(view instanceof CommentView) {
            commentView = (CommentView) view;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        commentRef = database.getReference("comments");
    }

    @Override
    public void addNewComment(String userId, String postId, Comment comment) {

        FirebaseMessaging.getInstance().subscribeToTopic(userId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        commentRef.child(postId).push().setValue(comment);
        commentView.onSuccess("Successfully commented");
    }

    @Override
    public void getCommentingUserDetails() {
        getUserByIdObservable(accessToken, userId).subscribe(getUserByIdSubscriber());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    public Observable<UserDetailsResponse> getUserByIdObservable(String accesToken, String userId) {
        try {
            return getRetrofitClient().getUserById(accesToken, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public Observer<UserDetailsResponse> getUserByIdSubscriber(){
        return new Observer<UserDetailsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
                DisposableManager.add(d);
            }

            @Override
            public void onNext(UserDetailsResponse userDetailsResponse) {
                commentView.saveComment(userDetailsResponse.getUser());
            }

            @Override
            public void onError(Throwable e) {
                try {
                    commentView.onError(handleApiError(e));
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
