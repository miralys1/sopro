import Vue from 'vue'
import Login from '../../../src/components/Login'
import expect, { createSpy, spyOn, isSpy } from 'expect'

describe('Login.vue', () => {

  // Mockconfiguration


  // Scenario 1
  it('should let you log in when entering correct userdetails', () => {
    const Constructor = Vue.extend(Login)
    const vm = new Constructor().$mount()

    var axios = require('axios')
    var MockAdapter = require('axios-mock-adapter')
    var Promise = require('es6-promise').polyfill()
    var mock = new MockAdapter(axios)
    mock.onPost('/login').reply(200)

    vm.$data.login = { email: 'user', password: 'password' }

    var spy = expect.spyOn(axios, 'post')
    axios.post('/login', vm.$data.login)
      .then(function(response) {
        expect(3).toNotEqual(3)
      })
      .catch(error => expect(3).toNotEqual(3))
      console.log('spast')
      expect(spy).toHaveBeenCalled(1)
  })

  // Scenario 1
  it('shouldn\'t let you log in when entering wrong userdetails', () => {
    const Constructor = Vue.extend(Login)
    const vm = new Constructor().$mount()
    var Promise = require('es6-promise').polyfill()
    var mock = new MockAdapter(axios)
    mock.onPost('/login').reply(401)

    vm.$data.login = { email: 'user', password: 'password' }

    axios.post('/login', vm.$data.login)
      .then(response => {
        expect(response).toExist()
        expect(response.status).toEqual(200)
      })
      .catch(error => expect(error).toNotExist())
  })

})
