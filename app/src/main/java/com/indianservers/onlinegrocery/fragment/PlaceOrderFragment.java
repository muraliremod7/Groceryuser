package com.indianservers.onlinegrocery.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.indianservers.onlinegrocery.CheckOutActivity;
import com.indianservers.onlinegrocery.PlaceOrderActivity;
import com.indianservers.onlinegrocery.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import adapter.AddressAdapter;
import adapter.CalenderSpinnerAdapter;
import adapter.PlaceOrderCommonClass;
import model.AddressCommonClass;
import model.CenterRepository;
import model.OrdersCommonClass;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceOrderFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String title;
    public int page;
    private Firebase firebase;
    private RecyclerView orcoreview;
    // TODO: Rename and change types of parameters

    private String SSkey;
    private Spinner date,time;
    private TextView noofitems,deliverydate,deliverytime,deliverycharges,totalpayableamount,orderCost;
    private Button placeorder;
    private Button addnewAddress, cancelAddress;
    private ArrayList<AddressCommonClass> commonClasses = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @param pageTitle Parameter 2.
     * @return A new instance of fragment PlaceOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceOrderFragment newInstance(int position, String pageTitle) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", position);
        args.putString("someTitle", pageTitle);
        fragment.setArguments(args);
        return fragment;
    }
    public static PlaceOrderFragment newInstanceOne(String date, String time) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        orcoreview = (RecyclerView)view.findViewById(R.id.confirmorderrecycler);
        date = (Spinner)view.findViewById(R.id.choosedate);
        time = (Spinner)view.findViewById(R.id.choosetime);

        noofitems = (TextView)view.findViewById(R.id.orderTotalnoofitems);
        deliverydate = (TextView)view.findViewById(R.id.orderDeliveryDate);
        deliverytime = (TextView)view.findViewById(R.id.orderDeliveryTime);
        totalpayableamount = (TextView)view.findViewById(R.id.orderDeliverypayamount);
        placeorder =(Button)view.findViewById(R.id.placeOrder);
        deliverycharges = (TextView)view.findViewById(R.id.orderDeliveryCharges) ;
        orderCost = (TextView)view.findViewById(R.id.orderCost);
        addnewAddress = (Button)view.findViewById(R.id.conaddNewaddress);
        addnewAddress.setOnClickListener(this);
        SharedPreferences sskey = PreferenceManager.getDefaultSharedPreferences(getContext());
        SSkey = sskey.getString("uid","0");
        Firebase.setAndroidContext(getContext());
        if(page==0){
            LinearLayout spinner = (LinearLayout)view.findViewById(R.id.spinnertimeslot);
            spinner.setVisibility(View.GONE);
            LinearLayout ordersummary = (LinearLayout)view.findViewById(R.id.ordersummury);
            ordersummary.setVisibility(View.GONE);
            LinearLayout recycler = (LinearLayout)view.findViewById(R.id.recyclerorder);
            recycler.setVisibility(View.VISIBLE);
            firebase  = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+SSkey);
            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        profileData();

                    }else{
                          showAlertDailog();

                        }
                    }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }else if(page==1){
            LinearLayout recycler = (LinearLayout)view.findViewById(R.id.recyclerorder);
            recycler.setVisibility(View.GONE);
            LinearLayout spinner = (LinearLayout)view.findViewById(R.id.spinnertimeslot);
            spinner.setVisibility(View.VISIBLE);
            LinearLayout ordersummary = (LinearLayout)view.findViewById(R.id.ordersummury);
            ordersummary.setVisibility(View.GONE);
            CalenderSpinnerAdapter calenderSpinnerAdapter = new CalenderSpinnerAdapter(getActivity(), 10);
            date.setAdapter(calenderSpinnerAdapter);
            date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    CenterRepository.getCenterRepository().setDate(date.getSelectedItem().toString());
                    PlaceOrderActivity.datee = getSelectedDateAsString(date);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    CenterRepository.getCenterRepository().setDate(time.getSelectedItem().toString());
                    PlaceOrderActivity.timee = time.getSelectedItem()
                            .toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else if(page==2){
            LinearLayout recycler = (LinearLayout)view.findViewById(R.id.recyclerorder);
            recycler.setVisibility(View.GONE);
            LinearLayout spinner = (LinearLayout)view.findViewById(R.id.spinnertimeslot);
            spinner.setVisibility(View.GONE);
            LinearLayout ordersummary = (LinearLayout)view.findViewById(R.id.ordersummury);
            ordersummary.setVisibility(View.VISIBLE);
            noofitems.setText(String.valueOf(CenterRepository.getCenterRepository().getListofAddress().size()));
            deliverytime.setText(PlaceOrderActivity.timee);
            deliverydate.setText(PlaceOrderActivity.datee);
            orderCost.setText(CheckOutActivity.finalprice.getText().toString());
            int n = Integer.parseInt(CheckOutActivity.finalprice.getText().toString());
            int j = 50;
            if(n<500){
                deliverycharges.setText("50");
                int k = n+j;
                totalpayableamount.setText(String.valueOf(k));
            }else {
                deliverycharges.setText("0");
                totalpayableamount.setText(CheckOutActivity.finalprice.getText().toString());
            }
            placeorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertbox = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle)
                            .setMessage("Do you want Confirm This Order")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    PlaceOrderCommonClass aClass = new PlaceOrderCommonClass();
                                    Random rnd = new Random();
                                    int n = 100000 + rnd.nextInt(900000);
                                    aClass.setUid(SSkey);
                                    aClass.setOrderid("KM"+String.valueOf(n));
                                    aClass.setOrderitems(String.valueOf(noofitems.getText().toString()));
                                    aClass.setOrderdate(String.valueOf(deliverydate.getText().toString()));
                                    aClass.setOrdertime(String.valueOf(deliverytime.getText().toString()));
                                    aClass.setStatus("Initiated");

                                    aClass.setOrderpayableamount(String.valueOf(totalpayableamount.getText().toString()));
                                    firebase  = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"PlacedOrders"+"/"+SSkey+"/"+SSkey);
                                    firebase.push().setValue(aClass);
                                    firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"AdminTable"+"/"+"ListOfOrders");
                                    firebase.push().setValue(aClass);
                                    firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"CartItems"+"/"+SSkey+"/"+SSkey);
                                    firebase.removeValue();
                                    ArrayList<OrdersCommonClass> commonClasses = new ArrayList<OrdersCommonClass>(CenterRepository.getCenterRepository().getListofAddress());
                                    for(int i=0;i<commonClasses.size();i++){
                                        OrdersCommonClass commonClass =  new OrdersCommonClass();
                                        commonClass.setPrpid(commonClasses.get(i).getPrpid());
                                        commonClass.setPrName(commonClasses.get(i).getPrName());
                                        commonClass.setPrPrice(commonClasses.get(i).getPrPrice());
                                        commonClass.setPrMeasure(commonClasses.get(i).getPrMeasure());
                                        commonClass.setPrQunatity(commonClasses.get(i).getPrQunatity());
                                        firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"AdminTable"+"/"+"CartItemsByid"+"/"+SSkey);
                                        firebase.push().setValue(commonClass);
                                    }
                                    Toast.makeText(getContext(),"Your Order Placed Successfully",Toast.LENGTH_SHORT).show();
                                    //close();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                // do something when the button is clicked
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            });

        }
        return view;
    }

    private void showAlertDailog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater1 = getActivity().getLayoutInflater();
        final View dialogView = inflater1.inflate(R.layout.update_address, null);
        builder.setView(dialogView);
        builder.setTitle("Add Your Address");
        final AlertDialog alertDialog = builder.create();
        final EditText nickname = (EditText)dialogView.findViewById(R.id.addressnicknameedital);
        final EditText personname = (EditText)dialogView.findViewById(R.id.addresspersonnameedital);
        final EditText hoseno = (EditText)dialogView.findViewById(R.id.addresshousenoedital);
        final EditText streetname = (EditText)dialogView.findViewById(R.id.addressstreetedital);
        final EditText areaname = (EditText)dialogView.findViewById(R.id.addressareaedital);
        final EditText apartmentname = (EditText)dialogView.findViewById(R.id.addressapartmentnameedital);
        final EditText landmark = (EditText)dialogView.findViewById(R.id.addresslandmarkedital);
        final EditText city = (EditText)dialogView.findViewById(R.id.addresscityedital);
        final EditText pincode = (EditText)dialogView.findViewById(R.id.addresspincodeedital);
        final EditText mobile = (EditText)dialogView.findViewById(R.id.addressmobileedital);
            cancelAddress = (Button)dialogView.findViewById(R.id.cancelAddressal);
        Button update = (Button)dialogView.findViewById(R.id.saveAddressal);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressCommonClass aClass = new AddressCommonClass();
                aClass.setAddnickname(nickname.getText().toString());
                aClass.setAddpersoname(personname.getText().toString());
                aClass.setAddhouseno(hoseno.getText().toString());
                aClass.setAddstreetname(streetname.getText().toString());
                aClass.setAddarea(areaname.getText().toString());
                aClass.setAddapartmentno(apartmentname.getText().toString());
                aClass.setAddlandmark(landmark.getText().toString());
                aClass.setAddcity(city.getText().toString());
                aClass.setAddpincode(pincode.getText().toString());
                aClass.setAddmobile(mobile.getText().toString());


                firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"Address"+"/"+SSkey+"/"+SSkey);
                firebase.push().setValue(aClass);
                alertDialog.dismiss();
                startActivity(getActivity().getIntent());
                getActivity().finish();
            }
        });
        cancelAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void profileData() {
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
        }
        AddressAdapter addressAdapter = new AddressAdapter(getActivity(),commonClasses);
        orcoreview.setAdapter(addressAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext());
        orcoreview.setLayoutManager(linearLayoutManager);
        orcoreview.setHasFixedSize(true);
        addressAdapter.notifyDataSetChanged();

        addressAdapter.SetOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                AlertDialog alertbox = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle)
                        .setMessage("Do you want Choose this your Default address")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                //close();
                                final AddressCommonClass aClass = new AddressCommonClass();
                                view.setSelected(true);
                                view.setBackgroundColor(Color.parseColor("#E5F1FB"));
                                String aid = ((TextView)view.findViewById(R.id.aid)).getText().toString();
                                String anName = ((TextView)view.findViewById(R.id.addressnickname)).getText().toString();
                                String apname = ((TextView)view.findViewById(R.id.addressName)).getText().toString();
                                String ahouseno = ((TextView)view.findViewById(R.id.addressHouseno)).getText().toString();
                                String astreetno = ((TextView)view.findViewById(R.id.addressStreet)).getText().toString();
                                String aArea = ((TextView)view.findViewById(R.id.addressArea)).getText().toString();
                                String aApname = ((TextView)view.findViewById(R.id.addressAppartmentName)).getText().toString();
                                String alandmark = ((TextView)view.findViewById(R.id.addressLandmark)).getText().toString();
                                final String aCity = ((TextView)view.findViewById(R.id.addressCity)).getText().toString();
                                String aPincode = ((TextView)view.findViewById(R.id.addressPincode)).getText().toString();
                                String aMobile = ((TextView)view.findViewById(R.id.addressmobile)).getText().toString();
                                aClass.setAddpid(aid);
                                aClass.setAddnickname(anName);
                                aClass.setAddpersoname(apname);
                                aClass.setAddhouseno(ahouseno);
                                aClass.setAddstreetname(astreetno);
                                aClass.setAddarea(aArea);
                                aClass.setAddapartmentno(aApname);
                                aClass.setAddlandmark(alandmark);
                                aClass.setAddcity(aCity);
                                aClass.setAddpincode(aPincode);
                                aClass.setAddmobile(aMobile);

                                firebase = new Firebase("https://online-grocery-88ba4.firebaseio.com/"+"ConfAddress"+"/"+SSkey+"/"+SSkey);
                                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                firebase.child(ds.getKey()).setValue(aClass);
                                            }
                                        }else {
                                            firebase.push().child("").setValue(aClass);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }
        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private String getSelectedDateAsString(Spinner dateSpinner) {
        Calendar selectedDate = (Calendar) dateSpinner.getSelectedItem();
        return new SimpleDateFormat("d MMM yyyy").format(selectedDate.getTimeInMillis());
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.conaddNewaddress:
                showAlertDailog();
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
