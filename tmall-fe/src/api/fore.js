import axios from 'axios'

const API_BASE = '/api/fore'

const api = {
  // Home page
  getHomeData() {
    return axios.get(`${API_BASE}/home`)
  },

  // Register page
  getRegisterPage() {
    return axios.get(`${API_BASE}/registerPage`)
  },

  // Login page
  getLoginPage() {
    return axios.get(`${API_BASE}/loginPage`)
  },

  // Product detail
  getProduct(pid) {
    return axios.get(`${API_BASE}/product/${pid}`)
  },

  // Category page
  getCategory(cid, sort) {
    return axios.get(`${API_BASE}/category/${cid}`, { params: { sort } })
  },

  // Cart
  getCart() {
    return axios.get(`${API_BASE}/cart`)
  },

  // Buy (checkout)
  getBuy(oiid) {
    return axios.get(`${API_BASE}/buy`, { params: { oiid } })
  },

  // Alipay page
  getAlipayPage(oid) {
    return axios.get(`${API_BASE}/alipayPage`, { params: { oid } })
  },

  // My orders
  getBought() {
    return axios.get(`${API_BASE}/bought`)
  },

  // Search
  search(keyword) {
    return axios.get(`${API_BASE}/search`, { params: { keyword } })
  },

  // Confirm pay page
  getConfirmPay(oid) {
    return axios.get(`${API_BASE}/confirmPay`, { params: { oid } })
  },

  // Order confirmed page
  getOrderConfirmed(oid) {
    return axios.get(`${API_BASE}/orderConfirmed`, { params: { oid } })
  },

  // Review page
  getReview(oid) {
    return axios.get(`${API_BASE}/review`, { params: { oid } })
  },

  // Check login status
  checkLogin() {
    return axios.get(`${API_BASE}/checkLogin`)
  },

  // Get user info
  getUserInfo() {
    return axios.get(`${API_BASE}/userInfo`)
  },

  // Login
  login(data) {
    return axios.post('/forelogin', data)
  },

  // Register
  register(data) {
    return axios.post('/foreregister', data)
  },

  // Logout
  logout() {
    return axios.get('/forelogout')
  },

  // Buy one (add to cart or direct buy)
  buyOne(pid, num) {
    return axios.get('/forebuyone', { params: { pid, num } })
  },

  // Add to cart
  addCart(pid, num) {
    return axios.get('foreaddCart', { params: { pid, num } })
  },

  // Change order item quantity
  changeOrderItem(pid, num) {
    return axios.get('forechangeOrderItem', { params: { pid, num } })
  },

  // Delete order item
  deleteOrderItem(oiid) {
    return axios.get('foredeleteOrderItem', { params: { oiid } })
  },

  // Create order
  createOrder(data) {
    return axios.post('forecreateOrder', data)
  },

  // Payed callback
  payed(oid) {
    return axios.get('forepayed', { params: { oid } })
  },

  // Confirm pay
  confirmPay(oid) {
    return axios.get('foreconfirmPay', { params: { oid } })
  },

  // Order confirmed
  orderConfirmed(oid) {
    return axios.get('foreorderConfirmed', { params: { oid } })
  },

  // Delete order
  deleteOrder(oid) {
    return axios.put('foredeleteOrder', { params: { oid } })
  },

  // Get review
  getReviewPage(oid) {
    return axios.get('forereview', { params: { oid } })
  },

  // Submit review
  doReview(oid, pid, content) {
    return axios.post('foredoreview', null, { params: { oid, pid, content } })
  }
}

export default api
