package com.hiresight.hiresightclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PastJobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PastJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastJobFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    private FirebaseFirestore db;
    private CollectionReference reference;
    FirestoreRecyclerAdapter adapter;
    private FirebaseAuth auth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PastJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PastJobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PastJobFragment newInstance(String param1, String param2) {
        PastJobFragment fragment = new PastJobFragment();
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
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_past_job, container, false);
        reference = db.collection("Client Posts");
        recyclerView = view.findViewById(R.id.pastjob_view);

        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView(){
        auth = FirebaseAuth.getInstance();
        Query query = reference.orderBy("postDateTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CreateJobPost> options = new FirestoreRecyclerOptions.Builder<CreateJobPost>()
                .setQuery(query, CreateJobPost.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<CreateJobPost, PostHolder>(options) {
            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_post_layout, parent, false);
                return new PostHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PostHolder holder, int position, @NonNull final CreateJobPost model) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference ref = db.collection("Clients").document(model.getClientID());

                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String imageURL = document.getString("imageURL");
                                Picasso.get().load(imageURL).into(holder.clientImage);
                            } else
                                Log.d("TAG", "No such document");
                        } else
                            Log.d("TAG", "get failed with ", task.getException());
                    }
                });
                holder.companyName.setText(model.getCompanyName());
                holder.hiredate.setText(model.getStartDate()+ " til " +model.getEndDate());
                holder.location.setText(model.getLocation());
                holder.pay.setText(model.getPay());
                holder.product.setText(model.getProduct());
                holder.paxRequired.setText(model.getPaxRequired());
                holder.profession.setText(model.getProfession());

                if (model.getClientID().equals(auth.getUid())){
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } else{
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }

                holder.viewBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String postID = getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
                        Intent intent = new Intent(getContext(), ViewApplicantsActivity.class);
                        intent.putExtra("postID", postID);
                        getContext().startActivity(intent);
                    }
                });

                holder.payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    class PostHolder extends RecyclerView.ViewHolder {
        ImageView clientImage;
        TextView companyName, hiredate, location, product, pay, paxRequired, profession;
        Button viewBtn, payBtn;


        public PostHolder(@NonNull View itemView) {
            super(itemView);
            clientImage = itemView.findViewById(R.id.client_image);
            companyName = itemView.findViewById(R.id.company_name);
            hiredate = itemView.findViewById(R.id.hire_date);
            location = itemView.findViewById(R.id.location);
            product = itemView.findViewById(R.id.product);
            pay = itemView.findViewById(R.id.pay);
            paxRequired = itemView.findViewById(R.id.pax_required);
            profession = itemView.findViewById(R.id.profession);
            viewBtn = itemView.findViewById(R.id.viewBtn);
            payBtn = itemView.findViewById(R.id.payBtn);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
