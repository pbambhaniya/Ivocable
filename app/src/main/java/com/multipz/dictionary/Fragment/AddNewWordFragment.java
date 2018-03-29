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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.dictionary.Activity.DetailsActivity;
import com.multipz.dictionary.Activity.HomeActivity;
import com.multipz.dictionary.Activity.WelcomeActivity;
import com.multipz.dictionary.Json.JSONParser;
import com.multipz.dictionary.Json.config;
import com.multipz.dictionary.R;
import com.multipz.dictionary.Util.Shared;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewWordFragment extends Fragment {

    ImageView imageView;
    private static int RESULT_LOAD_IMAGE = 1;
    LinearLayout word_btn;
    String encodedImage="",encodedImage1="",encodedImage2="";

    EditText edit_word,edit_desc,edit_formula,edit_example;
    ImageView img_word,img_formula,img_example;
    TextView word_txt,desc_txt,img_txt,word_btn1,formula_txt,formula_img,example_text,image_example;

    String w_title,w_desc,f_title,e_title;
    Shared shared;

    int upload_image = 0;

    public AddNewWordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_new_word, container, false);
        shared=new Shared(getActivity());
        Typeface medium = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Medium.ttf");
        Typeface thin = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Thin.ttf");
        Typeface regular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

        word_txt = (TextView)view.findViewById(R.id.word_txt);
        desc_txt = (TextView)view.findViewById(R.id.desc_txt);
        img_txt = (TextView)view.findViewById(R.id.image_word);
        formula_txt = (TextView)view.findViewById(R.id.formula_txt);
        formula_img = (TextView)view.findViewById(R.id.image_formula);
        image_example = (TextView)view.findViewById(R.id.image_example);
        example_text = (TextView)view.findViewById(R.id.example_txt);

        word_txt.setTypeface(medium);
        desc_txt.setTypeface(medium);
        img_txt.setTypeface(medium);
        formula_txt.setTypeface(medium);
        example_text.setTypeface(medium);
        formula_img.setTypeface(medium);
        image_example.setTypeface(medium);

        edit_word = (EditText)view.findViewById(R.id.edit_word);
        edit_formula = (EditText)view.findViewById(R.id.edit_formula);
        edit_desc = (EditText)view.findViewById(R.id.edit_desc);
        edit_example = (EditText)view.findViewById(R.id.edit_example);

        word_btn = (LinearLayout)view.findViewById(R.id.btn_text);
        word_btn1 = (TextView)view.findViewById(R.id.btn_text1);

        img_word = (ImageView)view.findViewById(R.id.word_img);
        img_formula = (ImageView)view.findViewById(R.id.formula_img);
        img_example = (ImageView)view.findViewById(R.id.example_img);

        edit_word.setTypeface(thin);
        edit_formula.setTypeface(thin);
        edit_desc.setTypeface(thin);
        word_btn1.setTypeface(regular);
        edit_example.setTypeface(regular);

        // get Word Data

        img_word.setOnClickListener(new View.OnClickListener() {
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

        img_formula.setOnClickListener(new View.OnClickListener() {
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

        img_example.setOnClickListener(new View.OnClickListener() {
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

        word_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_title = edit_word.getText().toString();
                w_desc = edit_desc.getText().toString();
                f_title = edit_formula.getText().toString();
                e_title = edit_example.getText().toString();

                if (w_title.contentEquals("")){
                    Toast.makeText(getActivity(), "Please enter title", Toast.LENGTH_SHORT).show();
                    return;
                }

                new NewWordAdd().execute();
            }
        });

        return view;
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
                    //new NewWordAdd().execute();
                    //permission granted successfully

                } else {

                    //permission denied

                }
                break;
        }
    }

    private class NewWordAdd extends AsyncTask<String, String, String> {

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
            parm.add(new BasicNameValuePair("json", "{\"word\":\"A\",\"subject_id\":\""+ HomeActivity.subject_id+"\",\"def_title\":\""+w_title+"\",\"def_desc\":\""+w_desc+"\",\"def_img_base\":\""+encodedImage+"\",\"def_img_name\":\"\",\"formula\":\""+f_title+"\",\"formula_img_base\":\""+encodedImage1+"\",\"formula_img_name\":\"\",\"example\":\""+e_title+"\",\"example_img_base\":\""+encodedImage2+"\",\"example_img_name\":\"1\",\"user_id\":\""+shared.getString("user id","")+"\",\"action\":\"addUserWord\"}\n"));

            JSONObject jsonObject = new JSONParser().makeHttpRequest(url, "POST", parm);
            Log.e("url", url);
            Log.e("Response:", "" + jsonObject + "");

            try {
                success = jsonObject.getInt("status");
                Log.e("Response:", "" + success);
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
                Toast.makeText(getActivity(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                edit_word.setText("");
                edit_desc.setText("");
                edit_formula.setText("");
                edit_example.setText("");
                img_word.setImageResource(R.drawable.upload_btn);
                img_formula.setImageResource(R.drawable.upload_btn);
                img_example.setImageResource(R.drawable.upload_btn);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
//            final Bitmap selectedImage = BitmapFactory.decodeStream(picturePath);
            if (upload_image == 1) {
                encodedImage = encodeImage(BitmapFactory.decodeFile(picturePath));
                img_word.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } else if (upload_image == 2) {
                encodedImage1 = encodeImage(BitmapFactory.decodeFile(picturePath));
                img_formula.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } else if (upload_image == 3) {
                encodedImage2 = encodeImage(BitmapFactory.decodeFile(picturePath));
                img_example.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
