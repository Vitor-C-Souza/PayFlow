package br.me.vitorcsouza.payflow.wallet.controller;

import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreateWalletRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.CreditBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.request.DebitBalanceRequestDTO;
import br.me.vitorcsouza.payflow.wallet.domain.dto.response.WalletResponseDTO;
import br.me.vitorcsouza.payflow.wallet.domain.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponseDTO> createWallet(@RequestBody @Valid CreateWalletRequestDTO request, UriComponentsBuilder uriComponentsBuilder){
        WalletResponseDTO wallet = walletService.createWallet(request);

        URI uri = uriComponentsBuilder
                .path("/api/v1/wallets/{id}")
                .buildAndExpand(wallet.id())
                .toUri();

        return ResponseEntity.created(uri).body(wallet);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponseDTO> getWalletByUserId(@PathVariable UUID userId) {
        WalletResponseDTO wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/{walletId}/credit")
    public ResponseEntity<WalletResponseDTO> credit(@PathVariable UUID walletId, @RequestBody @Valid CreditBalanceRequestDTO request) {
        return  ResponseEntity.ok(walletService.credit(walletId, request));
    }

    @PostMapping("/{walletId}/debit")
    public ResponseEntity<WalletResponseDTO> debit(@PathVariable UUID walletId, @RequestBody @Valid DebitBalanceRequestDTO request) {
        return  ResponseEntity.ok(walletService.debit(walletId, request));
    }
}
