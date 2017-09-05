package org.dallaybatta.gobblin.api;

import java.util.List;

public interface DataPartitionMetaData {

	public boolean exists() ;

	public List<DataPartitionInfo> getDataPartionInfo() ;

	public DataReader getChunkReader() ;
	public void setChunkReader(DataReader reader);

	public void setDataChunkUrl(String url) ;
	public String getDataChunkUrl() ;
	
}
