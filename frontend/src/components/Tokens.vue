<template>
  <div class="tokens">
    <button @click="createToken" class="create btn btn-primary">Create token</button>
    <div :is="createTokenForm"></div>
    <b-table :items="tokens" :fields="fields">
        <template slot="delete" slot-scope="row" :deleteToken="deleteToken">
          <button type="button" class="close" aria-label="Remove" @click="deleteToken(row)">
            <span aria-hidden="true">&times;</span>
          </button>
        </template>
    </b-table>
  </div>
</template>

<script>
  import axios from 'axios'
  import auth from '../auth'
  import CreateToken from '@/components/AddToken'
  var tokens = [];
  const fields = ['name', 'accessToken', 'rights', 'delete']

  export default {
    name: 'Tokens',
    data () {
      return {
        tokens: tokens,
        fields: fields,
        createTokenForm: null
      }
    },
    created () {
      axios.get('/api/tokens').then((res) => {
        this.tokens = res.data
      });
    },
    route: {
      canActivate() {
        return auth.checkAuth()
      }
    },
    methods: {
      deleteToken: function(event) {
        var c = confirm(`About to delete ${event.item.name}. Are you sure?`)
        if (c) {
          var that = this
          axios.post(`/api/tokens/remove/${event.item.name}`).then(res => {
          debugger
            var index = that.tokens.indexOf(event.item)
            that.tokens.splice(index, 1)
          }).catch(e => {
            debugger
          })
        }
      },
      createToken: function() {
        this.createTokenForm = CreateToken
      }
    }
  }
</script>
