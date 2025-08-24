package com.majlishekhidmat.service;

import com.majlishekhidmat.dto.StatsDto;

public interface StatsService {
    StatsDto getStats();
    void updateStats(StatsDto dto);
}
