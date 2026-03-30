<template>
  <div class="admin-category">
    <div class="header">
      <h2>分类管理</h2>
      <button @click="showAddModal" class="add-btn">新增分类</button>
    </div>

    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>分类名称</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="category in categories" :key="category.id">
          <td>{{ category.id }}</td>
          <td>{{ category.name }}</td>
          <td>
            <button @click="editCategory(category)" class="edit-btn">编辑</button>
            <button @click="deleteCategory(category.id)" class="delete-btn">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="page.start === 0">上一页</button>
      <span>第 {{ page.pageNum }} / {{ page.totalPages }} 页</span>
      <button @click="nextPage" :disabled="page.pageNum >= page.totalPages">下一页</button>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>{{ isEdit ? '编辑分类' : '新增分类' }}</h3>
        <input v-model="form.name" placeholder="分类名称" />
        <div class="modal-actions">
          <button @click="saveCategory">保存</button>
          <button @click="closeModal">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import adminApi from '../../api/admin'

const categories = ref([])
const page = ref({ start: 0, size: 5, pageNum: 1, totalPages: 1 })
const showModal = ref(false)
const isEdit = ref(false)
const form = ref({ id: 0, name: '' })

const loadData = async () => {
  try {
    const res = await adminApi.getCategoryList(page.value.start, page.value.size)
    if (res.data.success) {
      categories.value = res.data.data.content
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
  form.value = { id: 0, name: '' }
  showModal.value = true
}

const editCategory = (category) => {
  isEdit.value = true
  form.value = { id: category.id, name: category.name }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}

const saveCategory = async () => {
  try {
    if (isEdit.value) {
      await adminApi.updateCategory(form.value)
    } else {
      await adminApi.addCategory(form.value)
    }
    closeModal()
    loadData()
  } catch (e) {
    alert('保存失败')
  }
}

const deleteCategory = async (id) => {
  if (!confirm('确定删除?')) return
  try {
    await adminApi.deleteCategory(id)
    loadData()
  } catch (e) {
    alert('删除失败')
  }
}

onMounted(loadData)
</script>

<style scoped>
.admin-category {
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

.edit-btn, .delete-btn {
  padding: 5px 15px;
  margin-right: 5px;
  border: none;
  cursor: pointer;
}

.edit-btn {
  background: #4a90d9;
  color: white;
}

.delete-btn {
  background: #ff3b3b;
  color: white;
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
  width: 300px;
}

.modal-content input {
  width: 100%;
  padding: 10px;
  margin: 15px 0;
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
