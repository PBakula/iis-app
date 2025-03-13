package hr.algebra.iisProject.service;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

@Service
public class XmlRpcServiceImpl implements XmlRpcService {

    private static final String XML_RPC_URL = "http://localhost:8085";


    public String getTemperature(String cityName) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(XML_RPC_URL));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        return (String) client.execute("WeatherService.getTemperature", new Vector<>(List.of(cityName)));
    }


    public List<String> getMatchingCities(String cityName) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(XML_RPC_URL));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Object[] response = (Object[]) client.execute("WeatherService.getMatchingCities", new Vector<>(List.of(cityName)));
        return Stream.of(response).map(Object::toString).toList();
    }
}
