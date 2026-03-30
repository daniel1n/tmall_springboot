<template>
  <div class="admin-product">
    <div class="header">
      <h2>产品管理</h2>
      <button @click="showAddModal" class="add-btn">新增产品</button>
    </div>

    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>产品名称</th>
          <th>原价</th>
          <th>促销价</th>
          <th>销量</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>¥{{ product.originalPrice }}</td>
          <td>¥{{ product.promotePrice }}</td>
          <td>{{ product.saleCount }}</td>
          <td>
            <button @click="editProduct(product)" class="edit-btn">编辑</button>
            <button @click="manageImages(product)" class="info-btn">图片</button>
            <button @click="deleteProduct(product.id)" class="delete-btn">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="page.start === 0">上一页</button>
      <span>第 {{ page.pageNum }} / {{ page.totalPages }} 页</span>
      <button @click="nextPage" :disabled="page.pageNum >= page.totalPages">下一页</button>
    </div>

    <!-- Edit Modal -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑产品' : '新增产品' }}</h3>
        <div class="form-group">
          <label>产品名称</label>
          <input v-model="form.name" />
        </div>
        <div class="form-group">
          <label>原价</label>
          <input v-model="form.originalPrice" type="number" />
        </div>
        <div class="form-group">
          <label>促销价</label>
          <input v-model="form.promotePrice" type="number" />
        </div>
        <div class="form-group">
          <label>销量</label>
          <input v-model="form.saleCount" type="number" />
        </div>
        <div class="modal-actions">
          <button @click="saveProduct">保存</button>
          <button @click="closeModal">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import adminApi from '../../api/admin'

const products = ref([])
const page = ref({ start: 0, size: 5, pageNum: 1, totalPages: 1 })
const showModal = ref(false)
const isEdit = ref(false)
const form = ref({
  id: 0,
  name: '',
  originalPrice: 0,
  promotePrice: 0,
  saleCount: 0,
  category: { id: 1 }
})

const loadData = async () => {
  try {
    const res = await adminApi.getProductList(0, page.value.start, page.value.size)
    if (res.data.success) {
      products.value = res.data.data.content
      page.value.totalPages = res.data.data.totalPages
      page.value.pageNum = res.data.data.pageNum + 1
    }
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

const showAddModal = () => {
  isEdit.value = false
  form.value = { id: 0, name: '', originalPrice: 0, promotePrice: 0, saleCount: 0, category: { id: 1 } }
  showModal.value = true
}

const editProduct = (product) => {
  isEdit.value = true
  form.value = { ...product, category: product.category || { id: 1 } }
  showModal.value = true
}

const manageImages = (product) => {
  alert('图片管理功能开发中')
}

const closeModal = () => {
  showModal.value = false
}

const saveProduct = async () => {
  try {
    if (isEdit.value) {
      await adminApi.updateProduct(form.value)
    } else {
      await adminApi.addProduct(form.value)
    }
    closeModal()
    loadData()
  } catch (e) {
    alert('保存失败')
  }
}

const deleteProduct = async (id) => {
  if (!confirm('确定删除?')) return
  try {
    await adminApi.deleteProduct(id)
    loadData()
  } catch (e) {
    alert('删除失败')
  }
}

onMounted(loadData)
</script>

<style scoped>
.admin-product {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-btn {
  padding: 10px 20px;
  background: #ff5000;
  color: white;
  border: none;
  cursor: pointer;
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

.edit-btn, .delete-btn, .info-btn {
  padding: 5px 15px;
  margin-right: 5px;
  border: none;
  cursor: pointer;
}

.edit-btn { background: #4a90d9; color: white; }
.info-btn { background: #4caf50; color: white; }
.delete-btn { background: #ff3b3b; color: white; }

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

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 4px;
  width: 400px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.modal-actions button {
  padding: 8px 20px;
  border: none;
  cursor: pointer;
}

.modal-actions button:first-child {
  background: #ff5000;
  color: white;
}
</style>
