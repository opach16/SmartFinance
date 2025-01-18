package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.AssetDto;
import com.konrad.smartfinance.mapper.AssetsMapper;
import com.konrad.smartfinance.service.AssetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/assets")
public class AssetsController {

    private final AssetsService assetsService;
    private final AssetsMapper assetsMapper;

    @GetMapping
    public ResponseEntity<List<AssetDto>> getAllAssets() {
        return ResponseEntity.ok(assetsMapper.mapToAssetDtoList(assetsService.getAllAssets()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AssetDto>> getAllAssetsByUsername(@PathVariable Long userId) {
        return ResponseEntity.ok(assetsMapper.mapToAssetDtoList(assetsService.getAssetsByUserId(userId)));
    }
}
