package com.bytes.ms_accounts.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.bytes.ms_accounts.clients.CustomerClient;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import com.bytes.ms_accounts.dtos.CustomerResponseDTO;
import com.bytes.ms_accounts.dtos.TransferReqestDTO;
import com.bytes.ms_accounts.dtos.TransferResponseDTO;
import com.bytes.ms_accounts.enums.TransferStatus;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.exceptions.ResourceNotFoundException;
import com.bytes.ms_accounts.mappers.TransferMapper;
import com.bytes.ms_accounts.models.Transfer;
import com.bytes.ms_accounts.repositories.TransferRepository;
import com.bytes.ms_accounts.services.impl.TransferService;
import com.bytes.ms_accounts.services.recorder.TransferRecorderService;

import jakarta.transaction.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

    /**
        * Flat transfer fee used to keep pricing predictable for this service.
        * A fixed percentage is enough for the current scope and can be externalized later.
     */
    public static final BigDecimal FEE_PERCENTAGE = new BigDecimal("0.02");
    private final AccountServiceImpl accountService;
    private final TransferRepository transferRepository;
    private final TransferRecorderService transferRecorderService;
    private final TransferMapper transferMapper;
    private final CustomerClient customerClient;
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ONE;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000");

    public TransferServiceImpl(AccountServiceImpl accountService, TransferRepository transferRepository, TransferRecorderService transferRecorderService, TransferMapper transferMapper, CustomerClient customerClient) {
        this.accountService = accountService;
        this.transferRepository = transferRepository;
        this.transferRecorderService = transferRecorderService;
        this.transferMapper = transferMapper;
        this.customerClient = customerClient;
    }

    /**
    * Creates a transfer after validating customer state, ownership, amount limits, and available funds.
    *
    * The method is transactional to keep account balances and records consistent:
    * either all updates succeed (balances + transfer + transaction records) or all are rolled back.
     * 
     * @param transferReqestDTO the transfer request containing source account, destination, amount, and concept
     * @param customerUuid the authenticated customer performing the transfer
     * @return TransferResponseDTO with transfer details and beneficiary name (if applicable)
     * @throws BusinessException if validation fails or customer is inactive
     * @throws ResourceNotFoundException if source or destination account does not exist
     */
    @Transactional
    public TransferResponseDTO createTransfer(@NonNull TransferReqestDTO transferReqestDTO, @NonNull UUID customerUuid) {
        // Fee is computed upfront so all downstream checks use the same amount.
        BigDecimal fee = FEE_PERCENTAGE.multiply(transferReqestDTO.getAmount());
        Transfer transfer = transferMapper.toEntity(transferReqestDTO, fee);

        // Active-customer validation is delegated to ms-customers.
        customerClient.validateCustomer(customerUuid);

        // Source account must belong to the authenticated customer.
        AccountResponseDTO sourceAccount = accountService.getAccountByMe(transferReqestDTO.getSourceAccountId(), customerUuid);
        
        // Limits protect against invalid transfers and enforce product rules.
        if (transferReqestDTO.getAmount().compareTo(MIN_AMOUNT) < 0 || transferReqestDTO.getAmount().compareTo(MAX_AMOUNT) > 0){
            transferRecorderService.recordFailedTransfer(transfer, "Amount must be between 1 EUR and 10,000 EUR");
            throw new BusinessException("Amount must be between 1 EUR and 10,000 EUR");
        }

        // Destination account only needs to exist; ownership may be different.
        AccountResponseDTO targetAccount = accountService.getAccountByNumber(transferReqestDTO.getDestinationAccountNumber());
        
        // Total debit includes fee because fee is charged to the sender.
        String beneficiaryName = null;
        UUID targetAccountCustomerId = UUID.fromString(targetAccount.customerId());
        BigDecimal totalDebit = transferReqestDTO.getAmount().add(fee);

        // Beneficiary name is needed in receipts for inter-customer transfers.
        if (!targetAccountCustomerId.equals(customerUuid)) {
            CustomerResponseDTO beneficiaryCustomer = customerClient.getCustomerById(targetAccountCustomerId);
            beneficiaryName = beneficiaryCustomer.firstName() + " " + beneficiaryCustomer.lastName();
        }
        
        // Funds check happens before balance updates to avoid partial effects.
        if (sourceAccount.balance().compareTo(totalDebit) < 0){
            transferRecorderService.recordFailedTransfer(transfer, "Insufficient funds in source account");
            throw new BusinessException("Insufficient funds in source account");
        }

        // Receiver gets only the transfer amount; sender pays amount + fee.
        accountService.addMoney(targetAccount.id(), transferReqestDTO.getAmount());
        accountService.subtractMoney(sourceAccount.id(), totalDebit);

        // Mark and persist transfer after financial movement is successful.
        transfer.setStatus(TransferStatus.COMPLETED);
        transfer = transferRepository.save(transfer);

        BigDecimal sourceBalanceAfter = sourceAccount.balance().subtract(totalDebit);
        BigDecimal targetBalanceAfter = targetAccount.balance().add(transferReqestDTO.getAmount());

        // Record both sides for account history and audit traceability.
        transferRecorderService.recordSuccessfulTransferDebit(
            transfer,
            sourceAccount.id(),
            totalDebit,
            sourceBalanceAfter,
            targetAccount.accountNumber(),
            beneficiaryName
        );

        transferRecorderService.recordSuccessfulTransferCredit(
            transfer,
            targetAccount.id(),
            transferReqestDTO.getAmount(),
            targetBalanceAfter,
            sourceAccount.accountNumber()
        );

        // Response includes beneficiary when available.
        return transferMapper.toResponse(transfer, beneficiaryName);
    }

}
