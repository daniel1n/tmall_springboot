<template>
  <div class="admin-dashboard">
    <h1>管理后台</h1>
    <div class="admin-nav">
      <router-link to="/admin/category">分类管理</router-link>
      <router-link to="/admin/product">产品管理</router-link>
      <router-link to="/admin/order">订单管理</router-link>
      <router-link to="/admin/user">用户管理</router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.admin-nav {
  display: flex;
  gap: 20px;
  margin-top: 30px;
}

.admin-nav a {
  padding: 20px 40px;
  background: #f5f5f5;
  text-decoration: none;
  color: #333;
  border-radius: 4px;
}

.admin-nav a:hover {
  background: #ff5000;
  color: white;
}
</style>
