package pl.edu.agh.kis.soa;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public final class StudentBuilder {
    private String studentId;
    private String firstName;
    private String lastName;
    private List<String> subjects;
    private byte[] avatar;

    private StudentBuilder() {
    }

    public static StudentBuilder aStudent() {
        return new StudentBuilder();
    }

    public StudentBuilder withStudentId(String studentId) {
        this.studentId = studentId;
        return this;
    }

    public StudentBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentBuilder withSubjects(List<String> subjects) {
        this.subjects = subjects;
        return this;
    }

    public StudentBuilder withAvatar(String path) {
        if (!path.equals("")) {
            byte[] Photo;
            try {
                File f = new File(path);
                InputStream objFileIn = new FileInputStream(f);
                int length = (int) f.length();

                Photo = new byte[length];
                objFileIn.read(Photo);

                objFileIn.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            this.avatar = Photo;
        }
        return this;
    }

    public Student build() {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setSubjects(subjects);
        student.setAvatar(avatar);
        return student;
    }
}
