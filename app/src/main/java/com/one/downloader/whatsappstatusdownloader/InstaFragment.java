package com.one.downloader.whatsappstatusdownloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aravind on 6/11/17.
 */

public class InstaFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.insta_fragment, container, false);
        TextView mMessage = (TextView) rootView.findViewById(R.id.message);
        return rootView;
    }
}
