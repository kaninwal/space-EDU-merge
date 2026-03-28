package com.spacECE.spaceceedu.ConsultUS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.spacECE.spaceceedu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Fragment_Consultant_Categories extends Fragment {

    private ProgressBar progressBar;

    private ArrayList<ConsultantCategory> categories = new ArrayList<>();
    private RecyclerView categoryRecyclerView;
    private Consultant_Categories_RecyclerAdapter adapter;
    private Consultant_Categories_RecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consultant__categories, container, false);

        Log.i("Categories", "Initiated");

        categoryRecyclerView = v.findViewById(R.id.Consultant_Category_RecyclerView);

        categories = Consultant_Main.categoryList;

        setAdapter(categories);

        return v;
    }


    private void setAdapter(ArrayList<ConsultantCategory> myList) {
        Log.i("SetAdapter:", "Working");
        setOnClickListener();
        adapter = new Consultant_Categories_RecyclerAdapter(myList, listener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            progressBar = getActivity().findViewById(R.id.Loading_Consultants);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            getList(categories.get(position).getCategoryName());
        };
    }

    public void getList(String category) {
        new Thread(() -> {
            JSONObject apiCall = null;
            try {
                String encodedCategory = URLEncoder.encode(category, "UTF-8");
                String url = "https://hustle-7c68d043.mileswebhosting.com/spacece/ConsultUs/api_getconsultant.php?cat=" + encodedCategory;
                apiCall = UsefulFunctions.UsingGetAPI(url);

                if (apiCall != null && apiCall.has("data")) {
                    JSONArray jsonArray = apiCall.getJSONArray("data");
                    ArrayList<Consultant> newList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject response_element = jsonArray.getJSONObject(i);
                        Consultant consultant = new Consultant(
                                response_element.optString("u_name"),
                                response_element.optString("u_id"),
                                response_element.optString("image"),
                                response_element.optString("cat_name"),
                                response_element.optString("c_office"),
                                response_element.optString("c_language"),
                                response_element.optString("c_from_time"),
                                response_element.optString("c_to_time"),
                                response_element.optString("c_qualification"),
                                response_element.optString("c_fee"));
                        newList.add(consultant);
                    }
                    
                    ConsultantsLibrary.consultantsList = newList;

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            Intent intent = new Intent(getContext(), ConsultantsLibrary.class);
                            startActivity(intent);
                        });
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            Toast.makeText(getContext(), "No consultants found for " + category, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }
}
