<template>
  <div class="mainlayout" v-if="show">
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
                            label-for="register.email">
                <b-form-input id="register.email"
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
                <b-form-group horizontal
                              breakpoint="lg"
                              label-class="text-sm-right"
                              :label-cols="3"
                              label="Titel:"
                              label-for="register.title" >
                  <b-form-input id="register.title"
                                type="text"
                                v-model="form.title"
                                required
                                placeholder="Titel eingeben">
                  </b-form-input>
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
                      variant="primary">
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
        show: true,
        login: {
          email: '',
          password: ''
        },
        form: {
          email: '',
          password: '',
          title: '',
          firstName: '',
          lastName: ''
        },
        user: {
          fullName: '',
          id: -1,
          admin: false
        }
      }
    },
    methods: {
    signin (event) {
      var token = 'Basic ' +
        btoa(decodeURIComponent(encodeURIComponent(this.login.email +
        ':' + this.login.password)))
      event.preventDefault()
      this.axios({
        url: '/authentification',
        method: 'get',
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
        email: this.form.email,
        password: this.form.password,
        title: this.form.title,
        firstName: this.form.firstName,
        lastName: this.form.lastName
      })
      .then(() => {
        alert('Registrierung erfolgreich');
        this.reload();
      })
      .catch(error => alert('Registrierung fehlgeschlagen'))
    },
    reload() {
      this.show = false;
      this.$nextTick(() => {this.show = true});
    }
  }
}
</script>

<style>
</style>
