package com.bytes.ms_accounts.dtos;

import java.time.LocalDate;
import com.bytes.ms_accounts.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for transaction history query filters")
public class TransactionHistoryRequestDTO {
    
    @Schema(description = "Filter by transaction type", example = "WITHDRAWAL", allowableValues = {"DEPOSIT", "WITHDRAWAL", "TRANSFER_IN", "TRANSFER_OUT"})
    private TransactionType type;
    
    @Schema(description = "Start date for filtering (ISO 8601 format)", example = "2026-01-01T00:00:00Z")
    private LocalDate fromDate;
    
    @Schema(description = "End date for filtering (ISO 8601 format)", example = "2026-12-31T23:59:59Z")
    private LocalDate toDate;
    
    @Schema(description = "Page number (0-indexed)", minimum = "0", example = "0", defaultValue = "0")
    private Integer page = 0;
    
    @Schema(description = "Page size", minimum = "1", maximum = "100", example = "20", defaultValue = "20")
    private Integer size = 20;
}
