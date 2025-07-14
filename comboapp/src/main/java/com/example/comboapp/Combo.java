package com.example.comboapp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Combo {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "キャラクター名は必須です")
	@Column(name = "character_name")
	private String character;
	
	@NotBlank(message = "コンボ内容は必須です")
	private String combo;
	
	public Combo() {}
	
//	public Combo(String character, String combo) {
//		this.character = character;
//		this.combo = combo;
//	}

	public String getCharacter() {
		return character;
	}

	public String getCombo() {
		return combo;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public void setCombo(String combo) {
		this.combo = combo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
}
