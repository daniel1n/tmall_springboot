<template>
  <div class="admin-order">
    <h2>订单管理</h2>

    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>订单号</th>
          <th>用户</th>
          <th>金额</th>
          <th>状态</th>
          <th>日期</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.orderCode }}</td>
          <td>{{ order.user ? order.user.name : '-' }}</td>
          <td>¥{{ order.total }}</td>
          <td>{{ statusText(order.status) }}</td>
          <td>{{ formatDate(order.createDate) }}</td>
          <td>
            <button @click="viewDetail(order)" class="info-btn">详情</button>
            <button v-if="order.status === 'waitDelivery'" @click="deliver(order)" class="edit-btn">发货</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="prevPage" :disabled="page.start === 0">上一页</button>
      <span>第 {{ page.pageNum }} / {{ page.totalPages }} 页</span>
      <button @click="nextPage" :disabled="page.pageNum >= page.totalPages">下一页</button>
    </div>

    <!-- Detail Modal -->
    <div v-if="showDetail" class="modal">
      <div class="modal-content">
        <h3>订单详情</h3>
        <div class="order-info">
          <p><strong>订单号:</strong> {{ currentOrder.orderCode }}</p>
          <p><strong>用户:</strong> {{ currentOrder.user ? currentOrder.user.name : '-' }}</p>
          <p><strong>状态:</strong> {{ statusText(currentOrder.status) }}</p>
          <p><strong>地址:</strong> {{ currentOrder.address }}</p>
        </div>
        <div class="order-items">
          <h4>商品列表</h4>
          <div v-for="oi in currentOrder.orderItems" :key="oi.id" class="order-item">
            <span>{{ oi.product.name }}</span>
            <span>x{{ oi.number }}</span>
            <span>¥{{ oi.product.promotePrice }}</span>
          </div>
        </div>
        <div class="modal-actions">
          <button @click="showDetail = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import adminApi from '../../api/admin'

const orders = ref([])
const page = ref({ start: 0, size: 5, pageNum: 1, totalPages: 1 })
const showDetail = ref(false)
const currentOrder = ref({})

const loadData = async () => {
  try {
    const res = await adminApi.getOrderList(page.value.start, page.value.size)
    orders.value = res.data.data.content
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

const statusText = (status) => {
  const map = {
    waitPay: '待支付',
    waitDelivery: '待发货',
    waitConfirm: '待收货',
    waitReview: '待评价',
    finish: '已完成',
    delete: '已删除'
  }
  return map[status] || status
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString()
}

const viewDetail = (order) => {
  currentOrder.value = order
  showDetail.value = true
}

const deliver = async (order) => {
  try {
    await adminApi.deliveryOrder(order.id)
    loadData()
  } catch (e) {
    alert('操作失败')
  }
}

onMounted(loadData)
</script>

<style scoped>
.admin-order {
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

.edit-btn, .info-btn {
  padding: 5px 15px;
  margin-right: 5px;
  border: none;
  cursor: pointer;
  color: white;
}

.edit-btn { background: #4a90d9; }
.info-btn { background: #4caf50; }

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
  width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.order-info p {
  margin: 10px 0;
}

.order-items h4 {
  margin-top: 20px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.modal-actions button {
  padding: 8px 20px;
  border: none;
  background: #ff5000;
  color: white;
  cursor: pointer;
}
</style>
