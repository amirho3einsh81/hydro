package com.nitrogen.hydro;
import android.content.*;
import android.widget.*;
import android.net.*;
import android.util.*;
import java.nio.charset.*;

public class PublicFunctions
{
	Context con;
	
	public PublicFunctions (Context c)
	{
		this.con=c;
	}
	
	public void showToast (String strMessage)
	{
		Toast.makeText(con,strMessage,Toast.LENGTH_SHORT).show();
	}
	
	public long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }
	
	public boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null)
            return true;
        else
            return false;
    }
	
	public String base64Decode(String text)
    {
        byte[] data = Base64.decode(text, Base64.DEFAULT);
        try {
            String txt = new String(data, StandardCharsets.UTF_8);
            return txt;
        } catch (Exception e) {
            return "Error in :\n "+e.toString();
        }
    }
	
	public String base64Encode(String text)
    {
        try {
            byte[] data = text.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            return base64;
        } catch (Exception e) {
            return "Error In :\n "+e.toString();
        }
    }
	
	
}
