package hanelsoft.vn.timeattendance.common;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import hanelsoft.vn.timeattendance.model.helper.FileHelper;

/**
 * Created by Link Larkin on 9/1/2015.
 */
public class RequestHelper {
    public static boolean checkLogin(Context context, String userName, String password) {
        try {
            SoapObject so = new SoapObject(ConstCommon.NAMESPACE,
                    ConstCommon.METHOD_NAME_CHECK_LOGIN);
            so.addProperty("Username", userName);
            so.addProperty("Password", password);
            SoapSerializationEnvelope sse = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            new MarshalBase64().register(sse);
            sse.dotNet = true;
            sse.setOutputSoapObject(so);
            HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(context));
            htse.call(ConstCommon.SOAP_ACTION_CHECK_LOGIN, sse);
            SoapPrimitive response = (SoapPrimitive) sse.getResponse();
            String strResponse = response.toString();
            JSONArray jar = new JSONArray(strResponse);
            JSONObject obj = jar.getJSONObject(0);
            String success = obj.getString("Success");
            if (success == "1") {
                return true;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
