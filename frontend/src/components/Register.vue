<template>
  <div class="reg">
    <form id="register" @submit.prevent="validateBeforeSubmit()">
      <label>Email address</label>
      <input placeholder="Email address" type="email" required="true" v-model="email"/>
      <div class="input-group">
        <div class="input-group-addon">
          Name
        </div>

        <div class="input-fields">
          <input name="first" type="text" class="form-control" placeholder="First name" v-model="first">
          <input name="last" type="text" class="form-control" placeholder="Last name" v-model="last">
        </div>
      </div>

      <div class="input-group">
        <div class="input-group-addon">
          Enter Password
        </div>

        <div class="input-fields">
          <input v-validate="'required'" name="password" type="password" class="form-control" placeholder="Password" v-model="password">
          <input v-validate="'required|confirmed:password'" name="password_confirmation" type="password" class="form-control" placeholder="Password, Again" data-vv-as="password">
        </div>
      </div>

      <div class="alert alert-danger" v-show="errors.any()">
        <div v-if="errors.has('password')">
          {{ errors.first('password') }}
        </div>
        <div v-if="errors.has('password_confirmation')">
          {{ errors.first('password_confirmation') }}
        </div>
      </div>

      <button type="submit" class="btn btn-primary">
        Submit
      </button>
    </form>
  </div>
</template>

<style scoped>
  .input-fields input {
    width: 100%;
  }
</style>

<script>
import axios from 'axios'
import router from '../router'
export default {
  name: 'register',
  data () {
    return {
      email: '',
      password: '',
      first: '',
      last: ''
    }
  },
  methods: {
    validateBeforeSubmit () {
      var that = this
      this.$validator
        .validateAll()
        .then(function (response) {
          if (response) {
            axios.post('/register', {
              firstName: that.first,
              lastName: that.last,
              email: that.email,
              password: that.password

            }).then(resp => {
              if (resp.status === 201) {
                router.next('/login')
              }
            })
          }
          // Validation success if response === true
        })
        .catch(function (e) {
          console.log(e)
          // Catch errors
        })
    }
  }
}
</script>
