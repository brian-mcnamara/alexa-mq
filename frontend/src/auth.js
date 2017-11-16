import router from './router'
import jwtDecode from 'jwt-decode'
let axios = require('axios')

// URL and endpoint constants
const LOGIN_URL = '/login'
const SIGNUP_URL = 'users/'

export default {

  // User object will let us check authentication status
  user: {
    authenticated: false
  },

  // Send a request to the login URL and save the returned JWT
  login (context, creds, redirect) {
    axios.post(LOGIN_URL, creds).then((data) => {
      localStorage.setItem('id_token', data.headers.authorization)

      this.user.authenticated = true

      // Redirect to a specified route
      if (redirect) {
        router.push(redirect)
      }
    }).catch((err) => {
      context.error = err
    })
  },

  signup (context, creds, redirect) {
    axios.post(SIGNUP_URL, creds).then((data) => {
      localStorage.setItem('id_token', data.id_token)
      localStorage.setItem('access_token', data.access_token)

      this.user.authenticated = true

      if (redirect) {
        router.push(redirect)
      }
    }).catch((err) => {
      context.error = err
    })
  },

  // To log out, we just need to remove the token
  logout () {
    localStorage.removeItem('id_token')
    this.user.authenticated = false
    router.push('/login')
  },

  checkAuth () {
    var jwt = localStorage.getItem('id_token')
    if (jwt) {
      try {
        var decoded = jwtDecode(jwt)
        if (decoded && decoded.exp > Math.floor(new Date().getTime() / 1000)) {
          this.user.authenticated = true
          axios.defaults.headers.common['Authorization'] = `Bearer ${jwt}`
          return true
        }
      } catch (e) {
        this.logout()
        return false
      }
    } else {
      this.user.authenticated = false
      this.logout()
      return false
    }
  },

  // The object to be passed as a header for authenticated requests
  getAuthHeader () {
    return {
      'Authorization': 'Bearer ' + localStorage.getItem('id_token')
    }
  }
}
