package test.ex.models.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class BuyingCartKey implements Serializable{
private Long studentId;
private Long lessonId;
}
