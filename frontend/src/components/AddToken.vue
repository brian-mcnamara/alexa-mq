<template>
  <div class="addToken" v-if="!submitted">
    <label>Token name:</label>
    <input type="text" placeholder="Token name:" v-model="name"/>
    <label>Scopes:</label>
    <input type="text" placeholder="Token scopes (comma separated)" v-model="scopes"/>
    <button type="submit" class="btn btn-primary" @click="submit">Create</button>
  </div>
  <div v-else>
    <span>Token: {{ token }}</span>
  </div>
</template>

<script>
import axios from 'axios'
var name = ''
var scopes = ''
export default {
  name: 'AddToken',
  data () {
    return {
      name: name,
      scopes: scopes,
      submitted: false
    }
  },
  methods: {
    submit: function () {
      axios.post('/api/tokens/create', {
        name: this.name,
        scopes: this.scopes
      }).then(res => {
        this.submitted = true
        this.token = res.data
      })
    }
  }
}
</script>
