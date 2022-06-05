package API_Chaining;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEnd_Project {
	Response response;
	String BaseURI = "http://3.80.114.137:8088/employees";

	@Test
	public void TestCase() {
		response = GetMethodAll();
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println(response.getBody().asString());

		response = PostMethod("Mallika","Singh", "10000","mallikasingh@gmail.com");
		Assert.assertEquals(response.getStatusCode(), 201);
		JsonPath jpath = response.jsonPath();
		int Emp_ID = jpath.get("id");
		System.out.println("The new ID created is  " + Emp_ID);
		System.out.println(response.getBody().asString());
		

		response = PutMethod(Emp_ID, "Rohit","Sharma", "15000","rohitsharma@gmail.com");
		Assert.assertEquals(response.getStatusCode(), 200);
		jpath = response.jsonPath();
		Assert.assertEquals(jpath.get("firstName"), "Rohit");
		System.out.println("The first name is updated to Rohit");
		System.out.println("The updated details for Employee ID "+Emp_ID+" is ");
		System.out.println(response.getBody().asString());

		response = DeleteMethod(Emp_ID);
		Assert.assertEquals(response.getStatusCode(), 200);
		System.out.println("Employee ID "+Emp_ID+ " is deleted");
		
		response = GetMethod(Emp_ID);
		Assert.assertEquals(response.getStatusCode(), 400);
		System.out.println("Employee ID "+Emp_ID+" Not Found because it is deleted");
	}

	public Response GetMethodAll() {
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		return response;
	}

	public Response PostMethod(String f_name,String l_name,String salary, String email) {
		RestAssured.baseURI = BaseURI;

		JSONObject jobj = new JSONObject();
		jobj.put("firstName", f_name);
		jobj.put("lastName", l_name);
		jobj.put("salary", salary);
		jobj.put("email", email);

		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.post("");
		return response;
	}

	public Response PutMethod(int Emp_ID, String f_name,String l_name,String salary, String email) {
		RestAssured.baseURI = BaseURI;

		JSONObject jobj = new JSONObject();
		jobj.put("firstName", f_name);
		jobj.put("lastName", l_name);
		jobj.put("salary", salary);
		jobj.put("email", email);
		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.put("/" + Emp_ID);
		return response;
	}

	public Response DeleteMethod(int Emp_ID) {
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete("/" + Emp_ID);
		return response;
	}

	public Response GetMethod(int Emp_ID) {
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/" + Emp_ID);
		return response;
		
	}
}
