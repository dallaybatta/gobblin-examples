package org.dallaybatta.gobblin.impl;

import java.util.ArrayList;
import java.util.List;

import org.dallaybatta.gobblin.api.Accumulator;
import org.dallaybatta.gobblin.api.Data;

public class DefaultDataAccumulator implements Accumulator {

	List<Data> accumulatedData = new ArrayList<Data>();
	String startId;
	String endId;
	int count = 0;
	
	@Override
	public void accumulate(Data dataTuple) {
		if(accumulatedData.size()==0){
			startId = dataTuple.getInfo();
		}
		accumulatedData.add(dataTuple);
		endId = dataTuple.getInfo();
	}

	@Override
	public PartitionedData getPartitionedData() {
		PartitionedData pData = new PartitionedData();
		pData.setRecordCount(accumulatedData.size());
		pData.setPartionedData(accumulatedData);
		pData.setStartRange(startId);
		pData.setEndRange(endId);
		return pData;
	}

	@Override
	public void clear() {
		accumulatedData.clear();
	}

	@Override
	public void setStartId(String startId) {
		this.startId = startId;
	}

	@Override
	public void setEndId(String endId) {
		this.endId = endId;
	}

	@Override
	public String getStartId() {
		return startId;
	}

	@Override
	public String getEndId() {
		return endId;
	}
	
}
