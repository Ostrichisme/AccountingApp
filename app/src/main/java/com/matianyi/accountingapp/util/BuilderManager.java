package com.matianyi.accountingapp.util;

import android.graphics.Color;
import android.util.Pair;

import com.matianyi.accountingapp.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weiping Huang at 23:44 on 16/11/21
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */
public class BuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.menu_stat,
            R.drawable.menu_about
    };

    private static int[] textResources = new int[]{
            R.string.title_statistics,
            R.string.title_about
};

    private static int imageResourceIndex = 0;

    private static int textResourceIndex = 0;


    public static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }
    public static int getTextResource() {
        if (textResourceIndex >= textResources.length) textResourceIndex = 0;
        return textResources[textResourceIndex++];
    }

    private static BuilderManager ourInstance = new BuilderManager();

    public static BuilderManager getInstance() {
        return ourInstance;
    }

    private BuilderManager() {
    }
}