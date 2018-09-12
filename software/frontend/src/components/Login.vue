<template>
  <div class="mainlayout">
    <b-card no-body>
      <b-tabs pills card>
        <b-tab title="Login" active>
          <b-card bg-variant="light">
            <b-form @submit="signin">
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="4"
                            label-for="login.email">
                <b-input-group prepend="Email:">
                  <b-form-input id="login.email"
                                type="email"
                                v-model="login.email"
                                required
                                placeholder="Email eingeben">
                  </b-form-input>
                </b-input-group>
              </b-form-group>
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="4"
                            label-for="login.passwort"
                            class="mb-0">
                <b-input-group prepend="Passwort:">
                  <b-form-input id="login.passwort"
                                type="password"
                                v-model="login.password"
                                required
                                placeholder="Passwort eingeben">
                  </b-form-input>
                </b-input-group>
              </b-form-group>
              <b-button class="mt-4"
                        style="float: right"
                        type="submit"
                        variant="primary">
                Login
              </b-button>
            </b-form>
          </b-card>
        </b-tab>
        <b-tab title="Register" style="overflow: hidden">
          <b-form @submit="register">
            <b-card bg-variant="light" >
              <b-form-group horizontal
                            breakpoint="lg"
                            label="Anmeldedaten"
                            label-class="font-weight-bold pt-0"
                            class="mb-0">
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="3"
                            label="Email:"
                            label-for="email.register">
                <b-form-input id="email.register"
                              type="email"
                              v-model="form.email"
                              required
                              placeholder="Email eingeben">
                </b-form-input>
              </b-form-group>
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="3"
                            label="Passwort:"
                            label-for="passwort.register"
                            class="mb-0">
                <b-form-input id="passwort.register"
                              type="password"
                              v-model="form.password"
                              required
                              placeholder="Passwort eingeben">
                </b-form-input>
              </b-form-group>
            </b-form-group>
            </b-card>
            <br/>
            <b-card bg-variant="light">
              <b-form-group horizontal
                            breakpoint="lg"
                            label="Pers&ouml;nliche Daten"
                            label-class="font-weight-bold pt-0"
                            class="mb-0">
                <b-form-group horizontal
                              breakpoint="lg"
                              label-class="text-sm-right"
                              :label-cols="3"
                              label="Titel:"
                              label-for="title" >
                  <b-form-select id="title"
                                 :options="titles"
                                 v-model="form.title">
                  </b-form-select>
                </b-form-group>
                <b-form-group horizontal
                              breakpoint="lg"
                              label-class="text-sm-right"
                              :label-cols="3"
                              label="Vorname:"
                              label-for="register.firstname">
                  <b-form-input id="register.firstname"
                                type="text"
                                v-model="form.firstName"
                                required
                                placeholder="Vorname eingeben">
                  </b-form-input>
                </b-form-group>
                <b-form-group horizontal
                              breakpoint="lg"
                              label-class="text-sm-right"
                              :label-cols="3"
                              label="Nachname:"
                              label-for="register.lastname">
                  <b-form-input id="register.lastname"
                                type="text"
                                v-model="form.lastName"
                                required
                                placeholder="Nachname eingeben">
                  </b-form-input>
                </b-form-group>
              </b-form-group>
            </b-card>
            <b-button class="mt-4"
                      style="float:right"
                      type="submit"
                      variant="primary"
                      :to="'/'">
              Register
            </b-button>
          </b-form>
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
</template>

<script>
import Vue from 'vue'

export default {
    data() {
      return {
        login: {
          email: '',
          password: ''
        },
        form: {
        email: '',
        password: '',
        title: null,
        firstName: '',
        lastName: ''
      },
      titles: [
        { text: 'optional', value: null },
        'Prof.', 'Dr.'
      ]
      }
    },
    methods: {
    signin (event) {
      event.preventDefault()
      Vue.axios.post('/login', {
        email: this.login.email,
        password: this.login.password
      })
      .then(response => {
        alert('Login erfolgreich') // später raus
        console.log(response)
        this.$emit('login')
        window.location.replace('/')
      })
      .catch(error => {
        console.log(error)
        alert('Login fehlgeschlagen')
      }
    )
      this.$emit('login') // muss später raus

    },
    register (event) {
      event.preventDefault()
      Vue.axios.post('/users', {
        email: this.login.email,
        password: this.login.password,
        title: this.login.title,
        firstName: this.login.firstName,
        lastName: this.login.lastName
      })
      .then(response => {
        alert('Registrierung erfolgreich')
        window.location.replace('/login')
      }
      )
      .catch(error => alert('Registrierung fehlgeschlagen'))
    }
  }
}
</script>

<style>
</style>
