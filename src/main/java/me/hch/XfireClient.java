package me.hch;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.codehaus.xfire.client.Client;

import javax.xml.namespace.QName;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 14-4-12.
 */
public class XfireClient {
    public static void main(String[] args) throws Exception {
//        String hosid = "ZYYY";
//        String dptid = "102";
//        String docid = "";
//        String date = "2014-04-13";
//        String apm = "1";

        String hosid = "ZYYY";
        String dptid = "100";
        String docid = "13302";
        String date = "2014-04-14";
        String apm = "1";

        String url = WsdlUrlConf.getString(hosid);

        String cont = FileUtils.getFileContent("confidential/no-info-req.xml");
        cont = cont.replace("{HOSPITAL-ID}", hosid);
        cont = cont.replace("{DOCTOR-ID}", docid);
        cont = cont.replace("{DEPART-ID}", dptid);
        cont = cont.replace("{DATE}", date);
        cont = cont.replace("{APM}", apm);

        System.out.println(cont);

//        String ret = getXfireService(url, "getDtNoInfo", cont);
//        dump("getDtNoInfo." + hosid + ".xml", ret);

        String ret = getXfireService(url, "getDtNoInfo", cont);
        dump("getDtNoInfo.dt." + hosid + ".xml", ret);
    }

    public static void aaa() {
        Set<String> names = WsdlUrlConf.getNames();
        for (String hospitalId : names) {
            hospitalId = "ZYYY";

            String url = WsdlUrlConf.getString(hospitalId);
            try {
                Map<String, String> opMap = new HashMap<String, String>();
                opMap.put("getDepartInfo", "confidential/basic-info-req.xml");
                opMap.put("getDoctorInfo", "confidential/basic-info-req.xml");
                opMap.put("getDepartWorkInfo", "confidential/dp-work-req.xml");
                opMap.put("getDoctorWorkInfo", "confidential/dp-work-req.xml");

                for (String op : opMap.keySet()) {
                    String fileName = opMap.get(op);
                    String content = constructXml(fileName, hospitalId);
                    String ret = getXfireService(url, op, content);
                    dump(op + "." + hospitalId + ".xml", ret);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(hospitalId);
            }

            break;
        }
    }

    public static String constructXml(String fileName, String hospitalId) throws IOException {
        String content = FileUtils.getFileContent(fileName);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String beginTime = df.format(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, 6);
        String endTime = df.format(cal.getTime());

        content = content.replace("{HOSPITAL-ID}", hospitalId);
        content = content.replace("{BEGIN-TIME}", beginTime);
        content = content.replace("{END-TIME}", endTime);

        return content;
    }

    public static String getXfireService(String url, String operation, String xml) throws Exception {
        Client c = new Client(new URL(url));
        Object[] obj = new Object[]{xml};
        Object[] results = c.invoke(operation, obj);
        return (String) results[0];
    }

    public static String getDepartInfo2(String url, String xml) throws AxisFault {
        RPCServiceClient serviceClient = new RPCServiceClient();
        Options options = serviceClient.getOptions();
        EndpointReference targetEPR = new EndpointReference(url);
        options.setTo(targetEPR);
        //2013-11-11 add
        options.setManageSession(true);
        options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
        //设置连接超时时间
        options.setTimeOutInMilliSeconds(1000 * 60);

        QName qname = new QName("http://ws.apache.org/axis2", "getDepartInfo");

        String ret = (String) serviceClient.invokeBlocking(
                qname,
                new Object[]{xml},
                new Class[]{String.class}
        )[0];
        serviceClient.cleanupTransport();
        return ret;
    }

    public static void dump(String filename, String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(
                new FileWriter(filename)
        );
        bw.write(content);
        bw.flush();
        bw.close();
    }


}
