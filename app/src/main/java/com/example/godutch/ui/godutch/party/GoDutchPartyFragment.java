package com.example.godutch.ui.godutch.party;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godutch.Constants;
import com.example.godutch.MainActivity;
import com.example.godutch.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoDutchPartyFragment extends Fragment {
    private static int REQUEST_PARTY_DETAIL_ACTIVITY = 1;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private RecyclerView parties;
    private GoDutchPartyAdapter adapter;
    private String userID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.godutch_party_list, container, false);
        userID = getActivity().getIntent().getStringExtra("USER_ID");
        parties = root.findViewById(R.id.party_list);
        parties.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        parties.setLayoutManager(layoutManager);
        adapter = new GoDutchPartyAdapter(this, userID);
        parties.setAdapter(adapter);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PARTY_DETAIL_ACTIVITY && resultCode == Activity.RESULT_OK)  {
            String postBody = data.getStringExtra("party");
            RequestBody body = RequestBody.create(postBody, JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/api/parties/add", Constants.SERVER_IP))
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String jsonString = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)getActivity()).getViewPager().setCurrentItem(1);
                        }
                    });
                }
            });
        }
    }

    public GoDutchPartyAdapter getAdapter() {
        return this.adapter;
    }
}
