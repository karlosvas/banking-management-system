package com.bytes.ms_accounts.services.impl;

import java.util.UUID;
import org.springframework.lang.NonNull;
import com.bytes.ms_accounts.dtos.TransferReqestDTO;
import com.bytes.ms_accounts.dtos.TransferResponseDTO;

public interface TransferService {
    TransferResponseDTO createTransfer(@NonNull TransferReqestDTO transferReqestDTO, @NonNull UUID customerUuid);
}
