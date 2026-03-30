<template>
  <div class="home">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <div v-for="category in categories" :key="category.id" class="category-block">
        <div class="category-title">{{ category.name }}</div>
        <div class="product-row">
          <div v-for="product in category.products" :key="product.id" class="product-item">
            <router-link :to="`/product/${product.id}`">
              <img :src="`/img/productSingle/${product.firstProductImage.id}.jpg`" :alt="product.name" />
              <div class="product-name">{{ product.name }}</div>
              <div class="product-price">¥{{ product.promotePrice }}</div>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import foreApi from '../../api/fore'

const loading = ref(true)
const error = ref(null)
const categories = ref([])

onMounted(async () => {
  try {
    const res = await foreApi.getHomeData()
    if (res.data.success) {
      categories.value = res.data.data
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 50px;
}

.category-block {
  margin-bottom: 30px;
}

.category-title {
  font-size: 18px;
  font-weight: bold;
  padding: 10px 0;
  border-bottom: 2px solid #ff5000;
  margin-bottom: 15px;
}

.product-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.product-item {
  width: 200px;
}

.product-item img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #ff5000;
  font-weight: bold;
}
</style>
