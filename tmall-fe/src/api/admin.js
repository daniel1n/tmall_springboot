import axios from 'axios'

const API_BASE = '/api/admin'

const api = {
  // Dashboard
  getDashboard() {
    return axios.get(`${API_BASE}/dashboard`)
  },

  // Category operations
  getCategoryList(start, size) {
    return axios.get(`${API_BASE}/categoryList`, { params: { start, size } })
  },
  getCategoryEdit(id) {
    return axios.get(`${API_BASE}/categoryEdit`, { params: { id } })
  },

  // Product operations
  getProductList(cid, start, size) {
    return axios.get(`${API_BASE}/productList`, { params: { cid, start, size } })
  },
  getProductEdit(id) {
    return axios.get(`${API_BASE}/productEdit`, { params: { id } })
  },
  addProduct(data) {
    return axios.post('/products', data)
  },
  updateProduct(data) {
    return axios.put('/products', data)
  },
  deleteProduct(id) {
    return axios.delete(`/products/${id}`)
  },

  // Product image operations
  getProductImageList(pid, type) {
    return axios.get(`${API_BASE}/productImageList`, { params: { pid, type } })
  },
  addProductImage(formData) {
    return axios.post('/productImages', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  deleteProductImage(id) {
    return axios.delete(`/productImages/${id}`)
  },

  // Property operations
  getPropertyList(cid, start, size) {
    return axios.get(`${API_BASE}/propertyList`, { params: { cid, start, size } })
  },
  getPropertyEdit(id, cid) {
    return axios.get(`${API_BASE}/propertyEdit`, { params: { id, cid } })
  },
  addProperty(data) {
    return axios.post('/properties', data)
  },
  updateProperty(data) {
    return axios.put('/properties', data)
  },
  deleteProperty(id) {
    return axios.delete(`/properties/${id}`)
  },

  // Property value operations
  getPropertyValueEdit(pid) {
    return axios.get(`${API_BASE}/propertyValueEdit`, { params: { pid } })
  },
  updatePropertyValue(data) {
    return axios.put('/propertyValues', data)
  },

  // Order operations
  getOrderList(start, size) {
    return axios.get(`${API_BASE}/orderList`, { params: { start, size } })
  },
  deliveryOrder(oid) {
    return axios.put(`/deliveryOrder/${oid}`)
  },

  // User operations
  getUserList(start, size) {
    return axios.get(`${API_BASE}/userList`, { params: { start, size } })
  },

  // Category CRUD (REST)
  getCategories(start, size) {
    return axios.get('/categories', { params: { start, size } })
  },
  getCategory(id) {
    return axios.get(`/categories/${id}`)
  },
  addCategory(formData) {
    return axios.post('/categories', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  updateCategory(formData) {
    return axios.put(`/categories/${formData.get('id')}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  deleteCategory(id) {
    return axios.delete(`/categories/${id}`)
  },

  // Product CRUD (REST)
  getProducts(cid, start, size) {
    return axios.get(`/categories/${cid}/products`, { params: { start, size } })
  },
  getProduct(id) {
    return axios.get(`/products/${id}`)
  },

  // Property CRUD (REST)
  getProperties(cid, start, size) {
    return axios.get(`/categories/${cid}/properties`, { params: { start, size } })
  },
  getProperty(id) {
    return axios.get(`/properties/${id}`)
  },

  // Property value CRUD (REST)
  getPropertyValues(pid) {
    return axios.get(`/products/${pid}/propertyValues`)
  },

  // Order CRUD (REST)
  getOrders(start, size) {
    return axios.get('/orders', { params: { start, size } })
  },

  // User CRUD (REST)
  getUsers(start, size) {
    return axios.get('/users', { params: { start, size } })
  }
}

export default api
