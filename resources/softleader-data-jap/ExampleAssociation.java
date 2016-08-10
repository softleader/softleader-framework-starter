package {pkg}.example.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;
import tw.com.softleader.data.entity.GenericEntity;

/**
 * 相關文件: https://github.com/softleader/softleader-framework-docs/wiki/Bean-Validation
 * 
 * @author Matt S.Y. Ho
 */
@SuppressWarnings("serial")
@Setter
@Getter
@Entity
@Table(name = "EXAMPLE_ASSOCIATION")
public class ExampleAssociation extends GenericEntity<Long> {

  @JsonBackReference("example_associations")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EXAMPLE_ID")
  private Example example;
}
