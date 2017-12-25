package com.indianservers.onlinegrocery.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.indianservers.onlinegrocery.R;

import java.util.ArrayList;

import adapter.AddressAdapter;
import model.AddressCommonClass;
import model.CenterRepository;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryAddressFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences sskey;
    private String profileuid;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Firebase firebase;
    private EditText nickname,personname,houseno,street,area,apartment,landmark,city,pincode,mobile;
    private ProgressDialog progressDialog;
    private ArrayList<AddressCommonClass> commonClasses = new ArrayList<>();
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;

    public DeliveryAddressFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DeliveryAddressFragment newInstance() {
        DeliveryAddressFragment fragment = new DeliveryAddressFragment();

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_address, container, false);

        nickname = (EditText)view.findViewById(R.id.addressnicknameedit);
        personname = (EditText)view.findViewById(R.id.addresspersonnameedit);
        houseno = (EditText)view.findViewById(R.id.addresshousenoedit);
        street = (EditText)view.findViewById(R.id.addressstreetedit);
        area = (EditText)view.findViewById(R.id.addressareaedit);
        apartment = (EditText)view.findViewById(R.id.addressapartmentnameedit);
        landmark = (EditText)view.findViewById(R.id.addresslandmarkedit);
        city = (EditText)view.findViewById(R.id.addresscityedit);
        pincode = (EditText)view.findViewById(R.id.addresspincodeedit);
        mobile = (EditText)view.findViewById(R.id.addressmobileedit);
        recyclerView = (RecyclerView)view.findViewById(R.id.deliveryRecyclerview);
        Button button = (Button)view.findViewById(R.id.saveAddress);
        button.setOnClickListener(this);
            Firebase.setAndroidContext(getContext());
        sskey = PreferenceManager.getDefaultSharedPreferences(getActivity());
        profileuid = sskey.getString("uid","0");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebase  = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+profileuid);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressDialog.dismiss();
                    profileData();

                }else{
                    progressDialog.dismiss();
                    View forgotLayout = getActivity().findViewById(R.id.addresseditlayout);
                    forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getActivity()));
                    forgotLayout.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().findViewById(R.id.deliveryaddresslayout).setVisibility(View.GONE);
                        }
                    }, 500);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return view;
    }
    private void profileData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebase=new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+profileuid);
        refreshdata();
    }
    public void refreshdata() {
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getupdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void getupdates(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            AddressCommonClass aClass = new AddressCommonClass();
            aClass.setAddpid(ds.getKey());
            aClass.setAddnickname(ds.getValue(AddressCommonClass.class).getAddnickname());
            aClass.setAddpersoname(ds.getValue(AddressCommonClass.class).getAddpersoname());
            aClass.setAddhouseno(ds.getValue(AddressCommonClass.class).getAddhouseno());
            aClass.setAddstreetname(ds.getValue(AddressCommonClass.class).getAddstreetname());
            aClass.setAddarea(ds.getValue(AddressCommonClass.class).getAddarea());
            aClass.setAddapartmentno(ds.getValue(AddressCommonClass.class).getAddapartmentno());
            aClass.setAddlandmark(ds.getValue(AddressCommonClass.class).getAddlandmark());
            aClass.setAddcity(ds.getValue(AddressCommonClass.class).getAddcity());
            aClass.setAddpincode(ds.getValue(AddressCommonClass.class).getAddpincode());
            aClass.setAddmobile(ds.getValue(AddressCommonClass.class).getAddmobile());
            commonClasses.add(aClass);
            progressDialog.dismiss();
        }
        AddressAdapter addressAdapter = new AddressAdapter(getActivity(),commonClasses);
        recyclerView.setAdapter(addressAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        addressAdapter.notifyDataSetChanged();

        addressAdapter.SetOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveAddress:
            AddressCommonClass aClass = new AddressCommonClass();
                aClass.setAddnickname(nickname.getText().toString());
                aClass.setAddpersoname(personname.getText().toString());
                aClass.setAddhouseno(houseno.getText().toString());
                aClass.setAddstreetname(street.getText().toString());
                aClass.setAddarea(area.getText().toString());
                aClass.setAddapartmentno(apartment.getText().toString());
                aClass.setAddlandmark(landmark.getText().toString());
                aClass.setAddcity(city.getText().toString());
                aClass.setAddpincode(pincode.getText().toString());
                aClass.setAddmobile(mobile.getText().toString());
                firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+profileuid+"/"+profileuid);
                firebase.push().child("").setValue(aClass);

                View forgotLayout = getActivity().findViewById(R.id.addresseditlayout);
                forgotLayout.setAnimation(AnimationUtils.makeInChildBottomAnimation(getActivity()));
                forgotLayout.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().findViewById(R.id.deliveryaddresslayout).setVisibility(View.VISIBLE);
                    }
                }, 500);
                profileData();
                Toast.makeText(getContext(),"Address Saved",Toast.LENGTH_SHORT).show();
                break;
        }
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
