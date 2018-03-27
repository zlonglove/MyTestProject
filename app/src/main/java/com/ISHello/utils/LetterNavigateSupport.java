package com.ISHello.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.ishelloword.R;

import android.content.res.Resources;

public class LetterNavigateSupport {
    public static List<String> getLetters(Resources res) {
        ArrayList<String> list = new ArrayList<String>();
        appendHead(list, res);
        appendAlpha(list);
        appendTail(list);
        return list;
    }

    private static void appendHead(ArrayList<String> list, Resources res) {
        String star = res.getString(R.string.letter_bar_star);
        list.add(star);
    }

    private static void appendAlpha(ArrayList<String> list) {
        for (char c = 'A'; c <= 'Z'; ++c)
            list.add(String.valueOf(c));
    }

    private static void appendTail(ArrayList<String> list) {
        list.add("#");
    }
}
