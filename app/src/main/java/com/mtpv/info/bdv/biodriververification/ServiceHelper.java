package com.mtpv.info.bdv.biodriververification;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by CNU on 1/17/2018.
 */

public class ServiceHelper {

    public static String NAMESPACE = "http://www.echallan.info";

    private static String METHOD_NAME2 = "getNewUser";
    private static String METHOD_NAME1 = "getExitUser";
    private static String METHOD_NAME3 = "getDriverDetails";
    private static String METHOD_NAME4 = "getRTADetailsByLicenceNo";
    private static String METHOD_NAME5 = "getAADHARData";
    private static String METHOD_NAME6 = "sendOTP";
    private static String METHOD_NAME7 = "verifyOTP";
    private static String METHOD_NAME8 = "verifyDl";
    private static String METHOD_NAME9 = "verifyPerDl";
    private static String METHOD_NAME10 = "getDocumentValue";
    private static String METHOD_NAME11 = "getRenewalDriverDetails";
    private static String METHOD_NAME12 = "getUpdateRenewalDriverDetails";
    //public static String SOAP_ACTION1 = NAMESPACE + METHOD_NAME2 ;
    public static String SOAP_ACTION2 = NAMESPACE + METHOD_NAME1 ;

    public static  String[] login_master ,final_data_master  , Fps_data_master , renewal_data_master;

    public static String new_regn_data  , new_ExitUser_data  ,new_docment_verif_data ,licence_data ,aadhar_data ,otp_data ,verifyOTPdata ,finalverifydata ,final_fpsdata ,new_FPS_verif_data
                , fpsdataforServices  , renwaldataforServices ;

