package me.hch;


import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.codehaus.xfire.client.Client;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Administrator on 14-4-13.
 */
public class HisClient {
    public static void main(String[] args) {
        HisClient hc = new HisClient();

        Set<String> names = WsdlUrlConf.getNames();
        for (String hospitalId : names) {

            System.out.println(hospitalId + "---------");
            hc.testDocument(hospitalId);

        }
    }


    public static void testDocument2(String code) {
        try {
            String url = WsdlUrlConf.getString(code);
            String xml = constructXml("confidential/basic-info-req.xml", code);


            Client c = new Client(new URL(url));
            Object[] obj = new Object[]{xml};
            Object[] results = c.invoke("getDepartInfo", obj);

            FileUtils.dump(code + ".xfire.getDepartInfo.xml", results[0].toString());

        } catch (Exception e) {
            System.out.println(code);
        }
    }


    public static void testDocument(String code) {
        try {
            String url = WsdlUrlConf.getString(code);

            Options options = new Options();
            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(url);
            options.setTo(targetEPR);
            // options.setAction("urn:getPrice");

            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);


            OMFactory fac = OMAbstractFactory.getOMFactory();
            String tns = "http://ws.apache.org/axis2";
            // 命名空间，有时命名空间不增加没事，不过最好加上，因为有时有事，你懂的
            OMNamespace omNs = fac.createOMNamespace(tns, "");

            OMElement method = fac.createOMElement("getDepartInfo", omNs);
//            OMElement symbol = fac.createOMElement("symbol", omNs);
//            // symbol.setText("1");
//            symbol.addChild(fac.createOMText(symbol, "Axis2 Echo String "));
//            method.addChild(symbol);

            String xml = constructXml("confidential/basic-info-req.xml", code);
            OMElement symbol = fac.createOMElement("symbol", null);
            symbol.setText(xml);
            method.addChild(symbol);
            method.build();

            OMElement result = sender.sendReceive(method);

            FileUtils.dump(code + ".getDepartInfo.xml", result.toString());

//            System.out.println(result);

        } catch (Exception e) {
            System.out.println(code);
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

}
