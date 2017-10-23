package com.gokhanaliccii.colorfulbutton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gokhanaliccii.colorfulbutton.view.StackView;

/**
 * Created by gokhan.alici on 23.10.2017.
 */

public class MainBoardViewAdapter implements StackView.StackAdapter {

    private LayoutInflater inflater;

    public MainBoardViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public View getView(int position) {
        return inflater.inflate(R.layout.mainboard_item, null, false);
    }

    public static class MainBoardItemDecorator implements StackView.ItemDecorator {

        private int space;

        @Override
        public int getDecorationSpace(int position) {
            if(position == 2)
                return 60;

            return 20;
        }
    }
}
