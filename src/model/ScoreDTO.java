package model;

import lombok.Data;

@Data
public class ScoreDTO {
	private String id, kind;
	private int speed, acc;
}
