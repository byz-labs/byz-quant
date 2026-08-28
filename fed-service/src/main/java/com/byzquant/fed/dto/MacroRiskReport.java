package com.byzquant.fed.dto;

import java.time.LocalDate;

// Kendi bağımsız dosyasına taşındığı için derleyici artık susacak!
public record MacroRiskReport(int score, String advice, String color, LocalDate calculatedAt) {}
