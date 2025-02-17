package scipiofx.test.bean;

import com.github.scipioam.scipiofx.utils.RandomUtils;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnBind;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnComparator;
import com.github.scipioam.scipiofx.view.table.annotations.TableColumnTimeFormat;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 测试实体类：人员
 */
@NoArgsConstructor
public class Person implements Serializable {

    @TableColumnBind(title = "ID", filtered = true, alignment = Pos.CENTER, compared = true, widthPercent = 0.15)
    private final IntegerProperty id = new SimpleIntegerProperty();

    @TableColumnBind(title = "NAME", filtered = true, compared = true, widthPercent = 0.2)
    @TableColumnComparator(value = MFXPersonNameComparator.class, bindField = true)
    private final StringProperty name = new SimpleStringProperty();

//    @TableColumnBind(title = "NICKNAME")
    private final StringProperty nickName = new SimpleStringProperty();

    @TableColumnBind(title = "AGE", compared = true, widthPercent = 0.1)
    private final IntegerProperty age = new SimpleIntegerProperty();

    @TableColumnBind(title = "GENDER", filtered = true, widthPercent = 0.1)
    private final StringProperty gender = new SimpleStringProperty();

    @TableColumnBind(title = "EMAIL", widthPercent = 0.25)
    private final StringProperty email = new SimpleStringProperty();

    @TableColumnTimeFormat(pattern = "yyyy.MM.dd")
    @TableColumnBind(title = "BIRTHDAY", widthPercent = 0.2)
    @Getter @Setter
    private LocalDate birthday;

    public Person(int id, String name, String nickName, int age, String gender, String email, LocalDate birthday) {
        this.id.set(id);
        this.name.setValue(name);
        this.nickName.setValue(nickName);
        this.age.setValue(age);
        this.gender.setValue(gender);
        this.email.setValue(email);
        this.birthday = birthday;
    }

    public static Person build(int id, String name) {
        String nickName = name + "_nick";
        int age = RandomUtils.getInt(18,50);
        String email = name + RandomUtils.getStringWithSpecialChar(2, false) + "@mail.com";
        String gender = RandomUtils.getBoolean() ? "male" : "female";
        int year = RandomUtils.getInt(1980,2001);
        int month = RandomUtils.getInt(1,12);
        int day = RandomUtils.getInt(1,28);
        return new Person(id, name, nickName, age, gender, email, LocalDate.of(year, month, day));
    }

    public static void buildTestData(Collection<Person> tableData, int size) {
        List<String> namePool = new ArrayList<>();
        namePool.add("Alice");
        namePool.add("Bob");
        namePool.add("Charlie");
        namePool.add("David");
        namePool.add("Emma");
        namePool.add("Frank");
        namePool.add("Grace");
        namePool.add("Hannah");
        namePool.add("Isaac");
        namePool.add("Jack");
        namePool.add("Kate");
        namePool.add("Liam");
        namePool.add("Mia");
        namePool.add("Noah");
        namePool.add("Olivia");
        namePool.add("Peter");
        namePool.add("Quinn");
        namePool.add("Rachel");
        namePool.add("Samuel");
        namePool.add("Tom");
        for (int i = 0; i < size; i++) {
            int id = 100001 + i;
            String name;
            if (i < namePool.size()) {
                // 数量在namePool的长度内，直接取
                name = namePool.get(i);
            } else {
                // 超出namePool长度，随机取
                name = RandomUtils.getStringByCustom(namePool);
            }
            tableData.add(Person.build(id, name));
        }
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNickName() {
        return nickName.get();
    }

    public StringProperty nickNameProperty() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName.set(nickName);
    }

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

}
