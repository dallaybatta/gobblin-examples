package org.dallaybatta.gobblin.impl;

import java.util.ArrayList;
import java.util.List;

import org.dallaybatta.gobblin.api.Data;

public class PartitionedData {

	int recordCount;
	String startRange;
	String endRange;
	List<Data> pData = new ArrayList<Data>();
	
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getStartRange() {
		return startRange;
	}
	public void setStartRange(String startRange) {
		this.startRange = startRange;
	}
	public String getEndRange() {
		return endRange;
	}
	public void setEndRange(String endRange) {
		this.endRange = endRange;
	}
	public void setPartionedData(List<Data> accumulatedData) {
		pData.addAll(accumulatedData);
		
	}
	
	public List<Data> getPartionedData() {
		return pData;
		
	}
	
	
}
