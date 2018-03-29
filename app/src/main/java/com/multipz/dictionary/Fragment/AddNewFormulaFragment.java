package com.multipz.dictionary.Fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Activity.WelcomeActivity;
import com.multipz.dictionary.Adapter.MathsWordAdapter;
import com.multipz.dictionary.Adapter.SpinnerAdapter;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.Model.MathsWordModel;
import com.multipz.dictionary.Model.SpinnerModel;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Shared;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewFormulaFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    LinearLayout lin_img,lin_desc;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;
    String encodedImage="",encodedImage1="",encodedImage2="",spin;
    Shared shared;
    int upload_image = 0;

    EditText edit_for,edit_desc_for,edit_for_title,edit_formula,edit_desc;
    ImageView default_img,formula_img,example_img;
    TextView word_txt,desc_txt,img_txt,word_btn,formula_txt,formula_btn,for_subtype,for_title,formula_default,image_default,formula_desc,image_for_title,for_example,image_for;

    String subtype,title,desc,formula,example,subject_id,type,type_id;
    private JSONArray MathsWordList = new JSONArray();
    ArrayList<SpinnerModel> formulalist = new ArrayList<SpinnerModel>();

    Spinner spinner;
    private static SpinnerAdapter adapter;

    public AddNewFormulaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_new_formula, container, false);
        shared=new Shared(getActivity());

        Typeface medium = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Medium.ttf");
        Typeface thin = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Thin.ttf");
        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

        word_txt = (TextView)view.findViewById(R.id.formula_txt);
        for_subtype = (TextView)view.findViewById(R.id.for_subtype);
        formula_default = (TextView)view.findViewById(R.id.formula_default);
        image_default = (TextView)view.findViewById(R.id.image_default);

        for_title = (TextView)view.findViewById(R.id.for_title);
        formula_desc = (TextView)view.findViewById(R.id.formula_desc);
        image_for_title = (TextView)view.findViewById(R.id.image_for_title);
        for_example = (TextView)view.findViewById(R.id.for_example);
        image_for = (TextView)view.findViewById(R.id.image_for);

        for_title.setTypeface(medium);
        word_txt.setTypeface(medium);
        for_subtype.setTypeface(medium);
        formula_default.setTypeface(medium);
        image_default.setTypeface(medium);
        formula_desc.setTypeface(medium);
        image_for_title.setTypeface(medium);
        for_example.setTypeface(medium);
        image_for.setTypeface(medium);

        edit_for = (EditText)view.findViewById(R.id.edit_for);
        edit_for_title = (EditText)view.findViewById(R.id.edit_for_title);
        edit_desc_for = (EditText)view.findViewById(R.id.edit_desc_for);
        edit_formula = (EditText)view.findViewById(R.id.edit_formula);
        edit_desc = (EditText)view.findViewById(R.id.edit_desc);

        formula_btn = (TextView)view.findViewById(R.id.btn_text);


        default_img = (ImageView)view.findViewById(R.id.default_img);
        formula_img = (ImageView)view.findViewById(R.id.formula_img);
        example_img = (ImageView)view.findViewById(R.id.example_img);

        edit_for.setTypeface(thin);
        edit_for_title.setTypeface(thin);
        edit_desc_for.setTypeface(thin);
        edit_formula.setTypeface(thin);
        edit_desc.setTypeface(thin);

        formula_btn.setTypeface(regular);

        formula_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtype=edit_for.getText().toString();
                title=edit_for_title.getText().toString();
                desc=edit_desc_for.getText().toString();
                formula=edit_formula.getText().toString();
                spin = formulalist.get(spinner.getSelectedItemPosition()).getFormula_type_id();
                example=edit_desc.getText().toString();

                if (spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subtype.contentEquals("")){
                    Toast.makeText(getActivity(), "Please enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
                new NewFormulasAdd().execute();

            }
        });

        default_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    upload_image=1;
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},144);
                }

            }
        });

        formula_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    upload_image=2;
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},144);
                }

            }
        });

        example_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    upload_image=3;
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},144);
                }

            }
        });

        spinner = (Spinner) view.findViewById(R.id.Spin_formula);
        new SpinnerData().execute();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
//        Toast.makeText(getActivity(),spinner.getSelectedItem().toString() , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private class SpinnerData extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"subject_id\":\"1\",\"action\":\"getTypeFormula\"}\n\n"));
            formulalist.clear();
            formulalist.add(new SpinnerModel("Select Category","",""));
            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);
                if (success == 1) {
                    MathsWordList = jsonObject.getJSONArray("data");
                    for (int i1 = 0; i1 < MathsWordList.length(); i1++) {
                        JSONObject c = MathsWordList.getJSONObject(i1);

                        subject_id = c.getString("subject_id");
                        type_id = c.getString("formula_type_id");
                        type = c.getString("type");

                        SpinnerModel model = new SpinnerModel(type,type_id,subject_id);
                        formulalist.add(model);
                    }

                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (success == 1)
            {
                adapter = new SpinnerAdapter(getActivity(), formulalist);
                spinner.setAdapter(adapter);
            }
        }
    }

    private class NewFormulasAdd extends AsyncTask<String, String, String> {

        private String url = config.MAIN_API;
        private int success;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "iVocabe", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> parm = new ArrayList<NameValuePair>();
            parm.add(new BasicNameValuePair("json", "{\"formula_type_id\":\""+spin+"\",\"sub_type\":\""+subtype+"\",\"def_title\":\""+title+"\",\"def_desc\":\""+desc+"\",\"def_img_base\":\""+encodedImage+"\",\"formula\":\""+formula+"\",\"formula_img_base\":\""+encodedImage1+"\",\"example\":\""+example+"\",\"example_img_base\":\""+encodedImage2+"\",\"user_id\":\""+shared.getString("user id","")+"\",\"action\":\"addUserFormula\"}\n"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);
            }
                catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (success == 1)
            {
                Toast.makeText(getActivity(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                edit_for.setText("");
                edit_for_title.setText("");
                edit_desc_for.setText("");
                edit_formula.setText("");
                edit_desc.setText("");
                default_img.setImageResource(R.drawable.upload_btn);
                formula_img.setImageResource(R.drawable.upload_btn);
                example_img.setImageResource(R.drawable.upload_btn);
                spinner.setSelection(0);
            }
        }
    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 144:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //new NewFormulasAdd().execute();
                    //permission granted successfully

                } else {

                    //permission denied

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
//            final Bitmap selectedImage = BitmapFactory.decodeStream(picturePath);

            if (upload_image == 1) {
                encodedImage = encodeImage(BitmapFactory.decodeFile(picturePath));
                default_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } else if (upload_image == 2) {
                encodedImage1 = encodeImage(BitmapFactory.decodeFile(picturePath));
                formula_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } else if (upload_image == 3) {
                encodedImage2 = encodeImage(BitmapFactory.decodeFile(picturePath));
                example_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }

            cursor.close();
        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        encImage = encImage.replaceAll("\n","");

        return encImage;
    }

}
