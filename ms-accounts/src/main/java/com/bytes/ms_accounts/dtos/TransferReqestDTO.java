package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for transfer request")
public class TransferReqestDTO {
    @Schema(description = "Source account ID that will be debited")
    private UUID sourceAccountId;
    @Schema(description = "Destination account ID that will be credited")
    private UUID destinationAccountNumber;
    @Schema(description = "Transfer amount", minimum = "1.00", maximum = "10000.00", example = "250.00")
    private BigDecimal amount;
    @Schema(description = "Transfer concept or description", example = "Rent payment")
    private String concept;
    @Schema(description = "Optional scheduled execution date", example = "2026-03-10")
    private String scheduledDate;
}
