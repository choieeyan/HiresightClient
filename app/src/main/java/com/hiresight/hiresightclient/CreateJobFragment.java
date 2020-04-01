package com.hiresight.hiresightclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateJobFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText startDateText, endDateText, locationText, productText, payText, paxText, profText;
    Button submitBtn;
    private FirebaseAuth auth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateJobFragment newInstance(String param1, String param2) {
        CreateJobFragment fragment = new CreateJobFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_job, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        startDateText = (EditText) view.findViewById(R.id.startDateText);
        endDateText = (EditText) view.findViewById(R.id.endDateText);
        locationText = (EditText) view.findViewById(R.id.locationText);
        productText = (EditText) view.findViewById(R.id.productText);
        payText = (EditText) view.findViewById(R.id.payText);
        paxText = (EditText) view.findViewById(R.id.paxText);
        profText = (EditText) view.findViewById(R.id.profText);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        submitPost();
        return view;

    }

    public void submitPost(){

        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String currentDateTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()) + " " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        final String clientID = currentUser.getUid();
                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference reference = db.collection("Clients").document(clientID);
                        reference.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            String companyName = documentSnapshot.getString("name");
                                            CreateJobPost post = new CreateJobPost(currentDateTime, startDateText.getText().toString(), endDateText.getText().toString(), locationText.getText().toString(), productText.getText().toString(), payText.getText().toString(), paxText.getText().toString(), profText.getText().toString(), clientID, companyName);
                                            db.collection("Client Posts")
                                                    .add(post)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Job Posted!", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getActivity().getApplicationContext(), "Error! Please Try Again.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    }
                                });

                    }
                }

        );

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
