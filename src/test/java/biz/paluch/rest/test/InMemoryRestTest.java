package biz.paluch.rest.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dallaybatta.Utility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 12.03.15 08:33
 */
public class InMemoryRestTest {

	static int CHUNK_DATA_COUNT = 50;
	
    @Path("myresource")
    public static class MyResource {

        @POST
        @Consumes(MediaType.TEXT_PLAIN)
        @Produces(MediaType.APPLICATION_XML)
        public MyModel createMyModel(int number) {
        	System.out.println("number "+number);
            return new MyModel(number);
        }

    }

    @Path("bigchunkresource")
    public static class ChunkedDataResource {

        @GET
        @Produces({MediaType.APPLICATION_JSON})
        public String getChunkedDataList(
        		@QueryParam("from") int from,
        		@QueryParam("end") int end
        		) {
        	Log.info("from "+from);
        	Log.info("end "+end);
        	end = (end == 0)?CHUNK_DATA_COUNT:end;
        	if(end<from) {
        		throw new RuntimeException("Invalid range "+from+"-"+end);
        	}
        	ArrayList<ChunkedModel> chunkModelList = new ArrayList<ChunkedModel>();
        	for(int i = from;i < end; i++){
        		// Introduce a delay simulating DB/READ etc..
        		Utility.delay(20);
        		chunkModelList.add(new ChunkedModel(i));
        	}
        	Gson gson = new GsonBuilder().create();
        	String json = gson.toJson(chunkModelList);	
        	System.out.println("json"+json);
        	return json;
        }

    }
    
    public static MyResource sut = new MyResource();
    public static ChunkedDataResource chunkResource = new ChunkedDataResource();
    
    public static InMemoryRestServer server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = InMemoryRestServer.create(sut,chunkResource);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.close();
    }

    @Test
    public void postSimpleBody() throws Exception {

        Response response = server.newRequest("/myresource").request().buildPost(Entity.text("42")).invoke();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        MyModel myModel = response.readEntity(MyModel.class);
        System.out.println(myModel.getResult());
        assertEquals(42, myModel.getResult());
    }
    
    @Test
    public void testChunkedDataListRestCall() throws Exception {
        Response response = server.newRequest("/bigchunkresource").queryParamNoTemplate("from", 1).queryParamNoTemplate("end", 100).request().buildGet().invoke();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        System.out.println("--"+response);
        String json = response.readEntity(String.class);
        System.out.println(json);
        JSONArray myjson = new JSONArray(json);
        System.out.println("---"+myjson.length());
        System.out.println("--->>"+myjson.toString());
        for(int i =0; i<myjson.length();i++)
        {
        	JSONObject firstSport = myjson.getJSONObject(i);
        	System.out.println("--->>>>>>>"+firstSport.getInt("id"));
        }
    }
}
