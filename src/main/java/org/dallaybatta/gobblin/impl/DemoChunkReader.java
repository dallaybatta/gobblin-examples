package org.dallaybatta.gobblin.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response;

import org.dallaybatta.gobblin.DataChunkSource;
import org.dallaybatta.gobblin.api.Data;
import org.dallaybatta.gobblin.api.DataReader;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DemoChunkReader implements DataReader {

	private static final Logger LOG = LoggerFactory.getLogger(DataChunkSource.class);
	
	private String resourceUrl;
	
	public DemoChunkReader() {
		
	}
	
	@Override
	public Iterator<Data> read(Object dataChunkUrl) {
		ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
		LOG.info("Making GET Call "+resourceUrl+"/bigchunkresource");
		Response response = resteasyClient.target(resourceUrl+"/bigchunkresource").request().buildGet().invoke();
        String json = response.readEntity(String.class);
        JSONArray jsonArrayData = new JSONArray(json);
        int size = jsonArrayData.length();
        List<Data> list = new ArrayList<Data>();
        for (int i = 0; i < size; i++) {
        	JSONObject jsonObject = jsonArrayData.getJSONObject(i);
          	Data d = new Data();
           	d.setInfo(jsonObject.getInt("id")+"");
           	list.add(d);
        }
		return list.iterator();
	}

	@Override
	public void setResourceUrl(String url) {
		this.resourceUrl = url;
	}

}
