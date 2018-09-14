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
        <b-tab title="Registrieren" style="overflow: hidden">
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
              Registrieren
            </b-button>
          </b-form>
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
</template>

<script>
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
        ],
        user: {
          fullName: '',
          id: -1,
          admin: false
        }
      }
    },
    methods: {
    signin (event) {
      var token = 'Basic ' + btoa(unescape(encodeURIComponent(this.login.email +
        ':' + this.login.password)))
      event.preventDefault()
      this.axios({
        url: '/authentification',
        method: 'GET',
        headers:
        {
          Authorization: token
        }
      })
      .then(response => {
        this.user.token = token
        this.user.fullName = response.data.fullName
        this.user.id = response.data.id
        this.user.isAdmin = response.data.admin
        this.$emit('login', this.user)
        this.$router.push('/')
      })
      .catch(error => {
        alert('Authentifizierung fehlgeschlagen')
      })
    },
    register (event) {
      event.preventDefault()
      this.axios.post('/users', {
        email: this.login.email,
        password: this.login.password,
        title: this.login.title,
        firstName: this.login.firstName,
        lastName: this.login.lastName
      })
      .then(response => {
        alert('Registrierung erfolgreich')
        this.$router.push('/login')
      }
      )
      .catch(error => alert('Registrierung fehlgeschlagen'))
    }
  }
}
</script>

<style>
</style>
