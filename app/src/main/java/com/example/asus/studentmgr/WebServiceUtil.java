package com.example.asus.studentmgr;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
/**
 * Created by sun on 2019/5/27.
 */

public class WebServiceUtil{
    public static final String SERVICE_URL="http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";
    public static final String SERVICE_NAMESPACE="http://WebXml.com.cn/";
    public static SoapObject getWeatherByCity(){
        HttpTransportSE httpTransportSE=new HttpTransportSE(SERVICE_URL);
        httpTransportSE.debug=true;
        SoapSerializationEnvelope mEnvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        String method="getWeather";
        SoapObject soapObject=new SoapObject(SERVICE_NAMESPACE,method);
        soapObject.addProperty("theCityCode","镇江");
        mEnvelope.dotNet=true;
        mEnvelope.bodyOut=soapObject;
        /*ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
        headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));*/
        try {
            httpTransportSE.call("http://WebXml.com.cn/getWeather",mEnvelope);
            if(mEnvelope.getResponse()!=null){
                SoapObject result=(SoapObject)mEnvelope.bodyIn;
                SoapObject detail=(SoapObject)result.getProperty(method+"Result");
                return detail;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
