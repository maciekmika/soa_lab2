package pl.edu.agh.kis.soa.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.client.exception.ResteasyClientException;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.Base64;
import pl.edu.agh.kis.soa.resources.model.Student;
import pl.edu.agh.kis.soa.resources.model.StudentBuilder;

/**
 * Klasa wystawiająca interfejs REST.
 * @author teacher
 *
 */

@Path("rest")
@Stateless
public class StudentResource {

	private static final Logger logger = Logger.getLogger("StudentResource");
	private List<Student> students = new ArrayList<>();

	private final String UPLOADED_FILE_PATH = "C:\\Users\\Admin\\java_workspace\\soa\\lab2\\";

	public StudentResource(){
		List<String> subjects = new ArrayList<>();
		subjects.add("SOA");
		subjects.add("Kompilatory");
		subjects.add("Systemy Wbudowane");


		Student student = StudentBuilder.aStudent()
				.withStudentId("1")
				.withAvatar("C:\\Users\\Admin\\java_workspace\\soa\\abc\\avatar.png")
				.withFirstName("Baltazar")
				.withLastName("Gąbka")
				.withSubjects(subjects)
				.build();

		Student student2 = StudentBuilder.aStudent()
				.withStudentId("2")
				.withAvatar("C:\\Users\\Admin\\java_workspace\\soa\\abc\\avatar.png")
				.withFirstName("Tajemniczy Don Pedro")
				.withLastName("Z krainy deszczowców")
				.withSubjects(subjects)
				.build();

		students.add(student);
		students.add(student2);
	}

	@RolesAllowed("other")
	@GET
	@Path("getStudent")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudent(@QueryParam("id") String studentId) {
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(studentId))
				return students.get(i);
		}
		return null;
	}

	@RolesAllowed("other")
	@GET
	@Path("getStudents")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> getStudents() {
		return students;
	}

	@RolesAllowed("other")
	@GET
	@Path("getAvatar")
	@Produces(MediaType.APPLICATION_JSON)
	public byte[] getAvatarById(@QueryParam("id") String id) {
		List<Student> studentsWithId = students.stream().filter(t -> t.getStudentId().equals(id)).collect(Collectors.toList());
		if (studentsWithId.isEmpty())
			throw new IllegalArgumentException("Student with provided id does not exist");
		if (studentsWithId.get(0).getAvatar() == null)
			throw new ResteasyClientException("Student doesn't have any avatar!");
		return studentsWithId.get(0).getAvatar();
	}

	@RolesAllowed("other")
	@PUT
	@Path("addStudent")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addStudent(Student student){
		for(int i=0;i<students.size();i++){
			if(students.get(i).getStudentId().equals(student.getStudentId()))
				students.remove(i);
		}
		students.add(student);

	}

	@RolesAllowed("other")
	@POST
	@Path("addAvatarForStudent")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadAvatar(@QueryParam("id") String studentId, MultipartFormDataInput input){
		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				//convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,null);

				byte [] bytes = IOUtils.toByteArray(inputStream);

				for(int i=0;i<students.size();i++){
					if(students.get(i).getStudentId().equals(studentId))
						students.get(i).setAvatar(bytes);
				}
				//constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;

				writeFile(bytes,fileName);

				System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return Response.status(200)
				.entity("uploadFile is called, Uploaded file name : " + fileName).build();
	}

	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}
