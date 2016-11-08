package com.acadgild.s9A3AsyncNDialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    int asyncTaskNumber;
    EditText etSearch;
    Button dialogBtn, btnOk, btnCancel;
    Dialog dialogSearch, dialogLoading, dialogSuccess;
    ProgressBar progressBar;
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogBtn = (Button) findViewById(R.id.btn_open_dialog);
        dialogBtn.setOnClickListener(this);

        dialogSearch = new Dialog(MainActivity.this);

        dialogLoading = new Dialog(MainActivity.this);
        dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogSuccess = new Dialog(MainActivity.this);
        dialogSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_open_dialog: {
                showDialogSearch();
                break;
            }

            //On the click of OK Button get the number entered in the EditText and pass the same to AsyncTask.
            case R.id.btn_ok: {
                asyncTaskNumber = !etSearch.getText().toString().equals("") ? Integer.parseInt(etSearch.getText().toString()) : 0;
                startAsyncTask();
                break;
            }

            case R.id.btn_cancel: {
                dialogSearch.dismiss();
                break;
            }

            default: {
                dialogSearch.dismiss();
                break;
            }
        }

    }

    private void showDialogSearch() {
        /*Create Dialog -
        Dialog must consist of an EditText for accepting the user input (number)
        with "OK‟ & "CANCEL‟ Buttons.*/

        dialogSearch.setTitle("Load");
        dialogSearch.setContentView(R.layout.dialog_search);

        etSearch = (EditText) dialogSearch.findViewById(R.id.et_search);

        btnOk = (Button) dialogSearch.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(MainActivity.this);

        btnCancel = (Button) dialogSearch.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(MainActivity.this);

        dialogSearch.show();
    }

    private void showDialogLoading() {

        if (dialogSearch.isShowing())
            dialogSearch.dismiss();


        dialogLoading.setContentView(R.layout.dialog_loading);

        progressBar = (ProgressBar) dialogLoading.findViewById(R.id.progress_bar);
        loadingText = (TextView) dialogLoading.findViewById(R.id.loading_txt);

        dialogLoading.show();
    }

    protected void showDialogSuccess(){
        dialogSuccess.setContentView(R.layout.dialog_success);
        dialogSuccess.show();
    }

    private void startAsyncTask() {
        if (asyncTaskNumber >= 1 && asyncTaskNumber <= 100) {
            showDialogLoading();
            new DialogAsyncTask().execute(R.drawable.painter);
            Toast.makeText(getApplicationContext(), "Started startAsyncTask for " + asyncTaskNumber + " sec.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Enter number from 1 to 100", Toast.LENGTH_SHORT).show();

    }

    public class DialogAsyncTask extends AsyncTask<Integer, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap tmp = BitmapFactory.decodeResource(getResources(), params[0]);

            for (int i = 1; i <= asyncTaskNumber; i++) {
                sleep(1);
//                publishProgress(50); // will call onProgressUpdate()
            }
            return tmp;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            if (dialogLoading.isShowing())
                dialogLoading.dismiss();
            showDialogSuccess();
        }

        private void sleep(int seconds) {
            try {
                Thread.sleep((long) seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}