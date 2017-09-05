package org.dallaybatta.gobblin.impl;

import java.util.List;

import org.dallaybatta.gobblin.api.DataPartitionInfo;
import org.dallaybatta.gobblin.api.DataPartitionMetaData;
import org.dallaybatta.gobblin.api.DataReader;

public class DataChunkSourceMetaData implements DataPartitionMetaData {

	private String chunkUrl;
	private DataReader reader;
	
	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public List<DataPartitionInfo> getDataPartionInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataReader getChunkReader() {
		//reader = new DemoChunkReader(chunkUrl);
		return reader;
	}

	@Override
	public void setDataChunkUrl(String url) {
		// TODO Auto-generated method stub
		chunkUrl = url;
	}

	@Override
	public String getDataChunkUrl() {
		// TODO Auto-generated method stub
		return chunkUrl;
	}

	@Override
	public void setChunkReader(DataReader reader) {
		this.reader = reader;
		
	}

}
