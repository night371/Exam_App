package com.example.exam;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

public class simple_progress {
    Activity activity;
    Dialog dialog;
    TextView tv_name;


    public simple_progress(Activity myActivity) {
        this.activity = myActivity;

    }

    void startLoadDialog(String title) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog);
        tv_name = dialog.findViewById(R.id.textView8);
        tv_name.setText(title);
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();


    }

    public void dissmissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
