<template>
  <div class="alipay-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <h2>订单支付</h2>
      <div class="order-info">
        <div class="order-code">订单号: {{ order.orderCode }}</div>
        <div class="order-total">应付金额: ¥{{ order.total }}</div>
      </div>
      <div class="qr-code">
        <div class="placeholder">支付二维码</div>
        <div class="mock-qr">模拟支付宝扫码</div>
      </div>
      <button @click="simulatePay" class="pay-btn">模拟支付完成</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const error = ref(null)
const order = ref({})

onMounted(async () => {
  try {
    const res = await foreApi.getAlipayPage(route.params.oid)
    if (res.data.success) {
      order.value = res.data.data
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})

const simulatePay = async () => {
  try {
    await foreApi.payed(route.params.oid)
    router.push('/bought')
  } catch (e) {
    alert('支付失败')
  }
}
</script>

<style scoped>
.alipay-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

.loading, .error {
  padding: 50px;
}

.order-info {
  margin: 30px 0;
}

.order-code {
  color: #666;
}

.order-total {
  font-size: 24px;
  color: #ff5000;
  font-weight: bold;
  margin-top: 10px;
}

.qr-code {
  margin: 30px 0;
}

.placeholder {
  width: 200px;
  height: 200px;
  margin: 0 auto;
  border: 1px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mock-qr {
  margin-top: 10px;
  color: #999;
}

.pay-btn {
  padding: 15px 50px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
}
</style>
