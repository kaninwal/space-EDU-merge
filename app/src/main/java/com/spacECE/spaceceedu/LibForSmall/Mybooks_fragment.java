package com.spacECE.spaceceedu.LibForSmall;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.spacECE.spaceceedu.R;

public class Mybooks_fragment extends Fragment {

    public Mybooks_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mybooks, container, false);
    }
}