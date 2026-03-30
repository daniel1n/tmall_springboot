<template>
  <div class="cart-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="!isLoggedIn" class="not-login">
      请先 <router-link to="/login">登录</router-link>
    </div>
    <div v-else class="content">
      <h2>购物车</h2>
      <div v-if="cartItems.length === 0" class="empty-cart">购物车是空的</div>
      <div v-else class="cart-list">
        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <router-link :to="`/product/${item.product.id}`">
            <img :src="`/img/productSingle/${item.product.firstProductImage.id}.jpg`" />
          </router-link>
          <div class="item-info">
            <div class="item-name">{{ item.product.name }}</div>
            <div class="item-price">¥{{ item.product.promotePrice }}</div>
          </div>
          <div class="item-count">
            <button @click="decrease(item)">-</button>
            <span>{{ item.number }}</span>
            <button @click="increase(item)">+</button>
          </div>
          <div class="item-subtotal">¥{{ (item.product.promotePrice * item.number).toFixed(2) }}</div>
          <button class="delete-btn" @click="removeItem(item)">删除</button>
        </div>
      </div>
      <div v-if="cartItems.length > 0" class="cart-footer">
        <div class="total">总计: ¥{{ total.toFixed(2) }}</div>
        <button class="buy-btn" @click="buy">结算</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const router = useRouter()

const loading = ref(true)
const error = ref(null)
const isLoggedIn = ref(false)
const cartItems = ref([])

const total = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    return sum + item.product.promotePrice * item.number
  }, 0)
})

onMounted(async () => {
  try {
    const checkRes = await foreApi.checkLogin()
    if (!checkRes.data.success) {
      isLoggedIn.value = false
      return
    }
    isLoggedIn.value = true

    const res = await foreApi.getCart()
    if (res.data.success) {
      cartItems.value = res.data.data
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})

const decrease = async (item) => {
  if (item.number <= 1) return
  try {
    await foreApi.changeOrderItem(item.product.id, item.number - 1)
    item.number--
  } catch (e) {
    alert('修改失败')
  }
}

const increase = async (item) => {
  try {
    await foreApi.changeOrderItem(item.product.id, item.number + 1)
    item.number++
  } catch (e) {
    alert('修改失败')
  }
}

const removeItem = async (item) => {
  if (!confirm('确定删除?')) return
  try {
    await foreApi.deleteOrderItem(item.id)
    cartItems.value = cartItems.value.filter(i => i.id !== item.id)
  } catch (e) {
    alert('删除失败')
  }
}

const buy = () => {
  // Store selected items for buy page
  router.push('/buy')
}
</script>

<style scoped>
.cart-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error, .not-login {
  text-align: center;
  padding: 50px;
}

.empty-cart {
  text-align: center;
  padding: 50px;
  color: #999;
}

.cart-list {
  border: 1px solid #ddd;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #ddd;
}

.cart-item:last-child {
  border-bottom: none;
}

.cart-item img {
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
  width: 300px;
}

.item-price {
  color: #ff5000;
}

.item-count {
  display: flex;
  align-items: center;
  margin: 0 20px;
}

.item-count button {
  width: 30px;
  height: 30px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
}

.item-count span {
  padding: 0 15px;
}

.item-subtotal {
  width: 100px;
  text-align: right;
  font-weight: bold;
}

.delete-btn {
  margin-left: 15px;
  padding: 5px 10px;
  background: #ff3b3b;
  color: white;
  border: none;
  cursor: pointer;
}

.cart-footer {
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

.buy-btn {
  padding: 10px 40px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
}
</style>
