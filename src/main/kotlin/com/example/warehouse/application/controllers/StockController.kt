package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.stock.InboundStockRequest
import com.example.warehouse.application.dto.stock.OutboundStockRequest
import com.example.warehouse.application.dto.stock.StockBalanceDto
import com.example.warehouse.application.dto.stock.TransferStockRequest
import com.example.warehouse.application.usecases.stock.GetStockBalance
import com.example.warehouse.application.usecases.stock.InboundStock
import com.example.warehouse.application.usecases.stock.OutboundStock
import com.example.warehouse.application.usecases.stock.TransferStock
import com.example.warehouse.domain.entities.user.UserEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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
    fun inbound(@RequestBody request: InboundStockRequest, @AuthenticationPrincipal userDetails: UserEntity) {
        inboundUseCase.execute(request, userDetails)
    }

    @PostMapping("/outbound")
    fun outbound(@RequestBody request: OutboundStockRequest, @AuthenticationPrincipal userDetails: UserEntity) {
        outboundUseCase.execute(request, userDetails)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferStockRequest, @AuthenticationPrincipal userDetails: UserEntity) {
        transferUseCase.execute(request, userDetails)
    }
}
