<template>
  <div class="admin-user">
    <h2>用户管理</h2>

    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>邮箱</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.anonymousName || '-' }}</td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="page.start === 0">上一页</button>
      <span>第 {{ page.pageNum }} / {{ page.totalPages }} 页</span>
      <button @click="nextPage" :disabled="page.pageNum >= page.totalPages">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import adminApi from '../../api/admin'

const users = ref([])
const page = ref({ start: 0, size: 5, pageNum: 1, totalPages: 1 })

const loadData = async () => {
  try {
    const res = await adminApi.getUserList(page.value.start, page.value.size)
    users.value = res.data.data.content
    page.value.totalPages = res.data.data.totalPages
    page.value.pageNum = res.data.data.pageNum + 1
  } catch (e) {
    alert('加载失败')
  }
}

const prevPage = () => {
  page.value.start -= page.value.size
  loadData()
}

const nextPage = () => {
  page.value.start += page.value.size
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.admin-user {
  padding: 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th, .data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 20px;
}

.pagination button {
  padding: 8px 15px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
}

.pagination button:disabled {
  background: #ccc;
}
</style>
