import Vue from 'vue'
import Login from '@/components/Login'
import axios from 'axios'
import MockAdapter from 'axios-mock-adapter'

describe('Login.vue', () => {

  // Mockconfiguration
  var Promise = require('es6-promise').polyfill()
  var mock = new MockAdapter(axios)

  // Scenario 1
  it('should let you log in when entering correct userdetails', () => {
    const Constructor = Vue.extend(Login)
    const vm = new Constructor().$mount()

    mock.onGet('/users').reply(200, {
      users: [
        { id: 1, name: 'John Smith' }
      ]
    })

    axios.get('/users')
      .then(function(response) {
      console.log(response.data)
      })
  })
})
