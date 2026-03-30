<template>
  <div class="buy-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <h2>确认订单</h2>
      <div class="order-items">
        <div v-for="item in orderItems" :key="item.id" class="order-item">
          <img :src="`/img/productSingle/${item.product.firstProductImage.id}.jpg`" />
          <div class="item-info">
            <div class="item-name">{{ item.product.name }}</div>
            <div class="item-price">¥{{ item.product.promotePrice }} x {{ item.number }}</div>
          </div>
          <div class="item-subtotal">¥{{ (item.product.promotePrice * item.number).toFixed(2) }}</div>
        </div>
      </div>
      <div class="order-footer">
        <div class="total">总计: ¥{{ total.toFixed(2) }}</div>
        <button @click="submitOrder">提交订单</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const router = useRouter()

const loading = ref(true)
const error = ref(null)
const orderItems = ref([])
const total = ref(0)

onMounted(async () => {
  try {
    // In a real app, you'd get oiid from query params
    // For simplicity, we'll just load cart data
    const res = await foreApi.getCart()
    if (res.data.success) {
      orderItems.value = res.data.data
      total.value = orderItems.value.reduce((sum, item) => {
        return sum + item.product.promotePrice * item.number
      }, 0)
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})

const submitOrder = async () => {
  try {
    const res = await foreApi.createOrder({})
    if (res.data.success) {
      router.push(`/alipay/${res.data.data.oid}`)
    }
  } catch (e) {
    alert('提交订单失败')
  }
}
</script>

<style scoped>
.buy-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 50px;
}

.order-items {
  border: 1px solid #ddd;
  margin-top: 20px;
}

.order-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #ddd;
}

.order-item:last-child {
  border-bottom: none;
}

.order-item img {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.item-info {
  flex: 1;
  margin-left: 15px;
}

.item-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 400px;
}

.item-subtotal {
  font-weight: bold;
  color: #ff5000;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding: 20px;
  background: #f5f5f5;
}

.total {
  font-size: 20px;
  font-weight: bold;
  color: #ff5000;
}

button {
  padding: 10px 40px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
}
</style>
