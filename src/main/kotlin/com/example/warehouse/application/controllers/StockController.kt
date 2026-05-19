package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.stock.InboundStockRequest
import com.example.warehouse.application.dto.stock.OutboundStockRequest
import com.example.warehouse.application.dto.stock.StockBalanceDto
import com.example.warehouse.application.dto.stock.TransferStockRequest
import com.example.warehouse.application.services.stock.GetStockBalance
import com.example.warehouse.application.services.stock.InboundStock
import com.example.warehouse.application.services.stock.OutboundStock
import com.example.warehouse.application.services.stock.TransferStock
import com.example.warehouse.domain.entities.user.UserEntity
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock")
class StockController(
    private val getStockBalancesUseCase: GetStockBalance,
    private val inboundUseCase: InboundStock,
    private val outboundUseCase: OutboundStock,
    private val transferUseCase: TransferStock
) {

    @GetMapping("/balances/{id}")
    fun getBalances(@PathVariable id: Long): List<StockBalanceDto> {
        return getStockBalancesUseCase.execute(id)
    }
    @PostMapping("/inbound")
    fun inbound(@Valid @RequestBody request: InboundStockRequest, @AuthenticationPrincipal userDetails: UserEntity) {
        inboundUseCase.execute(request, userDetails)
    }

    @PostMapping("/outbound")
    fun outbound(@Valid @RequestBody request: OutboundStockRequest, @AuthenticationPrincipal userDetails: UserEntity) {
        outboundUseCase.execute(request, userDetails)
    }

    @PostMapping("/transfer")
    fun transfer(@Valid @RequestBody request: TransferStockRequest, @AuthenticationPrincipal userDetails: UserEntity) {
        transferUseCase.execute(request, userDetails)
    }
}
