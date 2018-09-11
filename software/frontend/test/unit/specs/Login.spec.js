import Vue from 'vue'
import Login from '../../../src/components/Login'
import axios from 'axios'
import expect from 'expect'
import MockAdapter from 'axios-mock-adapter'

describe('Login.vue', () => {

  // Mockconfiguration
  var Promise = require('es6-promise').polyfill()
  var mock = new MockAdapter(axios)

  // Scenario 1
  it('should let you log in when entering correct userdetails', () => {
    const Constructor = Vue.extend(Login)
    const vm = new Constructor().$mount()

    mock.onPost('/login').reply(200)

    vm.$data.login = { email: 'user', password: 'password' }
    
    expect(axios.post('/login', vm.$data.login)
    .then(1)
    .catch(error => console.log(error))).toBe(1)
  })

})
