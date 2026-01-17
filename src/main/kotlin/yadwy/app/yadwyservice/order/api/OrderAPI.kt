package yadwy.app.yadwyservice.order.api

interface OrderAPI {
    fun getOrderById(orderId: Long): OrderDto?
    fun getOrdersByAccountId(accountId: Long): List<OrderDto>
    fun getOrdersBySellerId(sellerId: Long): List<OrderDto>
}
