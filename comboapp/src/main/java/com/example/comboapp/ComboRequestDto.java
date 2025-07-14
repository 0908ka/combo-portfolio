package com.example.comboapp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComboRequestDto {
	
	@NotBlank(message = "キャラ名は必須です")
	@Size(max = 20,message = "キャラ名は20字以内で入力してください")
	private String character;
	
	@NotBlank(message = "コンボ内容は必須です")
	@Size(max = 50, message = "コンボ内容は50字以内で入力してください")
	private String combo;

	public ComboRequestDto() {
    }
	
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
	
	

}
