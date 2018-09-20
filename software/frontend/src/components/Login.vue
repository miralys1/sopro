<template>
  <div>
  <div class="mainlayout" v-if="show">
    <b-card no-body>
      <b-tabs pills card>
        <!-- login form -->
        <b-tab title="Login" active>
          <b-card bg-variant="light">
            <b-form @submit="signin">
              <!-- email -->
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
              <!-- password -->
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="4"
                            label-for="login.password"
                            class="mb-0">
                <b-input-group prepend="Passwort:">
                  <b-form-input id="login.password"
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
        <!-- register form -->
        <b-tab title="Registrieren" style="overflow: hidden">
          <b-form @submit="register">
            <b-card bg-variant="light" >
              <b-form-group horizontal
                            breakpoint="lg"
                            label="Anmeldedaten"
                            label-class="font-weight-bold pt-0"
                            class="mb-0">
              <!-- email -->
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="3"
                            label="Email:"
                            label-for="register.email">
                <b-form-input id="register.email"
                              type="email"
                              v-model="form.email"
                              required
                              placeholder="Email eingeben">
                </b-form-input>
              </b-form-group>
              <!-- password -->
              <b-form-group horizontal
                            breakpoint="lg"
                            label-class="text-sm-right"
                            :label-cols="3"
                            label="Passwort:"
                            label-for="register.password"
                            class="mb-0">
                <b-form-input id="register.password"
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
                <!-- title -->
                <b-form-group horizontal
                              breakpoint="lg"
                              label-class="text-sm-right"
                              :label-cols="3"
                              label="Titel:"
                              label-for="register.title" >
                  <b-form-input id="register.title"
                                type="text"
                                v-model="form.title"
                                placeholder="(optional) Titel eingeben">
                  </b-form-input>
                </b-form-group>
                <!-- first name -->
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
                <!-- last name -->
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
                      variant="primary">
              Registrieren
            </b-button>
          </b-form>
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
  <!-- modals as alerts -->
  <b-modal no-fade ref="registerModal" id="regMod" hide-footer title="Registrierung">
    <div class="d-block text-center">
      <h4>Erfolgreich registriert! <br /> Sie können sich jetzt einloggen.</h4>
    </div>
    <b-btn class="mt-3" variant="outline-info" block @click="hideRegisterModal">Zum Login</b-btn>
  </b-modal>
  <b-modal no-fade ref="registerFailModal" id="regFailMod" hide-footer title="Registrierung">
    <div class="d-block text-center">
      <h4>Registrierung fehlgeschlagen! <br /> Vielleicht ist die Email bereits vergeben oder versuchen Sie es später noch einmal.</h4>
    </div>
    <b-btn class="mt-3" variant="outline-info" block @click="hideRegisterFailModal">Zurück</b-btn>
  </b-modal>
  <b-modal no-fade ref="loginFailModal" id="logFailMod" hide-footer title="Login">
    <div class="d-block text-center">
      <h4>Login fehlgeschlagen! <br /> Bitte versuchen Sie es noch einmal.</h4>
    </div>
    <b-btn class="mt-3" variant="outline-info" block @click="hideLoginFailModal">Zurück</b-btn>
  </b-modal>
</div>
</template>

<script>
export default {
    data() {
      return {
        show: true,
        // login form data
        login: {
          email: '',
          password: ''
        },
        // register form data
        form: {
          email: '',
          password: '',
          title: '',
          firstName: '',
          lastName: ''
        },
        // after login to notify app.vue
        user: {
          fullName: '',
          id: -1,
          admin: false
        }
      }
    },
    methods: {
      // when login button is pressed
    signin (event) {
      // generate base64 token from username and password
      var token = 'Basic ' +
        btoa(decodeURIComponent(encodeURIComponent(this.login.email +
        ':' + this.login.password)))
      event.preventDefault()

      // make request to server
      this.axios({
        url: '/authentification',
        method: 'get',
        headers:
        {
          Authorization: token
        }
      })
      .then(response => {
        // if successful set user and notify parent to update everything
        this.user.token = token
        this.user.fullName = response.data.fullName
        this.user.id = response.data.id
        this.user.isAdmin = response.data.admin
        this.$emit('login', this.user)
        this.$router.push('/')
      })
      // if not, show alert
      .catch(() => this.$refs.loginFailModal.show())
    },
    // when register button is pressed
    register (event) {
      event.preventDefault()
      this.axios.post('/users', {
        email: this.form.email,
        password: this.form.password,
        title: this.form.title,
        firstName: this.form.firstName,
        lastName: this.form.lastName
      })
      // puts you back to login form
      .then(() => this.reload())
      // show failure alert
      .catch(() => this.$refs.registerFailModal.show())
    },
    // reload page and show that registering was successful
    reload() {
      this.show = false;
      this.$nextTick(() => {this.show = true});
      this.$refs.registerModal.show()
    },
    // modals
    hideRegisterModal() {
      this.$refs.registerModal.hide()
    },
    hideRegisterFailModal() {
      this.$refs.registerFailModal.hide()
    },
    hideLoginFailModal() {
      this.$refs.loginFailModal.hide()
    }
  }
}
</script>

<style>
</style>
