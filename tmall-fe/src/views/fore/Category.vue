<template>
  <div class="category-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <div class="sort-bar">
        <span>排序:</span>
        <button :class="{ active: sort === 'all' }" @click="changeSort('all')">综合</button>
        <button :class="{ active: sort === 'saleCount' }" @click="changeSort('saleCount')">销量</button>
        <button :class="{ active: sort === 'price' }" @click="changeSort('price')">价格</button>
        <button :class="{ active: sort === 'review' }" @click="changeSort('review')">评价</button>
        <button :class="{ active: sort === 'date' }" @click="changeSort('date')">新品</button>
      </div>

      <div class="product-list">
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
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import foreApi from '../../api/fore'

const route = useRoute()

const loading = ref(true)
const error = ref(null)
const products = ref([])
const sort = ref('all')

const cid = computed(() => route.params.cid)

const loadData = async () => {
  loading.value = true
  try {
    const res = await foreApi.getCategory(cid.value, sort.value)
    if (res.data.success) {
      products.value = res.data.data.products
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
}

const changeSort = (newSort) => {
  sort.value = newSort
  loadData()
}

onMounted(loadData)

watch(() => route.params.cid, loadData)
</script>

<style scoped>
.category-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 50px;
}

.sort-bar {
  margin-bottom: 20px;
  padding: 10px;
  background: #f5f5f5;
}

.sort-bar button {
  margin-left: 10px;
  padding: 5px 15px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
}

.sort-bar button.active {
  background: #ff5000;
  color: white;
  border-color: #ff5000;
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
