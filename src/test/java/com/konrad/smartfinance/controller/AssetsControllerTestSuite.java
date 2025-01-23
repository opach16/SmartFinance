package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AssetDto;
import com.konrad.smartfinance.domain.model.Asset;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.AssetsMapper;
import com.konrad.smartfinance.service.AssetsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringJUnitWebConfig
@WebMvcTest(AssetsController.class)
class AssetsControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetsService assetsService;

    @MockitoBean
    private AssetsMapper assetsMapper;

    @Test
    @WithMockUser
    void shouldReturnEmptyListOnGetAllAssets() throws Exception {
        //given
        when(assetsService.getAllAssets()).thenReturn(Collections.emptyList());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/assets").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        verify(assetsService, times(1)).getAllAssets();
    }

    @Test
    @WithMockUser
    void shouldReturnListOfAssets() throws Exception {
        //given
        Asset asset1 = Asset.builder()
                .id(1L)
                .build();
        Asset asset2 = Asset.builder()
                .id(2L)
                .build();
        AssetDto assetDto1 = AssetDto.builder()
                .id(1L)
                .build();
        AssetDto assetDto2 = AssetDto.builder()
                .id(2L)
                .build();
        List<Asset> assets = List.of(asset1, asset2);
        List<AssetDto> assetDtos = List.of(assetDto1, assetDto2);
        when(assetsService.getAllAssets()).thenReturn(assets);
        when(assetsMapper.mapToAssetDtoList(assets)).thenReturn(assetDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/assets").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)));
        verify(assetsService, times(1)).getAllAssets();
        verify(assetsMapper, times(1)).mapToAssetDtoList(assets);
    }

    @Test
    @WithMockUser
    void shouldGetAllAssetsByUserId() throws Exception {
        //given
        Long userId = 1L;
        Asset asset1 = Asset.builder()
                .id(1L)
                .build();
        Asset asset2 = Asset.builder()
                .id(2L)
                .build();
        AssetDto assetDto1 = AssetDto.builder()
                .id(1L)
                .build();
        AssetDto assetDto2 = AssetDto.builder()
                .id(2L)
                .build();
        List<Asset> assets = List.of(asset1, asset2);
        List<AssetDto> assetDtos = List.of(assetDto1, assetDto2);
        when(assetsService.getAssetsByUserId(userId)).thenReturn(assets);
        when(assetsMapper.mapToAssetDtoList(assets)).thenReturn(assetDtos);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/assets/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
        verify(assetsService, times(1)).getAssetsByUserId(userId);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetAssetsByUserId() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(assetsService).getAssetsByUserId(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/assets/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}