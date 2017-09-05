package org.dallaybatta.gobblin.api;

import java.util.Iterator;

public interface DataReader {

	public void setResourceUrl(String url);
	public Iterator<Data> read(Object dataChunkUrl);
	
}
