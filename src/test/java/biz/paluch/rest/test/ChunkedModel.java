package biz.paluch.rest.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChunkedModel {

	 	private int id;

	    public ChunkedModel() {
	        super();
	    }

	    public ChunkedModel(int id) {
	        super();
	        this.id = id;
	    }

	    @XmlElement 
	    public int getId() {
	        return id;
	    }
	    
	    public void setId(int id) {
	    	this.id = id;
	    }
}
