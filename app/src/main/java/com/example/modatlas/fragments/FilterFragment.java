package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.modatlas.R;
import com.example.modatlas.models.FilterHeader;
import com.example.modatlas.models.FilterItem;
import com.example.modatlas.models.FilterTag;
import com.example.modatlas.views.FilterAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FilterAdapter gameVersionAdapter, loaderAdapter;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        recyclerView = view.findViewById(R.id.modFilterTable);

        List<FilterItem> tes = new ArrayList<>();
        tes.add(new FilterHeader("game version"));
        tes.add(new FilterTag("1.2"));
        tes.add(new FilterTag("1.4"));
        tes.add(new FilterTag("1.5"));
        tes.add(new FilterTag("1.6"));
        tes.add(new FilterHeader("game version"));
        tes.add(new FilterTag("1.2"));
        tes.add(new FilterTag("1.4"));
        tes.add(new FilterTag("1.5"));
        tes.add(new FilterTag("1.6"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameVersionAdapter = new FilterAdapter(getContext(),tes);
        recyclerView.setAdapter(gameVersionAdapter);


        return view;
    }
}