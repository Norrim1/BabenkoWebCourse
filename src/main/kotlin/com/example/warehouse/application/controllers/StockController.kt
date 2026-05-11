package com.example.warehouse.application.controllers

import com.example.warehouse.application.dto.stock.InboundStockRequest
import com.example.warehouse.application.dto.stock.OutboundStockRequest
import com.example.warehouse.application.dto.stock.StockBalanceDto
import com.example.warehouse.application.dto.stock.TransferStockRequest
import com.example.warehouse.application.usecases.stock.GetStockBalance
import com.example.warehouse.application.usecases.stock.InboundStock
import com.example.warehouse.application.usecases.stock.OutboundStock
import com.example.warehouse.application.usecases.stock.TransferStock
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/balances")
    fun getBalances(@RequestParam warehouseId: Long): List<StockBalanceDto> {
        return getStockBalancesUseCase.execute(warehouseId)
    }
    @PostMapping("/inbound")
    fun inbound(@RequestBody request: InboundStockRequest) {
        inboundUseCase.execute(request)
    }

    @PostMapping("/outbound")
    fun outbound(@RequestBody request: OutboundStockRequest) {
        outboundUseCase.execute(request)
    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferStockRequest) {
        transferUseCase.execute(request)
    }
}
