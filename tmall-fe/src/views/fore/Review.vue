<template>
  <div class="review-page">
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <h2>商品评价</h2>
      <div class="product-info">
        <img :src="`/img/productSingle/${product.firstProductImage.id}.jpg`" />
        <div class="product-name">{{ product.name }}</div>
      </div>

      <div class="review-form">
        <textarea v-model="content" placeholder="请输入评价内容" rows="5"></textarea>
        <button @click="submitReview" :disabled="!content.trim()">提交评价</button>
      </div>

      <div class="review-list">
        <h3>商品评价</h3>
        <div v-if="reviews.length === 0" class="no-review">暂无评价</div>
        <div v-else v-for="review in reviews" :key="review.id" class="review-item">
          <div class="review-user">{{ review.user.name }}</div>
          <div class="review-content">{{ review.content }}</div>
          <div class="review-date">{{ formatDate(review.createDate) }}</div>
        </div>
      </div>
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
const product = ref({})
const reviews = ref([])
const content = ref('')

onMounted(async () => {
  try {
    const res = await foreApi.getReview(route.params.oid)
    if (res.data.success) {
      const data = res.data.data
      product.value = data.p
      reviews.value = data.reviews
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '加载失败'
  } finally {
    loading.value = false
  }
})

const submitReview = async () => {
  try {
    await foreApi.doReview(
      route.params.oid,
      product.value.id,
      content.value
    )
    router.push('/bought')
  } catch (e) {
    alert('提交失败')
  }
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString()
}
</script>

<style scoped>
.review-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 50px;
}

.product-info {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #f5f5f5;
  margin: 20px 0;
}

.product-info img {
  width: 80px;
  height: 80px;
  object-fit: cover;
}

.product-info .product-name {
  margin-left: 20px;
  font-size: 18px;
}

.review-form {
  margin: 20px 0;
}

textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
}

button {
  margin-top: 10px;
  padding: 10px 30px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
}

button:disabled {
  background: #ccc;
}

.review-list {
  margin-top: 30px;
}

.review-list h3 {
  margin-bottom: 15px;
}

.review-item {
  padding: 15px 0;
  border-bottom: 1px solid #ddd;
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

.no-review {
  color: #999;
  text-align: center;
  padding: 20px;
}
</style>