    public static void Login(String glno,String imei,String password) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME1);
            request.addProperty("glno", glno);
            request.addProperty("imei", imei);
            request.addProperty("password", password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(SOAP_ACTION2, envelope);
            Object result = envelope.getResponse();
            try {
                if (null != result) {
                    new_ExitUser_data = result.toString();
                    login_master = new_ExitUser_data.split("@");
                } else {
                    new_ExitUser_data = "";
                }
            } catch (Exception e) {
                new_ExitUser_data = "";
            }
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
            new_ExitUser_data = "";
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            new_ExitUser_data = "";
        } catch (IOException e) {
            e.printStackTrace();
            new_ExitUser_data = "";
        }
    }


    public static void Regn_ForLogin(String unitcode,String psname,String name,String glno,  String contactNo,String imei,String password) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME2);
            request.addProperty("unitcode", unitcode);
            request.addProperty("psname", psname);
            request.addProperty("name", name);
            request.addProperty("glno", glno);
            request.addProperty("contactNo", contactNo);
            request.addProperty("imei", imei);
            request.addProperty("password", password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getNewUser", envelope);
            Object result = envelope.getResponse();

            try {
                if (null != result){
                    new_regn_data= result.toString();
                }else {
                    new_regn_data="";
                }
            }catch (Exception e){
              e.printStackTrace();
            }
           // new_regn_data = result.toString();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Document_verif(String dlno, String applicationId) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME3);
            request.addProperty("dlno", dlno);
            request.addProperty("applicationId", applicationId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getDriverDetails", envelope);
            Object result = envelope.getResponse();

            try {
                if (null!=result){
                    new_docment_verif_data = result.toString();
                }else {
                    new_docment_verif_data="";
                }
            }catch (Exception e){
                e.printStackTrace();

            }
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {  //ArrayIndexOutOfBoundsException
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    public static void Licence_Document_verif(String dlno) {


        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME4);
            request.addProperty("dlno", dlno);

            Log.i("requestlic_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getRTADetailsByLicenceNo", envelope);
            Object result = envelope.getResponse();
            licence_data = result.toString();
            Log.i("new_DOCVERforLICdata-->",""+licence_data);

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void aadhra_Document_verif(String uid, String eid) {


        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME5);
            request.addProperty("uid", uid);
            request.addProperty("eid", eid);
            Log.i("requestlic_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getAADHARData", envelope);
            Object result = envelope.getResponse();
            aadhar_data = result.toString();
            Log.i("new_AADHRAdata-->",""+aadhar_data);

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //public String sendOTP(String dlno,String mobileNo,String date);

    public static void sendOTP(String dlno,String mobileNo,String date) {


        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME6);
            request.addProperty("dlno", dlno);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("date", date);
            Log.i("requestOTP_value-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"sendOTP", envelope);
            Object result = envelope.getResponse();
            otp_data = result.toString();
            Log.i("OTP_value-->",""+otp_data);

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //   public String verifyOTP(String dlno,String mobileNo,String date, String otp,String verify_status);

    public static void verifyOTP(String dlno,String mobileNo,String date, String otp,String verify_status) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME7);
            request.addProperty("dlno", dlno);
            request.addProperty("mobileNo", mobileNo);
            request.addProperty("date", date);
            request.addProperty("otp", otp);
            request.addProperty("verify_status", verify_status);
            Log.i("requestlic_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"verifyOTP", envelope);
            Object result = envelope.getResponse();
            verifyOTPdata = result.toString();
            Log.i("verifyOTP value-->",""+verifyOTPdata);
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void verifyDl(String dlno,String applicationId,String verifyDocByPid,String verifyDocStatus,String verifyDocDt,
                                String verifyPerByPid,String verifyPerStatus,String verifyPerDt) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME8);
            request.addProperty("dlno", dlno);
            request.addProperty("applicationId", applicationId);
            request.addProperty("verifyDocByPid", verifyDocByPid);
            request.addProperty("verifyDocStatus", verifyDocStatus);
            request.addProperty("verifyDocDt", verifyDocDt);
            request.addProperty("verifyPerByPid", verifyPerByPid);
            request.addProperty("verifyPerStatus", verifyPerStatus);
            request.addProperty("verifyPerDt", verifyPerDt);
            Log.i("requestlic_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"verifyDl", envelope);
            Object result = envelope.getResponse();
            finalverifydata = result.toString();
            Log.i("finalverifydata-->",""+finalverifydata);
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }
//final
    public static void final_verifyPerDl(String dlno, String applicationId,
                                         String verifyPerByPid, String verifyPerStatus, String verifyPerDt,
                                         String fingerprintdata, String biometricChoices, String fingerType) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME9);
            request.addProperty("dlno", dlno);
            request.addProperty("applicationId", applicationId);
            request.addProperty("verifyPerByPid", verifyPerByPid);
            request.addProperty("verifyPerStatus", verifyPerStatus);
            request.addProperty("verifyPerDt", verifyPerDt);
            request.addProperty("fingerprintdata", fingerprintdata);
            request.addProperty("biometricChoices", biometricChoices);
            request.addProperty("fingerType", fingerType);
            Log.i("requestlic_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"final_verifyPerDl", envelope);
            Object result = envelope.getResponse();
            final_fpsdata = result.toString();
            Log.i("finalverifydata-->",""+final_fpsdata);


            final_data_master = final_fpsdata.split("@");
             Log.i("FINAL_VER_FPS_DATA",""+final_data_master[0]);
            Log.i("FINAL_VER_FPS_DATA",""+final_data_master[1]);

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public static void FPS_verification(String dlno, String applicationId) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME10);
            request.addProperty("dlno", dlno);
            request.addProperty("applicationId", applicationId);
            Log.i("requestregn_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getDocumentValue", envelope);
            Object result = envelope.getResponse();
            new_FPS_verif_data = result.toString();
            Log.i("new_DOCVER_data-->",""+new_FPS_verif_data);

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    //for UPDATA FPS DATA to GEt Values for services


    public static void updataFPS(String dlno, String applicationId) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME11);
            request.addProperty("dlno", dlno);
            request.addProperty("applicationId", applicationId);
            Log.i("requestregn_no-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getRenewalDriverDetails", envelope);
            Object result = envelope.getResponse();
            fpsdataforServices = result.toString();
            Log.i("FORServicesSide_data-->",""+fpsdataforServices);

            Fps_data_master = fpsdataforServices.split("!");

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public static void final_renewalverifyPerDl(String dlno,String applicationId,
                                                String verifyPerByPid,String verifyPerByPidName ,String verifyPerStatus,String verifyPerDt) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, "" + METHOD_NAME12);
            request.addProperty("dlno", dlno);
            request.addProperty("applicationId", applicationId);
            request.addProperty("verifyPerByPid", verifyPerByPid);
            request.addProperty("verifyPerByPidName", verifyPerByPidName);
            request.addProperty("verifyPerStatus", verifyPerStatus);
            request.addProperty("verifyPerDt", verifyPerDt);
            Log.i("requestrenawal-->",""+request);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Welcome.URL);
            httpTransportSE.call(NAMESPACE+"getUpdateRenewalDriverDetails", envelope);
            Object result = envelope.getResponse();
            renwaldataforServices = result.toString();
            Log.i("FORServicesRenwal-->",""+renwaldataforServices);

            renewal_data_master = renwaldataforServices.split("@");

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
