package model;

import java.util.Date;

import lombok.Data;

@Data
public class ScoreDTO {
	private String id, kind;
	private int speed, acc;
	private Date regdate;
}
