<template>
  <div>
    <NavBar :loggedIn="user.loggedIn" :fullName="user.fullName" :isAdmin="user.isAdmin" @logout="logout" @admin="admin"/>
    <router-view @login="login" :loggedIn="user.loggedIn" :userId="user.userId"></router-view>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar'

export default {
  data() {
    return {
      user: {
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
    login() {
      this.user.loggedIn = true
      this.axios.get('http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/whoami')
        .then(response => {
          this.user.userId = response.data.id
          this.user.fullName = response.data.fullName
          this.user.isAdmin = response.data.isAdmin
          alert('Authentifizierung erfolgreich')
        })
        .catch(error => alert('Authentifizierung fehlgeschlagen'))
    },
    logout() {
      this.axios.post(
        'http://134.245.1.240:9061/composer-0.0.1-SNAPSHOT/logout'
      ).then(response => {
        this.user.loggedIn = false
        this.user.userId = -1
        this.user.isAdmin = false
        this.user.fullName = ''
        window.location.replace('/')
        alert('Sie wurden erfolgreich ausgeloggt!')
      }).catch(response => alert('Server konnte nicht erreicht werden'))
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
