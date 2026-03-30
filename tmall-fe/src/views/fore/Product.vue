<template>
  <div class="product-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <div class="product-main">
        <div class="product-image">
          <img :src="`/img/productSingle/${selectedImage}.jpg`" :alt="product.name" />
          <div class="thumbnail-list">
            <img
              v-for="img in product.productSingleImages"
              :key="img.id"
              :src="`/img/productSingle/${img.id}.jpg`"
              :class="{ active: selectedImage === img.id }"
              @click="selectedImage = img.id"
            />
          </div>
        </div>
        <div class="product-info">
          <h1>{{ product.name }}</h1>
          <div class="price">¥{{ product.promotePrice }}</div>
          <div class="sale-info">
            <span>销量: {{ product.saleCount }}</span>
            <span>评价: {{ product.reviewCount }}</span>
          </div>
          <div class="actions">
            <button @click="buyNow">立即购买</button>
            <button @click="addToCart">加入购物车</button>
          </div>
        </div>
      </div>

      <div class="product-detail">
        <h3>商品详情</h3>
        <div class="property-list">
          <div v-for="pv in propertyValues" :key="pv.id" class="property-item">
            <span class="property-name">{{ pv.property.name }}:</span>
            <span class="property-value">{{ pv.value }}</span>
          </div>
        </div>
        <div class="detail-images">
          <img
            v-for="img in product.productDetailImages"
            :key="img.id"
            :src="`/img/productDetail/${img.id}.jpg`"
          />
        </div>
      </div>

      <div class="product-review">
        <h3>商品评价</h3>
        <div v-if="reviews.length === 0" class="no-review">暂无评价</div>
        <div v-else class="review-list">
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="review-user">{{ review.user.name }}</div>
            <div class="review-content">{{ review.content }}</div>
            <div class="review-date">{{ formatDate(review.createDate) }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const error = ref(null)
const product = ref({})
const propertyValues = ref([])
const reviews = ref([])
const selectedImage = ref(0)

const pid = computed(() => route.params.pid)

onMounted(async () => {
  try {
    const res = await foreApi.getProduct(pid.value)
    if (res.data.success) {
      const data = res.data.data
      product.value = data.product
      propertyValues.value = data.propertyValues
      reviews.value = data.reviews
      if (data.product.productSingleImages.length > 0) {
        selectedImage.value = data.product.productSingleImages[0].id
      }
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})

const buyNow = async () => {
  try {
    await foreApi.buyOne(pid.value, 1)
    router.push('/buy')
  } catch (e) {
    alert('请先登录')
  }
}

const addToCart = async () => {
  try {
    await foreApi.addCart(pid.value, 1)
    alert('已加入购物车')
  } catch (e) {
    alert('请先登录')
  }
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString()
}
</script>

<style scoped>
.product-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 50px;
}

.product-main {
  display: flex;
  gap: 40px;
}

.product-image {
  width: 400px;
}

.product-image > img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.thumbnail-list img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid transparent;
}

.thumbnail-list img.active {
  border-color: #ff5000;
}

.product-info {
  flex: 1;
}

.price {
  font-size: 24px;
  color: #ff5000;
  font-weight: bold;
  margin: 20px 0;
}

.sale-info {
  color: #999;
  margin-bottom: 20px;
}

.sale-info span {
  margin-right: 20px;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions button {
  padding: 10px 30px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.actions button:first-child {
  background: #ff5000;
  color: white;
}

.actions button:last-child {
  background: #ffcc00;
  color: #333;
}

.product-detail, .product-review {
  margin-top: 40px;
  border-top: 1px solid #ddd;
  padding-top: 20px;
}

.property-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.property-name {
  color: #666;
  margin-right: 10px;
}

.detail-images img {
  width: 100%;
  margin-top: 10px;
}

.review-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.review-user {
  color: #ff5000;
  font-weight: bold;
}

.review-date {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
}
</style>
