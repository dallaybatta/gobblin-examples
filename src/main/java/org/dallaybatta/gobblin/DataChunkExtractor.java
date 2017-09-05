package org.dallaybatta.gobblin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.ws.rs.core.Response;

import java.util.List;

import org.dallaybatta.Utility;
import org.dallaybatta.gobblin.api.Data;
import org.dallaybatta.gobblin.impl.InMemoryStorage;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gobblin.configuration.WorkUnitState;
import gobblin.source.extractor.DataRecordException;
import gobblin.source.extractor.Extractor;

public class DataChunkExtractor implements Extractor<String, Data> {

	private static final Logger LOG = LoggerFactory.getLogger(DataChunkExtractor.class);

	ExtractorDataReader reader;
	Queue<Data> dataRecords = new LinkedList<>();
	String resourceUrl;
	boolean extractFastData;
	
	public DataChunkExtractor(WorkUnitState state) {
		resourceUrl = state.getProp(Utility.SOURCE_REST_ENDPOINT);
		// Get the reference of the 
		String workUnitRef = state.getProp("work.unit.number");
		LOG.info("workUnitRef "+workUnitRef);
		dataRecords = readData(state);
		renderData(dataRecords);
		this.reader = new ExtractorDataReader();
	}

	private void renderData(Queue<Data> dataRecords) {
		Iterator<Data> iterator = dataRecords.iterator();
		while(iterator.hasNext()) {
			Data data = iterator.next();
			if(!extractFastData)
				LOG.info("Reading Data from Rest  " +resourceUrl+" "+ data.getInfo());
			else
				LOG.info("Reading Data from InMemoryStorage " + data.getInfo());
		}
	}

	private Queue<Data> readData(WorkUnitState state) {
		String extractFast = state.getProp("extract.fast");
		String key = state.getProp("work.unit.number.key");
		// Either pull the data from the Actual Resource which could be introduce the network latency Or pull the data from the 
		// low latency storage which could be based on the memory, will involve local network calls.
		if(!extractFast.equalsIgnoreCase("yes")){
			int WORK_UNIT_COUNT = state.getPropAsInt(Utility.WORK_UNIT_COUNT,Utility.DEFAULT_WORK_UNIT_COUNT);
			int from = Utility.convertToInt(state.getProp("work.unit.number"))*WORK_UNIT_COUNT+1;
			int end =  from+WORK_UNIT_COUNT;
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			Response response = resteasyClient.target(resourceUrl+"/"+state.getProp(Utility.SOURCE_REST_RESOURCE)).
					queryParamNoTemplate("from", from).
					queryParamNoTemplate("end", end).
					request().buildGet().invoke();
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
			 dataRecords.addAll(list);
		}
		else {
			extractFastData = true;
			List<Data> readList = InMemoryStorage.workUnitMap.get(key);
			dataRecords.addAll(readList);
		}
		return dataRecords;
	}


	private class ExtractorDataReader implements Iterator<Data> {

		@Override
		public boolean hasNext() {
			if(!DataChunkExtractor.this.dataRecords.isEmpty()){
				return true;
			}
			return false;
		}

		@Override
		public Data next() {
			if(!DataChunkExtractor.this.dataRecords.isEmpty())
				return DataChunkExtractor.this.dataRecords.poll();
			return null;
		}

		@Override
		public void remove() {
		}
	}


	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSchema() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getExpectedRecordCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getHighWatermark() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Data readRecord(Data reuse) throws DataRecordException, IOException {
		 if (this.reader == null) {
		      return null;
		    }
		    if (this.reader.hasNext()) {
		      return this.reader.next();
		    }
		    return null;	
	}
	
}
