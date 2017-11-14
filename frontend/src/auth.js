import router from './router'
let axios = require('axios')
import jwtDecode from 'jwt-decode'

// URL and endpoint constants
const LOGIN_URL = '/login'
const SIGNUP_URL = 'users/'

export default {

  // User object will let us check authentication status
  user: {
    authenticated: false
  },

  // Send a request to the login URL and save the returned JWT
  login(context, creds, redirect) {
    axios.post(LOGIN_URL, creds).then((data) => {
    debugger
      localStorage.setItem('id_token', data.headers.authorization)

      this.user.authenticated = true

      // Redirect to a specified route
      if(redirect) {
        router.go(redirect)
      }

    }).catch((err) => {
      context.error = err
    })
  },

  signup(context, creds, redirect) {
    axios.post(SIGNUP_URL, creds).then((data) => {
      localStorage.setItem('id_token', data.id_token)
      localStorage.setItem('access_token', data.access_token)

      this.user.authenticated = true

      if(redirect) {
        router.go(redirect)
      }

    }).catch((err) => {
      context.error = err
    })
  },

  // To log out, we just need to remove the token
  logout() {
    localStorage.removeItem('id_token')
    this.user.authenticated = false
    router.go('/login')
  },

  checkAuth() {
    var jwt = localStorage.getItem('id_token')
    var decoded = jwtDecode(jwt)
    if(jwt && decoded && decoded.exp > Math.floor(new Date().getTime() / 1000)) {
      this.user.authenticated = true
      axios.defaults.headers.common['Authorization'] = `Bearer ${jwt}`
      return true
    }
    else {
      this.user.authenticated = false
      logout()
      return false
    }
  },

  // The object to be passed as a header for authenticated requests
  getAuthHeader() {
    return {
      'Authorization': 'Bearer ' + localStorage.getItem('id_token')
    }
  }
}
