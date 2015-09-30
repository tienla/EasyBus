package com.hieund.gui.Error;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.hieund.R;
import com.hieund.gui.Activity.Mail;
import com.hieund.gui.Activity.MainActivity;

public class Camon extends SherlockFragment{
	
	EditText name,email;
	Button okBtn;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.thank_fragment, container,
				false);
		getActivity().setTitle("Easy Bus");
        
        name = (EditText)rootView.findViewById(R.id.name);
        email = (EditText)rootView.findViewById(R.id.email);
        
        okBtn = (Button)rootView.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (email.getText()!=null){
					Sender sender = new Sender();
					sender.execute();
				}
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
        
        return rootView;
    }

	private class Sender extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Mail mail = new Mail(email.getText().toString());
            String body = "";
            Boolean result = null;
            
            String nameTemp = name.getText()!=null ? name.getText().toString() : email.getText().toString();
            
            body = "Chào bạn "+nameTemp+","
            	+"Mình đã nhận được góp ý của bạn. Mình sẽ cố gắng sửa trong thời gian sắp tới."
            	+"Xin cảm ơn bạn."
            	+"Chúc bạn 1 ngày vui vẻ!";

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
        	
        }
    }
}

