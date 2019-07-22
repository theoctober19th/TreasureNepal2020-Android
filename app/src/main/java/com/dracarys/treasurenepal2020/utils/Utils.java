package com.dracarys.treasurenepal2020.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.dracarys.treasurenepal2020.entities.LeaderBoard;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static List<LeaderBoard> getLeaderBoard(){
        List<LeaderBoard> list = new ArrayList<LeaderBoard>();

        list.add(new LeaderBoard(1, "Bikalpa Dhakal", 45));
        list.add(new LeaderBoard(2, "Sankalpa Dhakal", 40));
        list.add(new LeaderBoard(3, "Sanjay Dhakal", 30));
        list.add(new LeaderBoard(4, "Asmita Mallik", 23));
        list.add(new LeaderBoard(5, "Bikalpa Dhakal", 10));
        list.add(new LeaderBoard(6, "Gayatri Dhakal", 10));
        list.add(new LeaderBoard(9, "Tirtha Dhakal", 8));
        list.add(new LeaderBoard(7, "Bikalpa Dhakal", 7));
        list.add(new LeaderBoard(8, "Bibek Dhakal", 5));

        return list;
    }
}
