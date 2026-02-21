package br.me.vitorcsouza.payflow.transaction.controller;

import br.me.vitorcsouza.payflow.transaction.domain.dto.request.CreateTransactionRequestDTO;
import br.me.vitorcsouza.payflow.transaction.domain.dto.response.TransactionResponseDTO;
import br.me.vitorcsouza.payflow.transaction.domain.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;



@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody @Valid CreateTransactionRequestDTO request, UriComponentsBuilder uriBuilder) {
        TransactionResponseDTO transactionResponseDTO = transactionService.createTransaction(request);
        var uri = uriBuilder.path("/transactions/{id}").buildAndExpand(transactionResponseDTO.id()).toUri();

        return ResponseEntity.created(uri).body(transactionResponseDTO);
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByWalletId(@PathVariable UUID walletId) {
        return ResponseEntity.ok(transactionService.getTransactionsByWalletId(walletId));
    }
}
