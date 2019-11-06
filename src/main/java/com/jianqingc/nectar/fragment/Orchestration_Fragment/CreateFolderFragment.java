package com.jianqingc.nectar.fragment.Orchestration_Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.widget.EditText;

import com.jianqingc.nectar.R;
import com.jianqingc.nectar.httpRequest.HttpRequest;

import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.

 */
public class CreateFolderFragment extends Fragment {
    View myView;
    public static CreateFolderFragment folderLI = null;

    Bundle bundle;
    Bundle bundle2;

    public String folderName = " ";

    public String containerName;



    public CreateFolderFragment() {
        // Required empty public constructor
        folderLI = this;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_create_folder, container, false);
        bundle = getArguments();
        containerName = bundle.getString("containerName");

        Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Create pseudi-folder");

        final Dialog mOverlayDialog = new Dialog(getActivity(), android.R.style.Theme_Panel);
        mOverlayDialog.setCancelable(false);
        mOverlayDialog.setContentView(R.layout.loading_dialog);

        final EditText name = (EditText) myView.findViewById(R.id.newFolderName);

        final Button create = (Button)myView.findViewById(R.id.createFolder);

        final java.util.Timer timer = new java.util.Timer(true);

        FloatingActionButton fabLeft = (FloatingActionButton) getActivity().findViewById(R.id.fabLeft);
        fabLeft.setVisibility(View.VISIBLE);
        fabLeft.setEnabled(true);
        fabLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                ObjectFragment objectFragment = new ObjectFragment();
                bundle2 = new Bundle();
                bundle2.putString("containerName",containerName);
                objectFragment.setArguments(bundle2);

                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, objectFragment, objectFragment.getTag()).commit();
            }
        });

        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                folderName = name.getText().toString()+"/";
                boolean valid = checkInputValid();
                if(!valid){
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill in necessary information", Toast.LENGTH_SHORT).show();

                } else {
                    mOverlayDialog.show();
                    HttpRequest.getInstance(getActivity().getApplicationContext()).createFolder(new HttpRequest.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("success")){
                                Toast.makeText(getActivity().getApplicationContext(),"create folder successfully", Toast.LENGTH_SHORT).show();
                                TimerTask task = new TimerTask(){
                                    @Override
                                    public void run() {
                                        mOverlayDialog.dismiss();
                                        FragmentManager manager = getFragmentManager();
                                        ObjectFragment objectFragment = new ObjectFragment();
                                        bundle2 = new Bundle();
                                        bundle2.putString("containerName",containerName);
                                        objectFragment.setArguments(bundle2);
                                        manager.beginTransaction().replace(R.id.relativelayout_for_fragment, objectFragment, objectFragment.getTag()).commit();

                                    }
                                };
                                timer.schedule(task,4000);
                            } else {
                                mOverlayDialog.dismiss();
                            }
                        }
                    },containerName,folderName);
                }

            }
        });

    return myView;

    }

    private boolean checkInputValid(){

        boolean valid=true;
        if(folderName==" "){
            valid=false;
        }

        return valid;
    }


}
