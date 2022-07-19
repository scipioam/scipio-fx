package com.github.ScipioAM.scipio_fx.test.bean;

import com.github.ScipioAM.scipio_fx.persistence.DBEntity;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnComparator;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnTimeFormat;
import com.github.ScipioAM.scipio_fx.test.util.RandomUtil;
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
 *
 * @since 2022/6/9
 */
@NoArgsConstructor
public class Person implements Serializable, DBEntity {

    @TableColumnBind(title = "ID", filtered = true, alignment = Pos.CENTER, compared = true, widthPercent = 0.2)
    private final IntegerProperty id = new SimpleIntegerProperty();

    @TableColumnBind(title = "NAME", compared = true, widthPercent = 0.3)
    @TableColumnComparator(value = MFXPersonNameComparator.class, bindField = true)
    private final StringProperty name = new SimpleStringProperty();

//    @TableColumnBind(title = "NICKNAME")
    private final StringProperty nickName = new SimpleStringProperty();

    @TableColumnBind(title = "AGE", compared = true, widthPercent = 0.2)
    private final IntegerProperty age = new SimpleIntegerProperty();

    @TableColumnBind(title = "GENDER", widthPercent = 0.1)
    private final StringProperty gender = new SimpleStringProperty();

//    @TableColumnBind(title = "EM")
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
        int age = RandomUtil.getIntWithRange(18,50);
        String email = RandomUtil.getStringWithSpecialChar(8, false) + "@mail.com";
        String gender = RandomUtil.getBoolean() ? "male" : "female";
        int year = RandomUtil.getIntWithRange(1980,2001);
        int month = RandomUtil.getIntWithRange(1,12);
        int day = RandomUtil.getIntWithRange(1,28);
        return new Person(id, name, nickName, age, gender, email, LocalDate.of(year, month, day));
    }

    public static void buildTestData(Collection<Person> tableData) {
        List<String> namePool = new ArrayList<>();
        namePool.add("jack");
        namePool.add("alan");
        namePool.add("anne");
        namePool.add("water");
        namePool.add("jackson");
        namePool.add("sherman");
        namePool.add("paul");
        namePool.add("norman");
        namePool.add("white");
        namePool.add("jane");
        namePool.add("ruby");
        namePool.add("edwin");
        namePool.add("bella");
        namePool.add("aurora");
        for (int i = 0; i < 10; i++) {
            int id = 10000 + i;
            String name = RandomUtil.getStringByCustom(namePool);
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
