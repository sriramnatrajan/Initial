package com.one.downloader.whatsappstatusdownloader;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.one.downloader.whatsappstatusdownloader.adapter.RecyclerViewMediaAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by aravind on 5/11/17.
 */

public class ImagesFragment extends Fragment {
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    private RecyclerView mRecyclerViewMediaList;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String TAG = "Main";
    AdView mAdView;TextView  mMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);
        mMessage=(TextView)rootView.findViewById(R.id.message);
        mAdView=(AdView)rootView.findViewById(R.id.adView);
        AdRequest mAdRequest=new AdRequest.Builder().build();
        mAdView.loadAd(mAdRequest);
        mRecyclerViewMediaList = (RecyclerView) rootView.findViewById(R.id.recyclerViewMedia);
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerViewMediaList.setLayoutManager(mLayoutManager);
        System.out.println("......"+ Environment.getExternalStorageDirectory().toString());
        RecyclerViewMediaAdapter recyclerViewMediaAdapter = new RecyclerViewMediaAdapter(this.getListFiles(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION)), getActivity());
        mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);
        return rootView;
    }
    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();


        if (files != null) {
            for (File file : files) {
                Log.d("FILE++++_______",""+new Date(file.lastModified()));
                file.lastModified();
                Arrays.sort(files, new Comparator<File>() {
                    @Override
                    public int compare(File f1, File t1) {
                        return Long.valueOf(f1.lastModified()).compareTo(t1.lastModified());
                    }
                });
                if (file.getName().endsWith(".jpg") ||
                        file.getName().endsWith(".jpeg") ) {
                    if (!inFiles.contains(file))
                        inFiles.add(file);

                    Collections.sort(inFiles);
                    Collections.sort(inFiles,Collections.<File>reverseOrder());

                   mMessage.setVisibility(View.INVISIBLE);
                }
            }
        }
        return inFiles;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}
