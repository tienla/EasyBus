package com.hieund.gui.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hieund.R;

/**
 * Created by Minh vuong on 30/06/2015.
 */
public class SendEmailActivity extends Activity {

    private EditText diemDauEditText;
    private EditText diemCuoiEditText;
    private EditText thoiGianEditText;
    private EditText tanSuatEditText;
    private EditText chiPhiEditText;
    private EditText luotDiEditText;
    private EditText luotVeEditText;
    private EditText ghiChuEditText;
    private TextView tenTextView;
    private Button sendBtn;
    private Button cancelBtn;

    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i("aaaaaa", "fad");
        setContentView(R.layout.activity_send_email);

        diemDauEditText = (EditText) findViewById(R.id.diem_dau);
        diemCuoiEditText = (EditText) findViewById(R.id.diem_cuoi);
        thoiGianEditText = (EditText) findViewById(R.id.thoi_gian);
        tanSuatEditText = (EditText) findViewById(R.id.tan_suat);
        chiPhiEditText = (EditText) findViewById(R.id.chi_phi);
        luotDiEditText = (EditText) findViewById(R.id.luot_di);
        luotVeEditText = (EditText) findViewById(R.id.luot_ve);
        ghiChuEditText = (EditText) findViewById(R.id.ghi_chu);
        tenTextView = (TextView) findViewById(R.id.ten);

        tenTextView.setText("Tuyến số " + getIntent().getStringExtra("code"));
        diemDauEditText.setText(getIntent().getStringExtra("name").split(" - ")[0]);
        diemCuoiEditText.setText(getIntent().getStringExtra("name").split(" - ")[1]);
        tanSuatEditText.setText(getIntent().getStringExtra("frequency"));
        thoiGianEditText.setText(getIntent().getStringExtra("operationTime"));
        chiPhiEditText.setText(getIntent().getStringExtra("cost"));
        luotDiEditText.setText(getIntent().getStringExtra("go"));
        luotVeEditText.setText(getIntent().getStringExtra("re"));

        sendBtn = (Button) findViewById(R.id.btn_send);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sender sender = new Sender();
                sender.execute();

            }
        });
    }

    private class Sender extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Mail mail = new Mail();
            String body = "";
            Boolean result = null;

            if(diemDauEditText.getText().toString().equals("") ||
                    diemCuoiEditText.getText().toString().equals("") ||
                    thoiGianEditText.getText().toString().equals("") ||
                    tanSuatEditText.getText().toString().equals("") ||
                    chiPhiEditText.getText().toString().equals("") ||
                    luotDiEditText.getText().toString().equals("") ||
                    luotVeEditText.getText().toString().equals(""))
                return "co o de trong";

            body = tenTextView.getText().toString() + "\n\n";
            if(!diemDauEditText.getText().toString().equals(getIntent().getStringExtra("name").split(" - ")[0]))
                body = body + "Điểm đầu: " + diemDauEditText.getText() + "\n\n";
            if(!diemCuoiEditText.getText().toString().equals(getIntent().getStringExtra("name").split(" - ")[1]))
                body = body + "Điểm cuối: " + diemCuoiEditText.getText() + "\n\n";
            if(!thoiGianEditText.getText().toString().equals(getIntent().getStringExtra("operationTime")))
                body = body + "Thời gian hoạt động: " + thoiGianEditText.getText() + "\n\n";
            if(!tanSuatEditText.getText().toString().equals(getIntent().getStringExtra("frequency")))
                body = body + "Tần suất: " + tanSuatEditText.getText() + "\n\n";
            if(!chiPhiEditText.getText().toString().equals(getIntent().getStringExtra("cost")))
                body = body + "Chi phí: " + chiPhiEditText.getText() + "\n\n";
            if(!luotDiEditText.getText().toString().equals(getIntent().getStringExtra("go")))
                body = body + "Lượt đi: " + luotDiEditText.getText() + "\n\n";
            if(!luotVeEditText.getText().toString().equals(getIntent().getStringExtra("re")))
                body = body + "Lượt về: " + luotVeEditText.getText() + "\n\n";
            if(!ghiChuEditText.getText().toString().equals(""))
                body = body + "Ghi chú: " + ghiChuEditText.getText() + "\n\n";

            Log.i("aaaa",body);

            if(body.equals(tenTextView.getText() + "\n\n"))
                return "chua thay doi";

            mail.setBody(body);

            try {
                result = mail.send();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(result==null) return "error";
            else if(result==true) return "success";
            else return "fail";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog = new Dialog(SendEmailActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.send_email_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

            if(s.equals("error")) {
                ((TextView) dialog.findViewById(R.id.dialog_title)).setText("ERROR");
                ((TextView) dialog.findViewById(R.id.dialog_textView)).setText("Không thể gửi phản hồi. Bạn vui lòng kiểm tra lại kết nối mạng.");
                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            else if(s.equals("fail")) {
                ((TextView) dialog.findViewById(R.id.dialog_title)).setText("ERROR");
                ((TextView) dialog.findViewById(R.id.dialog_textView)).setText("Đã có lỗi xảy ra với hệ thống phản hồi. Chúng tôi sẽ cố gắng khắc phục trong thời gian sớm nhất. Xin cảm ơn!");
                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            else  if(s.equals("success")){
                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
            else if(s.equals("co o de trong")){
                ((TextView) dialog.findViewById(R.id.dialog_title)).setText("ERROR");
                ((TextView) dialog.findViewById(R.id.dialog_textView)).setText("Bạn chỉ có thể bỏ trống phần ghi chú.");
                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            else if(s.equals("chua thay doi")){
                ((TextView) dialog.findViewById(R.id.dialog_title)).setText("ERROR");
                ((TextView) dialog.findViewById(R.id.dialog_textView)).setText("Bạn chưa sửa bất kì thông tin nào.");
                dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            dialog.show();
        }
    }
}
