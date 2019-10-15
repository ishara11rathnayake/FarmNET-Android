package com.industrialmaster.farmnet.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.industrialmaster.farmnet.R;
import com.industrialmaster.farmnet.models.Comment;
import com.industrialmaster.farmnet.models.Notification;
import com.industrialmaster.farmnet.models.User;
import com.industrialmaster.farmnet.utils.FarmnetConstants;
import com.industrialmaster.farmnet.views.adapters.NotificationRecyclwViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {

    View rootView;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notificationRef = database.getReference("notification");

        String mUserId = rootView.getContext().getSharedPreferences("FarmnetPrefsFile", Context.MODE_PRIVATE)
                .getString(FarmnetConstants.USER_ID, "");

        notificationRef.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Notification> notifications = new ArrayList<>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    Notification notification = dsp.getValue(Notification.class);
                    notifications.add(notification);
                }
                showNotification(notifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private void showNotification(List<Notification> notifications){
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_notification);
        NotificationRecyclwViewAdapter adapter = new NotificationRecyclwViewAdapter(getActivity(), notifications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
