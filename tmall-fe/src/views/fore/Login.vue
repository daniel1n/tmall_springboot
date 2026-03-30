<template>
  <div class="login-page">
    <div class="login-form">
      <h2>用户登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <input v-model="form.name" type="text" placeholder="用户名" required />
        </div>
        <div class="form-group">
          <input v-model="form.password" type="password" placeholder="密码" required />
        </div>
        <div v-if="error" class="error">{{ error }}</div>
        <button type="submit" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
      <p class="register-link">
        还没有账号？ <router-link to="/register">立即注册</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const router = useRouter()
const form = ref({ name: '', password: '' })
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await foreApi.login(form.value)
    if (res.data.success) {
      router.push('/home')
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.login-form {
  width: 300px;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background: #ff5000;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background: #ccc;
}

.error {
  color: red;
  margin-bottom: 10px;
  text-align: center;
}

.register-link {
  text-align: center;
  margin-top: 15px;
}
</style>
