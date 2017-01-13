package com.blogspot.mowael.zgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.activities.MapActivity;
import com.blogspot.mowael.zgo.activities.SignUpActivity;
import com.blogspot.mowael.zgo.utilities.Constants;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static SignInFragment signInFragment;
    private ImageView ivProfilePic;
    private EditText etUserName, etPassword;
    private CheckBox cbRememberMe;
    private Button btnSignIn, btnRegister;
    private String userName, password;
    private OnRegisterListener registerListener;
    private TextView tvNotifier;
    private Intent mapActivityIntent;
    private Intent signUpIntent;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;


    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    public static SignInFragment newInstance(String param1, String param2) {
        if (signInFragment == null) {
            signInFragment = new SignInFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            signInFragment.setArguments(args);
            return signInFragment;
        } else return signInFragment;
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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mapActivityIntent = new Intent(getContext(), MapActivity.class);
        signUpIntent = new Intent(getContext(), SignUpActivity.class);
        prefs = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        boolean loginAutomatically = prefs.getBoolean(Constants.LOGIN_AUTOMATICALLY, false);
        if (loginAutomatically) {
            getActivity().startActivity(mapActivityIntent);
            getActivity().finish();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents();
        String imageUri = prefs.getString(Constants.IMAGE_URI, "");
        if (!imageUri.isEmpty()) {
            ivProfilePic.setImageURI(Uri.parse(imageUri));
        }
        if (registerListener != null) {
            registerListener.onRegister(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPause", "fragment paused");
        if (registerListener != null) {
            registerListener.onRegister(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        signInFragment = null;
    }

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
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setRegisterListener(OnRegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                userName = etUserName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (!userName.equals("") && !password.equals("")) {
                    String storedUserName = prefs.getString(Constants.USER_NAME, "");
                    String storedPassword = prefs.getString(Constants.PASSWORD, "");

                    if (storedUserName.equals(userName) && storedPassword.equals(password)) {
                        if (cbRememberMe.isChecked()) {
                            storeSignInData(userName, password);
                        }
                        getActivity().startActivity(mapActivityIntent);
                        getActivity().finish();
                    } else {
                        tvNotifier.setError("");
                        tvNotifier.setText("Make sure that you entered the right username and password");
                        tvNotifier.setTextColor(Color.RED);
                    }

                } else {
                    if (userName.equals("")) {
                        etUserName.setError("You must enter your username");
                    } else if (password.equals("")) {
                        etPassword.setError("You must enter your username");
                    }
                }
                break;
            case R.id.btnRegister:
                getActivity().startActivity(signUpIntent);
                break;
        }
    }

    private void storeSignInData(String userName, String password) {
        editor.putString(Constants.USER_NAME, userName);
        editor.putString(Constants.PASSWORD, password);
        editor.putBoolean(Constants.LOGIN_AUTOMATICALLY, true);
        editor.commit();
    }

    private void initComponents() {
        tvNotifier = (TextView) getActivity().findViewById(R.id.tvNotifier);
        ivProfilePic = (ImageView) getActivity().findViewById(R.id.ivProfilePic);
        etUserName = (EditText) getActivity().findViewById(R.id.etUserName);
        etPassword = (EditText) getActivity().findViewById(R.id.etPassword);
        cbRememberMe = (CheckBox) getActivity().findViewById(R.id.cbRememberMe);
        btnSignIn = (Button) getActivity().findViewById(R.id.btnSignIn);
        btnRegister = (Button) getActivity().findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
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
        void onFragmentInteraction(Uri uri);
    }

    public interface OnRegisterListener {
        void onRegister(FragmentActivity activity);
    }
}
