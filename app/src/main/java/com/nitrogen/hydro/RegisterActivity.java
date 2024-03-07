package com.nitrogen.hydro;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.*;

import com.google.android.material.textfield.TextInputEditText;
import com.nitrogen.hydro.*;
import com.nitrogen.hydro.utils.Utils;

public class RegisterActivity extends AppCompatActivity
{
	TextInputEditText edtUsername , edtPassword1 , edtPassword2 , register_EditText_Name , register_EditText_Expire;
	PublicFunctions pFunctions;
	Constants cons;
	SharedPreferences shared;
    AppCompatTextView Submit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		init();

	}




	public void btnClick(View view)
	{
		int id = view.getId();
		if(id == R.id.submit)
		{
			String Username = edtUsername.getText().toString();
			String Password1 = edtPassword1.getText().toString();
			String Password2 = edtPassword2.getText().toString();
			String Name = register_EditText_Name.getText().toString();
			String Expire = register_EditText_Expire.getText().toString();


			if(Username.equals("") | Password1.equals("") | Password2.equals("") | Name.equals("") | Expire.equals("") )
			{
				pFunctions.showToast("لطفا تمام فیلد هارا کامل کنید");
				return;
			}

			if(!Password1.equals(Password2))
			{
				pFunctions.showToast("پسورد ها یکی نیستند");
				return;
			}

			String str = "" + ( pFunctions.getCurrentTimeMillis() + (Long.parseLong(Expire) * 86400000) );

			new registerRequest(Username,Password1,Name,str).execute();


		} else if ( id == R.id.submit) {

			String Username = edtUsername.getText().toString();
			String Password = edtPassword1.getText().toString();

			if(Username.equals("") | Password.equals("")) {
				pFunctions.showToast("لطفا تمام فیلد هارا کامل کنید");
				return;
			}

			new loginRequest(Username,Password,RegisterActivity.this).execute();



			new deleteuserRequest(Username,Password,RegisterActivity.this).execute();
		}
	}

	private class registerRequest extends AsyncTask<Void,Void,String>
	{
		String Username , Password , Name , Expire;
		Context Context = RegisterActivity.this;

		public registerRequest(String username , String password , String name , String expire )
		{
			this.Username = username;
			this.Password = password;
			this.Name = name;
			this.Expire = expire;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void[] p1)
		{
			HashMap hashmap = new HashMap();
			hashmap.put("username",Username);
			hashmap.put("password",Password);
			hashmap.put("name",Name);
			hashmap.put("expire",Expire);
			return Utils.sendData(cons.HOST_Register ,hashmap);
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result==null){
				pFunctions.showToast("1ثبت نام انجام نشد");
				return;
			}

			if(result.equals("")){
				pFunctions.showToast("2ثبت نام انجام نشد");
				return;
			}

			String str = "eyk"+pFunctions.base64Encode(Username+"-"+Password)+"wrt";
			pFunctions.showToast(result);

			if( result.equals( "شما با موفقیت ثبت نام کردید") )
			{
				if ( shared.edit().putString( "myKey" , str ).commit() )
				{
					Intent i=new Intent(RegisterActivity.this,MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		}
	}

	private class loginRequest extends AsyncTask<Void,Void,String>
	{
		String Username , Password;
		Context Context;

		public loginRequest(String username , String password , Context context)
		{
			Username = username;
			Password = password;
			this.Password = password;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void[] p1)
		{
			HashMap hashmap = new HashMap();
			hashmap.put("username",Username);
			hashmap.put("password",Password);
			return Utils.sendData(cons.HOST_Login ,hashmap);
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result == null)
				return;


			pFunctions.showToast(result);


			if(result.equals("1")){
				pFunctions.showToast("شما قبلا به برنامه وارد شده اید ، امکان ورود مجدد نمیباشد");
			}else{
				String str = "eyk"+pFunctions.base64Encode(Username+"-"+Password)+"wrt";

				if( result.equals("ok") )
				{
					pFunctions.showToast("شما با موفقیت وارد برنامه شدید.");
					if ( shared.edit().putString( "myKey" , str ).commit() )
					{
						Intent i=new Intent(RegisterActivity.this,MainActivity.class);
						startActivity(i);
						finish();
					}
				}
			}
		}
	}


	private class deleteuserRequest extends AsyncTask<Void,Void,String>
	{
		String Username , Password;
		Context Context;

		public deleteuserRequest(String username , String password , Context context)
		{
			Username = username;
			Password = password;
			this.Password = password;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void[] p1)
		{
			HashMap hashmap = new HashMap();
			hashmap.put("username",Username);
			hashmap.put("password",Password);
			return Utils.sendData(cons.HOST_DeleteUser ,hashmap);
		}

		@Override
		protected void onPostExecute(String result)
		{
			pFunctions.showToast( result);
		}
	}









	public void init ()
	{
		edtUsername =findViewById(R.id.in_username);
		edtPassword1 =  findViewById(R.id.in_pass);
		edtPassword2 = findViewById(R.id.in_pass_confirm);
		register_EditText_Name =  findViewById(R.id.in_phone);
		register_EditText_Expire =  findViewById(R.id.in_melli);



		shared = getSharedPreferences("shared",MODE_PRIVATE);
		pFunctions=new PublicFunctions(this);
		cons=new Constants();


		String strEnText = shared.getString("myKey","");

		if(!strEnText.equals("")){

			String[]str=pFunctions.base64Decode(strEnText.substring(3,strEnText.length()-3)).split("-");

			if(str.length ==2 )
			{
				Intent i=new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}else{
				if( !pFunctions.isNetworkConnected() )
					pFunctions.showToast("اینترنت قطع است");
			}

		}






	}
}
