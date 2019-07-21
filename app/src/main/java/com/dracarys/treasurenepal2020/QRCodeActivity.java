package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialogKt;
import com.dracarys.treasurenepal2020.api.ApiService;
import com.dracarys.treasurenepal2020.api.RetrofitClient;
import com.dracarys.treasurenepal2020.entities.LeaderBoard;
import com.dracarys.treasurenepal2020.entities.Treasure;
import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    Treasure mTreasure;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        String id = rawResult.getText();
        Log.d("BIKALPA", id);
        if(!validateQR(id)){
            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);
        }else{
            startActivity(new Intent(QRCodeActivity.this, DrawerActivity.class));
        }

    }

    private boolean validateQR(String id) {
        final double threshold = 0.01;
        final double thisLatitude = 27.718217;
        final double thisLongitude = 85.3820915;

//        if (Math.sqrt(Math.abs(thisLatitude - treasure.getLatitude()) * Math.abs(thisLatitude - treasure.getLatitude()) + Math.abs(thisLongitude - treasure.getLongitude()) * Math.abs(thisLongitude - treasure.getLongitude())) > threshold){
        if(!id.equals( "7" )){
            return false;
        }else{
            /*Create handle for the RetrofitInstance interface*/
            ApiService service = RetrofitClient.getRetrofitInstance().create(ApiService.class);

            Call<LeaderBoard> call = service.validateQR("json", String.valueOf(id));
            call.enqueue(new Callback<LeaderBoard>() {

                @Override
                public void onResponse(Call<LeaderBoard> call, Response<LeaderBoard> response) {
                    DrawerActivity.showAlertDialog("Congratulations! You won some points!! :)");
                    LeaderBoard leaderBoard = response.body();
                    if(leaderBoard != null){
                        PreferenceManager.getDefaultSharedPreferences(QRCodeActivity.this).edit().putString("points", String.valueOf(leaderBoard.getPoints())).apply();
                        PreferenceManager.getDefaultSharedPreferences(QRCodeActivity.this).edit().putBoolean("pointsChanged", true).apply();
                    }
                }

                @Override
                public void onFailure(Call<LeaderBoard> call, Throwable t) {
                    DrawerActivity.showAlertDialog("Ivalid QR Input.");
                    //searchingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(QRCodeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
    }

    public void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}