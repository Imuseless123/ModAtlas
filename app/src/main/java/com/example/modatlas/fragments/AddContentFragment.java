package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.modatlas.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddContentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOADER = "param1";
    private static final String ARG_VERSION = "param2";

    // TODO: Rename and change types of parameters
    private String loader;
    private String version;

    public AddContentFragment() {
        // Required empty public constructor
    }

    public static AddContentFragment newInstance(String loader, String version) {
        AddContentFragment fragment = new AddContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOADER, loader);
        args.putString(ARG_VERSION, version);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loader = getArguments().getString(ARG_LOADER);
            version = getArguments().getString(ARG_VERSION);
            Log.v("AddContent",loader+" "+version);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_content, container, false);
    }
}