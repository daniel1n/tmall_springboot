<template>
  <div class="register-page">
    <div class="register-form">
      <h2>用户注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <input v-model="form.name" type="text" placeholder="用户名" required />
        </div>
        <div class="form-group">
          <input v-model="form.password" type="password" placeholder="密码" required />
        </div>
        <div class="form-group">
          <input v-model="form.repassword" type="password" placeholder="确认密码" required />
        </div>
        <div v-if="error" class="error">{{ error }}</div>
        <div v-if="success" class="success">{{ success }}</div>
        <button type="submit" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>
      <p class="login-link">
        已有账号？ <router-link to="/login">立即登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import foreApi from '../../api/fore'

const router = useRouter()
const form = ref({ name: '', password: '', repassword: '' })
const loading = ref(false)
const error = ref('')
const success = ref('')

const handleRegister = async () => {
  if (form.value.password !== form.value.repassword) {
    error.value = '两次密码不一致'
    return
  }
  loading.value = true
  error.value = ''
  try {
    const res = await foreApi.register(form.value)
    if (res.data.success) {
      success.value = '注册成功，请登录'
      setTimeout(() => router.push('/login'), 1500)
    } else {
      error.value = res.data.message
    }
  } catch (e) {
    error.value = '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.register-form {
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

.success {
  color: green;
  margin-bottom: 10px;
  text-align: center;
}

.login-link {
  text-align: center;
  margin-top: 15px;
}
</style>
