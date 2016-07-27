package {pkg}.example.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import lombok.Getter;
import lombok.Setter;
import tw.com.softleader.commons.validation.constraints.AssertThat;
import tw.com.softleader.commons.validation.constraints.Latin;
import tw.com.softleader.data.entity.GenericCodeEntity;

@SuppressWarnings("serial")
@Setter
@Getter
@Entity
@Table(name = "EXAMPLE")
@AssertThat(
    value = "this.age > 0 && this.birthday != null && forName('java.time.Period').between(this.birthday, forName('java.time.LocalDate').now()).getYears() == this.age",
    propertyNode = "age", message = "{example.birthday.and.age.not.match}")
public class ExampleEntity extends GenericCodeEntity<Long> {

  @Column(name = "age")
  private int age;

  @NotNull
  @Column(name = "birthday")
  private LocalDate birthday;

  @Email
  @Column(name = "email")
  private String email;

  @NotNull
  @Latin
  @Override
  public String getCode() {
    return super.getCode();
  }

}
