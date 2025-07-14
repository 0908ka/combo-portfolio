package com.example.comboapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(ComboRestController.class)
public class ComboRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComboRepository comboRepository;

    @Test
    @DisplayName("全件取得APIが200を返し、JSON配列を返す")
    void testGetAllCombos() throws Exception {
        Combo combo1 = new Combo();
        combo1.setId(1L);
        combo1.setCharacter("リュウ");
        combo1.setCombo("波動拳");

        Combo combo2 = new Combo();
        combo2.setId(2L);
        combo2.setCharacter("ガイル");
        combo2.setCombo("ソニックブーム");

        when(comboRepository.findAll()).thenReturn(List.of(combo1, combo2));

        mockMvc.perform(get("/api/combos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].character").value("リュウ"))
            .andExpect(jsonPath("$[0].combo").value("波動拳"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].character").value("ガイル"))
            .andExpect(jsonPath("$[1].combo").value("ソニックブーム"));
    }
    
    @Test
    @DisplayName("存在するID指定で1ケン取得できる")
    void testGetComboById_returnCombo() throws Exception{
    	
    	Combo combo = new Combo();
    	combo.setId(10L);
    	combo.setCharacter("ケン");
    	combo.setCombo("昇竜烈破");
    	
    	when(comboRepository.findById(10L)).thenReturn(Optional.of(combo));
    	
    	mockMvc.perform(get("/api/combos/10"))
    	.andExpect(status().isOk())
    	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    	.andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.character").value("ケン"))
        .andExpect(jsonPath("$.combo").value("昇竜烈破"));
    }
    
    @Test
    @DisplayName("存在しないID指定のGETリクエストで404が返る")
    void testGetComboByID_notFound_return404() throws Exception {
    	when(comboRepository.findById(999L)).thenReturn(Optional.empty());
    	
    	mockMvc.perform(get("/api/combos/999"))
    	.andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("新規登録APIが200を返し、登録内容を返す")
    void testCreateCombo() throws Exception{
    	
    	String requestJson = """
    			{
    				"character": "ザンギエフ",
    				"combo" : "スクリューパイルドライバー"
    			}
    			""";
    
    	Combo savedCombo = new Combo();
        savedCombo.setId(99L);
        savedCombo.setCharacter("ザンギエフ");
        savedCombo.setCombo("スクリューパイルドライバー");
        
        when(comboRepository.save(any(Combo.class))).thenReturn(savedCombo);
        
        mockMvc.perform(post("/api/combos")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(99))
        .andExpect(jsonPath("$.character").value("ザンギエフ"))
        .andExpect(jsonPath("$.combo").value("スクリューパイルドライバー"));
    
    }
    
    @Test
    @DisplayName("キャラ名が21文字以上の場合、400が返る")
    void testCreateCombo_characterTooLong_return400() throws Exception{
    	
    	String requestJson = """
    			{
    				"character": "あああああああああああああああああああああああああああああああああああああああああああああああああ",
    				"combo": "波動拳"
    			}
    			""";
    	
    	mockMvc.perform(post("/api/combos")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestJson))
    	.andExpect(status().isBadRequest())
    	.andExpectAll(jsonPath("$.errors[?(@.field == 'character')].message")
    			.value("キャラ名は20字以内で入力してください"));
    			
    }
    
    @Test
    @DisplayName("コンボ内容が50文字以上の場合、400が返る")
    void testCreateCombo_comboTooLong_return400() throws Exception{
    	
    	String requestJson = """
    			{
    				"character": "リュウ",
    				"combo": "あああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああ"
    			}
    			""";
    	
    	mockMvc.perform(post("/api/combos")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestJson))
    	.andExpect(status().isBadRequest())
    	.andExpectAll(jsonPath("$.errors[?(@.field == 'combo')].message")
    			.value("コンボ内容は50字以内で入力してください"));
    			
    }
    
    @Test
    @DisplayName("更新APIが200を返し、更新後の内容を返す")
    void testUpdateCombo() throws Exception{
    	
    	String requestJson = """
    			{
    				"character": "春麗",
    				"combo": "百裂脚"
    			}
    			""";
    	
    	Combo existingCombo = new Combo();
    	existingCombo.setId(5L);
    	existingCombo.setCharacter("リュウ");
    	existingCombo.setCombo("波動拳");
    	
    	Combo updatedCombo = new Combo();
    	updatedCombo.setId(5L);
    	updatedCombo.setCharacter("春麗");
    	updatedCombo.setCombo("百裂脚");
    	
    	when(comboRepository.findById(5L)).thenReturn(Optional.of(existingCombo));
    	when(comboRepository.save(any(Combo.class))).thenReturn(updatedCombo);
    	
    	mockMvc.perform(put("/api/combos/5")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestJson))
    	.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.character").value("春麗"))
        .andExpect(jsonPath("$.combo").value("百裂脚"));
    	
    }
    
    @Test
    @DisplayName("存在しないID指定の更新リクエストで404が返る")
    void testUpdateCombo_notFound_return404() throws Exception{
    	
    	String requestJson = """
    			{
    				"character": "春麗",
    				"combo": "百裂脚"
    			}
    			""";
    	
    	when(comboRepository.findById(999L)).thenReturn(Optional.empty());
    	
    	mockMvc.perform(put("/api/combos/999")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestJson))
    		.andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("削除APIが204を返す")
    void testDeleteCombo() throws Exception{
    	
    	Combo existingCombo = new Combo();
    	existingCombo.setId(7L);
    	existingCombo.setCharacter("ダルシム");
    	existingCombo.setCombo("ヨガファイア");
    	
    	when(comboRepository.findById(7L)).thenReturn(Optional.of(existingCombo));
    	
    	mockMvc.perform(delete("/api/combos/7"))
    			.andExpect(status().isNoContent());
    }
    
    
    @Test
    @DisplayName("存在しないID指定の削除リクエストで404が返る")
    void testDeleteCombo_notFound_return404() throws Exception{
    	
    	when(comboRepository.findById(999L)).thenReturn(Optional.empty());
    	
    	mockMvc.perform(delete("/api/combos/999"))
    	.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("character が空文字のとき 400を返す")
    void testCreateCombo_characterIsBlank_return400() throws Exception{
    	
    	String reqestJson = """
    			{
    				"character": "",
    				"combo": "昇竜拳"
    			}
    			""";
    	
    	mockMvc.perform(post("/api/combos")
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(reqestJson))
    		.andExpect(status().isBadRequest())
    		.andExpect(jsonPath("$.errors[?(@.field == 'character')].message").value("キャラ名は必須です"));
    }
    
    @Test
    @DisplayName("combo が空文字のとき 400を返す")
    void testCreateCombo_comboBlank_return400() throws Exception{
    	
    	String reqestJson = """
    			{
    				"character": "リュウ",
    				"combo": ""
    			}
    			""";
    	
    	mockMvc.perform(post("/api/combos")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(reqestJson))
    		.andDo(print())
    		.andExpect(status().isBadRequest())
    		.andExpect(jsonPath("$.errors[?(@.field == 'combo')].message")
    				.value("コンボ内容は必須です"));
    }
    
    @Test
    @DisplayName("バリデーションエラー時に400と整形レスポンスが返る")
    void testValidationErrorResponse() throws Exception{
    	String invalidRequestJson = """
    			{
    				"character": "",
    				"combo": ""
    			}
    			""";
    	
    	mockMvc.perform(post("/api/combos")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(invalidRequestJson))
    		.andExpect(status().isBadRequest())
    		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    		.andExpect(jsonPath("$.status").value(400))
    		.andExpect(jsonPath("$.errors[?(@.field == 'character')].message").value("キャラ名は必須です"))
    		.andExpect(jsonPath("$.errors[?(@.field == 'combo')].message").value("コンボ内容は必須です"));
    }
    
    
    
}    

