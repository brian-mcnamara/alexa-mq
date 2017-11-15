// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import Auth from './auth'
import Validation from 'vee-validate'

import 'bootstrap/dist/css/bootstrap.css'
import '../node_modules/bootstrap-vue/dist/bootstrap-vue.css'

Vue.config.productionTip = false

/* eslint-disable no-new */
Vue.use(BootstrapVue)
Vue.use(Validation)
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App },
  created: function () {
    Auth.checkAuth()
  }
})
