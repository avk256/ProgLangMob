package com.example.lab6;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class StatisticsFragment extends Fragment {
    private final String TAG = "StatisticsFragment";
    private Map<String, Integer> easyStatistics;
    private Map<String, Integer> normalStatistics;
    private Map<String, Integer> hardStatistics;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        easyStatistics = new TreeMap<String, Integer>(Collections.reverseOrder());
        normalStatistics = new TreeMap<String, Integer>(Collections.reverseOrder());
        hardStatistics = new TreeMap<String, Integer>(Collections.reverseOrder());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        TableLayout tableEasy = (TableLayout) v.findViewById(R.id.table_easy);
        TableLayout tableNormal = (TableLayout) v.findViewById(R.id.table_normal);
        TableLayout tableHard = (TableLayout) v.findViewById(R.id.table_hard);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab4-1d1e9-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference();
        Query EasyQuery = myRef.orderByChild("Легко/BestResult").limitToLast(5);
        EasyQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if (!dataSnapshot.hasChild("Легко"))
//                    return;
                int res = dataSnapshot.child("Легко/BestResult").getValue(Integer.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                easyStatistics.put(name, res);
                if(easyStatistics.size() == 5){
                    LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
                    easyStatistics.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
                    Set set = reverseSortedMap.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry)i.next();
                        TextView testTV = (TextView) v.findViewById(R.id.textView17);
                        TableRow tr = new TableRow(getActivity());
                        TextView tv1 = new TextView(getActivity());
                        tv1.setText(me.getKey().toString());
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setLayoutParams(testTV.getLayoutParams());
                        tv1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(""+me.getValue());
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setLayoutParams(testTV.getLayoutParams());
                        tv2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
                        tr.addView(tv1);
                        tr.addView(tv2);
                        tableEasy.addView(tr);
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        Query NormalQuery = myRef
                .orderByChild("Нормально/BestResult").limitToLast(5);
        NormalQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!dataSnapshot.hasChild("Нормально"))
                    return;
                int res = dataSnapshot.child("Нормально/BestResult").getValue(Integer.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                normalStatistics.put(name, res);
                if(normalStatistics.size() == 5){
                    LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
                    normalStatistics.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
                    Set set = reverseSortedMap.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry)i.next();
                        TextView testTV = (TextView) v.findViewById(R.id.textView17);
                        TableRow tr = new TableRow(getActivity());
                        TextView tv1 = new TextView(getActivity());
                        tv1.setText(me.getKey().toString());
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setLayoutParams(testTV.getLayoutParams());
                        tv1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(""+me.getValue());
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setLayoutParams(testTV.getLayoutParams());
                        tv2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
                        tr.addView(tv1);
                        tr.addView(tv2);
                        tableNormal.addView(tr);
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        Query HardQuery = myRef
                .orderByChild("Складно/BestResult").limitToLast(5);
        HardQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!dataSnapshot.hasChild("Складно"))
                    return;
                int res = dataSnapshot.child("Складно/BestResult").getValue(Integer.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                hardStatistics.put(name, res);
                if(hardStatistics.size() == 5){
                    LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
                    hardStatistics.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
                    Set set = reverseSortedMap.entrySet();
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry)i.next();
                        TextView testTV = (TextView) v.findViewById(R.id.textView17);
                        TableRow tr = new TableRow(getActivity());
                        TextView tv1 = new TextView(getActivity());
                        tv1.setText(me.getKey().toString());
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
                        tv1.setLayoutParams(testTV.getLayoutParams());
                        TextView tv2 = new TextView(getActivity());
                        tv2.setText(""+me.getValue());
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border));
                        tv2.setLayoutParams(testTV.getLayoutParams());
                        tr.addView(tv1);
                        tr.addView(tv2);
                        tableHard.addView(tr);
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return v;
    }

}