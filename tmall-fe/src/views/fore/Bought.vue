<template>
  <div class="bought-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="!isLoggedIn" class="not-login">
      请先 <router-link to="/login">登录</router-link>
    </div>
    <div v-else class="content">
      <h2>我的订单</h2>
      <div v-if="orders.length === 0" class="empty">暂无订单</div>
      <div v-else class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <span class="order-date">{{ formatDate(order.createDate) }}</span>
            <span class="order-code">订单号: {{ order.orderCode }}</span>
            <span class="order-status">{{ statusText(order.status) }}</span>
          </div>
          <div class="order-products">
            <div v-for="oi in order.orderItems" :key="oi.id" class="product-item">
              <img :src="`/img/productSingle/${oi.product.firstProductImage.id}.jpg`" />
              <span class="product-name">{{ oi.product.name }}</span>
              <span class="product-price">¥{{ oi.product.promotePrice }}</span>
              <span class="product-count">x{{ oi.number }}</span>
            </div>
          </div>
          <div class="order-footer">
            <div class="order-total">总计: ¥{{ order.total }}</div>
            <div class="order-actions">
              <button v-if="order.status === 'waitPay'" @click="pay(order)">去支付</button>
              <button v-if="order.status === 'waitDelivery'" disabled>待发货</button>
              <button v-if="order.status === 'waitConfirm'" @click="confirm(order)">确认收货</button>
              <button v-if="order.status === 'waitReview'" @click="review(order)">去评价</button>
              <button v-if="order.status !== 'delete'" @click="deleteOrder(order)" class="delete-btn">删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const router = useRouter()

const loading = ref(true)
const error = ref(null)
const isLoggedIn = ref(false)
const orders = ref([])

onMounted(async () => {
  try {
    const checkRes = await foreApi.checkLogin()
    if (!checkRes.data.success) {
      isLoggedIn.value = false
      return
    }
    isLoggedIn.value = true

    const res = await foreApi.getBought()
    if (res.data.success) {
      orders.value = res.data.data
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString()
}

const statusText = (status) => {
  const map = {
    waitPay: '待支付',
    waitDelivery: '待发货',
    waitConfirm: '待收货',
    waitReview: '待评价',
    finish: '已完成',
    delete: '已删除'
  }
  return map[status] || status
}

const pay = (order) => {
  router.push(`/alipay/${order.id}`)
}

const confirm = async (order) => {
  try {
    await foreApi.confirmPay(order.id)
    order.status = 'waitReview'
  } catch (e) {
    alert('操作失败')
  }
}

const review = (order) => {
  router.push(`/review/${order.id}`)
}

const deleteOrder = async (order) => {
  if (!confirm('确定删除?')) return
  try {
    await foreApi.deleteOrder(order.id)
    order.status = 'delete'
  } catch (e) {
    alert('删除失败')
  }
}
</script>

<style scoped>
.bought-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error, .not-login {
  text-align: center;
  padding: 50px;
}

.empty {
  text-align: center;
  padding: 50px;
  color: #999;
}

.order-item {
  border: 1px solid #ddd;
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  gap: 20px;
  padding: 15px;
  background: #f5f5f5;
}

.order-status {
  color: #ff5000;
}

.order-products {
  padding: 15px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.product-item img {
  width: 60px;
  height: 60px;
  object-fit: cover;
}

.product-name {
  flex: 1;
  margin-left: 15px;
}

.product-price {
  margin-left: 15px;
}

.product-count {
  margin-left: 15px;
  color: #999;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-top: 1px solid #ddd;
}

.order-total {
  font-size: 18px;
  font-weight: bold;
  color: #ff5000;
}

.order-actions button {
  margin-left: 10px;
  padding: 8px 20px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
}

.order-actions button:disabled {
  background: #ccc;
}

.order-actions .delete-btn {
  background: #ff3b3b;
}
</style>
