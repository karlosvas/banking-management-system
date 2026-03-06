package com.bytes.ms_accounts.dtos;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Paginated response for transaction history")
public record TransactionHistoryResponseDTO(
    @Schema(description = "List of transactions in current page")
    List<TransactionHistoryItemDTO> content,

    @Schema(description = "Current page number (0-indexed)")
    int page,

    @Schema(description = "Page size")
    int size,

    @Schema(description = "Total number of transactions matching filters")
    long totalElements,

    @Schema(description = "Total number of pages")
    int totalPages
) {}
