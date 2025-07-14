package com.example.comboapp;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/combos")
public class ComboRestController {
	
	private final ComboRepository comboRepository;
	
	public ComboRestController(ComboRepository comboRepository){
		this.comboRepository = comboRepository;
	}
	
	@Operation(summary = "全件取得", description = "登録されている全てのコンボをJSON形式で返します。")
	@GetMapping
	public List<ComboResponseDto> getAllCombos(){
		
		return comboRepository.findAll()
				.stream()
				.map(this::convertToResponseDto)
				.toList();
	}
	
	@Operation(summary = "ID指定でデータ取得", description = "指定したIDのコンボ情報を取得します。存在しない場合は404を返します。")
	@GetMapping("/{id}")
	public ResponseEntity<ComboResponseDto> getComboById(@Parameter(description = "取得対象のコンボID") @PathVariable Long id) {
		return comboRepository.findById(id)
				.map(combo -> ResponseEntity.ok(convertToResponseDto(combo)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "新規登録", description = "キャラ名とコンボ内容をJSON形式で受け取りDBに新規登録します。")
	@PostMapping
	public ResponseEntity<ComboResponseDto> createCombo(@Parameter(description = "受け取ったキャラ名とコンボ内容") @Valid @RequestBody ComboRequestDto requestDto) {
		Combo combo = convertToEntity(requestDto);
		Combo savedCombo = comboRepository.save(combo);
		return ResponseEntity.ok(convertToResponseDto(savedCombo));
	}
	
	@Operation(summary = "ID指定でデータ更新", description = "指定したIDのコンボ情報を更新します。存在しない場合は404を返します。")
	@PutMapping("/{id}")
	public ResponseEntity<ComboResponseDto> updateCOmbo(@Parameter(description = "取得対象のコンボID") @PathVariable Long id, @Parameter(description = "取得受け取ったキャラ名とコンボ内容のコンボID") @Valid @RequestBody ComboRequestDto requestDto) {
		
		return comboRepository.findById(id)
				.map(existingCombo -> {
					existingCombo.setCharacter(requestDto.getCharacter());
					existingCombo.setCombo(requestDto.getCombo());
					Combo updateCombo = comboRepository.saveAndFlush(existingCombo);
					return ResponseEntity.ok(convertToResponseDto(updateCombo));
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Operation(summary = "ID指定でデータ削除", description = "指定したIDのコンボ情報を削除します。存在しない場合は404を返します。")
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteCombo(@Parameter(description = "取得対象のコンボID") @PathVariable Long id){
		return comboRepository.findById(id)
				.map(combo -> {
					comboRepository.delete(combo);
					return ResponseEntity.noContent().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	private ComboResponseDto convertToResponseDto(Combo combo) {
		
		ComboResponseDto dto = new ComboResponseDto();
		dto.setId(combo.getId());
		dto.setCharacter(combo.getCharacter());
		dto.setCombo(combo.getCombo());
		
		return dto;
	}
	
	private Combo convertToEntity(ComboRequestDto dto) {
		Combo combo = new Combo();
		combo.setCharacter(dto.getCharacter());
		combo.setCombo(dto.getCombo());
		return combo;
	}

}
