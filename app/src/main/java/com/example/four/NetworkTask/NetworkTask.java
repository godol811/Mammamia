package com.example.four.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

import com.example.four.Bean.AddressDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "네트워크타스크";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<AddressDto> Address;
    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2020.12.25
    //
    // Description:
    //  - NetworkTask를 검색, 입력, 수정, 삭제 구분없이 하나로 사용키 위해 생성자 변수 추가.
    //
    ///////////////////////////////////////////////////////////////////////////////////////
    String where = null;

    public NetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.Address = new ArrayList<AddressDto>();
        this.where = where;
        Log.v(TAG, "Start : " + mAddr);
    }


    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("");
        progressDialog.setMessage("주소록 불러오는중");
        progressDialog.show();

    }

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        ///////////////////////////////////////////////////////////////////////////////////////
        // Date : 2020.12.25
        //
        // Description:
        //  - NetworkTask에서 입력,수정,검색 결과값 관리.
        //
        ///////////////////////////////////////////////////////////////////////////////////////
        String result = null;
        ///////////////////////////////////////////////////////////////////////////////////////

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String strline = bufferedReader.readLine();
                    if (strline == null) break;
                    stringBuffer.append(strline + "\n");
                }
                ///////////////////////////////////////////////////////////////////////////////////////
                // Date : 2020.12.25
                //
                // Description:
                //  - 검색으로 들어온 Task는 parserSelect()로
                //  - 입력, 수정, 삭제로 들어온 Task는 parserAction()으로 구분
                //
                ///////////////////////////////////////////////////////////////////////////////////////
                if (where.equals("select")) {
                    parserSelect(stringBuffer.toString());
                } else {
                    result = parserAction(stringBuffer.toString());
                }
                ///////////////////////////////////////////////////////////////////////////////////////

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        // Date : 2020.12.25
        //
        // Description:
        //  - 검색으로 들어온 Task는 members를 return
        //  - 입력, 수정, 삭제로 들어온 Task는 result를 return
        //
        ///////////////////////////////////////////////////////////////////////////////////////
        if (where.equals("select")) {
            return Address;
        } else {
            return result;
        }
        ///////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "onPostExecute()");
        super.onPostExecute(o);
        progressDialog.dismiss();

    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled()");
        super.onCancelled();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2020.12.25
    //
    // Description:
    //  - 검색후 json parsing
    //
    ///////////////////////////////////////////////////////////////////////////////////////
    private void parserSelect(String s) {
        Log.v(TAG, "parserSelect()");

        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("addrlist"));
            Address.clear();
            Log.v(TAG, "s" + s);





            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int addrNo = jsonObject1.getInt("addrNo");
                String addrName = jsonObject1.getString("addrName");
                String addrTel = jsonObject1.getString("addrTel");
                String addrAddr = jsonObject1.getString("addrAddr");
                String addrDetail = jsonObject1.getString("addrDetail");
                String addrTag = jsonObject1.getString("addrTag");

                String addrImagePath = jsonObject1.getString("addrImagePath");



                AddressDto address = new AddressDto(addrNo,addrName,addrTel,addrAddr,addrDetail,addrTag,addrImagePath);
                Address.add(address);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2020.12.25
    //
    // Description:
    //  - 입력, 수정, 삭제후 json parsing
    //
    ///////////////////////////////////////////////////////////////////////////////////////
    private String parserAction(String s) {
        Log.v(TAG, "Parser()");
        String returnValue = null;

        try {
            Log.v(TAG, s);

            JSONObject jsonObject = new JSONObject(s);
            returnValue = jsonObject.getString("result");
            Log.v(TAG, returnValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    ///////////////////////////////////////////////////////////////////////////////////////


} // ------
