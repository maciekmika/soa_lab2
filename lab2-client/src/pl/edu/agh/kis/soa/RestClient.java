package pl.edu.agh.kis.soa;


import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.*;


import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RestClient {

    public static void main(String args[]){

        System.out.println("get student 1");
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/lab2-web/rest/getStudent/1");
        Response response = target.register(new BasicAuthentication("user", "user")).request().get();
        String student1 = response.readEntity(String.class);
        response.close();
        System.out.println(student1+"\n");

        System.out.println("\nget student's 1 avatar");
        target = client.target("http://localhost:8080/lab2-web/rest/getAvatar/1");
        response = target.register(new BasicAuthentication("user", "user")).request().get();
        byte[] avatar = response.readEntity(byte[].class);
        response.close();
        System.out.println(avatar+"\n");

        List<String> subjects = new ArrayList<>();
        subjects.add("SOA");
        subjects.add("Kompilatory");
        subjects.add("Systemy Wbudowane");

        Student student = StudentBuilder.aStudent()
                .withStudentId("3")
                .withAvatar("C:\\Users\\Admin\\java_workspace\\soa\\abc\\avatar.png")
                .withFirstName("Baltazar")
                .withLastName("Gąbka")
                .withSubjects(subjects)
                .build();


        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("C:\\Users\\Admin\\java_workspace\\soa\\abc\\file.json"), student);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(student);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Student 3 added");
        target = client.target("http://localhost:8080/lab2-web/rest/addStudent");
        response = target.register(new BasicAuthentication("user", "user")).request().put(Entity.entity(jsonInString, "application/json"));
        System.out.println(response.getStatus()+" "+response.getStatusInfo());
        response.close();


        System.out.println("\nget students");
        target = client.target("http://localhost:8080/lab2-web/rest/getStudents");
        response = target.register(new BasicAuthentication("user", "user")).request().get();
        String students = response.readEntity(String.class);
        response.close();
        System.out.println(students);


        System.out.println("\nstudent 1 removed");
        target = client.target("http://localhost:8080/lab2-web/rest/deleteStudent/1");
        response = target.register(new BasicAuthentication("user", "user")).request().delete();
        response.close();

        System.out.println("\nget students");
        target = client.target("http://localhost:8080/lab2-web/rest/getStudents");
        response = target.register(new BasicAuthentication("user", "user")).request().get();
        students = response.readEntity(String.class);
        response.close();
        System.out.println(students);

        System.out.println("\nStudent 2 updated");
        target = client.target("http://localhost:8080/lab2-web/rest/updateStudent/2");
        response = target.register(new BasicAuthentication("user", "user")).request().put(Entity.entity(jsonInString, "application/json"));
        System.out.println(response.getStatus()+" "+response.getStatusInfo());
        response.close();

        target = client.target("http://localhost:8080/lab2-web/rest/getStudent/2");
        response = target.register(new BasicAuthentication("user", "user")).request().get();
        student1 = response.readEntity(String.class);
        response.close();
        System.out.println(student1+"\n");

    }

}
