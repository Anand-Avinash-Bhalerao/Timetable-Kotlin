package com.sih.timetable.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.sih.timetable.R;
import com.sih.timetable.activities.AdminActivity;

public class AdminLoginDialog extends AppCompatDialogFragment {
    private Dialog dialog;
    private Context context;

    private TextInputEditText password;
    private ImageView close;
    private Button login;
    public AdminLoginDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(context);
        int drawable = R.drawable.background_rounded;
        int nightModeFlags =
                context.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                drawable = R.drawable.background_rounded_night;
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                drawable = R.drawable.background_rounded;
                break;
        }
        dialog.setContentView(R.layout.dialog_admin_login);
        init();
        closeClicked();
        loginClicked();
        dialog.getWindow().setBackgroundDrawable(context.getDrawable(drawable));
        dialog.show();
        return dialog;
    }
    private void loginClicked(){
        login.setOnClickListener(view -> {
            String pw = password.getText().toString();
            if(TextUtils.isEmpty(pw)){
                password.setError("Password is Empty!");
                password.requestFocus();
            }else{
                if(pw.equals("69")){
                    startActivity(new Intent(context, AdminActivity.class));
                    dialog.dismiss();
                }
            }
        });
    }
    private void closeClicked(){
        close.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }
    private void init(){
        password = dialog.findViewById(R.id.et_password);
        close = dialog.findViewById(R.id.iv_close);
        login = dialog.findViewById(R.id.btn_login);
    }
}
