<template>
  <div>
    <b-card no-body>
      <b-tabs pills card>
        <b-tab title="Login" active>
          <b-form @submit="onSubmit" @reset="onReset" v-if="show">
            <b-form-group horizontal breakpoint="lg" label="Email:" :label-cols="2" label-for="email">
              <b-form-input id="email"
                            type="email"
                            v-model="login.email"
                            required
                            placeholder="Enter email">
              </b-form-input>
            </b-form-group>
            <b-form-group horizontal breakpoint="lg" :label-cols="2" label="Password:" label-for="password">
              <b-form-input id="exampleInput2"
                            type="password"
                            v-model="login.password"
                            required
                            placeholder="Enter password">
              </b-form-input>
            </b-form-group>

            <b-button type="submit" variant="primary">Login</b-button>
            <b-button type="reset" variant="danger">Reset</b-button>
          </b-form>
        </b-tab>
        <b-tab title="Register">
          <b-form @submit="onSubmit" @reset="onReset" v-if="show">
            <b-form-group id="exampleInputGroup1"
                          label="Email address:"
                          label-for="exampleInput1"
                          description="We'll never share your email with anyone else.">
              <b-form-input id="exampleInput1"
                            type="email"
                            v-model="form.email"
                            required
                            placeholder="Enter email">
              </b-form-input>
            </b-form-group>
            <b-form-group id="exampleInputGroup2"
                          label="Your Name:"
                          label-for="exampleInput2">
              <b-form-input id="exampleInput2"
                            type="text"
                            v-model="form.name"
                            required
                            placeholder="Enter name">
              </b-form-input>
            </b-form-group>
            <b-form-group id="titel"
                          label="Titel:"
                          label-for="titel">
              <b-form-select id="titel"
                            :options="titel"
                            required
                            v-model="form.titel">
              </b-form-select>
            </b-form-group>
            <b-button type="submit" variant="primary">Register</b-button>
            <b-button type="reset" variant="danger">Reset</b-button>
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
        name: '',
        titel: null,
        checked: []
      },
      titel: [
        { text: 'Select One', value: null },
        'Prof.', 'Dr.'
      ],
      show: true
      }
    },
    methods: {
    onSubmit (evt) {
      evt.preventDefault();
      alert(JSON.stringify(this.form));
    },
    onReset (evt) {
      evt.preventDefault();
      /* Reset our form values */
      this.form.email = '';
      this.form.name = '';
      this.form.food = null;
      this.form.checked = [];
      /* Trick to reset/clear native browser form validation state */
      this.show = false;
      this.$nextTick(() => { this.show = true });
    }
  }
}
</script>

<style>
</style>
