package com.example.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StartFragment extends Fragment {
    private Button start_btn ;
    private Button leave_btn ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        start_btn = (Button) view.findViewById(R.id.start_btn);
        leave_btn = (Button) view.findViewById(R.id.leave_btn);
        addListenerOnButton();

        return view;
    }


    void addListenerOnButton(){
        start_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), GameActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }
                }
        );
        leave_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                }
        );

    }

}