<template>
  <div class="search-page">
    <div class="search-bar">
      <input v-model="keyword" @keyup.enter="search" placeholder="搜索商品" />
      <button @click="search">搜索</button>
    </div>

    <div v-if="loading" class="loading">搜索中...</div>
    <div v-else-if="products.length === 0 && searched" class="empty">未找到相关商品</div>
    <div v-else class="product-list">
      <div v-for="product in products" :key="product.id" class="product-item">
        <router-link :to="`/product/${product.id}`">
          <img :src="`/img/productSingle/${product.firstProductImage.id}.jpg`" :alt="product.name" />
          <div class="product-name">{{ product.name }}</div>
          <div class="product-price">¥{{ product.promotePrice }}</div>
          <div class="product-sale">销量 {{ product.saleCount }}</div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import foreApi from '../../api/fore'

const route = useRoute()

const keyword = ref('')
const products = ref([])
const loading = ref(false)
const searched = ref(false)

onMounted(() => {
  if (route.query.keyword) {
    keyword.value = route.query.keyword
    search()
  }
})

const search = async () => {
  if (!keyword.value.trim()) return
  loading.value = true
  searched.value = true
  try {
    const res = await foreApi.search(keyword.value)
    products.value = res.data
  } catch (e) {
    products.value = []
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.search-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-bar {
  display: flex;
  margin-bottom: 20px;
}

.search-bar input {
  flex: 1;
  padding: 10px;
  border: 2px solid #ff5000;
  border-radius: 4px 0 0 4px;
}

.search-bar button {
  padding: 10px 30px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
}

.loading, .empty {
  text-align: center;
  padding: 50px;
  color: #999;
}

.product-list {
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

.product-sale {
  color: #999;
  font-size: 12px;
}
</style>
