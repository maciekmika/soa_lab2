package pl.edu.agh.kis.soa;


import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.*;
import pl.edu.agh.kis.soa.model.Student;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


public class RestClient {

    public static void main(String args[]){

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/lab2-web/rest/getStudent?id=1");
        Response response = target.register(new BasicAuthentication("user", "user")).request().get();
        String student1 = response.readEntity(String.class);
        response.close();

        target = client.target("http://localhost:8080/lab2-web/rest/getStudents");
        response = target.register(new BasicAuthentication("user", "user")).request().get();
        String students = response.readEntity(String.class);

        target = client.target("http://localhost:8080/lab2-web/rest/getAvatar?id=1");
        response = target.register(new BasicAuthentication("user", "user")).request().get();
        byte[] avatar = response.readEntity(byte[].class);

        System.out.println(student1);
        System.out.println("");
        System.out.println(avatar);
        System.out.println("");
        System.out.println(students);
    }

}
