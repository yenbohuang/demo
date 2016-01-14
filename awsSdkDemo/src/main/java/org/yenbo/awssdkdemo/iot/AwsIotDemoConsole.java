package org.yenbo.awssdkdemo.iot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.awssdkdemo.PropertyReader;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPrincipalPolicyRequest;
import com.amazonaws.services.iot.model.AttachThingPrincipalRequest;
import com.amazonaws.services.iot.model.AttachThingPrincipalResult;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.amazonaws.services.iot.model.CreatePolicyRequest;
import com.amazonaws.services.iot.model.CreatePolicyResult;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import com.amazonaws.services.iot.model.DescribeCertificateRequest;
import com.amazonaws.services.iot.model.DescribeCertificateResult;
import com.amazonaws.services.iot.model.DescribeEndpointRequest;
import com.amazonaws.services.iot.model.DescribeEndpointResult;
import com.amazonaws.services.iot.model.DescribeThingRequest;
import com.amazonaws.services.iot.model.DescribeThingResult;
import com.amazonaws.services.iot.model.GetPolicyRequest;
import com.amazonaws.services.iot.model.GetPolicyResult;
import com.amazonaws.services.iot.model.ListPrincipalPoliciesRequest;
import com.amazonaws.services.iot.model.ListPrincipalPoliciesResult;
import com.amazonaws.services.iot.model.ListThingPrincipalsRequest;
import com.amazonaws.services.iot.model.ListThingPrincipalsResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AwsIotDemoConsole {

	private static final Logger log = LoggerFactory.getLogger(AwsIotDemoConsole.class);
	
	private AWSIotClient client = new AWSIotClient();
	private Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	
	public static void main(String[] args) {
		
		AwsIotDemoConsole console = new AwsIotDemoConsole();
		
		console.readProperties();
		
		console.describeThing();
		console.describeCertificate();
		console.getPolicy();
		console.listPrincipalPolicies();
		console.listThingPrincipals();
		console.describeEndpoint();
	}
	
	public void readProperties() {
		
		log.info("---- readProperties ----");
		
		PropertyReader.getInstance().getParam("iot.thingName");
		PropertyReader.getInstance().getParam("iot.thingArn");
		PropertyReader.getInstance().getParam("iot.certificateArn");
		PropertyReader.getInstance().getParam("iot.certificateId");
		PropertyReader.getInstance().getParam("iot.policy.PubSubToAnyTopic.name");
		PropertyReader.getInstance().getParam("iot.policy.PubSubToAnyTopic.arn");
		PropertyReader.getInstance().getParam("iot.endpointAddress");
	}
	
	public CreateThingResult createThing() {
		
		log.info("---- createThing ----");
		
		CreateThingRequest request = new CreateThingRequest();
		request.setThingName(PropertyReader.getInstance().getParam("iot.thingName"));
		
		CreateThingResult result = client.createThing(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public DescribeThingResult describeThing() {
		
		log.info("---- describeThing ----");
		
		DescribeThingRequest request = new DescribeThingRequest();
		request.setThingName(PropertyReader.getInstance().getParam("iot.thingName"));
		
		DescribeThingResult result = client.describeThing(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public CreateKeysAndCertificateResult createKeysAndCertificate() {
		
		log.info("---- createKeysAndCertificate ----");
		
		CreateKeysAndCertificateRequest request = new CreateKeysAndCertificateRequest();
		request.setSetAsActive(true);
		
		CreateKeysAndCertificateResult result = client.createKeysAndCertificate(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public DescribeCertificateResult describeCertificate() {
		
		log.info("---- describeCertificate ----");
		
		DescribeCertificateRequest request = new DescribeCertificateRequest();
		request.setCertificateId(PropertyReader.getInstance().getParam("iot.certificateId"));
		
		DescribeCertificateResult result = client.describeCertificate(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public CreatePolicyResult createPolicy() {
		
		log.info("---- createPolicy ----");
		
		JsonArray actions = new JsonArray();
		actions.add("iot:*");
		
		JsonArray resources = new JsonArray();
		resources.add("*");
		
		JsonObject stat1 = new JsonObject();
		stat1.addProperty("Effect", "Allow");
		stat1.add("Action", actions);
		stat1.add("Resource", resources);
		
		JsonArray statements = new JsonArray();
		statements.add(stat1);
		
		JsonObject policyDocument = new JsonObject();
		policyDocument.addProperty("Version", "2012-10-17");
		policyDocument.add("Statement", statements);
		
		CreatePolicyRequest request = new CreatePolicyRequest();
		request.setPolicyName(
				PropertyReader.getInstance().getParam("iot.policy.PubSubToAnyTopic.name"));
		request.setPolicyDocument(gson.toJson(policyDocument));
		
		CreatePolicyResult result = client.createPolicy(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public GetPolicyResult getPolicy() {
		
		log.info("---- createPolicy ----");
		
		GetPolicyRequest request = new GetPolicyRequest();
		request.setPolicyName(
				PropertyReader.getInstance().getParam("iot.policy.PubSubToAnyTopic.name"));
		
		GetPolicyResult result = client.getPolicy(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public void attachPrincipalPolicy() {
		
		log.info("---- attachPrincipalPolicy ----");
		
		AttachPrincipalPolicyRequest request = new AttachPrincipalPolicyRequest();
		request.setPolicyName(
				PropertyReader.getInstance().getParam("iot.policy.PubSubToAnyTopic.name"));
		request.setPrincipal(PropertyReader.getInstance().getParam("iot.certificateArn"));
		
		client.attachPrincipalPolicy(request);
	}
	
	public ListPrincipalPoliciesResult listPrincipalPolicies() {
		
		log.info("---- listPrincipalPolicies ----");
		
		ListPrincipalPoliciesRequest request = new ListPrincipalPoliciesRequest();
		request.setPrincipal(PropertyReader.getInstance().getParam("iot.certificateArn"));
		
		ListPrincipalPoliciesResult result = client.listPrincipalPolicies(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public AttachThingPrincipalResult attachThingPrincipal() {
		
		log.info("---- attachThingPrincipal ----");
		
		AttachThingPrincipalRequest request = new AttachThingPrincipalRequest();
		request.setPrincipal(PropertyReader.getInstance().getParam("iot.certificateArn"));
		request.setThingName(PropertyReader.getInstance().getParam("iot.thingName"));
		
		AttachThingPrincipalResult result = client.attachThingPrincipal(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public ListThingPrincipalsResult listThingPrincipals() {
		
		log.info("---- listThingPrincipals ----");
		
		ListThingPrincipalsRequest request = new ListThingPrincipalsRequest();
		request.setThingName(PropertyReader.getInstance().getParam("iot.thingName"));
		
		ListThingPrincipalsResult result = client.listThingPrincipals(request);
		
		log.info(gson.toJson(result));
		return result;
	}
	
	public DescribeEndpointResult describeEndpoint() {
		
		log.info("---- describeEndpoint ----");
		
		DescribeEndpointRequest request = new DescribeEndpointRequest();
		
		DescribeEndpointResult result = client.describeEndpoint(request);
		
		log.info(gson.toJson(result));
		return result;
	}
}
