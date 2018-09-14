<template>
  <div>
    <NavBar :user="user" @logout="logout" @admin="admin"/>
    <router-view @login="login" :user="user"></router-view>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'

export default {
  data() {
    return {
      user: {
        token: '',
        loggedIn: false,
        userId: -1,
        isAdmin: false,
        fullName: ''
      }
    }
  },
  name: 'app',
  components: {
    NavBar
  },
  methods: {
    login(user) {
      this.user.loggedIn = true
      this.user.email = user.email
      this.user.password = user.password
      this.user.id = user.id
      this.user.isAdmin = user.isAdmin
      this.user.fullName = user.fullName
      alert('Willkommen ' + this.user.fullName + '!')
    },
    logout() {
      this.user.email = ''
      this.user.loggedIn = false
      this.user.id = -1
      this.user.isAdmin = false
      this.user.fullName = ''
      this.$router.push('/')
      alert('Sie wurden erfolgreich ausgeloggt!')
    },

    // später raus
    admin() {
      this.user.isAdmin = !this.user.isAdmin
    }
  }
}
</script>

<style>
.mainlayout {
  margin: 10vh auto;
  width: 90vw;
  padding: 2.5% 5%;
  background-color: #f8f9fa;
  border-radius: 5px;
  border: 1px solid black;
}
.round {
  border-radius: 10px;
}
</style>
